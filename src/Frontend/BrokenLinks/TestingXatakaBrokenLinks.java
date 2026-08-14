package Frontend.BrokenLinks;

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
import java.util.Arrays;
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
    private static final Set<String> IGNORED_SCHEMES = new HashSet<String>(
            Arrays.asList("javascript", "mailto", "tel", "sms", "data", "blob", "whatsapp", "skype"));

    @Test
    public void checkBrokenLinks() throws Exception {
        BrokenLinkOptions options = new BrokenLinkOptions(
                property("brokenLinks.baseUrl", DEFAULT_BASE_URL),
                property("brokenLinks.username", DEFAULT_USERNAME),
                property("brokenLinks.password", DEFAULT_PASSWORD),
                intProperty("brokenLinks.maxPages", 25),
                intProperty("brokenLinks.maxLinks", 500),
                intProperty("brokenLinks.timeoutMs", 15000),
                intProperty("brokenLinks.maxBodyChars", 2000000),
                booleanProperty("brokenLinks.checkExternalLinks", false),
                booleanProperty("brokenLinks.includeAssets", false));

        BrokenLinkReport report = new BrokenLinkChecker(options).run();
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
        if ("true".equalsIgnoreCase(value) || "1".equals(value) || "yes".equalsIgnoreCase(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value) || "0".equals(value) || "no".equalsIgnoreCase(value)) {
            return false;
        }
        return fallback;
    }

    private static final class BrokenLinkChecker {
        private final BrokenLinkOptions options;
        private final String authHeader;

        BrokenLinkChecker(BrokenLinkOptions options) {
            this.options = options;
            this.authHeader = "Basic " + Base64.getEncoder()
                    .encodeToString((options.username + ":" + options.password).getBytes(StandardCharsets.UTF_8));
        }

        BrokenLinkReport run() {
            BrokenLinkReport report = new BrokenLinkReport(options);
            Queue<PageToVisit> pagesToVisit = new ArrayDeque<PageToVisit>();
            Set<String> visitedPages = new HashSet<String>();
            Set<String> checkedLinks = new LinkedHashSet<String>();

            pagesToVisit.add(new PageToVisit(options.baseUrl, 0));

            while (!pagesToVisit.isEmpty() && visitedPages.size() < options.maxPages && checkedLinks.size() < options.maxLinks) {
                PageToVisit nextPage = pagesToVisit.remove();
                if (!visitedPages.add(nextPage.url)) {
                    continue;
                }

                HttpResult page = request(nextPage.url, "GET");
                if (isBroken(page.statusCode)) {
                    report.addBroken(nextPage.url, nextPage.url, page.statusCode, page.error);
                    continue;
                }

                report.pagesChecked++;
                if (!isHtml(page.contentType) || page.body == null) {
                    continue;
                }

                for (DiscoveredLink discoveredLink : extractLinks(page.body, nextPage.url)) {
                    if (checkedLinks.size() >= options.maxLinks) {
                        break;
                    }

                    String link = normalize(discoveredLink.rawUrl, nextPage.url);
                    if (link == null || !isAllowedHost(link)) {
                        continue;
                    }

                    if (checkedLinks.add(link)) {
                        HttpResult result = check(link);
                        report.linksChecked++;
                        if (isBroken(result.statusCode)) {
                            report.addBroken(nextPage.url, link, result.statusCode, result.error);
                        } else if (discoveredLink.crawlable && isSameHost(link) && isHtml(result.contentType)
                                && visitedPages.size() + pagesToVisit.size() < options.maxPages) {
                            pagesToVisit.add(new PageToVisit(link, nextPage.depth + 1));
                        }
                    }
                }
            }

            return report;
        }

        private List<DiscoveredLink> extractLinks(String html, String pageUrl) {
            List<DiscoveredLink> links = new ArrayList<DiscoveredLink>();
            Document document = Jsoup.parse(html, pageUrl);

            for (Element element : document.select("a[href]")) {
                links.add(new DiscoveredLink(element.attr("href"), true));
            }

            if (options.includeAssets) {
                for (Element element : document.select("[src]")) {
                    links.add(new DiscoveredLink(element.attr("src"), false));
                }
                for (Element element : document.select("[srcset]")) {
                    for (String candidate : element.attr("srcset").split(",")) {
                        String[] parts = candidate.trim().split("\\s+");
                        if (parts.length > 0) {
                            links.add(new DiscoveredLink(parts[0], false));
                        }
                    }
                }
                for (Element element : document.select("link[href]")) {
                    links.add(new DiscoveredLink(element.attr("href"), false));
                }
            }

            return links;
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
                connection.setConnectTimeout(options.timeoutMs);
                connection.setReadTimeout(options.timeoutMs);
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 BrokenLinkChecker/1.0");
                connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                if (isSameHost(link)) {
                    connection.setRequestProperty("Authorization", authHeader);
                }

                int statusCode = connection.getResponseCode();
                String contentType = connection.getContentType();
                String body = null;
                if ("GET".equals(method) && !isBroken(statusCode) && isHtml(contentType)) {
                    body = read(connection.getInputStream(), options.maxBodyChars);
                }
                return new HttpResult(statusCode, contentType, body, null);
            } catch (IOException e) {
                return new HttpResult(-1, null, null, e.getClass().getSimpleName() + ": " + e.getMessage());
            } catch (RuntimeException e) {
                return new HttpResult(-1, null, null, e.getClass().getSimpleName() + ": " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        private boolean isAllowedHost(String link) {
            return options.checkExternalLinks || isSameHost(link);
        }

        private boolean isSameHost(String link) {
            try {
                String host = new URI(link).getHost();
                return options.baseHost != null && options.baseHost.equalsIgnoreCase(host);
            } catch (URISyntaxException e) {
                return false;
            }
        }

        private static boolean isHtml(String contentType) {
            return contentType != null && contentType.toLowerCase().contains("text/html");
        }

        private static boolean isBroken(int statusCode) {
            return statusCode <= 0 || statusCode >= 400;
        }
    }

    private static String normalize(String rawUrl, String pageUrl) {
        if (rawUrl == null) {
            return null;
        }

        String trimmed = rawUrl.trim();
        if (trimmed.isEmpty() || trimmed.startsWith("#")) {
            return null;
        }

        int colonIndex = trimmed.indexOf(':');
        if (colonIndex > 0) {
            String scheme = trimmed.substring(0, colonIndex).toLowerCase();
            if (IGNORED_SCHEMES.contains(scheme)) {
                return null;
            }
        }

        try {
            URI base = new URI(pageUrl);
            URI resolved = base.resolve(trimmed.replace(" ", "%20")).normalize();
            String scheme = resolved.getScheme();
            if (scheme == null || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) {
                return null;
            }

            String path = resolved.getPath();
            if (path == null || path.isEmpty()) {
                path = "/";
            }

            URI withoutFragment = new URI(
                    resolved.getScheme().toLowerCase(),
                    resolved.getAuthority(),
                    path,
                    resolved.getQuery(),
                    null);
            String normalized = withoutFragment.toString();
            if (normalized.endsWith("/") && path.length() > 1) {
                return normalized.substring(0, normalized.length() - 1);
            }
            return normalized;
        } catch (URISyntaxException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String read(InputStream inputStream, int maxChars) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null && builder.length() < maxChars) {
            builder.append(line).append('\n');
        }
        return builder.toString();
    }

    private static final class BrokenLinkOptions {
        private final String baseUrl;
        private final String baseHost;
        private final String username;
        private final String password;
        private final int maxPages;
        private final int maxLinks;
        private final int timeoutMs;
        private final int maxBodyChars;
        private final boolean checkExternalLinks;
        private final boolean includeAssets;

        BrokenLinkOptions(String baseUrl, String username, String password, int maxPages, int maxLinks, int timeoutMs,
                int maxBodyChars, boolean checkExternalLinks, boolean includeAssets) throws URISyntaxException {
            this.baseUrl = normalize(baseUrl, baseUrl);
            this.baseHost = new URI(this.baseUrl).getHost();
            this.username = username;
            this.password = password;
            this.maxPages = maxPages;
            this.maxLinks = maxLinks;
            this.timeoutMs = timeoutMs;
            this.maxBodyChars = maxBodyChars;
            this.checkExternalLinks = checkExternalLinks;
            this.includeAssets = includeAssets;
        }
    }

    private static final class DiscoveredLink {
        private final String rawUrl;
        private final boolean crawlable;

        DiscoveredLink(String rawUrl, boolean crawlable) {
            this.rawUrl = rawUrl;
            this.crawlable = crawlable;
        }
    }

    private static final class PageToVisit {
        private final String url;
        private final int depth;

        PageToVisit(String url, int depth) {
            this.url = url;
            this.depth = depth;
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
        private final BrokenLinkOptions options;
        private int pagesChecked;
        private int linksChecked;
        private final List<String> brokenLinks = new ArrayList<String>();

        BrokenLinkReport(BrokenLinkOptions options) {
            this.options = options;
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
            System.out.println("Base URL: " + options.baseUrl);
            System.out.println("Pages checked: " + pagesChecked + " / " + options.maxPages);
            System.out.println("Links checked: " + linksChecked + " / " + options.maxLinks);
            System.out.println("External links: " + (options.checkExternalLinks ? "included" : "skipped"));
            System.out.println("Assets: " + (options.includeAssets ? "included" : "skipped"));
            System.out.println("Broken links: " + brokenLinks.size());
            for (String brokenLink : brokenLinks) {
                System.out.println("BROKEN " + brokenLink);
            }
        }
    }
}
