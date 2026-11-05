package Admin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
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
 * Fills the superevento create form: start date, start time, end date, end time, then title.
 * Date and time controls are MUI fields. The locator accepts a native input or the picker section list.
 */
public class CreateShotForm {

    private static final String START_DATE = "Fecha de inicio";
    private static final String START_TIME = "Hora de inicio";
    private static final String END_DATE = "Fecha de fin";
    private static final String END_TIME = "Hora de fin";

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

    @Test(description = "Create shot: start date, start time, end date, end time, title")
    public void createShot() {
        String startDate = configuredDate("createshot_start_date", 1);
        String endDate = configuredDate("createshot_end_date", 8);
        String startTime = configuredText("createshot_start_time", "10:00");
        String endTime = configuredText("createshot_end_time", "18:00");
        String title = configuredTitle();

        typeField(START_DATE, startDate);
        typeField(START_TIME, startTime);
        typeField(END_DATE, endDate);
        typeField(END_TIME, endTime);
        addTitle(title);

        assertFieldContains(START_DATE, startDate);
        assertFieldContains(START_TIME, startTime);
        assertFieldContains(END_DATE, endDate);
        assertFieldContains(END_TIME, endTime);
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
        waitForPageReady();
        waitField(START_DATE);
    }

    private void waitForPageReady() {
        wait.until(webDriver -> "complete".equals(
                ((JavascriptExecutor) webDriver).executeScript("return document.readyState")));
    }

    private void typeField(String label, String value) {
        WebElement field = waitField(label);
        field.click();
        String tag = field.getTagName();
        String toType = ("input".equals(tag) || "textarea".equals(tag))
                ? value
                : value.replaceAll("[^0-9]", "");
        try {
            field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            field.sendKeys(Keys.DELETE);
            field.sendKeys(toType);
        } catch (RuntimeException e) {
            new Actions(driver).sendKeys(toType).perform();
        }
        field.sendKeys(Keys.TAB);
    }

    private void assertFieldContains(String label, String value) {
        String actual = fieldText(waitField(label));
        String expectedDigits = value.replaceAll("[^0-9]", "");
        String actualDigits = actual.replaceAll("[^0-9]", "");
        Assert.assertTrue(actualDigits.contains(expectedDigits),
                label + " should contain " + value + " but was '" + actual + "'");
    }

    private String fieldText(WebElement field) {
        String value = field.getAttribute("value");
        if (value == null || value.length() == 0) {
            value = field.getText();
        }
        return value == null ? "" : value;
    }

    private WebElement waitField(String label) {
        try {
            wait.until(webDriver -> markField(label));
        } catch (TimeoutException e) {
            Assert.fail("Field '" + label + "' was not on the page. URL: " + driver.getCurrentUrl()
                    + ". Labels on the page: " + labelsOnPage());
            return null;
        }
        WebElement field = driver.findElement(By.cssSelector("[data-createshot-field=\"" + label + "\"]"));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", field);
        return field;
    }

    private boolean markField(String label) {
        Object result = js.executeScript(FIND_FIELD, label);
        return "OK".equals(result);
    }

    private String labelsOnPage() {
        Object result = js.executeScript(
                "var out=[]; var all=document.querySelectorAll('*');"
                        + "for (var i=0;i<all.length && out.length<30;i++){"
                        + " var t=''; var n=all[i].childNodes;"
                        + " for (var c=0;c<n.length;c++) if(n[c].nodeType===3) t+=n[c].textContent;"
                        + " t=t.replace(/\\s+/g,' ').trim();"
                        + " if(t.length>1 && t.length<60 && /fecha|hora|tulo|dd\\/mm/i.test(t)) out.push(t);"
                        + "} return out.join(' | ');");
        return result == null ? "" : result.toString();
    }

    private static final String FIND_FIELD =
            "var label=arguments[0];"
                    + "function ownText(el){var t=''; var n=el.childNodes;"
                    + " for(var i=0;i<n.length;i++) if(n[i].nodeType===3) t+=n[i].textContent;"
                    + " return t.replace(/\\s+/g,' ').trim();}"
                    + "function visible(el){if(!el) return false; var s=window.getComputedStyle(el);"
                    + " if(s.display==='none'||s.visibility==='hidden') return false;"
                    + " var r=el.getBoundingClientRect(); return r.width>0&&r.height>0;}"
                    + "function nearest(el){var node=el.parentElement;"
                    + " for(var d=0;d<8&&node;d++){"
                    + "  var all=node.querySelectorAll('textarea, input:not([type=hidden]), [contenteditable=true], .MuiInputBase-root, .MuiPickersInputBase-root');"
                    + "  var fields=[];"
                    + "  for(var i=0;i<all.length;i++){var f=all[i];"
                    + "   if(f.getAttribute('aria-hidden')==='true') continue;"
                    + "   if(el.contains(f)) continue;"
                    + "   if(!visible(f)) continue;"
                    + "   fields.push(f);}"
                    + "  if(fields.length===1) return fields[0];"
                    + "  node=node.parentElement;}"
                    + " var list=document.querySelectorAll('textarea, input:not([type=hidden]), [contenteditable=true]');"
                    + " for(var j=0;j<list.length;j++){var f=list[j];"
                    + "  if(f.getAttribute('aria-hidden')==='true'||!visible(f)) continue;"
                    + "  if(el.compareDocumentPosition(f)&4) return f;} return null;}"
                    + "var all=document.querySelectorAll('*');"
                    + "for(var i=0;i<all.length;i++){var t=ownText(all[i]);"
                    + " if(t.indexOf(label)!==-1 && t.length<40){var field=nearest(all[i]);"
                    + "  if(field){field.setAttribute('data-createshot-field', label); return 'OK';}}}"
                    + "var aria=document.querySelector('[aria-label*=\"'+label+'\"]');"
                    + "if(aria){var field=nearest(aria)||aria;"
                    + " if(visible(field)){field.setAttribute('data-createshot-field', label); return 'OK';}}"
                    + "return '';";

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
        WebElement field;
        try {
            field = wait.until(ExpectedConditions.visibilityOfElementLocated(titleLocator()));
        } catch (TimeoutException e) {
            Assert.fail("Title field was not on the page. URL: " + driver.getCurrentUrl()
                    + ". XPath: " + titleLocator());
            return;
        }
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
        return By.xpath("//div[contains(@class,'Title_input')]//textarea"
                + " | //textarea[contains(@placeholder,'Título') or contains(@placeholder,'Titulo')]"
                + " | //*[@id='input-post-title']");
    }

    private String configuredText(String key, String fallback) {
        String configured = prop.getProperty(key, "");
        if (configured != null && configured.trim().length() > 0) {
            return configured.trim();
        }
        return fallback;
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
