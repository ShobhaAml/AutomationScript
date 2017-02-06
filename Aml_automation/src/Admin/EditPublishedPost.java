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
    String Postname = "Automate hello test repost slidepost testing no more sweat:- Sumt";
    String posttype = ""; // Enter 'Slide' for slideshow post
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
        Postname = Postname.trim();

        String catagory = adminproperties.getcatagoryname(
                "DashboardEditbuttontr", "DashboardCatagoryname", Postname);

        System.out.println(catagory);

        if (catagory.equalsIgnoreCase("Ecommerce")) {
            status = adminproperties.clickButton("DashboardEditbuttontr",
                    "DashboardEditbuttontd", Postname, "Dashboardlist");
        } else {
            status = adminproperties.clickButton("DashboardEditbuttontr",
                    "DashboardEditforotherpost", Postname, "Dashboardlist");
        }

        if (status == true) {

            InsertPostdata();

            if (posttype == "Slide") {
                addSlides("\\src\\Images\\car.jpg");
            }

        } else {
            Assert.assertFalse(false, "No Post with same Post title exists");
        }
    }

    public void InsertPostdata()
    {
        String previous_text = adminproperties.findElement(
                prop.getProperty("post_content")).getText();
        adminproperties.implicitWait();
        adminproperties.findAndClick("post_content");
        adminproperties.findElement(prop.getProperty("post_content")).clear();
        adminproperties.findAndWrite("post_content",
                "Added new Data by Shobha  to test Edit feature"
                        + previous_text);
        adminproperties.findAndClick("post_title");
    }

    public void addSlides(String slide) throws Exception
    {
        adminproperties.findAndSendkey("post_content", Keys.END);
        adminproperties.findAndSendkey("post_content", Keys.ENTER);
        adminproperties.findAndClick("toolbar_slideshow");
        adminproperties.implicitWait();
        adminproperties.findAndClick("slideshow_addimage");
        adminproperties.findAndClick("slideshow_input_image");
        adminproperties.implicitWait();
        Runtime.getRuntime().exec(
                System.getProperty("user.dir")
                        + "\\src\\DriverFiles\\fileupload.exe" + " "
                        + System.getProperty("user.dir") + slide);
        adminproperties.implicitWait();
        adminproperties.findAndWrite("Slideshow_subtitle", "Slide 1");
        adminproperties.findAndWrite("Slideshow_desc",
                "Slide Description goes here");
        adminproperties.findAndClick("slide_button");
        adminproperties.findAndClick("slide_button_close");
        adminproperties.implicitWait();
    }

}
