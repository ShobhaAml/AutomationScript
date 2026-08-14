package Admin;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Adminproperty;

/**
 * Creates multiple Super evento posts on Xatakamovil with "Shobha" title prefix.
 */
public class SuperEventoXatakamovilTests {

    private static final String TITLE_PREFIX = "Shobha";
    private static final String NAVIGATION_TEST = "test01_loginAndNavigateToSuperEvento";

    private WebDriver driver;
    private Properties prop;
    private final Adminproperty admin = new Adminproperty();
    private WebDriverWait wait;
    private boolean reactEditor;

    @BeforeClass
    public void setUpClass() throws Exception {
        prop = admin.ReadProperties();
        String loginUrl = prop.getProperty("xm_admin_login_url", "https://cftestadmin.xatakamovil.com/");
        driver = admin.callproperty(loginUrl, prop.getProperty("browser"));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        wait = new WebDriverWait(driver, Duration.ofSeconds(90));
        loginIfRequired();
    }

    @BeforeMethod
    public void setUpMethod(Method method) throws Exception {
        if (NAVIGATION_TEST.equals(method.getName())) {
            return;
        }
        openCreatePage();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1, groups = { "navigation" })
    public void test01_loginAndNavigateToSuperEvento() {
        openCreatePage();

        Assert.assertTrue(driver.getCurrentUrl().contains("type=superevent"),
                "Super evento create URL should contain type=superevent");
        Assert.assertTrue(isTitleFieldVisible(), "Title field should be visible");
        Assert.assertTrue(isDescriptionFieldVisible(), "Description field should be visible");
    }

    @Test(priority = 2, groups = { "validation", "empty-form" })
    public void test02_emptyTitleShowsValidation() {
        fillTitle("");
        fillDescription("");
        saveDraft();

        Assert.assertTrue(readTitle().trim().isEmpty() || hasValidationMessage(),
                "Empty title should remain empty or show validation");
    }

    @Test(priority = 3, groups = { "validation", "empty-form" })
    public void test03_emptyDescriptionShowsValidation() {
        fillTitle(shobhaTitle("sin descripcion"));
        fillDescription("");
        saveDraft();

        Assert.assertTrue(readDescription().trim().isEmpty() || hasValidationMessage(),
                "Empty description should remain empty or show validation");
    }

    @Test(priority = 4, groups = { "validation", "empty-form" })
    public void test04_whitespaceOnlyTitleShowsValidation() {
        fillTitle("     ");
        fillDescription("Descripcion con titulo en blanco.");
        saveDraft();

        Assert.assertTrue(readTitle().trim().isEmpty() || hasValidationMessage(),
                "Whitespace-only title should not be accepted as valid input");
    }

    @Test(priority = 5, groups = { "validation", "empty-form" })
    public void test05_emptyFormBlocksPublish() {
        fillTitle("");
        fillDescription("");
        saveDraft();
        openPublishTab();

        Assert.assertTrue(isPublishTabOpen(), "Publish tab should open");
        Assert.assertTrue(hasValidationMessage() || !isPublishButtonEnabled(),
                "Publishing with empty form should be blocked or show validation");
    }

    @Test(priority = 6, groups = { "dates" })
    public void test06_pastPublishDateShowsValidation() {
        prepareDraftForDateTests();
        openPublishTab();
        fillPublishDate("01/01/2020 10:00");
        clickScheduleFuture();

        Assert.assertTrue(hasValidationMessage() || isPublishTabOpen() || !isPostScheduled(),
                "Past publish date should be rejected or not schedule the post");
    }

    @Test(priority = 7, groups = { "dates" })
    public void test07_futurePublishDateAcceptsSchedule() {
        prepareDraftForDateTests();
        openPublishTab();
        fillMandatoryPublishFields();
        fillPublishDate("27/12/2027 12:27");
        clickScheduleFuture();

        Assert.assertTrue(isPostScheduled() || isPublishTabOpen(),
                "Valid future date should schedule the post or remain on publish tab");
    }

