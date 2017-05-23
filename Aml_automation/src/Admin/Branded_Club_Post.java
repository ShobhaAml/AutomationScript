package Admin;

import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class Branded_Club_Post {

	WebDriver driver;
	String Text1 = "Apple";
	String postcatagory = "Especiales";
	String tittle_data = "hello test";
	String editor_data = "El aspecto final de Beast v2 era sobrecogedor: un enorme cilindro de 2 metros de altura y casi 150 kg de peso en el que se agrupaban esas 144 Raspberry Pi2. El proyecto se terminó en agosto y se";
	String Slideshow_subtitle_data = "test test test test";
	String Slideshow_desc_data = "test test test";
	String fbtext = "Hi testing for FB content";
	String primaryimage = "\\4.jpg";
	String BrandedClubName = "Especial BQ";
	String tag = "Encuesta";
	String twitter_data = "#1 #2";
	String videoURL = "https://vine.co/v/h0JZIZ7a5mp";
	String layout = "normal";
	String summary_data = "summary_datasummary_datasummary_datasummary_datasummary_datasummary_datasummary_datasummary_data";
	String actuallization_data = "actuallization_dataactuallization_dataactuallization_dataactuallization_dataactuallization_data";
	String summary_insert_button = "left";
	String name = "";
	String FichaName = "ABCD";
	String mainImage = "\\tyty.jpg";
	String optionalImage = "";
	String Price = "Precio: 1.99€";
	String priceText = "Texto: Comprar ahora";
	String priceURL = "http://www.gearbest.com/cell-phones/pp_308825.html";
	String browser = "chrome";
	int i = 1;
	int invalidImageCount = 0;
	Adminproperty adminProperties = new Adminproperty();
	Properties prop = new Properties();

	@BeforeMethod
	public void Setup() throws IOException {
		prop = adminProperties.ReadProperties();
		driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
	}

	public void login() throws Exception {
		adminProperties.adminLogin();
	}

	@Test
	public void Branded_club() throws Exception {
		login();
		adminProperties.implicitWait();
		adminProperties.findAndClick("navigation_header");
		adminProperties.implicitWait();
		adminProperties.findAndClick("navigate_brandClub");
		adminProperties.findAndWrite("post_title", tittle_data);
		adminProperties.findAndWrite("post_content", editor_data);
		adminProperties.findAndClick("post_title");
		adminProperties.findAndSendkey("post_content", Keys.ENTER);
		adminProperties.implicitWait();
		adminProperties.findAndClick("toolbar_image");
		adminProperties.implicitWait();
		adminProperties.uploadPrimaryImage(primaryimage, browser);
		adminProperties.findAndSendkey("post_content", Keys.ENTER);
		adminProperties.implicitWait();
		adminProperties.findAndClick("post_content");
		adminProperties.findAndWrite("post_content", editor_data);
		adminProperties.findAndClick("post_content");
		adminProperties.findAndSendkey("post_content", Keys.ENTER);
		adminProperties.implicitWait();
		adminProperties.summaryActuallization(summary_data, actuallization_data, summary_insert_button);
		adminProperties.implicitWait();
		adminProperties.findAndSendkey("post_content", Keys.ENTER);
		adminProperties.findAndClick("toolbar_basic");
		adminProperties.findAndSendkey("post_content", Keys.ENTER);
		adminProperties.implicitWait();
		adminProperties.videoHandle(videoURL, layout,prop.getProperty("browser"));
		adminProperties.findAndClick("publish_tab");
		adminProperties.findAndSendkey("post_content", Keys.ENTER);
		adminProperties.implicitWait();
		adminProperties.insertBrandedClub(BrandedClubName, tag);
		adminProperties.implicitWait();
		adminProperties.findAndWrite("twitter_InputBox", twitter_data);
		adminProperties.implicitWait();
		adminProperties.findAndClick("publish_post");

	}
}
