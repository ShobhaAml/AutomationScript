package Common;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
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
        if (browser.equalsIgnoreCase("Chrome")) {
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

}
