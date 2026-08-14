import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestingXatakaBrokenLinks {

    private static final String DEFAULT_BASE_URL = "https://testing.xataka.com";
    private static final String DEFAULT_USERNAME = "guest";
    private static final String DEFAULT_PASSWORD = "guest2026";

    @Test
    public void checkBrokenLinks() throws Exception {
        BrokenLinkChecker checker = new BrokenLinkChecker(
                property("brokenLinks.baseUrl", DEFAULT_BASE_URL),
                property("brokenLinks.username", DEFAULT_USERNAME),
                property("brokenLinks.password", DEFAULT_PASSWORD),
                intProperty("brokenLinks.maxPages", 25),
                intProperty("brokenLinks.maxLinks", 500),
                intProperty("brokenLinks.timeoutMs", 15000));

        BrokenLinkReport report = checker.run();
        report.print();

        if (booleanProperty("brokenLinks.failOnBroken", true) && !report.brokenLinks.isEmpty()) {
            Assert.fail("Broken links found: " + report.brokenLinks.size());
        }
    }

    private static String property(String name, String fallback) {
        String value = System.getProperty(name);
        if (value == null || value.trim().isEmpty() || value.startsWith("${")) {
            return fallback;
        }
        return value.trim();
    }

    private static int intProperty(String name, int fallback) {
        String value = property(name, String.valueOf(fallback));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private static boolean booleanProperty(String name, boolean fallback) {
        String value = property(name, String.valueOf(fallback));
        return "true".equalsIgnoreCase(value) || "1".equals(value) || "yes".equalsIgnoreCase(value);
    }

    private static final class BrokenLinkChecker {
        private final String baseUrl;
        private final String baseHost;
        private final String authHeader;
        private final int maxPages;
        private final int maxLinks;
        private final int timeoutMs;

        BrokenLinkChecker(String baseUrl, String username, String password, int maxPages, int maxLinks, int timeoutMs)
                throws Exception {
            this.baseUrl = normalize(baseUrl);
            this.baseHost = new URI(this.baseUrl).getHost();
            this.authHeader = "Basic " + Base64.getEncoder()
                    .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            this.maxPages = maxPages;
            this.maxLinks = maxLinks;
            this.timeoutMs = timeoutMs;
        }

        BrokenLinkReport run() throws Exception {
            BrokenLinkReport report = new BrokenLinkReport(baseUrl, maxPages, maxLinks);
            Queue<String> pagesToVisit = new ArrayDeque<String>();
            Set<String> visitedPages = new HashSet<String>();
            Set<String> checkedLinks = new LinkedHashSet<String>();

            pagesToVisit.add(baseUrl);

            while (!pagesToVisit.isEmpty() && visitedPages.size() < maxPages && checkedLinks.size() < maxLinks) {
                String pageUrl = pagesToVisit.remove();
                if (!visitedPages.add(pageUrl)) {
                    continue;
                }

                HttpResult page = request(pageUrl, "GET");
                if (isBroken(page.statusCode)) {
                    report.addBroken(pageUrl, pageUrl, page.statusCode, page.error);
                    continue;
                }

                report.pagesChecked++;
                if (!isHtml(page.contentType) || page.body == null) {
                    continue;
                }

                Document document = Jsoup.parse(page.body, pageUrl);
                for (Element element : document.select("a[href]")) {
                    if (checkedLinks.size() >= maxLinks) {
                        break;
                    }

                    String link = normalize(element.absUrl("href"));
                    if (link.isEmpty() || !isSameHost(link)) {
                        continue;
                    }

                    if (checkedLinks.add(link)) {
                        HttpResult result = check(link);
                        report.linksChecked++;
                        if (isBroken(result.statusCode)) {
                            report.addBroken(pageUrl, link, result.statusCode, result.error);
                        } else if (isHtml(result.contentType) && visitedPages.size() + pagesToVisit.size() < maxPages) {
                            pagesToVisit.add(link);
                        }
                    }
                }
            }

            return report;
        }

        private HttpResult check(String link) {
            HttpResult head = request(link, "HEAD");
            if (head.statusCode == HttpURLConnection.HTTP_BAD_METHOD || head.statusCode == HttpURLConnection.HTTP_FORBIDDEN
                    || isBroken(head.statusCode)) {
                return request(link, "GET");
            }
            return head;
        }

        private HttpResult request(String link, String method) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(link);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(method);
                connection.setInstanceFollowRedirects(true);
                connection.setConnectTimeout(timeoutMs);
                connection.setReadTimeout(timeoutMs);
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 BrokenLinkChecker/1.0");
                connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                if (isSameHost(link)) {
                    connection.setRequestProperty("Authorization", authHeader);
                }

                int statusCode = connection.getResponseCode();
                String contentType = connection.getContentType();
                String body = null;
                if ("GET".equals(method) && !isBroken(statusCode)) {
                    body = read(connection.getInputStream());
                }
                return new HttpResult(statusCode, contentType, body, null);
            } catch (IOException e) {
                return new HttpResult(-1, null, null, e.getClass().getSimpleName() + ": " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        private boolean isSameHost(String link) {
            try {
                String host = new URI(link).getHost();
                return baseHost != null && baseHost.equalsIgnoreCase(host);
            } catch (URISyntaxException e) {
                return false;
            }
        }

        private static String normalize(String rawUrl) {
            if (rawUrl == null || rawUrl.trim().isEmpty()) {
                return "";
            }
            try {
                URI uri = new URI(rawUrl.trim()).normalize();
                String path = uri.getPath();
                if (path == null || path.isEmpty()) {
                    path = "/";
                }
                URI withoutFragment = new URI(uri.getScheme(), uri.getAuthority(), path, uri.getQuery(), null);
                String normalized = withoutFragment.toString();
                if (normalized.endsWith("/") && path != null && path.length() > 1) {
                    return normalized.substring(0, normalized.length() - 1);
                }
                return normalized;
            } catch (URISyntaxException e) {
                return rawUrl.trim();
            }
        }

        private static boolean isHtml(String contentType) {
            return contentType != null && contentType.toLowerCase().contains("text/html");
        }

        private static boolean isBroken(int statusCode) {
            return statusCode <= 0 || statusCode >= 400;
        }

        private static String read(InputStream inputStream) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            return builder.toString();
        }
    }

    private static final class HttpResult {
        private final int statusCode;
        private final String contentType;
        private final String body;
        private final String error;

        HttpResult(int statusCode, String contentType, String body, String error) {
            this.statusCode = statusCode;
            this.contentType = contentType;
            this.body = body;
            this.error = error;
        }
    }

    private static final class BrokenLinkReport {
        private final String baseUrl;
        private final int maxPages;
        private final int maxLinks;
        private int pagesChecked;
        private int linksChecked;
        private final List<String> brokenLinks = new ArrayList<String>();

        BrokenLinkReport(String baseUrl, int maxPages, int maxLinks) {
            this.baseUrl = baseUrl;
            this.maxPages = maxPages;
            this.maxLinks = maxLinks;
        }

        void addBroken(String sourcePage, String link, int statusCode, String error) {
            String message = "source=" + sourcePage + " link=" + link + " status=" + statusCode;
            if (error != null) {
                message += " error=" + error;
            }
            brokenLinks.add(message);
        }

        void print() {
            System.out.println("Broken link check");
            System.out.println("Base URL: " + baseUrl);
            System.out.println("Pages checked: " + pagesChecked + " / " + maxPages);
            System.out.println("Links checked: " + linksChecked + " / " + maxLinks);
            System.out.println("Broken links: " + brokenLinks.size());
            for (String brokenLink : brokenLinks) {
                System.out.println("BROKEN " + brokenLink);
            }
        }
    }
}
