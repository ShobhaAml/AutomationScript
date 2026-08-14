package Frontend;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Common.Frontend;

/**
 * Sanity checks and broken-link scan for testing.xataka.com (basic auth).
 * Uses Jsoup + HttpClient — no browser required.
 */
public class BrokenLinks {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) XatakaBrokenLinks/1.0";
    private static final int TIMEOUT_MS = 60000;
    private static final int MAX_REDIRECTS = 8;

    private static final Pattern SKIP_LINK = Pattern.compile(
            "javascript:|mailto:|#|youtu|tiktok|redirect\\?|utm_campaign=footer",
            Pattern.CASE_INSENSITIVE);

    private static final String[] SANITY_PATHS = {
            "/",
            "/contacto",
            "/quienes-somos",
            "/categoria/analisis",
            "/categoria/moviles",
            "/categoria/seleccion",
            "/categoria/magnet",
            "/archivos",
            "/feedburner.xml",
            "/robots.txt",
            "/ads.txt"
    };

    private static final String[] OPTIONAL_CRAWL_PATHS = {
            "/categoria/hardware",
            "/event/sony-vaio-2013",
            "/event/sony-vaio-2013/amp"
    };

    private static final String[] INTERNAL_CRAWL_PATHS = {
            "/archivos",
            "/quienes-somos",
            "/contacto",
            "/tecnologialg",
            "/black-friday",
            "/la-cacharreria/search?"
    };

    private static final Set<String> OPTIONAL_PATHS = new HashSet<String>();
    static {
        for (String path : OPTIONAL_CRAWL_PATHS) {
            OPTIONAL_PATHS.add(path);
        }
    }

    private String baseUrl;
    private String authUser;
    private String authPass;
    private String siteHost;

    private final Set<String> checkedUrls = new HashSet<String>();
    private final List<String> brokenLinks = new ArrayList<String>();
    private final List<String> redirectLoops = new ArrayList<String>();
    private final Map<String, Integer> sanityResults = new LinkedHashMap<String, Integer>();

    @BeforeClass
    public void setUp() throws Exception {
        Frontend frontend = new Frontend();
        Properties prop = frontend.ReadProperties();
        baseUrl = normalizeBaseUrl(prop.getProperty("url",
                "https://guest:newguest@testing.xataka.com"));
        parseAuthFromBaseUrl();
        String propUser = prop.getProperty("auth.user");
        String propPass = prop.getProperty("auth.pass");
        if (propUser != null && !propUser.trim().isEmpty()) {
            authUser = propUser.trim();
        }
        if (propPass != null && !propPass.trim().isEmpty()) {
            authPass = propPass.trim();
        }
        siteHost = URI.create(stripCredentials(baseUrl)).getHost();
        System.out.println("=== Xataka Broken Links & Sanity ===");
        System.out.println("Base URL : " + maskCredentials(baseUrl));
        System.out.println("Site host: " + siteHost);
    }

    @Test(priority = 1)
    public void sanityCheckKeyPages() throws IOException {
        System.out.println("\n--- Sanity: key pages ---");
        for (String path : SANITY_PATHS) {
            String url = resolveUrl(path);
            if (isTextResource(path)) {
                int status = fetchStatus(url, true);
                sanityResults.put(path, status);
                System.out.println((status == 200 ? "PASS" : "FAIL") + " | HTTP " + status + " | " + path);
            } else {
                try {
                    Document doc = fetchDocumentWithRetry(url, path.equals("/") ? 2 : 1);
                    sanityResults.put(path, 200);
                    System.out.println("PASS | HTTP 200 | " + path);
                    if (path.equals("/")) {
                        String title = resolvePageTitle(doc);
                        Assert.assertFalse(title.isEmpty(), "Homepage title must not be empty");
                        Assert.assertTrue(doc.select("article, h2").size() >= 5,
                                "Homepage should contain articles/headlines");
                        System.out.println("  Title: " + title);
                    }
                } catch (IOException e) {
                    sanityResults.put(path, -1);
                    System.out.println("FAIL | ERROR | " + path + " | " + e.getMessage());
                }
            }
        }

        long failed = 0;
        for (Integer status : sanityResults.values()) {
            if (status != 200) {
                failed++;
            }
        }
        Assert.assertEquals(failed, 0L, "Sanity failures: " + sanityResults);
        System.out.println("PASS: All " + SANITY_PATHS.length + " sanity pages returned HTTP 200.");
    }

    private static boolean isTextResource(String path) {
        return path.endsWith(".xml") || path.endsWith(".txt");
    }

    @Test(priority = 2, dependsOnMethods = "sanityCheckKeyPages")
    public void verifyHomepageLinks() throws IOException {
        System.out.println("\n--- Broken links: homepage ---");
        Document homepage = fetchDocument(resolveUrl("/"));
        Set<String> links = extractLinks(homepage);
        System.out.println("Links to verify: " + links.size());
        verifyLinkSet(links, "homepage");
    }

    @Test(priority = 3, dependsOnMethods = "sanityCheckKeyPages")
    public void verifyInternalPageLinks() throws IOException {
        System.out.println("\n--- Broken links: internal pages ---");
        Set<String> allLinks = new LinkedHashSet<String>();

        for (String path : INTERNAL_CRAWL_PATHS) {
            String pageUrl = resolveUrl(path);
            System.out.println("Crawling: " + path);
            try {
                if (path.contains("/black-friday") || path.contains("/la-cacharreria")) {
                    checkedUrls.add(pageUrl);
                    LinkCheckResult result = checkLink(pageUrl);
                    if (!result.ok && !result.redirectLoop) {
                        brokenLinks.add(path + " | " + result.message);
                    } else if (result.redirectLoop) {
                        redirectLoops.add(path + " | " + result.message);
                    }
                    continue;
                }
                allLinks.addAll(extractLinks(fetchDocument(pageUrl)));
            } catch (IOException e) {
                if (OPTIONAL_PATHS.contains(path)) {
                    System.out.println("  WARN: optional page not available | " + path + " | " + e.getMessage());
                } else {
                    brokenLinks.add(path + " | page fetch failed | " + e.getMessage());
                }
            }
        }

        for (String path : OPTIONAL_CRAWL_PATHS) {
            String pageUrl = resolveUrl(path);
            System.out.println("Crawling optional: " + path);
            try {
                allLinks.addAll(extractLinks(fetchDocument(pageUrl)));
            } catch (IOException e) {
                System.out.println("  WARN: optional page not available | " + path + " | " + e.getMessage());
            }
        }

        Document homepage = fetchDocument(resolveUrl("/"));
        for (Element link : homepage.select("a[href*='/autor/']")) {
            String href = link.attr("abs:href");
            if (href != null && !href.isEmpty()) {
                allLinks.add(applyAuth(href));
            }
        }

        System.out.println("Total unique links to verify: " + allLinks.size());
        verifyLinkSet(allLinks, "internal pages");
    }

    @Test(priority = 4, dependsOnMethods = { "verifyHomepageLinks", "verifyInternalPageLinks" })
    public void assertNoBrokenLinks() {
        System.out.println("\n--- Final summary ---");
        System.out.println("Sanity pages checked : " + sanityResults.size());
        System.out.println("URLs verified        : " + checkedUrls.size());
        System.out.println("Broken links         : " + brokenLinks.size());
        System.out.println("Redirect loops       : " + redirectLoops.size());

        if (!brokenLinks.isEmpty()) {
            System.out.println("\nBroken links:");
            for (String broken : brokenLinks) {
                System.out.println("  " + broken);
            }
        }
        if (!redirectLoops.isEmpty()) {
            System.out.println("\nRedirect loops:");
            for (String loop : redirectLoops) {
                System.out.println("  " + loop);
            }
        }

        Assert.assertTrue(redirectLoops.isEmpty(), "Redirect loops: " + redirectLoops);
        Assert.assertTrue(brokenLinks.isEmpty(),
                "Broken links:\n" + String.join("\n", brokenLinks));
    }

    private void verifyLinkSet(Set<String> links, String source) {
        int count = 0;
        for (String url : links) {
            if (!shouldVerify(url) || !checkedUrls.add(url)) {
                continue;
            }
            count++;
            LinkCheckResult result = checkLink(url);
            if (result.redirectLoop) {
                redirectLoops.add(source + " | " + result.message);
            } else if (!result.ok) {
                brokenLinks.add(source + " | " + result.message);
                System.out.println("  BROKEN: " + result.message);
            }
        }
        System.out.println("Verified " + count + " links from " + source);
    }

    private LinkCheckResult checkLink(String url) {
        List<String> chain = new ArrayList<String>();
        String current = url;

        for (int i = 0; i <= MAX_REDIRECTS; i++) {
            if (chain.contains(current)) {
                return LinkCheckResult.loop(current, chain);
            }
            chain.add(current);

            int status = fetchStatus(current, true);
            if (status >= 200 && status < 400) {
                return LinkCheckResult.ok(status);
            }
            if (status == 301 || status == 302 || status == 303 || status == 307 || status == 308) {
                String location = fetchRedirectLocation(current);
                if (location == null || location.isEmpty()) {
                    return LinkCheckResult.fail(url, status, "redirect without Location header");
                }
                current = applyAuth(toAbsolute(current, location));
                continue;
            }
            return LinkCheckResult.fail(url, status, "unexpected status");
        }
        return LinkCheckResult.fail(url, 0, "too many redirects: " + chain);
    }

    private int fetchStatus(String url, boolean followRedirects) {
        CloseableHttpClient client = buildClient(followRedirects);
        try {
            HttpGet get = new HttpGet(applyAuth(url));
            get.setHeader("User-Agent", USER_AGENT);
            addAuthHeader(get);
            HttpClientContext context = createAuthContext(get.getURI());
            return client.execute(get, context).getStatusLine().getStatusCode();
        } catch (Exception e) {
            return -1;
        } finally {
            closeQuietly(client);
        }
    }

    private String fetchRedirectLocation(String url) {
        CloseableHttpClient client = buildClient(false);
        try {
            HttpHead request = new HttpHead(applyAuth(url));
            request.setHeader("User-Agent", USER_AGENT);
            addAuthHeader(request);
            HttpClientContext context = createAuthContext(request.getURI());
            HttpResponse response = client.execute(request, context);
            return response.getFirstHeader("Location") != null
                    ? response.getFirstHeader("Location").getValue() : null;
        } catch (Exception e) {
            return null;
        } finally {
            closeQuietly(client);
        }
    }

    private Document fetchDocumentWithRetry(String url, int attempts) throws IOException {
        IOException lastError = null;
        for (int i = 0; i < attempts; i++) {
            try {
                return fetchDocument(url);
            } catch (IOException e) {
                lastError = e;
                System.out.println("  Retry " + (i + 1) + " for " + url + ": " + e.getMessage());
            }
        }
        throw lastError;
    }

    private Document fetchDocument(String url) throws IOException {
        CloseableHttpClient client = buildClient(true);
        try {
            HttpGet get = new HttpGet(applyAuth(url));
            get.setHeader("User-Agent", USER_AGENT);
            addAuthHeader(get);
            HttpClientContext context = createAuthContext(get.getURI());
            HttpResponse response = client.execute(get, context);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                throw new IOException("HTTP " + status + " for " + maskCredentials(url));
            }
            if (response.getEntity() == null) {
                throw new IOException("Empty response body for " + maskCredentials(url));
            }
            return Jsoup.parse(response.getEntity().getContent(), null, stripCredentials(url));
        } finally {
            closeQuietly(client);
        }
    }

    private static String resolvePageTitle(Document doc) {
        for (Element titleTag : doc.select("title")) {
            if (!titleTag.text().trim().isEmpty()) {
                return titleTag.text().trim();
            }
        }
        Element ogTitle = doc.selectFirst("meta[property=og:title]");
        if (ogTitle != null && !ogTitle.attr("content").trim().isEmpty()) {
            return ogTitle.attr("content").trim();
        }
        Element appName = doc.selectFirst("meta[name=application-name]");
        if (appName != null && !appName.attr("content").trim().isEmpty()) {
            return appName.attr("content").trim();
        }
        return doc.title().trim();
    }

    private Set<String> extractLinks(Document doc) {
        Set<String> links = new LinkedHashSet<String>();
        for (Element anchor : doc.select("a[href]")) {
            String href = anchor.attr("abs:href");
            if (href != null && !href.isEmpty() && !SKIP_LINK.matcher(href).find()
                    && isStagingLink(href)) {
                links.add(applyAuth(href));
            }
        }
        return links;
    }

    private boolean shouldVerify(String url) {
        return url != null && !url.trim().isEmpty()
                && !SKIP_LINK.matcher(url).find() && isStagingLink(url);
    }

    private boolean isStagingLink(String url) {
        try {
            String host = URI.create(stripCredentials(url)).getHost();
            return host != null && host.equalsIgnoreCase(siteHost);
        } catch (Exception e) {
            return false;
        }
    }

    private String applyAuth(String url) {
        if (authUser == null || authPass == null || url == null) {
            return stripCredentials(url);
        }
        try {
            URI uri = URI.create(stripCredentials(url));
            String host = uri.getHost();
            if (host != null && needsAuth(host)) {
                int port = uri.getPort();
                String portPart = port > 0 ? ":" + port : "";
                String path = uri.getRawPath() != null ? uri.getRawPath() : "";
                String query = uri.getRawQuery() != null ? "?" + uri.getRawQuery() : "";
                return uri.getScheme() + "://" + authUser + ":" + authPass + "@" + host
                        + portPart + path + query;
            }
        } catch (Exception e) {
            // fall through
        }
        return stripCredentials(url);
    }

    private String resolveUrl(String path) {
        if (path.startsWith("http")) {
            return applyAuth(path);
        }
        String root = stripCredentials(baseUrl);
        if (!root.endsWith("/")) {
            root = root + "/";
        }
        return applyAuth(root + (path.startsWith("/") ? path.substring(1) : path));
    }

    private void parseAuthFromBaseUrl() {
        authUser = "guest";
        authPass = "newguest";
        try {
            URI uri = URI.create(baseUrl.contains("://") ? baseUrl : "https://" + baseUrl);
            if (uri.getUserInfo() != null && uri.getUserInfo().contains(":")) {
                String[] parts = uri.getUserInfo().split(":", 2);
                authUser = parts[0].trim();
                authPass = parts[1].trim();
            }
        } catch (Exception e) {
            System.out.println("Using default basic-auth credentials.");
        }
    }

    private String basicAuthHeader() {
        return "Basic " + Base64.getEncoder().encodeToString(
                (authUser + ":" + authPass).getBytes(StandardCharsets.UTF_8));
    }

    private void addAuthHeader(HttpUriRequest request) {
        String host = request.getURI().getHost();
        if (host != null && needsAuth(host)) {
            request.setHeader("Authorization", basicAuthHeader());
        }
    }

    private boolean needsAuth(String host) {
        return host.contains("testing.") || host.contains("test.") || host.contains("mtest.")
                || host.equalsIgnoreCase(siteHost);
    }

    private CloseableHttpClient buildClient(boolean followRedirects) {
        return HttpClientBuilder.create()
                .setDefaultCredentialsProvider(buildCredentialsProvider())
                .setDefaultRequestConfig(
                RequestConfig.custom()
                        .setConnectTimeout(TIMEOUT_MS)
                        .setConnectionRequestTimeout(TIMEOUT_MS)
                        .setSocketTimeout(TIMEOUT_MS)
                        .setCookieSpec(CookieSpecs.STANDARD)
                        .setRedirectsEnabled(followRedirects)
                        .build()).build();
    }

    private CredentialsProvider buildCredentialsProvider() {
        BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        if (authUser != null && authPass != null) {
            credsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(authUser, authPass));
        }
        return credsProvider;
    }

    private HttpClientContext createAuthContext(URI uri) {
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(buildCredentialsProvider());
        if (authUser == null || authPass == null || uri.getHost() == null) {
            return context;
        }
        try {
            String scheme = uri.getScheme() != null ? uri.getScheme() : "https";
            int port = uri.getPort() > 0 ? uri.getPort()
                    : ("https".equalsIgnoreCase(scheme) ? 443 : 80);
            HttpHost target = new HttpHost(uri.getHost(), port, scheme);
            BasicAuthCache authCache = new BasicAuthCache();
            authCache.put(target, new BasicScheme());
            context.setAuthCache(authCache);
        } catch (Exception e) {
            // ignore
        }
        return context;
    }

    private static String normalizeBaseUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return "https://guest:newguest@testing.xataka.com/";
        }
        String normalized = url.trim();
        return normalized.endsWith("/") ? normalized : normalized + "/";
    }

    private static String stripCredentials(String url) {
        return url.replaceAll("//[^@/]+@", "//");
    }

    private static String maskCredentials(String url) {
        return url.replaceAll("//[^@/]+@", "//***:***@");
    }

    private static String toAbsolute(String base, String location) {
        if (location.startsWith("http")) {
            return location;
        }
        try {
            return URI.create(base).resolve(location).toString();
        } catch (Exception e) {
            return location;
        }
    }

    private static void closeQuietly(CloseableHttpClient client) {
        if (client != null) {
            try {
                client.close();
            } catch (IOException ignored) {
                // ignore
            }
        }
    }

    private static final class LinkCheckResult {
        final boolean ok;
        final boolean redirectLoop;
        final String message;

        private LinkCheckResult(boolean ok, boolean redirectLoop, String message) {
            this.ok = ok;
            this.redirectLoop = redirectLoop;
            this.message = message;
        }

        static LinkCheckResult ok(int status) {
            return new LinkCheckResult(true, false, "OK " + status);
        }

        static LinkCheckResult fail(String url, int status, String reason) {
            return new LinkCheckResult(false, false, url + " | HTTP " + status + " | " + reason);
        }

        static LinkCheckResult loop(String url, List<String> chain) {
            return new LinkCheckResult(false, true, url + " | redirect loop | " + chain);
        }
    }
}
