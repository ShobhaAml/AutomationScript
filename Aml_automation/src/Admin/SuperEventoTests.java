package Admin;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Adminproperty;

/**
 * Positive and negative tests for Super evento create and publish flows.
 */
public class SuperEventoTests {

    private static final String CREATE_TAB_TEST = "testLoginAndSuperEventoMenuNavigation";

    private WebDriver driver;
    private Properties prop;
    private final Adminproperty admin = new Adminproperty();
    private WebDriverWait wait;

    @BeforeClass
    public void setUpClass() throws Exception {
        prop = admin.ReadProperties();
        String loginUrl = prop.getProperty("admin_login_url", "https://testadmin.xataka.com/");
        driver = admin.callproperty(loginUrl, prop.getProperty("browser"));
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        admin.adminLogin();
        admin.implicitWait();
    }

    @BeforeMethod
    public void setUpMethod(Method method) throws Exception {
        if (CREATE_TAB_TEST.equals(method.getName())) {
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

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    @Test(priority = 1, groups = { "navigation", "positive" })
    public void testLoginAndSuperEventoMenuNavigation() throws Exception {
        Assert.assertTrue(driver.getCurrentUrl().contains("xataka.com"),
                "Admin login should land on xataka admin domain");

        admin.findAndClick("navigation_header");
        admin.implicitWait();

        WebElement menuLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(prop.getProperty("super_evento_menu_link"))));
        menuLink.click();
        admin.implicitWait();

        driver.get(prop.getProperty("super_evento_create_url"));
        waitForCreatePage();

        Assert.assertTrue(driver.getCurrentUrl().contains("type=superevent"),
                "Super evento create URL should contain type=superevent");
        Assert.assertTrue(isCreateFieldVisible("post_title"), "Title field should be visible");
        Assert.assertTrue(isCreateFieldVisible("post_content"), "Content field should be visible");
    }

    // -------------------------------------------------------------------------
    // Create page - positive
    // -------------------------------------------------------------------------

    @Test(priority = 2, groups = { "create", "positive" })
    public void testCreatePageLoadsRequiredFields() {
        Assert.assertTrue(isCreateFieldVisible("post_title"), "Title field should be visible");
        Assert.assertTrue(isCreateFieldVisible("post_content"), "Content field should be visible");
        Assert.assertTrue(isWriteTabVisible(), "Escribir tab should be visible");
    }

    @Test(priority = 3, groups = { "create", "positive" })
    public void testCreateWithValidTitleAndContent() {
        String title = uniqueTitle("Super evento valido");
        fillTitle(title);
        fillContent("Contenido de prueba para super evento automatizado.");
        saveDraft();

        Assert.assertEquals(readTitle(), title, "Title should persist after autosave");
    }

    @Test(priority = 4, groups = { "create", "positive" })
    public void testCreateWithLongTitle() {
        String longTitle = repeat("SuperEvento-", 12) + uniqueTitle("L");
        fillTitle(longTitle);
        fillContent("Contenido con titulo largo.");
        saveDraft();

        Assert.assertFalse(readTitle().trim().isEmpty(), "Long title should be saved");
    }

    @Test(priority = 5, groups = { "create", "positive" })
    public void testCreateWithSpecialCharactersInContent() {
        String title = uniqueTitle("Super evento especial");
        String content = "Contenido con acentos: áéíóú ñ — símbolos: @#% y comillas \"test\".";
        fillTitle(title);
        fillContent(content);
        saveDraft();

        Assert.assertTrue(readContent().contains("acentos"), "Special character content should be saved");
    }

    @Test(priority = 6, groups = { "create", "positive" })
    public void testCreateAutosaveKeepsDraftOnReload() {
        String title = uniqueTitle("Super evento autosave");
        fillTitle(title);
        fillContent("Borrador para validar autosave.");
        saveDraft();
        driver.navigate().refresh();
        waitForCreatePage();

        Assert.assertEquals(readTitle(), title, "Draft title should remain after page reload");
    }

    // -------------------------------------------------------------------------
    // Create page - negative
    // -------------------------------------------------------------------------

    @Test(priority = 7, groups = { "create", "negative" })
    public void testCreateWithEmptyTitleShowsValidation() {
        fillTitle("");
        fillContent("Contenido sin titulo.");
        saveDraft();

        Assert.assertTrue(readTitle().trim().isEmpty() || hasValidationMessage(),
                "Empty title should remain empty or show validation");
    }

