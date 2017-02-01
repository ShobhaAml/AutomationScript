package Admin;

import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Common.Adminproperty;

public class DraftPublish
{
    WebDriver driver;
    Properties prop = new Properties();
    Adminproperty property = new Adminproperty();
    String postName = "Automated Normal Post testing";
    String type = "Draft";
    String catagoryText = "Especiales";
    String tag = "Encuesta";

    @BeforeClass
    public void Initial() throws Exception
    {
        prop = property.ReadProperties();
        driver = property.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
        property.adminLogin();
    }

    @Test
    public void draftPublish()
    {
        property.implicitWait();
        property.clickButton("Draftlist_dashboard", "Draftlist_Edit", postName, type);
        property.implicitWait();
        property.findAndClick("publish_tab");
        WebElement categoryBox = property.findElement(prop.getProperty("Catagory_click"));
        String tagBox = property.findElement(prop.getProperty("tag_input")).getText();
        String fbText = property.findElement(prop.getProperty("fb_text")).getText();
        if (((categoryBox.isSelected()) == false) || ((tagBox.isEmpty()) == false) || (fbText.isEmpty()) == false) {
            property.implicitWait();
            property.insertTagAndCategory(catagoryText, tag);
            property.addFbTwitterText("fb_text");
        }
    }
}
