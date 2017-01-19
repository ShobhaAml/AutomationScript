package TestNGpackage;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import Common.Adminproperty;
public class NormalPost
{
	WebDriver driver;
	Properties prop = new Properties();
	String catagoryText="Especiales";
	String tag="Encuesta";
	String postTitle = "Automated Normal Post creation";
	String postContent = "This is for testing. This is for testing \n";
	String Image = "\\car.jpg";
	Adminproperty property = new Adminproperty(); 
	
@Test	
public void Initial() throws Exception
{
	prop = property.ReadProperties();
	driver = property.callproperty(prop.getProperty("url"),prop.getProperty("browser"));
	property.adminLogin();
	property.findAndClick("navigation_header");
	property.findAndClick("create_post_link");
	property.findAndWrite("post_title", postTitle);
	property.findAndWrite("post_content", postContent);
	property.findAndClick("post_title");
	property.implicitWait();
	property.findAndClick("toolbar_image");
	property.uploadPrimaryImage(Image);
	property.findAndClick("Auto_save"); 
	property.findAndClick("publish_tab");
	property.insertTagAndCategory(catagoryText, tag);
    property.addFbTwitterText("fb_text");
}
}
