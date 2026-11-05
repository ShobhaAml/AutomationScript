package Admin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Common.Adminproperty;

/**
 * Creates a superevento shot on the React editor:
 * start date, end date, cover image, then title.
 */
public class CreateShotForm {

    private static final String START_LABEL = "Fecha de inicio";
    private static final String END_LABEL = "Fecha de fin";

    private final Adminproperty admin = new Adminproperty();
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    private Properties prop;

    @BeforeClass
    public void setUp() throws Exception {
        prop = admin.ReadProperties();
        driver = admin.callproperty(createUrl(), prop.getProperty("browser", "chrome"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        js = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        loginIfRequired();
        openCreatePage();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(description = "Create shot: start date, end date, image, title")
    public void createShot() {
        String startDate = configuredDate("createshot_start_date", 1);
        String endDate = configuredDate("createshot_end_date", 8);
        String title = configuredTitle();
        File image = imageFile();

        addDate(START_LABEL, startDate);
        addDate(END_LABEL, endDate);
        uploadImage(image);
        addTitle(title);

        Assert.assertEquals(readDate(START_LABEL), startDate, "Start date should stay on the form");
        Assert.assertEquals(readDate(END_LABEL), endDate, "End date should stay on the form");
        Assert.assertTrue(imageUploaded(), "Cover image should be uploaded");
        Assert.assertEquals(readTitle(), title, "Title should stay on the form");
    }

    private void loginIfRequired() {
        if (!isLoginFormVisible()) {
            return;
        }
        WebElement username = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(prop.getProperty("login_username_txt"))));
        username.clear();
        username.sendKeys(prop.getProperty("admin_usename", "").trim());
        WebElement password = driver.findElement(By.xpath(prop.getProperty("login_pwd_txt")));
        password.clear();
        password.sendKeys(prop.getProperty("admin_pwd", "").trim());
        driver.findElement(By.xpath(prop.getProperty("login_submit_button"))).click();
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath(prop.getProperty("login_username_txt"))));
        } catch (TimeoutException e) {
            Assert.fail("Login stayed on the sign-in form. " + loginError());
        }
    }

    private String loginError() {
        String text = driver.findElement(By.tagName("body")).getText().replace('\n', ' ');
        int invalid = text.indexOf("Credenciales");
        int password = text.indexOf("contraseña");
        int start = invalid >= 0 ? invalid : password;
        if (start < 0) {
            return "Check admin_usename and admin_pwd.";
        }
        int end = Math.min(text.length(), start + 120);
        return text.substring(start, end).trim();
    }

    private boolean isLoginFormVisible() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(8));
            WebElement username = shortWait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath(prop.getProperty("login_username_txt"))));
            return username.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void openCreatePage() {
        try {
            driver.get(createUrl());
        } catch (TimeoutException e) {
            // The React editor can skip the load event once the shell is interactive.
        }
        loginIfRequired();
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(dateLabel(START_LABEL)),
                ExpectedConditions.visibilityOfElementLocated(titleLocator())));
    }

    private void addDate(String label, String ddMMyyyy) {
        WebElement input = dateInput(label);
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", input);
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);
        input.sendKeys(ddMMyyyy);
        input.sendKeys(Keys.TAB);
        Assert.assertEquals(readDate(label), ddMMyyyy, label + " should be " + ddMMyyyy);
    }

    private String readDate(String label) {
        WebElement input = dateInput(label);
        String value = input.getAttribute("value");
        if (value == null) {
            value = "";
        }
        return value.replace(" ", "");
    }

    private WebElement dateInput(String label) {
        String key = START_LABEL.equals(label) ? "createshot_start_date_input" : "createshot_end_date_input";
        String custom = prop.getProperty(key, "");
        if (custom != null && custom.trim().length() > 0) {
            return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(custom.trim())));
        }
        WebElement labelElement = wait.until(ExpectedConditions.visibilityOfElementLocated(dateLabel(label)));
        String id = labelElement.getAttribute("for");
        if (id != null && id.length() > 0) {
            return driver.findElement(By.id(id));
        }
        return labelElement.findElement(By.xpath("following::input[1]"));
    }

    private By dateLabel(String label) {
        return By.xpath("//label[contains(normalize-space(.),'" + label + "')]");
    }

    private void uploadImage(File image) {
        Assert.assertTrue(image.isFile(), "Image file should exist: " + image.getAbsolutePath());
        keepFileInputInThePage();
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(imageButton()));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", button);
        button.click();
        WebElement file = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[type='file'][data-createshot-file='1']")));
        file.sendKeys(image.getAbsolutePath());
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[contains(normalize-space(.),'Cambiar')]")));
        } catch (TimeoutException e) {
            Assert.fail("Cover image was not accepted. " + visibleError());
        }
    }

    private void keepFileInputInThePage() {
        js.executeScript(
                "if (!window.__createshotFileClick) {"
                        + " window.__createshotFileClick = HTMLInputElement.prototype.click;"
                        + " HTMLInputElement.prototype.click = function() {"
                        + "   if (this.type === 'file') { this.setAttribute('data-createshot-file','1'); return; }"
                        + "   return window.__createshotFileClick.apply(this, arguments);"
                        + " };"
                        + "}");
    }

    private By imageButton() {
        String custom = prop.getProperty("createshot_image_button", "");
        if (custom != null && custom.trim().length() > 0) {
            return By.xpath(custom.trim());
        }
        return By.xpath("//button[contains(normalize-space(.),'Subir imagen')]");
    }

    private boolean imageUploaded() {
        return !driver.findElements(By.xpath("//button[contains(normalize-space(.),'Cambiar')]")).isEmpty();
    }

    private String visibleError() {
        String text = driver.findElement(By.tagName("body")).getText();
        int marker = text.indexOf("no es una imagen");
        if (marker >= 0) {
            int end = Math.min(text.length(), marker + 180);
            return text.substring(Math.max(0, marker - 40), end).replace('\n', ' ');
        }
        return "";
    }

    private void addTitle(String title) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(titleLocator()));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", field);
        field.click();
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        field.sendKeys(Keys.DELETE);
        field.sendKeys(title);
        field.sendKeys(Keys.TAB);
        Assert.assertEquals(readTitle(), title, "Title should be entered");
    }

    private String readTitle() {
        WebElement field = driver.findElement(titleLocator());
        String value = field.getAttribute("value");
        return value == null ? "" : value;
    }

    private By titleLocator() {
        String custom = prop.getProperty("createshot_title_input", "");
        if (custom != null && custom.trim().length() > 0) {
            return By.xpath(custom.trim());
        }
        return By.xpath("//textarea[contains(@placeholder,'Título') or contains(@placeholder,'Titulo')]");
    }

    private String createUrl() {
        String url = prop.getProperty("createshot_url", "");
        if (url == null || url.trim().length() == 0) {
            return "https://cftestadmin.xatakamovil.com/post/new?type=superevent";
        }
        return url.trim();
    }

    private String configuredDate(String key, int daysFromToday) {
        String configured = prop.getProperty(key, "");
        if (configured != null && configured.trim().length() > 0) {
            return configured.trim();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, daysFromToday);
        return new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
    }

    private String configuredTitle() {
        String configured = prop.getProperty("createshot_title", "");
        if (configured != null && configured.trim().length() > 0) {
            return configured.trim();
        }
        return "Shot automatizado " + new SimpleDateFormat("ddMMyyyy HHmm").format(Calendar.getInstance().getTime());
    }

    private File imageFile() {
        String configured = prop.getProperty("createshot_image", "src/Images/primary.jpg");
        if (configured == null || configured.trim().length() == 0) {
            configured = "src/Images/primary.jpg";
        }
        File image = new File(configured.trim());
        if (!image.isAbsolute()) {
            image = new File(System.getProperty("user.dir"), configured.trim());
        }
        if (!image.isFile()) {
            image = new File(System.getProperty("user.dir"), "Aml_automation/" + configured.trim());
        }
        return image;
    }
}
