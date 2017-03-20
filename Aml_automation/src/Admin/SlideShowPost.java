package Admin;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SlideShowPost {
	WebDriver driver;
	String Text1 = "Apple";
	String postcatagory = "Especiales";
	// String tag = "Televisión";
	String tag = "Apple";
	String catagoryText = "Especiales";
	// String catagoryText= "Divulgación";
	String tittle_data = "Qué bonito es ver la lluvia y no mojarse:- Sumt";
	String editor_data = "El aspecto final de Beast v2 era sobrecogedor: un enorme cilindro de 2 metros de altura y casi 150 kg de peso en el que se agrupaban esas 144 Raspberry Pi2. El proyecto se terminó en agosto y se";
	String Slideshow_subtitle_data = "test test test test";
	String Slideshow_desc_data = "test test test";
	String fbtext = "Hi testing for FB content";
	String primaryimage = "\\5.jpg";
	String twitter_text = "Hi testing for twittt content";
	String browser = "chrome";
	String slides = "1.jpg@##@qwerty@##@qwertyqwerty~3.jpg@##@qwerty@##@qwertyqwerty~https://www.youtube.com/watch?v=RuFra6JxJmg@##@qwerty@##@qwertyqwerty";
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
	public void Writeboard() throws Exception {
		login();
		adminProperties.implicitWait();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement element = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("navigation_header"))));
		adminProperties.findAndClick("navigation_header");
		adminProperties.findAndClick("navigate_Slideshow");
		adminProperties.findAndWrite("post_title", tittle_data);
		adminProperties.findAndWrite("post_content", editor_data);
		adminProperties.findAndClick("post_content");
		adminProperties.implicitWait();
		Thread.sleep(2000);
		adminProperties.uploadPrimaryImage(primaryimage, browser);
		adminProperties.implicitWait();
		adminProperties.findAndClick("post_content");
		adminProperties.findAndWrite("post_content", editor_data);
		adminProperties.implicitWait();
		Thread.sleep(2000);
		adminProperties.implicitWait();
		adminProperties.addslides(slides, browser);
		adminProperties.findAndClick("publish_tab");
		adminProperties.implicitWait();
		adminProperties.insertTagAndCategory(catagoryText, tag);
		adminProperties.implicitWait();
		adminProperties.addFbTwitterText(fbtext, twitter_text);

	}
}