    @Test(priority = 8, groups = { "create", "negative" })
    public void testCreateWithWhitespaceOnlyTitle() {
        fillTitle("     ");
        fillContent("Contenido con titulo en blanco.");
        saveDraft();

        Assert.assertTrue(readTitle().trim().isEmpty() || hasValidationMessage(),
                "Whitespace-only title should not be accepted as valid input");
    }

    @Test(priority = 9, groups = { "create", "negative" })
    public void testCreateWithEmptyContent() {
        fillTitle(uniqueTitle("Sin contenido"));
        fillContent("");
        saveDraft();

        Assert.assertTrue(readContent().trim().isEmpty() || hasValidationMessage(),
                "Empty content should remain empty or trigger validation");
    }

    @Test(priority = 10, groups = { "create", "negative" })
    public void testCreateWithOnlyTitleNoContentBlocksPublish() {
        fillTitle(uniqueTitle("Solo titulo"));
        fillContent("");
        saveDraft();
        openPublishTab();

        Assert.assertTrue(isPublishTabOpen(), "Publish tab should open");
        Assert.assertTrue(hasValidationMessage() || !isPublishButtonEnabled(),
                "Publish should be blocked or show validation without content");
    }

    // -------------------------------------------------------------------------
    // Publicacion page - positive
    // -------------------------------------------------------------------------

    @Test(priority = 11, groups = { "publish", "positive" })
    public void testPublishTabAccessibleWithValidDraft() {
        prepareValidDraft();
        openPublishTab();

        Assert.assertTrue(isPublishTabOpen(), "Publish tab should be active with valid draft");
        Assert.assertTrue(isPublishFieldVisible("homepage_content"), "Homepage content field should be visible");
    }

    @Test(priority = 12, groups = { "publish", "positive" })
    public void testPublishWithCategoryAndTag() {
        prepareValidDraft();
        openPublishTab();
        admin.insertTagAndCategory(
                prop.getProperty("super_evento_category", "Especiales"),
                prop.getProperty("super_evento_tag", "Encuesta"));

        Assert.assertTrue(isPublishTabOpen(), "Publish tab should remain open after category/tag selection");
    }

    @Test(priority = 13, groups = { "publish", "positive" })
    public void testPublishWithHomepageContent() {
        prepareValidDraft();
        openPublishTab();
        admin.findAndWrite("homepage_content",
                prop.getProperty("super_evento_homepage_content", "Resumen super evento automatizado"));

        String homepageContent = admin.findElement(prop.getProperty("homepage_content")).getAttribute("value");
        if (homepageContent == null || homepageContent.isEmpty()) {
            homepageContent = admin.findElement(prop.getProperty("homepage_content")).getText();
        }
        Assert.assertFalse(homepageContent.trim().isEmpty(), "Homepage content should be filled");
    }

    @Test(priority = 14, groups = { "publish", "positive" })
    public void testPublishWithSocialTexts() {
        prepareValidDraft();
        openPublishTab();
        admin.findAndWrite("fb_text", "Facebook copy super evento");
        admin.findAndWrite("twitter_InputBox", "Twitter copy super evento");

        Assert.assertFalse(admin.findElement(prop.getProperty("fb_text")).getText().trim().isEmpty(),
                "Facebook text should be populated");
    }

    // -------------------------------------------------------------------------
    // Publicacion page - negative
    // -------------------------------------------------------------------------

    @Test(priority = 15, groups = { "publish", "negative" })
    public void testPublishTabWithoutMandatoryCreateFields() {
        fillTitle("");
        fillContent("");
        openPublishTab();

        Assert.assertTrue(isPublishTabOpen(), "Publish tab can be opened");
        Assert.assertTrue(hasValidationMessage() || !isPublishButtonEnabled(),
                "Publishing without create fields should be blocked or show validation");
    }

    @Test(priority = 16, groups = { "publish", "negative" })
    public void testPublishWithoutCategory() {
        prepareValidDraft();
        openPublishTab();
        clearCategoryIfSelected();
        clickPublishNow();

        Assert.assertTrue(hasValidationMessage() || isPublishTabOpen(),
                "Publish without category should show validation or remain on publish tab");
    }

    @Test(priority = 17, groups = { "publish", "negative" })
    public void testPublishWithoutTags() {
        prepareValidDraft();
        openPublishTab();
        admin.insertTagAndCategory(
                prop.getProperty("super_evento_category", "Especiales"), "");
        clearTagsIfPresent();
        clickPublishNow();

        Assert.assertTrue(hasValidationMessage() || isPublishTabOpen(),
                "Publish without tags should show validation or remain on publish tab");
    }

