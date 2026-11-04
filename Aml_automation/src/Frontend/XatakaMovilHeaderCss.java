package Frontend;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import Common.Frontend;

public class XatakaMovilHeaderCss {

    private static final String DEFAULT_URL = "https://guest:guest2026@testing.xatakamovil.com/";
    private static final String HEADER_SECTIONS_SELECTOR = ".o-siteheader-section__menu-toggle";
    private static final String HEADER_LOGIN_SELECTOR = ".o-siteheader-section__user .a-user";
    private static final String COLOR_BORDER_HEAD_LINK_RGB = "71, 107, 61";
    private static final String EXPECTED_OPAQUE_RGB_STOP = "rgb(" + COLOR_BORDER_HEAD_LINK_RGB + ")";
    private static final String EXPECTED_OPAQUE_RGBA_STOP = "rgba(" + COLOR_BORDER_HEAD_LINK_RGB + ", 1)";
    private static final String EXPECTED_TRANSPARENT_STOP = "rgba(" + COLOR_BORDER_HEAD_LINK_RGB + ", 0)";
    private static final double EXPECTED_BOX_OPACITY = 0.7;
    private static final double OPACITY_DELTA = 0.01;
    private static final int PAGE_LOAD_TIMEOUT_SEC = 45;
    private static final int HEADER_WAIT_TIMEOUT_SEC = 45;

    private WebDriver driver;
    private final Frontend frontendProperties = new Frontend();

    @Test
    public void headerActionsUseBorderHeadLinkGradient() throws Exception {
        Properties prop = frontendProperties.ReadProperties();
        String url = prop.getProperty("xatakamovil.header.url", DEFAULT_URL);
        driver = openHeadlessBrowser(url);

        WebElement sectionsButton = waitForVisibleCss(HEADER_SECTIONS_SELECTOR);
        WebElement loginButton = waitForVisibleCss(HEADER_LOGIN_SELECTOR);

        String sectionsGradient = backgroundImage(sectionsButton);
        String loginGradient = backgroundImage(loginButton);
        String sectionsOpacity = opacity(sectionsButton);
        String loginOpacity = opacity(loginButton);

        System.out.println("Secciones gradient: " + sectionsGradient);
        System.out.println("Secciones opacity: " + sectionsOpacity);
        System.out.println("Entra o Regístrate gradient: " + loginGradient);
        System.out.println("Entra o Regístrate opacity: " + loginOpacity);

        assertUsesExpectedGradient(sectionsGradient, "Secciones");
        assertUsesExpectedGradient(loginGradient, "Entra o Regístrate");
        assertUsesExpectedOpacity(sectionsOpacity, "Secciones");
        assertUsesExpectedOpacity(loginOpacity, "Entra o Regístrate");
        Assert.assertEquals(loginGradient, sectionsGradient,
                "Secciones and Entra o Regístrate should use the same header action gradient.");
        Assert.assertEquals(loginOpacity, sectionsOpacity,
                "Secciones and Entra o Regístrate should use the same header action opacity.");
    }

    @AfterMethod(alwaysRun = true)
    public void quitDriver() {
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

    private WebDriver openHeadlessBrowser(String url) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless=new");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--window-size=1440,1000");

        ChromeDriver chromeDriver = new ChromeDriver(chromeOptions);
        chromeDriver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT_SEC, TimeUnit.SECONDS);
        chromeDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        try {
            chromeDriver.get(url);
        } catch (TimeoutException e) {
            chromeDriver.executeScript("window.stop();");
        }
        return chromeDriver;
    }

    private WebElement waitForVisibleCss(String selector) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(HEADER_WAIT_TIMEOUT_SEC));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));
    }

    private String backgroundImage(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript(
                "return window.getComputedStyle(arguments[0]).backgroundImage;", element);
    }

    private String opacity(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript(
                "return window.getComputedStyle(arguments[0]).opacity;", element);
    }

    private static void assertUsesExpectedGradient(String gradient, String buttonName) {
        Assert.assertTrue(gradient.startsWith("linear-gradient"),
                buttonName + " should use a linear-gradient background. Actual: " + gradient);
        Assert.assertTrue(gradient.contains(EXPECTED_OPAQUE_RGB_STOP)
                || gradient.contains(EXPECTED_OPAQUE_RGBA_STOP),
                buttonName + " should use color-border-head-link at 100% opacity in the gradient. Actual: "
                        + gradient);
        Assert.assertTrue(gradient.contains(EXPECTED_TRANSPARENT_STOP),
                buttonName + " should fade color-border-head-link to 0% opacity. Actual: " + gradient);
    }

    private static void assertUsesExpectedOpacity(String opacity, String buttonName) {
        Assert.assertEquals(Double.parseDouble(opacity), EXPECTED_BOX_OPACITY, OPACITY_DELTA,
                buttonName + " should render the gradient box at 70% opacity. Actual: " + opacity);
    }
}