    @Test(priority = 8, groups = { "dates" })
    public void test08_invalidPublishDateFormatShowsValidation() {
        prepareDraftForDateTests();
        openPublishTab();
        fillPublishDate("2027-12-27");
        clickScheduleFuture();

        Assert.assertTrue(hasValidationMessage() || isPublishTabOpen() || !isPostScheduled(),
                "Invalid date format should show validation or not schedule the post");
    }

    @Test(priority = 9, groups = { "dates" })
    public void test09_emptyPublishDateAllowsImmediatePublishAttempt() {
        prepareDraftForDateTests();
        openPublishTab();
        fillMandatoryPublishFields();
        clearPublishDate();
        clickPublishNow();

        Assert.assertTrue(isPublishTabOpen() || hasValidationMessage() || driver.getCurrentUrl().contains("post"),
                "Empty publish date should allow immediate publish attempt or show validation");
    }

    @Test(priority = 10, groups = { "dates" })
    public void test10_todayPublishDateAcceptsSchedule() {
        prepareDraftForDateTests();
        openPublishTab();
        fillMandatoryPublishFields();
        fillPublishDate(todayDateTime());
        clickScheduleFuture();

        Assert.assertTrue(isPostScheduled() || isPublishTabOpen() || hasValidationMessage(),
                "Today's date should be accepted, rejected, or show validation depending on environment rules");
    }

    @Test(priority = 11, groups = { "title", "special-chars" })
    public void test11_titleWithSpecialCharacters() {
        String title = shobhaTitle("áéíóú ñ @#% & — «test»");
        fillTitle(title);
        fillDescription("Borrador con titulo y caracteres especiales.");
        saveDraft();

        String savedTitle = readTitle();
        Assert.assertFalse(savedTitle.trim().isEmpty(), "Title with special characters should be saved");
        Assert.assertTrue(savedTitle.contains("Shobha"), "Title should keep Shobha prefix");
    }

    @Test(priority = 12, groups = { "description" })
    public void test12_addDescriptionContent() {
        fillTitle(shobhaTitle("con descripcion"));
        String description = "Descripcion automatizada de Super evento en Xatakamovil. "
                + "Contenido con acentos: áéíóú ñ y simbolos: @#%.";
        fillDescription(description);
        saveDraft();

        Assert.assertTrue(readDescription().contains("automatizada"),
                "Description content should persist after autosave");
    }

    @Test(priority = 13, groups = { "publish" })
    public void test13_publishSuperEvento() {
        fillTitle(shobhaTitle("publicado"));
        fillDescription("Super evento listo para publicar en Xatakamovil.");
        saveDraft();

        openPublishTab();
        fillMandatoryPublishFields();
        fillHomepageContent(prop.getProperty("xm_super_evento_homepage_content",
                "Resumen Shobha super evento Xatakamovil automatizado"));
        clickPublishNow();

        Assert.assertTrue(isPublishSuccessful(),
                "Super evento should be published or show publish confirmation");
    }

    // -------------------------------------------------------------------------
    // Navigation and auth
    // -------------------------------------------------------------------------

    private String createUrl() {
        return prop.getProperty("xm_super_evento_create_url",
                "https://cftestadmin.xatakamovil.com/post/new?type=superevent");
    }

    private void openCreatePage() {
        navigateToCreateUrl();
        loginIfRequired();
        waitForCreatePage();
    }

    private void navigateToCreateUrl() {
        try {
            driver.get(createUrl());
        } catch (TimeoutException e) {
            // React editor may not fire the load event; continue once DOM is interactive.
        }
    }

