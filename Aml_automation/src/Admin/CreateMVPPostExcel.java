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
		Object[][] postdata = adminProperties.readExcel("Normal", 106);
		return postdata;
	}

	@Test(dataProvider = "testdata")
	public void createPost(String posttype, String posttitle, String postcontent, String image, String imageLayout,
			String postcontent_more, String Youtube_Video, String Youtube_layout, String Vine, String Vine_layout,
			String Vimeo, String Vimeo_layout, String Gallery_name, String Gallery_description, String Gallery_tag,
			String Gallery_ShowHeader, String Gallery_photos, String multiple_images, String embeded_code,
			String summary, String summary_layout, String actualizacion, String ficha_technica, String ficha_review,
			String giphy_url, String giphy_layout, String giphy_caption, String instagram,
			String Inforgram_datawrapper_URL, String infographLayout, String infographCaption, String slideshowimages,
			String tabledata, String Checkbox_same_width, String Checkbox_table_first_row_heading,
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
			String Pivot_newsletter, String homecontent, String homeimage, String Branded_club, String disclaimer,
			String category, String catagory_other, String tag, String seotitle, String seodesc, String specialpost,
			String comment_closed, String author, String Twittertext, String fbtext, String Contenido_Patrocinado,
			String Repost, String Run, String Republish, String Future_time, String Pivot_product_Article,
			String Pivot_product_Article_posttype, String Publish_to_homepage_checkbox, String hookCustomerLogo,
			String hookCustomerName, String hookLogoLink, String hookTextarea, String hookImage, String hookButtonText,
			String richContent_alternativo, String rich_iFrame, String rich_URL, String flipboard_blogname,
			String flipboard_magazine, String pivot_ExternalUrl, String pivot_ExternalNombre,
			String pivot_ExternalArticletitle, String flipboard) throws Exception {

		if (Run.trim().equalsIgnoreCase("Y")) {
			adminProperties.LoginAdmin(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
			adminProperties.CreateMVPpost(posttype);
			Thread.sleep(2000);
			adminProperties.implicitWait();
			Thread.sleep(2000);
						
			if (!posttitle.equalsIgnoreCase("null")) {
				adminProperties.AddMVPTitle(posttitle);
				adminProperties.implicitWait();
				adminProperties.primary_section_MVP(Youtube_Video);/// pass image or Youtube_Video
			}
			
			if (!postcontent.equalsIgnoreCase("null")) {
				adminProperties.MVP_sectionContent(postcontent);
				adminProperties.implicitWait();
			}

			if (!instagram.equalsIgnoreCase("null")) {
				adminProperties.ClickICON(driver, "instagram");
				adminProperties.implicitWait();
				adminProperties.MVP_modules_withoutLayout(instagram);
			}

			if (!image.equalsIgnoreCase("null")) {
				adminProperties.ClickICON(driver, "image");
				adminProperties.implicitWait();
				adminProperties.MVP_image_viaURL(image);
				adminProperties.MVP_uploadImage(imageLayout, "MVP_insertImage");
			}

			if (!Twittertext.equalsIgnoreCase("null")) {
				adminProperties.ClickICON(driver, "twitter");
				adminProperties.implicitWait();
				adminProperties.MVP_modules_withoutLayout(Twittertext);
				adminProperties.implicitWait();
			}

			if (!giphy_url.equalsIgnoreCase("null")) {
				adminProperties.ClickICON(driver, "giphy");
				adminProperties.implicitWait();
				adminProperties.MVP_modules_withLayout(giphy_url, giphy_layout);
			}

			if (!summary.equalsIgnoreCase("null")) {
				adminProperties.implicitWait();
				adminProperties.ClickICON(driver, "sumario");
				adminProperties.MVP_add_sumario(summary, summary_layout);
				adminProperties.implicitWait();
			}

			if (!(Youtube_Video.equalsIgnoreCase("null"))) {
				adminProperties.ClickICON(driver, "video");
				adminProperties.implicitWait();
				adminProperties.MVP_modules_withLayout(Youtube_Video, Youtube_layout);
			}

			if (!richContent_alternativo.equalsIgnoreCase("null")) {
				adminProperties.implicitWait();
				adminProperties.ClickICON(driver, "richcontent");
				adminProperties.mvp_addRichContent(richContent_alternativo, rich_iFrame, rich_URL);
				adminProperties.implicitWait();
			}

			if (!Inforgram_datawrapper_URL.equalsIgnoreCase("null")) {
				adminProperties.implicitWait();
				adminProperties.ClickICON(driver, "infogram");
				adminProperties.MVP_modules_withLayout(Inforgram_datawrapper_URL, infographLayout);
				adminProperties.implicitWait();
			}
			
			if (!(Pivot_dropdown.equalsIgnoreCase("null"))) {
				adminProperties.CF_pivot_dropdown("Producto");
				
				if (Pivot_dropdown.equalsIgnoreCase("otherstores")) {
					adminProperties.MVP_addPivot_Ecommerce("otherstores","", Pivot_otherStorevalues, Pivot_otherStoreProductTitle, Pivot_otherStoreProductImage);
				} 
				else if (Pivot_dropdown.equalsIgnoreCase("amazon")) {
					adminProperties.implicitWait();
					adminProperties.MVP_addPivot_Ecommerce("Amazon",Pivot_amazon_search, Pivot_otherStorevalues, "", "");
				}
				else if(Pivot_dropdown.equalsIgnoreCase("ebay"))
				{
					adminProperties.MVP_addPivot_Ecommerce("Ebay",Pivot_amazon_search, Pivot_otherStorevalues, "", "");
				}
			}


			if (!Pivot_newsletter.equalsIgnoreCase("null")) {
				adminProperties.ClickICON(driver, "pivot");
				adminProperties.findAndClick("MvP_pivot_newsletter");
				adminProperties.MVP_pivot_newsletter(Pivot_newsletter.toLowerCase());
			}
			if (!(Pivot_product_Article.equalsIgnoreCase("null"))) {
				adminProperties.ClickICON(driver, "pivot");
				adminProperties.implicitWait();
				adminProperties.add_MVP_pivotarticle(Pivot_product_Article, Pivot_product_Article_posttype);
			}

			if (!Pivot_product_Article.equalsIgnoreCase("null")) {
				Actions action = new Actions(driver);
				action.sendKeys(Keys.ENTER);
				action.build().perform();
				action.moveToElement(driver.findElement(By.xpath(prop.getProperty("content_section_path"))));
				action.click();
				adminProperties.ClickICON(driver, "pivot");
				System.out.println("Pivot Article =" + Pivot_product_Article);
				adminProperties.add_MVP_pivotarticle(Pivot_product_Article, Pivot_product_Article_posttype);
				adminProperties.implicitWait();
			}
			if (!(image.equalsIgnoreCase("null"))) {
				adminProperties.ClickICON(driver, "image");
				adminProperties.implicitWait();
				adminProperties.MVP_image_viaURL(image);
				adminProperties.MVP_uploadImage(imageLayout, "MVP_insertImage");
			}


			if (flipboard.equalsIgnoreCase("Y")) {
				
				String blognames=adminProperties.getflipboardBlogs();
				adminProperties.addpivotFlipboard_Alfa(blognames);
				adminProperties.implicitWait();
				
				String[] sitesArr = blognames.split("@##@");
				for(int i =0; i<sitesArr.length; i++)
				{
					System.out.println("Flipboard magzine for " + sitesArr[i]);
					adminProperties.CF_flipboard_magazine(sitesArr[i]); 
				}
				
				
			}
			
			if (!ficha_review.equalsIgnoreCase("null")) {
				adminProperties.ClickICON(driver, "review");
				adminProperties.implicitWait();
				adminProperties.MVP_add_review(ficha_review);
			}

			// Add pivot external to CF post
			//adminProperties.addpivotExternal_Alfa(pivot_ExternalUrl, pivot_ExternalNombre, pivot_ExternalArticletitle);

			// Add pivot instagram to CF post
			//adminProperties.insertInsta_MVP();

			// Add hook to MVP
			//adminProperties.insertHook_MVP(hookCustomerName, hookLogoLink, hookTextarea, hookButtonText);

		}
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(By.xpath(prop.getProperty("content_section_path"))));
		action.click();

	}
	// adminProperties.publicar_MVP(category, tag, fbtext);
	// adminProperties.brandCategory_MVP(Branded_club, disclaimer);
}