package Frontend.respuestas;

import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import Common.Frontend;

public class createRespuestas
{
    Date date = new Date();
    String logintype="Std";  //Standard= Std , Fb=fb, twitter=twitter
    String username="shobha@agilemedialab.in";
    String password="shobha";    
    String usersession="2";   // 1-homepage, 2- login from respuestas
    String respuestasquestion="automate A por testing " + date.toString();
    String description="respuestas desc";
    Properties prop = new Properties();
    Frontend frontendProperties= new Frontend();
    String browser="",url="";
    WebDriver driver;
    
    @Test (priority=1)
    public void openbrowser() throws Exception
    {
        prop = frontendProperties.ReadProperties();
        driver = frontendProperties.frontcallproperty(prop.getProperty("url")+"/respuestas",
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
    public void addRespuestas()
    {
        frontendProperties.findAndWrite("respuestatextbox",respuestasquestion );
        frontendProperties.findAndClick("respuestasbutton");
    
        if(usersession!="1")
        {
            frontendProperties.login(username, password,  url,  usersession,  logintype);            
            usersession=frontendProperties.checkifuserloggedin();
            System.out.println(usersession + "from Respuestas page");
            frontendProperties.implicitWait();
            System.out.println("hi click button");
            frontendProperties.findAndClick("CloseMenu");
            frontendProperties.implicitWait();
            frontendProperties.findAndClick("respuestasbutton");
        }
        frontendProperties.findAndWrite("respuestasdescopcional", description);
        frontendProperties.findAndClick("respuestascat");
        frontendProperties.findAndClick("respuestastag");
       // frontendProperties.findAndClick("respuestastag1");
        frontendProperties.findAndClick("respuestasSumit");
        
        if(frontendProperties.findElement(".//*[@id='questionstatus']").getAttribute("display")!="none")
        {
            System.out.println(" Oops Error occured, this Respuestas question was already created. Please choose the different");
        }
        else
        { 
            System.out.println("Respuestas created successfully");
        }
    
        
    }
   
}
