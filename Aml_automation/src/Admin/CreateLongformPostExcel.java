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
		Object[][] postdata = adminProperties.readExcel("Longform", 38);
		return postdata;
	}

	@Test(dataProvider = "testdata")
	public void createPost(String posttype, String posttitle, String postcontent, String primaryimage,
			String Youtube_Video, String Youtube_layout, String summary, String summary_layout, String ficha_technica,
			String ficha_review, String giphy_url, String giphy_layout, String giphy_caption,
			String Inforgram_datawrapper_URL, String infographLayout, String infographCaption, String slideshowimages,
			String tabledata, String Checkbox_same_width, String Checkbox_table_first_row_heading,
			String Checkbox_table_first_column_heading, String Checkbox_table_occupy_all_avaiable_width,
			String homecontent, String homeimage, String category, String catagory_other, String tag, String seotitle,
			String seodesc, String specialpost, String comment_closed, String author, String Twittertext, String fbtext,
			String Repost, String Run, String Republish, String Future_time) throws Exception {
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

			// Adding Text in the section field
			adminProperties.insertText_lfe(postcontent);

			// Adding Video in the section field
			adminProperties.insertVideo_lfe(Youtube_Video);

			/*
			 * adminProperties.findAndClick("longform_homepageImagePlusIcon");
			 * adminProperties.findAndClick("longform_addtogalleryButton");
			 * adminProperties.findAndClick("longform_resourcepanel");
			 * 
			 * driver.switchTo().defaultContent(); // you are now outside both frames
			 * driver.switchTo().frame("cq-cf-frame");
			 * driver.findElement(By.xpath("//button[text()='OK']")).click();
			 */

			// Clicking Publicar Tab
			Actions action = new Actions(driver);
			action.moveToElement(driver.findElement(By.xpath(prop.getProperty("longform_publicarTab"))));
			action.click();
			action.build().perform();
			adminProperties.implicitWait();

			adminProperties.insertCategoryAndTag_lfe(category, tag);

			adminProperties.insertFbContent_lfe(fbtext);

			adminProperties.imageCropperLfe();
			adminProperties.LFE_add_Sumario(summary, summary_layout);


		}
	}

}
