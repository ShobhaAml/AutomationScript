package Frontend.comments;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.Test;

import Common.Frontend;

public class Spam  
{
    String logintype="Std";  //Standard= Std , Fb=fb, twitter=twitter
    String username="shobha@agilemedialab.in";
    String password="shobha";    
    String usersession="2";   // 1-homepage, 2- login from post
    String comment="automate A por el tercer intento de aterrizaje test testing for new";
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
    @Test (priority=2)
    public void calllogin(){
        
        if(usersession=="2")
        {
            frontendProperties.findAndClick("cookie");
        }
        else
        {
            usersession= frontendProperties.login(username, password,  url,  usersession,  logintype);
        }
    }
    
    @Test (priority=3)
    public void MovetoPostcomment()
    {
        frontendProperties.implicitWait(); 
       //Click 1st post
        List<WebElement> list = frontendProperties.findElementByClass(prop.getProperty("commentcountHome"));
        System.out.println(list.size());
            for (WebElement element1 : list) {            
            if (Integer.parseInt(element1.getText()) > 1) {
                System.out.println(element1.getText());
                 element1.click();
                frontendProperties.implicitWait();
                break;
            }
         }
       frontendProperties.implicitWait();                
       if(usersession!="1")
       {
           //login
           frontendProperties.findAndClick("commentEntralogin");
           frontendProperties.login(username, password,  url,  usersession,  logintype);
           
           usersession=frontendProperties.findElement(prop.getProperty("commentloogeduser")).getText();
           System.out.println(usersession + " successfully logged in from post page");
       }
   }
    
    @Test (priority=4)
    public void spam()
    {
        
     System.out.println(frontendProperties.findElement(prop.getProperty("Commentcountpost")).getText().replace("COMENTARIOS", "").trim());
        
     if(Integer.parseInt(frontendProperties.findElement(prop.getProperty("Commentcountpost")).getText().replace("COMENTARIOS", "").trim()) > 0 )
        {
        String commentid;
        List<WebElement> Spamlist= frontendProperties.findElementsByXpath(prop.getProperty("commentlist"));
        System.out.println(Spamlist.size() +"=="+ usersession);
        String commentauthor="";
        
        for(WebElement el:Spamlist)
        {
            String commentauthorId= el.getAttribute("id");
            commentauthor=driver.findElement(By.xpath(".//*[@id='"+commentauthorId+"']/div[@class='comment-author']/p/a")).getText();
            System.out.println(commentauthor);
            
            if(!usersession.equalsIgnoreCase(commentauthor))
            {
                WebElement spam= el.findElement(By.className(prop.getProperty("abuse")));
                System.out.println(spam.getText());
                if(!spam.getText().equals("Reportado"))
                {
                    Actions actions = new Actions(driver);
                    actions.moveToElement(spam);
                    actions.click().build().perform();
                    frontendProperties.implicitWait();
                    String abuse_msg=el.findElement(By.className(prop.getProperty("abuse_msg"))).getText();
                    System.out.println("ABUSE Successsfully: "+abuse_msg);
                    break;
                }
                else
                {
                    System.out.println("Comment was already marked as SPAM by "+ usersession);
                }
            }
        }
       
        }
        else
        {
          System.out.println("No comments");
        }
     }

    @Test (priority=3)
    public void teardown() throws IOException
    {
    	//frontendProperties.FullScreenshot("test.png");
    	frontendProperties.captureScreenshot(driver, "testing.png");
    }
    
}
