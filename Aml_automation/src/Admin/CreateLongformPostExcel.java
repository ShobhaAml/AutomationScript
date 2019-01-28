package Admin;

import java.sql.Connection;
import java.util.List;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
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

public class CreateLongformPostExcel {
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
		Object[][] postdata = adminProperties.readExcel("Normal", 86);
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
			String Recipe_summary_layout, String Pivot_amazon_search,String Pivot_dropdown,
			String Pivot_otherStoreProductTitle, String Pivot_otherStoreProductImage, String Pivot_otherStorevalues,
			String Pivot_newsletter, String homecontent, String homeimage, String Branded_club, String category,
			String catagory_other, String tag, String seotitle, String seodesc, String specialpost,
			String comment_closed, String author, String Twittertext, String fbtext, String Repost, String Run,
			String Republish,String Future_time, String Pivot_product_Article, String Pivot_product_Article_posttype) throws Exception {
		if (Run.trim().equalsIgnoreCase("Y")) {
			String blogrole = "";
			adminProperties.LoginAdmin(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
			/*
			 * Connection conn = adminProperties.connectDb(); String arrlogin =
			 * adminProperties.checkuserlogintype(conn
			 * ,prop.getProperty("admin_usename"),prop.getProperty("admin_pwd")) ;
			 * if(arrlogin!=null) { String[] logintypes=arrlogin.split("@##@"); blogrole=
			 * logintypes[1]; if(blogrole==null) { blogrole="Admin"; }
			 * System.out.println(blogrole+" account"); }
			 */
			adminProperties.findAndClick("navigation_header");
			if (blogrole.contains("Branded")) {
				adminProperties.findAndClick("create_brandlongform_link");
			} else {
				adminProperties.findAndClick("create_normallongform_link");
			}

			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			driver.switchTo().window(tabs.get(1));
			adminProperties.implicitWait();

			if (!posttitle.equalsIgnoreCase("null")) {
				adminProperties.findAndWrite("longform_title", posttitle);
			}
			driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div/div[1]")).click();
			Actions act = new Actions(driver);
			act.moveToElement(driver.findElement(By.xpath(prop.getProperty("longform_clickplus"))))
					.moveToElement(driver.findElement(By.xpath(prop.getProperty("longform_clickTextIcon")))).build().perform();
			Thread.sleep(2000);
			if(!postcontent.equalsIgnoreCase("null")) {
			act.sendKeys(postcontent);}
			act.build().perform();
			Thread.sleep(25000);
			 

			/*
			 * adminProperties.findAndClick("longform_homepageImagePlusIcon");
			 * adminProperties.findAndClick("longform_addtogalleryButton");
			 * adminProperties.findAndClick("longform_resourcepanel");
			 * 
			 * driver.switchTo().defaultContent(); // you are now outside both frames
			 * driver.switchTo().frame("cq-cf-frame");
			 * driver.findElement(By.xpath("//button[text()='OK']")).click();
			 */

			//Clicking Publicar Tab
			Actions action = new Actions(driver);
			action.moveToElement(driver.findElement(By.xpath(prop.getProperty("longform_publicarTab"))));
			action.click();
			action.build().perform();
			adminProperties.implicitWait();

			insertCategoryAndTag_lfe(category, tag);

			insertFbContent_lfe(fbtext);

			imageCropperLfe();

		}
	}

	public void insertCategoryAndTag_lfe(String category, String tag) throws InterruptedException, AWTException {
		Actions act = new Actions(driver);
		act.moveToElement(driver.findElement(By.xpath(prop.getProperty("longform_categoryXpath"))));
		act.click();
		adminProperties.implicitWait();
		act.sendKeys(category);
		act.sendKeys(Keys.ENTER);
		act.build().perform();
		System.out.println("*****Category inserted sucessfully*****");
		Thread.sleep(1000);
		
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(By.xpath(prop.getProperty("longform_tagXpath"))));
		action.click();
		adminProperties.implicitWait();
		action.sendKeys(tag);
		action.build().perform();
		Thread.sleep(1000);
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		System.out.println("*****Tag inserted sucessfully*****");

	}

	public void insertFbContent_lfe(String fbtext) throws InterruptedException, AWTException {
		
		Actions act = new Actions(driver);
		act.moveToElement(driver.findElement(By.xpath(prop.getProperty("longform_FbTextarea"))));
		act.click();
		adminProperties.implicitWait();
		if (!(fbtext).equalsIgnoreCase("null")) {
		act.sendKeys(fbtext);}
		act.build().perform();
		System.out.println("*****FB content inserted sucessfully*****");

	}

	public void imageCropperLfe() throws InterruptedException {
		Thread.sleep(2000);
		List<WebElement> validarButtons = adminProperties.findElementsByXpath(prop.getProperty("lfe_validarButtons"));
		for (WebElement validButton : validarButtons) {
			if (validButton.getText().equalsIgnoreCase("validar")) {
				validButton.click();
				Thread.sleep(2000);
			}
		}
		List<WebElement> editarButtons = adminProperties.findElementsByXpath(prop.getProperty("lfe_editarButtons"));
		for (int i = 0; i < editarButtons.size(); i++) {
			editarButtons.get(i).isDisplayed();
		}
		System.out.println("*****Images have been cropped sucessfully*****");
		driver.findElement(By.xpath(prop.getProperty("longform_programorBtn"))).click();
		System.out.println("*****Your post has been published sucessfully*****");
	}
}
