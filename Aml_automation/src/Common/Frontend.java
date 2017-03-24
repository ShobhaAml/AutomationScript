package Common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Common.Adminproperty;

public class Frontend extends Adminproperty
{

    public Properties ReadProperties() throws IOException
    {
        FileInputStream inStream = new FileInputStream(
                System.getProperty("user.dir")
                        + "\\src\\Common\\frontend.properties");
        prop.load(inStream);
        return prop;
    }

    public WebDriver frontcallproperty(String url, String browser)
            throws IOException
    {
        if (browser.trim().equalsIgnoreCase("Chrome")) {
            System.setProperty("webdriver.chrome.driver",
                    System.getProperty("user.dir") + "//src//Driverfiles//"
                            + "chromedriver.exe");
           ChromeOptions options = new ChromeOptions();
           options.addArguments("start-maximized");
           driver = new ChromeDriver(options);
        } else {
            System.setProperty("webdriver.gecko.driver",
                    System.getProperty("user.dir") + "//src//Driverfiles//"
                            + "geckodriver.exe");
            driver = new FirefoxDriver();
        }
        driver.get(url);
        if (browser.trim().equalsIgnoreCase("firefox")) {
            driver.switchTo().alert().accept();
            driver.manage().window().maximize();
        } 
        
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        return driver;
    }

    public void clickMenu(String Linktext)
    {
        super.findAndClick("cookie");
        driver.findElement(By.className(prop.getProperty("Menu"))).click();
        super.implicitWait();
        if (Linktext.equalsIgnoreCase("EntraORegistrate")) {
            super.findAndClick("login");
        }
        super.implicitWait();
    }

    public String checkifuserloggedin()
    {
        String name = "";
        driver.findElement(By.className(prop.getProperty("Menu"))).click();
        super.implicitWait();
        if (driver
                .findElement(By.className(prop.getProperty("loggedusername"))) != null) {
            name = driver.findElement(
                    By.className(prop.getProperty("loggedusername"))).getText() +" Logged in successfully";
        } else {
            name = "User not logged";
        }

        return name;
    }
    
    public String StandardLogin(String username, String password)
    {
        
        clickMenu("EntraORegistrate");
        implicitWait();
        findAndWrite("standard_email", username);
        findAndWrite("standard_password", password);
        findAndClick("standard_button");
        String invalidmessage="";
        try
        {
            if(new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("standard_invalid_validation"))))!=null)
            {
                invalidmessage = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("standard_invalid_validation")))).getText();
                invalidmessage  ="Invalid Login credentials: " + invalidmessage;
            }
        } catch (Exception e) {

        }

        if(invalidmessage=="")
        {
            invalidmessage=checkifuserloggedin();
        }

        return invalidmessage;
    }
 }