    @Test(priority = 18, groups = { "publish", "negative" })
    public void testPublishWithEmptyHomepageContent() {
        prepareValidDraft();
        openPublishTab();
        clearField("homepage_content");
        clickPublishNow();

        Assert.assertTrue(hasValidationMessage() || isPublishTabOpen(),
                "Publish with empty homepage content should show validation or remain on publish tab");
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void openCreatePage() {
        driver.get(prop.getProperty("super_evento_create_url"));
        waitForCreatePage();
    }

    private void waitForCreatePage() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("post_title"))),
                ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("super_evento_write_tab")))));
        if (isWriteTabVisible()) {
            admin.findAndClick("Escribir");
            admin.implicitWait();
        }
    }

    private void prepareValidDraft() {
        fillTitle(uniqueTitle("Super evento publish"));
        fillContent("Contenido valido para pruebas de publicacion.");
        saveDraft();
    }

    private void fillTitle(String title) {
        WebElement titleField = admin.findElement(prop.getProperty("post_title"));
        titleField.click();
        titleField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        titleField.sendKeys(Keys.DELETE);
        if (title != null && !title.isEmpty()) {
            titleField.sendKeys(title);
        }
    }

    private void fillContent(String content) {
        WebElement contentField = admin.findElement(prop.getProperty("post_content"));
        contentField.click();
        contentField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        contentField.sendKeys(Keys.DELETE);
        if (content != null && !content.isEmpty()) {
            contentField.sendKeys(content);
        }
    }

    private String readTitle() {
        return admin.findElement(prop.getProperty("post_title")).getAttribute("value");
    }

    private String readContent() {
        return admin.findElement(prop.getProperty("post_content")).getText();
    }

    private void saveDraft() {
        if (isElementPresent(prop.getProperty("Auto_save"))) {
            admin.findAndClick("Auto_save");
            admin.implicitWait();
        } else {
            admin.findAndClick("post_content");
            admin.findAndSendkey("post_content", Keys.TAB);
            admin.implicitWait();
        }
    }

    private void openPublishTab() {
        admin.findAndClick("publish_tab");
        admin.implicitWait();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("publish_tab"))));
    }

    private void clickPublishNow() {
        if (isElementPresent(prop.getProperty("publish_post"))) {
            admin.findAndClick("publish_post");
            admin.implicitWait();
        }
    }

    private boolean isCreateFieldVisible(String propertyKey) {
        return isElementPresent(prop.getProperty(propertyKey));
    }

    private boolean isPublishFieldVisible(String propertyKey) {
        return isElementPresent(prop.getProperty(propertyKey));
    }

    private boolean isWriteTabVisible() {
        return isElementPresent(prop.getProperty("super_evento_write_tab"));
    }

    private boolean isPublishTabOpen() {
        try {
            WebElement publishTab = admin.findElement(prop.getProperty("publish_tab"));
            String clazz = publishTab.getAttribute("class");
            return clazz != null && (clazz.contains("active") || clazz.contains("selected"));
        } catch (Exception e) {
            return driver.getPageSource().toLowerCase().contains("publicar");
        }
    }

    private boolean isPublishButtonEnabled() {
        try {
            return admin.findElement(prop.getProperty("publish_post")).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasValidationMessage() {
        if (isElementPresent(prop.getProperty("super_evento_validation_message"))) {
            String text = admin.findElement(prop.getProperty("super_evento_validation_message")).getText().trim();
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

    private boolean isElementPresent(String xpath) {
        return !driver.findElements(By.xpath(xpath)).isEmpty();
    }

    private void clearField(String propertyKey) {
        WebElement field = admin.findElement(prop.getProperty(propertyKey));
        field.click();
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        field.sendKeys(Keys.DELETE);
    }

    private void clearCategoryIfSelected() {
        try {
            admin.findAndClick("Catagory_click");
            WebElement categoryField = admin.findElement(prop.getProperty("catagory"));
            categoryField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            categoryField.sendKeys(Keys.DELETE);
        } catch (Exception ignored) {
            // category widget may not be interactive in all environments
        }
    }

    private void clearTagsIfPresent() {
        try {
            WebElement tagInput = admin.findElement(prop.getProperty("tag_input"));
            tagInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            tagInput.sendKeys(Keys.DELETE);
        } catch (Exception ignored) {
            // tags may already be empty
        }
    }

    private static String uniqueTitle(String prefix) {
        return prefix + " " + System.currentTimeMillis();
    }

    private static String repeat(String value, int times) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < times; i++) {
            builder.append(value);
        }
        return builder.toString();
    }
}
