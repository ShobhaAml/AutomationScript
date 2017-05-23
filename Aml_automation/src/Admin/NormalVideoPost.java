package Admin;

import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import Common.Adminproperty;

public class NormalVideoPost
{
    WebDriver driver;
    Properties prop = new Properties();
    String catagoryText = "Especiales";
    String tag = "Encuesta";
    String postTitle = "Automated Normal Post testing";
    String postContent = "Un equipo conformado por científicos e investigadores de la Universidad de Estocolmo, la Universidad de Uppsala";
    String gifImage = "//animated1.gif";
    String normalImage = "//car.jpg";
    String imageType = "gif";
    String postType = "Video";
    String videoURL = "https://vine.co/v/ehLUIMreaDj";
    String layout = "big";
    boolean insertHomePageContent = true;
    Adminproperty property = new Adminproperty();

    @Test(priority = 0)
    public void Initial() throws Exception
    {
        prop = property.ReadProperties();
        driver = property.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
        property.adminLogin();
        property.findAndClick("navigation_header");
        property.findAndClick("create_post_link");
        property.findAndWrite("post_title", postTitle);
        property.findAndWrite("post_content", postContent);
        property.findAndClick("post_title");
        if (postType == "Video")
            property.videoHandle(videoURL, layout,prop.getProperty("browser"));
        else {
           // property.imageType(imageType, gifImage);
        }
        property.findAndClick("Auto_save");
        property.findAndClick("publish_tab");
    }

    @Test(priority = 1)
    public void insertHomePageContent() throws Exception
    {
        if (insertHomePageContent == true) {
            property.HomePageContent();
            property.insertTagAndCategory(catagoryText, tag);
            property.addFbTwitterText("fb_text",null);
        } else {
            property.insertTagAndCategory(catagoryText, tag);
            property.addFbTwitterText("fb_text",null);
        }
    }
}
