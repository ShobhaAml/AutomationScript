package slide_show;

import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RepostDifundir
{

    WebDriver driver;
    String Text1 = "Apple";
    String postcatagory = "Especiales";
    String tag = "Encuesta";
    String tittle_data = "Automate hello test repost slidepost testing no more sweat:- Sumt";
    String editor_data = "El aspecto final de Beast v2 era sobrecogedor: un enorme cilindro de 2 metros de altura y casi 150 kg de peso en el que se agrupaban esas 144 Raspberry Pi2. El proyecto se terminó en agosto y se <!--more-->";
    String Slideshow_subtitle_data = "test test test test";
    String Slideshow_desc_data = "test test test";
    String fbtext = "Hi testing for FB content";
    String primaryimage = "//2.jpg";
    String slide_video_data = "https://www.youtube.com/watch?v=n6m4lZ1TL0s";
    String Selector1 = "input[value='trendencias']";
    String Selector2 = "input[value='xatakafoto']";
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
        WebDriverWait wait = new WebDriverWait(driver, 10);
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
        // for(int i=0; i<=1;i++){
        adminProperties.findAndClick("slideshow_addimage");
        adminProperties.implicitWait();
        adminProperties.findAndClick("slideshow_input_image");
        adminProperties.implicitWait();
        adminProperties.implicitWait();
        Runtime.getRuntime().exec(System.getProperty("user.dir") + "\\src\\DriverFiles\\fileupload.exe" + " "
                + System.getProperty("user.dir") + "\\src\\Images\\3.jpg");
        adminProperties.findAndWrite("Slideshow_subtitle", Slideshow_subtitle_data);
        adminProperties.findAndWrite("Slideshow_desc", Slideshow_desc_data);
        adminProperties.findAndClick("slide_button");
        Thread.sleep(10000);
        adminProperties.implicitWait();
        Thread.sleep(3000);
        adminProperties.findAndClick("slide_video_button");
        adminProperties.findAndWrite("slide_video_url", slide_video_data);
        adminProperties.findAndWrite("Slideshow_subtitle", Slideshow_subtitle_data);
        adminProperties.findAndWrite("Slideshow_desc", Slideshow_desc_data);
        Thread.sleep(5000);
        adminProperties.implicitWait();
        adminProperties.findAndClick("slide_button");
        adminProperties.implicitWait();
        adminProperties.findAndClick("slide_button_close");
        adminProperties.implicitWait();
        adminProperties.implicitWait();
        WebElement element = wait
                .until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("publish_tab"))));
        adminProperties.findAndClick("publish_tab");
        adminProperties.implicitWait();
        adminProperties.insertTagAndCategory(postcatagory, tag);
        adminProperties.implicitWait();
        adminProperties.addFbTwitterText(fbtext);
        adminProperties.implicitWait();
        adminProperties.findAndClick("publish_post");
        adminProperties.implicitWait();
        WebElement element9 = wait
                .until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("difundir_Link"))));
        adminProperties.findAndClick("difundir_Link");
        adminProperties.findAndClick("repost_list_button");
        adminProperties.implicitWait();
        adminProperties.repostCheckbox(Selector1, Selector2);
        adminProperties.implicitWait();
        adminProperties.findAndClick("repost_post_button");
        Thread.sleep(3000);
        // adminProperties.findAndClick("clear_repost_popup");
        driver.navigate().to("https://testing.xatakafoto.com/admin");
        adminProperties.adminLogin();
        adminProperties.implicitWait();
        driver.navigate().refresh();
        driver.navigate().refresh();
        adminProperties.implicitWait();
        adminProperties.findAndClick("notification_button");
        adminProperties.implicitWait();
        adminProperties.clickNotificationButton(tittle_data);

    }

}
