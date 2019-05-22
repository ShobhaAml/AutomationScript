package Admin;

import java.util.List;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import Common.Adminproperty;

public class CreateMVPPostExcel {
	int invalidImageCount = 0;
	WebDriver driver;
	Adminproperty adminProperties = new Adminproperty();
	Properties prop = new Properties();
	String browser = "";

	@BeforeMethod
	public void Setup() throws Exception {
		prop = adminProperties.ReadProperties();
		driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
		browser = prop.getProperty("browser");
	}

	@DataProvider(name = "testdata")
	public Object[][] TestDataFeed() throws Exception {
		Object[][] postdata = adminProperties.readExcel("Normal",102);
		return postdata;
	}

	@Test(dataProvider = "testdata")
	public void createPost(String posttype, String posttitle, String postcontent, String primary,String image, String imageLayout,
			String postcontent_more, String Youtube_Video, String Youtube_layout, String Vine, String Vine_layout,
			String Vimeo, String Vimeo_layout, String Gallery_name, String Gallery_description, String Gallery_tag,
			String Gallery_ShowHeader, String Gallery_photos, String multiple_images, String embeded_code,
			String summary, String summary_layout, String actualizacion, String ficha_technica, String ficha_review,
			String giphy_url, String giphy_layout, String giphy_caption, String instagram,String Inforgram_datawrapper_URL,
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
			String Recipe_summary_layout, String Pivot_amazon_search, String Pivot_dropdown,
			String Pivot_otherStoreProductTitle, String Pivot_otherStoreProductImage, String Pivot_otherStorevalues,
			String Pivot_newsletter, String homecontent, String homeimage, String Branded_club, String disclaimer, String category,
			String catagory_other, String tag, String seotitle, String seodesc, String specialpost,
			String comment_closed, String author, String Twittertext, String fbtext, String Contenido_Patrocinado, String Repost, String Run,
			String Republish, String Future_time, String Pivot_product_Article, String Pivot_product_Article_posttype,
			String Publish_to_homepage_checkbox, String hook, String hookCustomerLogo, String hookCustomerName,
			String hookLogoLink, String hookTextarea, String hookImage, String hookButtonText, String richContent_alternativo, String rich_text, String rich_URL) throws Exception {
		if (Run.trim().equalsIgnoreCase("Y")) {
			adminProperties.LoginAdmin(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
			adminProperties.CreateMVPpost(posttype);
			Thread.sleep(10000);
			System.out.println("Let's add title to post");
			adminProperties.AddMVPTitle("Testing title");
			adminProperties.implicitWait();
			/*List<WebElement> list = adminProperties.findElementsByXpath(prop.getProperty("MVPSectionList"));
			System.out.println(list.size() +"list count");
			System.out.println(list.get(list.size()-1).getAttribute("id"));
			adminProperties.implicitWait();*/
			
			//adminProperties.primary_section_MVP(primary, image);
			adminProperties.primary_section_MVP(primary, Youtube_Video);
	
			/*if(!Pivot_newsletter.equalsIgnoreCase("null"))
			{
				adminProperties.ClickICON(driver,"pivot");
				adminProperties.findAndClick("MvP_pivot_newsletter");
				adminProperties.MVP_pivot_newsletter(Pivot_newsletter.toLowerCase());			
			}
			if (!(Pivot_product_Article.equalsIgnoreCase("null"))) {
				adminProperties.ClickICON(driver,"pivot");
				adminProperties.implicitWait();
				adminProperties.add_MVP_pivotarticle(Pivot_product_Article, Pivot_product_Article_posttype);		
			}
			
			if(!Pivot_dropdown.equalsIgnoreCase("null"))
			{
			Actions action = new Actions(driver);
			action.sendKeys(Keys.ENTER);
			action.build().perform();
			action.moveToElement(driver.findElement(By.xpath(prop.getProperty("content_section_path"))));
			action.click();
			adminProperties.ClickICON(driver,"pivot");
				
			if(Pivot_dropdown.equalsIgnoreCase("amazon")) {
				adminProperties.findAndClick("MvP_pivot_Product");
				adminProperties.MVP_pivot_amazon(Pivot_amazon_search.toLowerCase());	
				adminProperties.implicitWait();
				adminProperties.MVP_add_ecommerce(Pivot_otherStorevalues);
			}
			}
			
			if (!Pivot_product_Article.equalsIgnoreCase("null")) {
				Actions action = new Actions(driver);
				action.sendKeys(Keys.ENTER);
				action.build().perform();
				action.moveToElement(driver.findElement(By.xpath(prop.getProperty("content_section_path"))));
				action.click();
				adminProperties.ClickICON(driver,"pivot");
				System.out.println("Pivot Article =" + Pivot_product_Article);
				adminProperties.add_MVP_pivotarticle(Pivot_product_Article, Pivot_product_Article_posttype);
				adminProperties.implicitWait();
			}if (!(image.equalsIgnoreCase("null"))) {
				adminProperties.ClickICON(driver,"image");
				adminProperties.implicitWait();
				adminProperties.mvpUrlImage(image);
				adminProperties.Insertimage(imageLayout, "MVP_insertImage");
				}
			
			if (!(video.equalsIgnoreCase("null"))) {
				adminProperties.ClickICON(driver,"video");
				adminProperties.implicitWait();
				adminProperties.MVPaddVideo(video, videoLayout);
			}
			 if (!(richContent_alternativo.equalsIgnoreCase("null"))) {
				adminProperties.ClickICON(driver,"richcontent");
				adminProperties.implicitWait();
				adminProperties.mvp_addRichContent(richContent_alternativo, rich_text, rich_URL);		
			}
	
			if (!(primaryimage.equalsIgnoreCase("null"))) {
				adminProperties.ClickICON(driver,"image");
				adminProperties.implicitWait();
				adminProperties.mvpUrlImage("https://i.blogs.es/dee55e/tinoiv/1024_682.jpg");
				adminProperties.Insertimage("");
			}
			
			if(!postcontent.equalsIgnoreCase("null"))
			{
				adminProperties.addMVP_SectionContent(postcontent);
				adminProperties.implicitWait();

			}
			if(!Infogram_datawrapper_URL.equalsIgnoreCase("null"))
			{
				adminProperties.ClickICON(driver,"infogram");
				adminProperties.implicitWait();
				adminProperties.addMVPInfogram(Infogram_datawrapper_URL, infographLayout);
				adminProperties.implicitWait();
			}
			
			if(!giphy_url.equalsIgnoreCase("null"))
			{
				adminProperties.ClickICON(driver,"giphy");
				adminProperties.implicitWait();
				adminProperties.MVPmodules(giphy_url);
			}
			if(!instagram.equalsIgnoreCase("null"))
			{
			    adminProperties.ClickICON(driver,"instagram");
				adminProperties.implicitWait();
				adminProperties.MVPmodules(instagram);
			}
			
			if(!summary.equalsIgnoreCase("null"))
			{
				adminProperties.ClickICON(driver,"sumario");
				adminProperties.implicitWait();
				adminProperties.addSumarioMVP(summary, summary_layout);
				adminProperties.implicitWait();
			}
			if(!Twittertext.equalsIgnoreCase("null"))
			{
				adminProperties.ClickICON(driver,"twitter");
				adminProperties.implicitWait();
				adminProperties.MVPmodules(Twittertext);
				adminProperties.implicitWait();
			}
			
					/*Actions action = new Actions(driver);
			action.moveToElement(driver.findElement(By.xpath(prop.getProperty("content_section_path"))));
			action.click();*/
			
	}
		adminProperties.publicar_MVP(category, tag, fbtext);
		adminProperties.brandCategory_MVP(Branded_club, disclaimer);
}
}
