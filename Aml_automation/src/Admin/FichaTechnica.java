package Admin;

import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import Common.Adminproperty;

public class FichaTechnica
{
    WebDriver driver;
    Properties prop = new Properties();
    String catagoryText = "Especiales";
    String tag = "Encuesta";
    String postTitle = "Automated Ficha Tecnica";
    String postContent = "Un equipo conformado por científicos e investigadores de la Universidad de Estocolmo, la Universidad de Uppsala";
    String Image = "\\car.jpg";
    String videoURL = "https://www.youtube.com/watch?v=5Qjfp93sIz0";
    String layout = "big";
    boolean insertHomePageContent = true;
    String post = "video";
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
        property.uploadPrimaryImage(Image, prop.getProperty("browser"));
        property.addNewlines();
        property.fichaTechnica();
        property.findAndClick("publish_tab");
    }

    @Test(priority = 1)
    public void insertHomePageContent()
    {
        if (insertHomePageContent == true) {
            property.HomePageContent();
            property.insertTagAndCategory(catagoryText, tag);
            property.addFbTwitterText("fb_text", "twitter_text");
        } else {
            property.insertTagAndCategory(catagoryText, tag);
            property.addFbTwitterText("fb_text", "twitter_text");
        }
    }

    @Test(enabled = false)
    public void NameValidation()
    {
        property.findAndClick("toolbar_Advance");
        property.findAndClick("toolbar_fichatechnica");
        property.findAndClick("Ficha_insertButton");
        WebElement NameField = property.findElement(prop.getProperty("Ficha_name"));
        String NameText = NameField.getAttribute("value");
        if (NameText.isEmpty()) {
            WebElement validate1 = property.findElement(prop.getProperty("NameValidation"));
            System.out.println("Validation message for NAME--" + validate1.getText());
        }
    }

    @Test(enabled = false)
    public void URLValidation()
    {
        property.findAndClick("toolbar_Advance");
        property.findAndClick("toolbar_fichatechnica");
        property.findAndWrite("Ficha_name", "name");
        property.findAndWrite("Ficha_price", "price");
        property.findAndWrite("Ficha_text", "Text");
        property.findAndClick("Ficha_insertButton");
        WebElement URLField = property.findElement(prop.getProperty("Ficha_URL"));
        String URLText = URLField.getAttribute("value");
        if (URLText.isEmpty()) {
            WebElement validate2 = property.findElement(prop.getProperty("URLValidation"));
            System.out.println("Validation message for URL--" + validate2.getText());
        }
    }
}
