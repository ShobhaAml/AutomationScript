package Common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class Frontend extends Adminproperty
{
    WebDriver driver;
    Properties prop = new Properties();

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
            driver = new ChromeDriver();
        } else {
            System.setProperty("webdriver.gecko.driver",
                    System.getProperty("user.dir") + "//src//Driverfiles//"
                            + "geckodriver.exe");
            driver = new FirefoxDriver();
        }
        driver.get(url);
        if (browser.trim().equalsIgnoreCase("firefox")) 
        {
            driver.switchTo().alert().accept();
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        return driver;
    }
    

    
    public void clickMenu(String Linktext)
    {   
        System.out.println("hi");
        driver.findElement(By.xpath(".//*[@id='cookies-overlay']/div/a")).click();
        driver.findElement(By.className("head-link")).click();
        implicitWait();
        if(Linktext.equalsIgnoreCase("EntraORegistrate"))
        {
            findAndClick("EntraORegistrate");
        }
        implicitWait();

    }

}
