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
    String Postname = "Slide show post testing:- Sumt";
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
            adminproperties.implicitWait();
            adminproperties.findAndClick("post_content");
            adminproperties.findAndWrite("post_content",
                    "Added new Data by Shobha  to test Edit feature");
            adminproperties.findAndClick("post_title");
        } else {
            Assert.assertFalse(false, "No Post with same Post title exists");
        }
    }
}