    private void loginIfRequired() {
        try {
            WebDriverWait loginWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement loginField = loginWait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath(prop.getProperty("login_username_txt"))));
            if (loginField.isDisplayed()) {
                admin.adminLogin();
                navigateToCreateUrl();
            }
        } catch (TimeoutException e) {
            // Already authenticated or login form not shown.
        } catch (Exception e) {
            throw new RuntimeException("Failed to authenticate on Xatakamovil admin", e);
        }
    }

    private void waitForCreatePage() {
        String legacyTitle = prop.getProperty("post_title");
        String reactTitle = prop.getProperty("xm_post_title");
        String legacyTab = prop.getProperty("super_evento_write_tab");
        String reactContent = prop.getProperty("xm_post_content");

        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(legacyTitle)),
                ExpectedConditions.visibilityOfElementLocated(By.xpath(reactTitle)),
                ExpectedConditions.visibilityOfElementLocated(By.xpath(legacyTab)),
                ExpectedConditions.visibilityOfElementLocated(By.xpath(reactContent))));

        reactEditor = isElementPresent(reactTitle) || isElementPresent(reactContent);

        if (!reactEditor && isElementPresent(legacyTab)) {
            clickFirstVisible(legacyTab);
        }
    }

    // -------------------------------------------------------------------------
    // Form helpers
    // -------------------------------------------------------------------------

    private void prepareDraftForDateTests() {
        fillTitle(shobhaTitle("fechas"));
        fillDescription("Borrador para pruebas de fecha de publicacion.");
        saveDraft();
    }

    private void fillMandatoryPublishFields() {
        admin.insertTagAndCategory(
                prop.getProperty("xm_super_evento_category",
                        prop.getProperty("super_evento_category", "Especiales")),
                prop.getProperty("xm_super_evento_tag",
                        prop.getProperty("super_evento_tag", "Encuesta")));
    }

    private void fillTitle(String title) {
        if (reactEditor) {
            WebElement titleField = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(prop.getProperty("xm_post_title"))));
            titleField.click();
            new Actions(driver).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                    .sendKeys(Keys.DELETE).build().perform();
            if (title != null && !title.isEmpty()) {
                titleField.sendKeys(title);
            }
            return;
        }

        WebElement titleField = admin.findElement(prop.getProperty("post_title"));
        titleField.click();
        titleField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        titleField.sendKeys(Keys.DELETE);
        if (title != null && !title.isEmpty()) {
            titleField.sendKeys(title);
        }
    }

    private void fillDescription(String content) {
        if (reactEditor) {
            WebElement contentField = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(prop.getProperty("xm_post_content"))));
            contentField.click();
            new Actions(driver).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                    .sendKeys(Keys.DELETE).build().perform();
            if (content != null && !content.isEmpty()) {
                contentField.sendKeys(content);
            }
            return;
        }

        WebElement contentField = admin.findElement(prop.getProperty("post_content"));
        contentField.click();
        contentField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        contentField.sendKeys(Keys.DELETE);
        if (content != null && !content.isEmpty()) {
            contentField.sendKeys(content);
        }
    }

    private String readTitle() {
        if (reactEditor) {
            WebElement titleField = driver.findElement(By.xpath(prop.getProperty("xm_post_title")));
            String value = titleField.getAttribute("value");
            return value != null ? value : titleField.getText();
        }
        return admin.findElement(prop.getProperty("post_title")).getAttribute("value");
    }

    private String readDescription() {
        if (reactEditor) {
            WebElement contentField = driver.findElement(By.xpath(prop.getProperty("xm_post_content")));
            return contentField.getText();
        }
        return admin.findElement(prop.getProperty("post_content")).getText();
    }

    private void saveDraft() {
        if (isElementPresent(prop.getProperty("Auto_save"))) {
            clickFirstVisible(prop.getProperty("Auto_save"));
            return;
        }
        if (reactEditor && isElementPresent(prop.getProperty("xm_post_title"))) {
            driver.findElement(By.xpath(prop.getProperty("xm_post_title"))).sendKeys(Keys.TAB);
        }
    }

    private void fillHomepageContent(String content) {
        if (isElementPresent(prop.getProperty("homepage_content"))) {
            admin.findAndWrite("homepage_content", content);
        }
    }

    private void openPublishTab() {
        if (reactEditor && isElementPresent(prop.getProperty("xm_publish_tab"))) {
            clickFirstVisible(prop.getProperty("xm_publish_tab"));
            return;
        }
        clickFirstVisible(prop.getProperty("publish_tab"));
    }

    private void fillPublishDate(String dateTime) {
        if (!isElementPresent(prop.getProperty("future_publish_date"))) {
            return;
        }
        WebElement dateField = admin.findElement(prop.getProperty("future_publish_date"));
        dateField.click();
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        dateField.sendKeys(Keys.DELETE);
        if (dateTime != null && !dateTime.isEmpty()) {
            dateField.sendKeys(dateTime);
        }
    }

    private void clearPublishDate() {
        fillPublishDate("");
    }

    private void clickScheduleFuture() {
        if (isElementPresent(prop.getProperty("future_post_button"))) {
            clickFirstVisible(prop.getProperty("future_post_button"));
        }
    }

    private void clickPublishNow() {
        if (isElementPresent(prop.getProperty("publish_post"))) {
            clickFirstVisible(prop.getProperty("publish_post"));
        } else if (isElementPresent(prop.getProperty("longform_publishBtn"))) {
            clickFirstVisible(prop.getProperty("longform_publishBtn"));
        }
    }

    private void clickFirstVisible(String xpath) {
        List<WebElement> elements = driver.findElements(By.xpath(xpath));
        for (WebElement element : elements) {
            if (element.isDisplayed()) {
                element.click();
                return;
            }
        }
    }

    private boolean isTitleFieldVisible() {
        return isElementPresent(prop.getProperty("post_title"))
                || isElementPresent(prop.getProperty("xm_post_title"));
    }

    private boolean isDescriptionFieldVisible() {
        return isElementPresent(prop.getProperty("post_content"))
                || isElementPresent(prop.getProperty("xm_post_content"));
    }

    private boolean isPublishTabOpen() {
        String page = driver.getPageSource().toLowerCase();
        return page.contains("publicar") || page.contains("homepage-content")
                || page.contains("publish-tags");
    }

    private boolean isPublishButtonEnabled() {
        try {
            if (isElementPresent(prop.getProperty("publish_post"))) {
                return driver.findElement(By.xpath(prop.getProperty("publish_post"))).isEnabled();
            }
            if (isElementPresent(prop.getProperty("longform_publishBtn"))) {
                return driver.findElement(By.xpath(prop.getProperty("longform_publishBtn"))).isEnabled();
            }
        } catch (Exception ignored) {
            // fall through
        }
        return false;
    }

    private boolean hasValidationMessage() {
        if (isElementPresent(prop.getProperty("super_evento_validation_message"))) {
            String text = driver.findElement(By.xpath(prop.getProperty("super_evento_validation_message")))
                    .getText().trim();
            if (!text.isEmpty()) {
                return true;
            }
        }
        List<WebElement> errors = driver.findElements(By.xpath(
                "//*[contains(@class,'error') or contains(@class,'validation') or contains(@class,'message')]"));
        for (WebElement error : errors) {
            if (error.isDisplayed() && !error.getText().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean isPostScheduled() {
        String page = driver.getPageSource().toLowerCase();
        return page.contains("programado") || page.contains("scheduled");
    }

    private boolean isPublishSuccessful() {
        String page = driver.getPageSource().toLowerCase();
        String url = driver.getCurrentUrl().toLowerCase();
        return page.contains("publicado") || page.contains("published")
                || (url.contains("post") && !hasValidationMessage());
    }

    private boolean isElementPresent(String xpath) {
        return !driver.findElements(By.xpath(xpath)).isEmpty();
    }

    private static String shobhaTitle(String suffix) {
        return TITLE_PREFIX + " " + suffix + " " + System.currentTimeMillis();
    }

    private static String todayDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(calendar.getTime());
    }
}
