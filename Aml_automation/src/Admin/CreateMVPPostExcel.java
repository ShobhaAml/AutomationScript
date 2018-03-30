package Admin;

import java.sql.Connection;
import java.util.List;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import Common.Adminproperty;

public class CreateMVPPostExcel {
	int invalidImageCount = 0;
	WebDriver driver;
	Adminproperty adminProperties = new Adminproperty();
	Properties prop = new Properties();
	String Postdata, posttitle, postcontent, primaryimage, postcontent_more, images, productname, productOrder,
			productDesc, productLink, productLinktext = "";
	String productPrice, productCatagory, homecontent, homeImage, postcatagory, postcatagoryOther, tag, seotitle,
			seodesc, specialpost = "";
	String author, Twittertext, fbtext, allowHomepageImage, allowHomepageContent = "";
	String browser = "";
	String toolbarstatus = "B";

	@BeforeMethod
	public void Setup() throws Exception {
		prop = adminProperties.ReadProperties();
		driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
		browser = prop.getProperty("browser");
	}

	@DataProvider(name = "testdata")
	public Object[][] TestDataFeed() throws Exception {
		Object[][] postdata = adminProperties.readExcel("Normal", 77);
		return postdata;
	}

	@Test(dataProvider = "testdata")
	public void createPost(String posttype, String posttitle, String postcontent, String primaryimage,
			String postcontent_more, String Youtube_Video, String Youtube_layout, String Vine, String Vine_layout,
			String Vimeo, String Vimeo_layout, String Gallery_name, String Gallery_description, String Gallery_tag,
			String Gallery_ShowHeader, String Gallery_photos, String multiple_images, String embeded_code,
			String summary, String summary_layout, String actualizacion, String ficha_technica, String ficha_review,
			String giphy_url, String giphy_layout, String giphy_caption, String Inforgram_datawrapper_URL,
			String infographLayout, String infographCaption, String slideshowimages, String tabledata,
			String Checkbox_same_width, String Checkbox_table_first_row_heading,
			String Checkbox_table_first_column_heading, String Checkbox_table_occupy_all_avaiable_width,
			String Recipe_name, String Recipe_Person, String Recipe_level, String Recipe_ingredients,
			String Recipe_ingredients_Cantidad, String Recipe_ingredients_units, String Recipe_ingredients_Detalles,
			String Preparation_time_hours, String Preparation_time_Mintues, String Cooking_time_hours,
			String Cooking_time_minutes, String Rest_time_hours, String Rest_time_mintues, String Recipe_postcontent,
			String Recipe_Image, String Recipe_More_postcontent, String Recipe_Youtube_Video,
			String Recipe_Youtube_Video_layout, String Vine_Video, String Recipe_Vine_Video_layout, String Vimeo_Video,
			String Recipe_Vimeo_Video_layout, String FB_Video, String Recipe_FB_Video_layout, String Recipe_summary,
			String Recipe_summary_layout, String homecontent, String homeimage, String Branded_club, String category,
			String catagory_other, String tag, String seotitle, String seodesc, String specialpost,
			String comment_closed, String author, String Twittertext, String fbtext, String Repost, String Run,
			String Republish) throws Exception {
		if (Run.trim().equalsIgnoreCase("Y")) {
			String blogrole = "";
			adminProperties.LoginAdmin(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
			
			adminProperties.CreateMVPpost();

			adminProperties.implicitWait();
			adminProperties.AddMVPTitle("Testing title");
			adminProperties.implicitWait();
			List<WebElement> list = adminProperties.findElementsByXpath(prop.getProperty("MVPSectionList"));
			System.out.println(list.size() +"list count");
			System.out.println(list.get(list.size()-1).getAttribute("id"));

			if (!postcontent.equalsIgnoreCase("null")) {
				/*adminProperties.findAndWrite("post_content", postcontent);
				adminProperties.addNewline();*/
			}
			adminProperties.implicitWait();
			
			if (!(primaryimage.equalsIgnoreCase("null"))) {
				adminProperties.ClickImageICON(driver);
				adminProperties.implicitWait();

			}
			
		}
	}

	public void movecursorpostion(String browser) {
		if (browser.trim().equalsIgnoreCase("Chrome")) {
			Actions action = new Actions(driver);
			action.sendKeys(Keys.PAGE_DOWN);
			adminProperties.implicitWait();
			action.click(driver.findElement(By.partialLinkText("Escribir"))).perform();
			adminProperties.implicitWait();
			adminProperties.findAndClick("post_title");
			adminProperties.implicitWait();
		} else {
			adminProperties.implicitWait();
			adminProperties.findAndClick("post_title");
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,0)");
			adminProperties.findAndClick("post_title");
		}
	}

}
