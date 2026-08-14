package Admin;


import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.annotations.Test;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class Recommendations
{
    String browser="chrome";
    String url="https://chartbeat.com/publishing/dashboard/xataka.com/#_";
    WebDriver driver;
    @Test
    public void chartbeadposts() throws Exception
    {
        
        

        
        if (browser.trim().equalsIgnoreCase("Chrome")) {
            System.setProperty("webdriver.chrome.driver",
                    System.getProperty("user.dir") + "//src//Driverfiles//"
                            + "chromedriver.exe");
           ChromeOptions options = new ChromeOptions();
           options.addArguments("disable-extensions");
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
     
        driver.findElement(By.xpath("html/body/div/div[1]/div[2]/form/input[2]")).sendKeys("klaas@weblogssl.com");
        driver.findElement(By.xpath("html/body/div/div[1]/div[2]/form/input[3]")).sendKeys("27ps6kn6");
        driver.findElement(By.xpath("html/body/div/div[1]/div[2]/form/button")).click();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.findElement(By.xpath("html/body/div[2]/div/div/div[2]/div/div[3]/div[1]/ul/li[2]/span/div[1]/span")).click();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        List<WebElement> lst= driver.findElements(By.className("ng-scope"));
        
       
 /*       for(int i=1;i<=20;i++)            
        {
             posttitel=driver.findElement(By.xpath("html/body/div[2]/div/div/div[3]/div/div[1]/ng-include/div[2]/ul/li["+i+"]/div/div[3]/span")).getText();
             System.out.println(posttitel);
  }*/
        
        List<String> list = new LinkedList<String>();
        for (int i = 1; i <=20; i++) {
            System.out.println(driver.findElement(By.xpath("html/body/div[2]/div/div/div[3]/div/div[1]/ng-include/div[2]/ul/li["+i+"]/div/div[3]/span")).getText());
            list.add(String.valueOf(driver.findElement(By.xpath("html/body/div[2]/div/div/div[3]/div/div[1]/ng-include/div[2]/ul/li["+i+"]/div/div[3]/span")).getText()));
        }
 
        System.out.println("My List : " + list);
        System.out.println("\nHere are the duplicate elements from list : " + findDuplicates(list));
        
       
        getscreenshot();
        
      
    }
 
    
    public void getscreenshot() throws Exception 
    {

        
       /* Robot robot = new Robot();
        BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIO.write(screenShot, "JPG", new File("D:\\screenShot.jpg"));
*/
        
            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File("D:\\screenshot.png"));
    }

    public static Set<String> findDuplicates(List<String> listContainingDuplicates) {
        
        final Set<String> setToReturn = new HashSet<String>();
        final Set<String> set1 = new HashSet<String>();
 
        for (String yourInt : listContainingDuplicates) {
            if (!set1.add(yourInt)) {
                setToReturn.add(yourInt);
            }
        }
        return setToReturn;
    }

}
