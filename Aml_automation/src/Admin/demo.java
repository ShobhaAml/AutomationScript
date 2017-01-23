package Admin;

import java.io.IOException;
import java.util.Properties;




import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class demo
{
    WebDriver driver;
    Properties prop = new Properties();
    Adminproperty property = new Adminproperty();
    String postName = "Slide show post testing:- Sumt";
    String type = "Draft";
  
    @Test
    public void draftPublish() throws Exception
    {
        prop = property.ReadProperties();
        driver = property.callproperty(prop.getProperty("url"),prop.getProperty("browser"));
        property.adminLogin();
        property.clickButton("Draftlist_dashboard", "Draftlist_Edit", postName, type);
    }
}
