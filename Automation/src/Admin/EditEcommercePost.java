package Admin;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class EditEcommercePost
{

    Adminproperty adminproperties = new Adminproperty();
    Properties prop = new Properties();
    WebDriver driver;   
    String Postname = "Automated eCommerce Post without homepage Image/content";
    Boolean status=true;
   
    public void Adminlogin() throws Exception
    {
        prop = adminproperties.ReadProperties();
        driver = adminproperties.callproperty(prop.getProperty("url"),
                prop.getProperty("browser"));
        adminproperties.adminLogin();
    }

    @Test(groups = { "EcommerceMovetoDraft", "EcommercewithoutHomeMovetoDraft" }, priority = 1,enabled=false)
    public void draftPublishedPost() throws Exception
    {
       Adminlogin();
       adminproperties.clickButton("DashboardEditbuttontr", "DashboardDraftEcomtd",Postname);
        driver.switchTo().alert().accept();
    }

  

    @Test(groups = { "CreateAndEdit", "CreateAndEditWithouthomapageImage" }, priority = 0)
    public void editPublishedPost() throws Exception
    {
        Adminlogin();
        status=  adminproperties.clickButton("DashboardEditbuttontr", "DashboardEditbuttontd",Postname);
        if(status==true)
        {
        adminproperties.implicitWait();
        adminproperties.findAndClick("post_content");
       
        adminproperties.findAndSendkey("post_content",
                Keys.ENTER);
        adminproperties.findAndWrite("post_content",
                "Added new Data by Shobha  to test Edit feature");
        adminproperties.findAndClick("post_title");
        }
        else
        {
            Assert.assertFalse(false, "No Post with same Post title exists");
        }
    }

}
