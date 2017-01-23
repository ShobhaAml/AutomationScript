package Admin;

import java.util.Properties;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class EditPublishedPost
{
    Adminproperty adminproperties = new Adminproperty();
    Properties prop = new Properties();
    WebDriver driver;
    String Postname = "Automated eCommerce Post without homepage Image/content";
    Boolean status = true;

    public void Adminlogin() throws Exception
    {
        prop = adminproperties.ReadProperties();
        driver = adminproperties.callproperty(prop.getProperty("url"),
                prop.getProperty("browser"));
        adminproperties.adminLogin();
    }
  @Test
    public void editPublishedPost() throws Exception
    {
        Adminlogin();

        String catagory = adminproperties
                .getcatagoryname("DashboardEditbuttontr", "DashboardCatagoryname", Postname);
        
        System.out.println(catagory);
        
        if (catagory.equalsIgnoreCase("Ecommerce")) {
            status = adminproperties.clickButton("DashboardEditbuttontr",
                    "DashboardEditbuttontd", Postname,"Dashboardlist");
        } else {
            status = adminproperties.clickButton("DashboardEditbuttontr",
                    "DashboardEditforotherpost", Postname,"Dashboardlist");
        }

        if (status == true) {
            
            String previous_text= adminproperties.findElement(prop.getProperty("post_content")).getText();
            adminproperties.implicitWait();
            adminproperties.findAndClick("post_content");
            adminproperties.findElement(prop.getProperty("post_content")).clear();
            adminproperties.findAndWrite("post_content",
                    "Added new Data by Shobha  to test Edit feature"+previous_text);
            adminproperties.findAndClick("post_title");
        } else {
            Assert.assertFalse(false, "No Post with same Post title exists");
        }
    }
}
