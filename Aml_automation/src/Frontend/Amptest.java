package Frontend;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import Common.Frontend;

public class Amptest {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) XatakaAmpTest/1.0";
    private static final int PAGE_LOAD_TIMEOUT_SEC = 90;
    private static final int HTTP_TIMEOUT_MS = 30000;

    private WebDriver driver;
    private final Frontend frontendProperties = new Frontend();
    private final String sites = "https://www.xataka.com,https://www.directoalpaladar.com,"
            + "https://www.vitonica.com,https://www.espinof.com";

    @Test
    public void Setup() throws Exception {
        Properties prop = frontendProperties.ReadProperties();
        String configuredSites = prop.getProperty("amp.sites", sites);
        String[] blogUrls = configuredSites.split(",");
        List<String> failures = new ArrayList<String>();

        for (String blogUrl : blogUrls) {
            String site = blogUrl.trim();
            if (site.isEmpty()) {
                continue;
            }
            System.out.println("\n=== AMP validation: " + site + " ===");
            try {
                failures.addAll(validateSite(site));
            } catch (Exception e) {
                String message = "Site load failed | " + site + " | " + e.getMessage();
                System.out.println("FAIL | " + message);
                failures.add(message);
            } finally {
                quitDriver();
            }
        }

        if (!failures.isEmpty()) {
            System.out.println("\nAMP validation failures:");
            for (String failure : failures) {
                System.out.println("  " + failure);
            }
        }
        Assert.assertTrue(failures.isEmpty(),
                "AMP validation failures:\n" + String.join("\n", failures));
    }

    private List<String> validateSite(String siteUrl) {
        List<String> failures = new ArrayList<String>();
        driver = headlessbrowser(siteUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(PAGE_LOAD_TIMEOUT_SEC));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath(".//h2[@class='abstract-title']/a")));

        List<WebElement> articles = driver.findElements(
                By.xpath(".//h2[@class='abstract-title']/a"));
        System.out.println("Articles found: " + articles.size());

        int checked = 0;
        for (WebElement article : articles) {
            if (checked >= 10) {
                break;
            }
            String href = article.getAttribute("href");
            if (href == null || href.contains("utm_campaign=repost")) {
                continue;
            }
            checked++;
            if (!isValidAmpPage(href)) {
                failures.add(siteUrl + " | " + href);
            }
        }

        System.out.println("Checked " + checked + " AMP pages on " + siteUrl);
        return failures;
    }

    private boolean isValidAmpPage(String articleUrl) {
        String ampUrl = buildAmpUrl(articleUrl);
        try (CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(HTTP_TIMEOUT_MS)
                        .setConnectionRequestTimeout(HTTP_TIMEOUT_MS)
                        .setSocketTimeout(HTTP_TIMEOUT_MS)
                        .build())
                .build()) {
            HttpGet request = new HttpGet(ampUrl);
            request.setHeader("User-Agent", USER_AGENT);
            HttpResponse response = client.execute(request);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                System.out.println("FAIL | HTTP " + status + " | " + ampUrl);
                return false;
            }
            if (response.getEntity() == null) {
                System.out.println("FAIL | empty body | " + ampUrl);
                return false;
            }
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            boolean validAmp = isAmpHtml(body);
            System.out.println((validAmp ? "PASS" : "FAIL") + " | " + ampUrl);
            return validAmp;
        } catch (Exception e) {
            System.out.println("FAIL | " + ampUrl + " | " + e.getMessage());
            return false;
        }
    }

    private static String buildAmpUrl(String articleUrl) {
        if (articleUrl.endsWith("/amp")) {
            return articleUrl;
        }
        return articleUrl.endsWith("/") ? articleUrl + "amp" : articleUrl + "/amp";
    }

    private static boolean isAmpHtml(String html) {
        String lower = html.toLowerCase();
        return lower.contains("<html amp") || lower.contains("<html ⚡")
                || lower.contains("html amp") || lower.contains("\u26a1");
    }

    private WebDriver headlessbrowser(String url) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless=new");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--disable-dev-shm-usage");

        ChromeDriver chromeDriver = new ChromeDriver(chromeOptions);
        chromeDriver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT_SEC, TimeUnit.SECONDS);
        chromeDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        try {
            chromeDriver.get(url);
        } catch (TimeoutException e) {
            chromeDriver.executeScript("window.stop();");
        }
        return chromeDriver;
    }

    private void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.out.println("WARN | driver quit failed: " + e.getMessage());
            } finally {
                driver = null;
            }
        }
    }
}
