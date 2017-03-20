package Admin;

import java.util.Properties;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import Common.Adminproperty;

public class LeadVideo
{
    WebDriver driver;
    Properties prop = new Properties();
    Adminproperty property = new Adminproperty();
    String catagoryText = "Especiales";
    String tag = "Encuesta";
    String postTitle = "Automated Lead Video Chrome";
    String postContent = "Si Facebook Inc no había tenido suficiente con plantar el concepto de las historias efímeras en Instagram, WhatsApp y en la propia red social matriz, Facebook, ahora le ha llegado el turno a otra de sus aplicaciones";
    String videoURL = "https://www.facebook.com/netflix/videos/875774479221068/";
    String layout = "normal";
    String browser = "firefox";

    @Test
    public void Initial() throws Exception
    {
        prop = property.ReadProperties();
        driver = property.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
        property.adminLogin();
        property.implicitWait();
        property.findAndClick("navigation_header");
        property.findAndClick("LeadVideo");
        property.findAndWrite("post_title", postTitle);
        property.findAndWrite("post_content", postContent);
        property.addNewline();
        Assert.assertTrue("Validation is there", property.findElement(prop.getProperty("LeadValidationMessage"))
                .getText().equalsIgnoreCase("Por favor, incluye el vídeo en el artículo antes que cualquier imagen"));
        property.videoHandle(videoURL, layout, browser);
        Assert.assertTrue("Validation is there",
                property.findElement(prop.getProperty("LeadValidationMessage")).getText().equalsIgnoreCase(
                        "No hay texto destacado en negrita ni en el primer ni en el último párrafo. Recuerda que debes destacar en negrita el tema principal del artículo"));
        property.findAndWrite("post_content", "some text");
        property.findAndClick("publish_tab");
        property.insertTagAndCategory(catagoryText, tag);
        property.addFbTwitterText("fb_text", "text");
    }
}
