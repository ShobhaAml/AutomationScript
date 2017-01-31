package Common;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Adminproperty
{

    WebDriver driver;
    Properties prop = new Properties();

    public Properties ReadProperties() throws IOException
    {
        FileInputStream inStream = new FileInputStream(
                System.getProperty("user.dir")
                        + "\\src\\Common\\admin.properties");
        prop.load(inStream);
        return prop;
    }

    public WebDriver callproperty(String url, String browser)
            throws IOException
    {
        if (browser.trim().equalsIgnoreCase("Chrome")) {
            System.setProperty("webdriver.chrome.driver",
                    System.getProperty("user.dir") + "//src//Driverfiles//"
                            + "chromedriver.exe");
            driver = new ChromeDriver();
        } else {
            System.setProperty("webdriver.gecko.driver",
                    System.getProperty("user.dir") + "//src//Driverfiles//"
                            + "geckodriver.exe");
            driver = new FirefoxDriver();
        }
        driver.get(url);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        return driver;
    }

    public void uploadPrimaryImage(String primaryimage) throws Exception
    {

        findAndWrite("primary_image_insert", System.getProperty("user.dir")
                + prop.getProperty("image_path") + "\\" + primaryimage);
        findAndClick("primary_image_upload");
        WebElement element1 = findElement(prop
                .getProperty("product_image_bulkupload")
                + prop.getProperty("product_image_bulkupload1"));
        if (element1.getAttribute("href") != null) {
            isLinkBroken(new URL(element1.getAttribute("href")));
            System.out.println(isLinkBroken(new URL(element1
                    .getAttribute("href"))));
        }
        implicitWait();
        findAndClick("primary_noraml_insert");
        implicitWait();
    }

    public void findAndClick(String element)
    {
        findElement(prop.getProperty(element)).click();
    }

    public void findAndWrite(String element, String content)
    {
        findElement(prop.getProperty(element)).sendKeys(content);
    }

    public void findAndSendkey(String element, Keys end)
    {
        findElement(prop.getProperty(element)).sendKeys(end);
    }

    public void adminLogin() throws Exception
    {
        String username = prop.getProperty("admin_usename");
        String pwd = prop.getProperty("admin_pwd");
        findElement(prop.getProperty("login_username_txt")).sendKeys(username);
        findElement(prop.getProperty("login_pwd_txt")).sendKeys(pwd);
        findElement(prop.getProperty("login_submit_button")).click();
    }

    public WebElement findElement(String xpath)
    {
        WebElement element = driver.findElement(By.xpath(xpath));
        return element;
    }

    public List<WebElement> findElementByClass(String className)
    {
        List<WebElement> element = driver.findElements(By.className(className));
        return element;
    }

    public List<WebElement> findElementsByXpath(String xpath)
    {
        List<WebElement> element = driver.findElements(By.xpath(xpath));
        return element;
    }

    public void implicitWait()
    {
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.SECONDS);
    }

    public static String isLinkBroken(URL url) throws Exception
    {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            connection.connect();
            String response = connection.getResponseMessage();
            connection.disconnect();
            return response;
        } catch (Exception exp) {
            return exp.getMessage();
        }
    }

    public void verifyImageActive(WebElement imgElement)
    {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(imgElement.getAttribute("src"));
            HttpResponse response = client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFbTwitterText(String fbtext)
    {
        implicitWait();
        findElement(prop.getProperty("fb_text")).sendKeys(fbtext);
        findElement(prop.getProperty("publish_post")).click();
        implicitWait();
        System.out.println("Published post");
    }

    public void insertTagAndCategory(String postcatagory, String tag)
    {
        findAndClick("Catagory_click");
        findAndWrite("catagory", postcatagory);
        List<WebElement> optionlist = findElementByClass(prop
                .getProperty("catagory_ecommerce_by_ClassName"));
        for (WebElement options : optionlist) {
            if (options.getText().equalsIgnoreCase(postcatagory)) {
                options.click();
                break;
            }
        }
        findAndWrite("tag_input", tag);
        List<WebElement> Tagoptionlist = findElementByClass(prop
                .getProperty("tag_list_Byclassname"));
        for (WebElement options : Tagoptionlist) {
            if (options.getText().equalsIgnoreCase(tag)) {
                options.click();
                break;
            }
        }

    }

    public void Imagestatus(WebElement element) throws Exception
    {
        if (element.getAttribute("href") != null) {
            isLinkBroken(new URL(element.getAttribute("href")));
            System.out.println(isLinkBroken(new URL(element
                    .getAttribute("href"))));
        }
    }

    public void Conditionalwait(String xpath)
    {
        WebDriverWait wait = new WebDriverWait(driver, 50);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop
                .getProperty(xpath))));
    }

    public Boolean clickButton(String row, String Column, String Postname,
            String listtype)
    {
        int cnt = 1;
        Boolean status = false;
        List<WebElement> postlist = null;
        if (listtype == "Dashboardlist") {
            postlist = findElementsByXpath(prop.getProperty("Dashboardlist"));
        } else if (listtype == "Draft") {
            postlist = findElementsByXpath(prop
                    .getProperty("Draftlist_dashboard"));

        }

        for (WebElement list : postlist) {
            System.out.println(list.getText());
            if (list.getText().equalsIgnoreCase(Postname)) {
                if (listtype == "Dashboardlist") {
                    findElement(
                            prop.getProperty(row) + "[" + cnt + "]"
                                    + prop.getProperty(Column)).click();
                } else if (listtype == "Draft") {

                    Actions action = new Actions(driver);
                    action.moveToElement(
                            findElement(prop.getProperty(row) + "[" + cnt + "]"
                                    + "/a")).perform();

                    findElement(
                            prop.getProperty(row) + "[" + cnt + "]"
                                    + prop.getProperty(Column)).click();
                }

                status = true;
                break;
            }

            cnt++;
        }

        return status;
    }

    public String getcatagoryname(String row, String Column, String Postname)
    {
        String catgoryname = "";
        int cnt = 1;
        Boolean status = false;
        List<WebElement> postlist = findElementsByXpath(prop
                .getProperty("Dashboardlist"));
        for (WebElement list : postlist) {
            System.out.println(list.getText());
            if (list.getText().equalsIgnoreCase(Postname)) {

                catgoryname = findElement(
                        prop.getProperty(row) + "[" + cnt + "]"
                                + prop.getProperty(Column)).getText();
                break;
            }

            cnt++;
        }

        return catgoryname;
    }

    public void repostCheckbox(String Selector1, String Selector2)
    {
        WebElement CheckBox1 = driver.findElement(By
                .cssSelector("input[value='" + Selector1 + "']"));
        CheckBox1.click();

        WebElement CheckBox = driver.findElement(By.cssSelector("input[value='"
                + Selector2 + "']"));
        CheckBox.click();

    }

    public void summaryActuallization(String summary_data,
            String actuallization_data, String summary_insert_button)
            throws Exception
    {
        findElement(prop.getProperty("toolbar_summary")).click();
        findElement(prop.getProperty("summary_input_field")).sendKeys(
                summary_data);
        Thread.sleep(3000);
        implicitWait();
        switch (summary_insert_button) {
        case "left":
            implicitWait();
            findElement(prop.getProperty("summary_insert_left")).click();
            break;
        case "right":
            implicitWait();
            findElement(prop.getProperty("summary_insert_right")).click();
            break;
        case "center":
            implicitWait();
            findElement(prop.getProperty("summary_insert_center")).click();
            break;
        }
        implicitWait();
        findElement(prop.getProperty("toolbar_Advance")).click();
        findElement(prop.getProperty("toolbar_actuallization")).click();

        findElement(prop.getProperty("actuallization_input_field")).sendKeys(
                actuallization_data);

        implicitWait();
        findElement(prop.getProperty("actuallization_insert_button")).click();
        implicitWait();
    }

    public void repost_By_Difundir(String Selector1, String Selector2,
            String tittle_data, String navigate_blog) throws Exception
    {
        findElement(prop.getProperty("difundir_Link")).click();
        findElement(prop.getProperty("repost_list_button")).click();
        implicitWait();
        repostCheckbox(Selector1, Selector2);
        implicitWait();
        findElement(prop.getProperty("repost_post_button")).click();
        Thread.sleep(3000);
        driver.navigate().to(navigate_blog);
        adminLogin();
        implicitWait();
        Thread.sleep(5000);
        driver.navigate().refresh();
        driver.navigate().refresh();
        driver.navigate().refresh();
        implicitWait();
        findElement(prop.getProperty("notification_button")).click();
        implicitWait();
        clickNotificationButton("tittle_data");
    }

    public void clickNotificationButton(String tittle_data)
    {
        int cnt = 1;
        List<WebElement> postlist = findElementByClass(prop
                .getProperty("notification_list_by_ClassName"));
        for (WebElement list : postlist) {
            String sender = driver.findElement(
                    By.className(prop.getProperty("notify_sender"))).getText();
            String text = list.getText().replace(sender, "");
            if (text.trim().equalsIgnoreCase(tittle_data)) {
                System.out.println(cnt + "hi");
                Actions act = new Actions(driver);
                act.doubleClick(
                        driver.findElement(By.className(prop
                                .getProperty("notify_sender")))).build()
                        .perform();
                driver.findElement(By.className("actions-approve")).click();
                break;
            }
            cnt++;
        }
    }
}
