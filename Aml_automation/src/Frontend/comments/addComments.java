package Frontend.comments;

import java.nio.file.InvalidPathException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import Common.Frontend;

public class addComments
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
       frontendProperties.findAndClick("postcommentlink");
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
    public void addcomments()
    {
        System.out.println("Let's add Comment");
        frontendProperties.addcomments(comment);
    }
    
  
   
}
