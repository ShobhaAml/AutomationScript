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
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestingXatakaSanityCheck {

    private static final String DEFAULT_BASE_URL = "https://testing.xataka.com";
    private static final String DEFAULT_USERNAME = "guest";
    private static final String DEFAULT_PASSWORD = "guest2026";

    @Test
    public void checkSanityRobotsAndSitemaps() throws Exception {
        SanityOptions options = new SanityOptions(
                property("brokenLinks.baseUrl", DEFAULT_BASE_URL),
                property("brokenLinks.username", DEFAULT_USERNAME),
                property("brokenLinks.password", DEFAULT_PASSWORD),
                intProperty("brokenLinks.timeoutMs", 15000),
                intProperty("sanity.maxSitemaps", 1000),
                intProperty("sanity.maxSitemapUrls", 0),
                intProperty("sanity.maxBodyChars", 5000000));

        SanityReport report = new SanityChecker(options).run();
        report.print();

        if (booleanProperty("sanity.failOnError", false) && !report.errors.isEmpty()) {
            Assert.fail("Sanity check errors found: " + report.errors.size());
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

    private static final class SanityChecker {
        private final SanityOptions options;
        private final String authHeader;

        SanityChecker(SanityOptions options) {
            this.options = options;
            this.authHeader = "Basic " + Base64.getEncoder()
                    .encodeToString((options.username + ":" + options.password).getBytes(StandardCharsets.UTF_8));
        }

        SanityReport run() {
            SanityReport report = new SanityReport(options);
            checkHomepage(report);
            List<String> sitemapSeeds = checkRobots(report);
            checkSitemaps(report, sitemapSeeds);
            return report;
        }

        private void checkHomepage(SanityReport report) {
            HttpResult homepage = request(options.baseUrl, "GET");
            report.homeStatus = homepage.statusCode;
            if (isBroken(homepage.statusCode)) {
                report.addError("Homepage is not reachable: " + options.baseUrl + " status=" + homepage.statusCode
                        + errorSuffix(homepage.error));
            }
        }

        private List<String> checkRobots(SanityReport report) {
            String robotsUrl = resolve(options.baseUrl, "/robots.txt");
            HttpResult robots = request(robotsUrl, "GET");
            report.robotsStatus = robots.statusCode;
            List<String> sitemapSeeds = new ArrayList<String>();

            if (isBroken(robots.statusCode)) {
                report.addError("robots.txt is not reachable: " + robotsUrl + " status=" + robots.statusCode
                        + errorSuffix(robots.error));
            } else if (robots.body != null) {
                for (String line : robots.body.split("\\r?\\n")) {
                    if (line.toLowerCase().startsWith("sitemap:")) {
                        String sitemapUrl = line.substring("sitemap:".length()).trim();
                        if (!sitemapUrl.isEmpty()) {
                            sitemapSeeds.add(sitemapUrl);
                        }
                    }
                }
            }

            if (sitemapSeeds.isEmpty()) {
                sitemapSeeds.add(resolve(options.baseUrl, "/sitemap_index.xml"));
            }
            report.sitemapSeeds.addAll(sitemapSeeds);
            return sitemapSeeds;
        }

        private void checkSitemaps(SanityReport report, List<String> sitemapSeeds) {
            Queue<String> pending = new ArrayDeque<String>(sitemapSeeds);
            Set<String> seenSitemaps = new LinkedHashSet<String>();
            Set<String> checkedUrls = new LinkedHashSet<String>();

            while (!pending.isEmpty() && seenSitemaps.size() < options.maxSitemaps) {
                String sitemapUrl = normalizeUrl(pending.remove(), options.baseUrl);
                if (sitemapUrl == null || !seenSitemaps.add(sitemapUrl)) {
                    continue;
                }

                HttpResult sitemap = request(sitemapUrl, "GET");
                report.sitemapsChecked++;
                if (isBroken(sitemap.statusCode)) {
                    report.addError("Sitemap is not reachable: " + sitemapUrl + " status=" + sitemap.statusCode
                            + errorSuffix(sitemap.error));
                    continue;
                }
                if (sitemap.body == null || !isXml(sitemap.contentType)) {
                    report.addError("Sitemap did not return XML content: " + sitemapUrl + " contentType="
                            + sitemap.contentType);
                    continue;
                }

                Document document = Jsoup.parse(sitemap.body, "", Parser.xmlParser());
                for (Element loc : document.select("sitemap > loc")) {
                    String childSitemap = normalizeUrl(loc.text(), sitemapUrl);
                    if (childSitemap != null && !seenSitemaps.contains(childSitemap)
                            && seenSitemaps.size() + pending.size() < options.maxSitemaps) {
                        pending.add(childSitemap);
                    }
                }

                if (options.maxSitemapUrls > 0) {
                    for (Element loc : document.select("url > loc")) {
                        if (checkedUrls.size() >= options.maxSitemapUrls) {
                            break;
                        }
                        String url = normalizeUrl(loc.text(), sitemapUrl);
                        if (url != null && checkedUrls.add(url)) {
                            HttpResult result = check(url);
                            report.sitemapUrlsChecked++;
                            if (isBroken(result.statusCode)) {
                                report.addError("Sitemap URL is broken: " + url + " status=" + result.statusCode
                                        + errorSuffix(result.error));
                            }
                        }
                    }
                }
            }
        }

        private HttpResult check(String url) {
            HttpResult head = request(url, "HEAD");
            if (head.statusCode == HttpURLConnection.HTTP_BAD_METHOD || head.statusCode == HttpURLConnection.HTTP_FORBIDDEN
                    || isBroken(head.statusCode)) {
                return request(url, "GET");
            }
            return head;
        }

        private HttpResult request(String url, String method) {
            HttpURLConnection connection = null;
            try {
                HttpURLConnection.setFollowRedirects(true);
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod(method);
                connection.setInstanceFollowRedirects(true);
                connection.setConnectTimeout(options.timeoutMs);
                connection.setReadTimeout(options.timeoutMs);
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 SiteSanityChecker/1.0");
                connection.setRequestProperty("Accept", "*/*");
                if (isSameHost(url)) {
                    connection.setRequestProperty("Authorization", authHeader);
                }

                int statusCode = connection.getResponseCode();
                String contentType = connection.getContentType();
                String body = null;
                if ("GET".equals(method) && !isBroken(statusCode)) {
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

        private boolean isSameHost(String url) {
            try {
                String host = new URI(url).getHost();
                return options.baseHost != null && options.baseHost.equalsIgnoreCase(host);
            } catch (URISyntaxException e) {
                return false;
            }
        }

        private static boolean isBroken(int statusCode) {
            return statusCode <= 0 || statusCode >= 400;
        }

        private static boolean isXml(String contentType) {
            String value = contentType == null ? "" : contentType.toLowerCase();
            return value.contains("xml") || value.contains("text/plain");
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

    private static String resolve(String baseUrl, String path) {
        try {
            return new URI(baseUrl).resolve(path).toString();
        } catch (URISyntaxException e) {
            return baseUrl + path;
        }
    }

    private static String normalizeUrl(String rawUrl, String baseUrl) {
        if (rawUrl == null || rawUrl.trim().isEmpty()) {
            return null;
        }
        try {
            URI base = new URI(baseUrl);
            URI resolved = base.resolve(rawUrl.trim().replace(" ", "%20")).normalize();
            String path = resolved.getPath();
            if (path == null || path.isEmpty()) {
                path = "/";
            }
            URI withoutFragment = new URI(
                    resolved.getScheme() == null ? null : resolved.getScheme().toLowerCase(),
                    resolved.getAuthority(),
                    path,
                    resolved.getQuery(),
                    null);
            return withoutFragment.toString();
        } catch (URISyntaxException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String errorSuffix(String error) {
        return error == null ? "" : " error=" + error;
    }

    private static final class SanityOptions {
        private final String baseUrl;
        private final String baseHost;
        private final String username;
        private final String password;
        private final int timeoutMs;
        private final int maxSitemaps;
        private final int maxSitemapUrls;
        private final int maxBodyChars;

        SanityOptions(String baseUrl, String username, String password, int timeoutMs, int maxSitemaps,
                int maxSitemapUrls, int maxBodyChars) throws URISyntaxException {
            this.baseUrl = normalizeUrl(baseUrl, baseUrl);
            this.baseHost = new URI(this.baseUrl).getHost();
            this.username = username;
            this.password = password;
            this.timeoutMs = timeoutMs;
            this.maxSitemaps = maxSitemaps;
            this.maxSitemapUrls = maxSitemapUrls;
            this.maxBodyChars = maxBodyChars;
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

    private static final class SanityReport {
        private final SanityOptions options;
        private final List<String> sitemapSeeds = new ArrayList<String>();
        private final List<String> errors = new ArrayList<String>();
        private int homeStatus;
        private int robotsStatus;
        private int sitemapsChecked;
        private int sitemapUrlsChecked;

        SanityReport(SanityOptions options) {
            this.options = options;
        }

        void addError(String error) {
            errors.add(error);
        }

        void print() {
            System.out.println("Site sanity check");
            System.out.println("Base URL: " + options.baseUrl);
            System.out.println("Homepage status: " + homeStatus);
            System.out.println("robots.txt status: " + robotsStatus);
            System.out.println("Sitemap seeds: " + sitemapSeeds);
            System.out.println("Sitemaps checked: " + sitemapsChecked + " / " + options.maxSitemaps);
            System.out.println("Sitemap URLs checked: " + sitemapUrlsChecked + " / " + options.maxSitemapUrls);
            System.out.println("Sanity errors: " + errors.size());
            for (String error : errors) {
                System.out.println("SANITY_ERROR " + error);
            }
        }
    }
}
