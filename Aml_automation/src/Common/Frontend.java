package Common;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
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
                    By.className(prop.getProperty("loggedusername"))).getText()
                    + " Logged in successfully";
        } else {
            name = "User not logged";
        }

        return name;
    }

    public String StandardLogin(String username, String password)
    {
        implicitWait();
        findAndWrite("standard_email", username);
        findAndWrite("standard_password", password);
        findAndClick("standard_button");
        String invalidmessage = "";
        try {
            if (new WebDriverWait(driver, 10).until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath(prop
                            .getProperty("standard_invalid_validation")))) != null) {
                invalidmessage = new WebDriverWait(driver, 10)
                        .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop
                                .getProperty("standard_invalid_validation"))))
                        .getText();
                invalidmessage = "Invalid Login credentials: " + invalidmessage;
            }
        } catch (Exception e) {

        }
        System.out.println(invalidmessage);
        return invalidmessage;
    }

    public void addcomments(String comment)
    {
        String error = "";
        findAndWrite("commentbox", comment);
        implicitWait();
        findAndClick("commentsubmit");
        try {
            System.out.println(new WebDriverWait(driver, 10).until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath(prop
                            .getProperty("commenterror")))));
            if (new WebDriverWait(driver, 10).until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath(prop
                            .getProperty("commenterror")))) != null) {
                error = new WebDriverWait(driver, 10).until(
                        ExpectedConditions.visibilityOfElementLocated(By.xpath(prop
                                .getProperty("commenterror")))).getText();

                System.out.println("ERROR: " + error);
            }
            
        } catch (Exception e) {

        }
        if(error=="") {
            List<WebElement> lst = findElementByClass(prop
                    .getProperty("commentlist"));
            for (WebElement list : lst) {
                if (list.findElement(By.className("comment-content")).getText()
                        .equalsIgnoreCase(comment)) {
                    System.out.println("<b>Added comment: </b>" + comment);
                }
            }
        }
    }
    public Boolean IsElementPresent(By by, WebDriver driver)
    {
        try
        {
            driver.findElement(by);
            return true;
        }
        catch (NoSuchElementException e)
        {
            return false;
        }
       
    }
    public String login(String username, String password, String url, String usersession, String logintype)
    {
      String message="";        
      if(url.contains("testing.") || url.contains("mtest.") || url.contains("test."))
      {
          if(usersession=="1")
          {
              clickMenu("EntraORegistrate");
          }
         
          
        if(logintype=="twiiter")
        {  
        }
        else if(logintype=="fb")
        { 
        }
        else
        {
            message=  StandardLogin(username, password);
            if(usersession=="1")
            {
                if(message=="")
                {
                    message=checkifuserloggedin();
                }
                findAndClick("CloseMenu");
            }
            System.out.println(message);
        }  
        usersession=message;  
      }
      else
      {
          System.out.println("Production site restricted");
          throw new InvalidPathException("Production site restricted", "Production site restricted");
      }
      
      return usersession;
    }
}
