package Admin;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class SlideShowPost
{
    WebDriver driver;
    String Text1 = "Apple";
    String postcatagory = "Especiales";
    String tag = "Encuesta";
    String tittle_data = "Slide show post testing:- Sumt";
    String editor_data = "El aspecto final de Beast v2 era sobrecogedor: un enorme cilindro de 2 metros de altura y casi 150 kg de peso en el que se agrupaban esas 144 Raspberry Pi2. El proyecto se termin� en agosto y se";
    String Slideshow_subtitle_data = "test test test test";
    String Slideshow_desc_data = "test test test";
    String fbtext = "Hi testing for FB content";
    String primaryimage = "//primary.jpg";
    int i = 1;
    int invalidImageCount = 0;
    Adminproperty adminProperties = new Adminproperty();
    Properties prop = new Properties();

    @BeforeMethod
    public void Setup() throws IOException
    {
        prop = adminProperties.ReadProperties();
        driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
    }

    public void login() throws Exception
    {
        adminProperties.adminLogin();
    }

    @Test
    public void Writeboard() throws Exception
    {
        login();
        adminProperties.implicitWait();
        adminProperties.findAndClick("navigation_header");
        adminProperties.findAndClick("navigate_Slideshow");
        adminProperties.findAndWrite("post_title", tittle_data);
        adminProperties.findAndWrite("post_content", editor_data);
        adminProperties.findAndClick("post_content");
        adminProperties.implicitWait();
        adminProperties.findAndClick("toolbar_image");
        adminProperties.implicitWait();
        adminProperties.uploadPrimaryImage(primaryimage);
        adminProperties.implicitWait();
        adminProperties.findAndClick("post_content");
        adminProperties.findAndWrite("post_content", editor_data);
        adminProperties.findAndClick("toolbar_slideshow");
        adminProperties.implicitWait();
        adminProperties.findAndClick("slideshow_addimage");
        adminProperties.findAndClick("slideshow_input_image");
        adminProperties.implicitWait();
        Runtime.getRuntime().exec(System.getProperty("user.dir") + "\\src\\DriverFiles\\fileupload.exe" + " "
                + System.getProperty("user.dir") + "\\src\\Images\\slide.jpg");
        adminProperties.implicitWait();
        adminProperties.findAndWrite("Slideshow_subtitle", Slideshow_subtitle_data);
        adminProperties.findAndWrite("Slideshow_desc", Slideshow_desc_data);
        adminProperties.findAndClick("slide_button");
        adminProperties.findAndClick("slide_button_close");
        adminProperties.implicitWait();
        adminProperties.findAndClick("publish_tab");
        adminProperties.implicitWait();
        adminProperties.insertTagAndCategory(postcatagory, tag);
        adminProperties.implicitWait();
        adminProperties.addFbTwitterText(fbtext);
        adminProperties.implicitWait();
        adminProperties.findAndClick("publish_post");
    }

}
