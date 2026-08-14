package Frontend.comments;

import java.nio.file.InvalidPathException;
import java.util.Properties;

import javax.naming.spi.DirStateFactory.Result;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import Common.Frontend;

public class addComments
{
    String logintype="Std";  //Standard= Std , Fb=fb, twitter=twitter
    String username="sumit@agilemedialab.in";
    String password="qwerty011";    
    String usersession="2";   // 1-homepage, 2- login from post
    String comment="automate A por el tercer intento de abc sdasd new test new 3434 ";
    
    
    Properties prop = new Properties();
    Frontend frontendProperties= new Frontend();
    String browser="",url="";
    WebDriver driver;
    
    @Test (priority=1)
    public void openbrowser() throws Exception
    {
        prop = frontendProperties.ReadProperties();
        driver = frontendProperties.frontcallproperty(prop.getProperty("url"),
                prop.getProperty("browser"));
        browser = prop.getProperty("browser");
        url=prop.getProperty("url");
    }
    @Test (priority=2,dependsOnMethods="openbrowser")
    public void calllogin(){
        
        if(usersession=="2")
        {
            frontendProperties.findAndClick("cookie");
        }
        else
        {
           // usersession= frontendProperties.login(username, password,  url,  usersession,  logintype);
        }
    }
    
    @Test (priority=3,dependsOnMethods="calllogin")
    public void MovetoPostcomment()
    {
        frontendProperties.implicitWait(); 
       //Click 1st post
       frontendProperties.findAndClick("postcommentlink");
       frontendProperties.implicitWait();                
       if(usersession=="2")
       {
           //login
           frontendProperties.findAndClick("commentEntralogin");
          // frontendProperties.login(username, password,  url,  usersession,  logintype);
           
           usersession=frontendProperties.findElement(prop.getProperty("commentloogeduser")).getText();
           System.out.println(usersession + " successfully logged in from post page");
       }
    }
    
    @Test (priority=4,dependsOnMethods="MovetoPostcomment")
    public void addcomments()
    {
        System.out.println("Let's add Comment");
        frontendProperties.addcomments(comment);
    }
    
   
  /* public void teardown(ITestResult result)
   {
       try
       {
           System.out.println(result.getName());
           frontendProperties.captureScreenshot(driver,"testing" );
           
       }
       catch(Exception e)
       {
           System.out.println(e.getMessage());
       }
       
   }*/
   
   @Test(priority=5,dependsOnMethods="addcomments")
   public void editcomments()  
 {
 	
 	  System.out.println("Let's edit Comment");
 	 WebElement element = driver.findElement(By.className(prop.getProperty("edit_comment_button")));
 	JavascriptExecutor executor = (JavascriptExecutor)driver;
 	executor.executeScript("arguments[0].click();", element);
 System.out.println("Comment has been edited");
}
   }
