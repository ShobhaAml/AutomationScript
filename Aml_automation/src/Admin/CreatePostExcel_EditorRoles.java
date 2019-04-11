package Admin;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import Common.Adminproperty;
import Admin.CheckUserRoles;

public class CreatePostExcel_EditorRoles {
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
	Connection conn;
	String blogrole = "", blogroleName = "", uname = "", Authorname = "";

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
			String Republish,String Future_time) throws Exception {

		if (Run.trim().equalsIgnoreCase("Y")) {
			// String blogrole = "Branded Collaborator";
			Boolean status = true;

			System.out.println(prop.getProperty("admin_usename"));
			CheckUserRoles ck = new CheckUserRoles();
			ck.openConnection(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
			blogrole = ck.blogrole;
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

			if (blogrole.equalsIgnoreCase("admin")) {
				status = true;

				if (!slideshowimages.equalsIgnoreCase("null")) {
					adminProperties.findAndClick("navigate_Slideshow");
				} else if (!Branded_club.equalsIgnoreCase("null")) {
					adminProperties.findAndClick("navigate_brandClub");
				} else if (category.equalsIgnoreCase("basics")) {
					adminProperties.findAndClick("Basic_post");
				} else if (category.equalsIgnoreCase("V�deos")) {
					adminProperties.findAndClick("LeadVideo");
				} else {
					adminProperties.findAndClick("create_post_link");
				}
			} else if ((blogrole.equalsIgnoreCase("Branded Coordinator")
					|| blogrole.equalsIgnoreCase("Branded Collaborator")) && posttype.contains("Branded")) {

				status = true;

			}

			else if ((blogrole.equalsIgnoreCase("Coordinator") || blogrole.equalsIgnoreCase("Collaborator")
					|| blogrole.equalsIgnoreCase("Editor") || blogrole.equalsIgnoreCase("Lead Editor")
					|| blogrole.equalsIgnoreCase("Editor Senior")) && (!posttype.contains("Branded"))) {

				status = true;

			} else {

				System.out.println(blogrole + " : "
						+ "You are not authorised to click on the link as excel file does not match your data");
				status = false;

			}

			if (status == true) {
				adminProperties.findAndClick("create_post_link");
				adminProperties.findAndWrite("post_title", posttitle);

				if (!postcontent.equalsIgnoreCase("null")) {
					adminProperties.findAndWrite("post_content", postcontent);
					adminProperties.addNewline();
				}
				adminProperties.implicitWait();

				if (!(primaryimage.equalsIgnoreCase("null"))) {
					adminProperties.uploadPrimaryImage(primaryimage, browser);
					adminProperties.addNewlines();
				}

				if (!Youtube_Video.equalsIgnoreCase("null")) {
					// movecursorpostion(browser);
					adminProperties.videoHandle(Youtube_Video, Youtube_layout, browser);
					adminProperties.implicitWait();
					adminProperties.findAndClick("post_content");
					// movecursorpostion(browser);
					adminProperties.addNewlines();
					adminProperties.implicitWait();
				}

				if (!Vine.equalsIgnoreCase("null")) {
					// movecursorpostion(browser);
					adminProperties.videoHandle(Vine, Vine_layout, browser);
					adminProperties.implicitWait();
					adminProperties.findAndClick("post_content");
					adminProperties.addNewlines();
					adminProperties.implicitWait();
				}

				if (!Vimeo.equalsIgnoreCase("null")) {
					// movecursorpostion(browser);
					adminProperties.videoHandle(Vimeo, Vimeo_layout, browser);
					adminProperties.implicitWait();
					adminProperties.findAndClick("post_content");
					adminProperties.addNewlines();
					adminProperties.implicitWait();
				}

				adminProperties.implicitWait();
				adminProperties.addNewlines();
				adminProperties.findAndClick("toolbar_more");
				adminProperties.implicitWait();

				if (!postcontent_more.equalsIgnoreCase("null")) {
					adminProperties.addNewlines();
					adminProperties.findAndWrite("post_content", postcontent_more);
					adminProperties.addNewlines();
				}

				if (!(multiple_images.equalsIgnoreCase("null"))) {
					adminProperties.implicitWait();
					adminProperties.uploadMultipleImage(multiple_images, browser);
					adminProperties.implicitWait();
					adminProperties.addNewlines();
				}

				if (!slideshowimages.equalsIgnoreCase("null")) {
					adminProperties.addslides(slideshowimages, browser);
				}

				if ((!summary.equalsIgnoreCase("null")) || (!actualizacion.equalsIgnoreCase("null"))) {
					// movecursorpostion(browser);
					adminProperties.summaryActuallization(summary, actualizacion, summary_layout);
					if (!(actualizacion.equalsIgnoreCase("null"))) {
						toolbarstatus = "A";
					} /*
						 * else { movecursorpostion(browser); }
						 */
				}

				if (!embeded_code.equalsIgnoreCase("null")) {
					// movecursorpostion(browser);
					adminProperties.implicitWait();
					adminProperties.addNewlines();
					adminProperties.findAndWrite("post_content", embeded_code);
					adminProperties.implicitWait();
					adminProperties.addNewlines();
					adminProperties.implicitWait();
				}

				if (!(ficha_review).equalsIgnoreCase("null")) {
					// movecursorpostion(browser);
					if (toolbarstatus.equalsIgnoreCase("B")) {
						adminProperties.implicitWait();
						adminProperties.findAndClick("toolbar_Advance");
						toolbarstatus = "A";
					}
					adminProperties.implicitWait();
					adminProperties.addNewlines();
					adminProperties.fichaDeReview(ficha_review);
					adminProperties.implicitWait();
					adminProperties.addNewlines();
				} else {
					adminProperties.addNewlines();
				}

				if (!(tabledata.equalsIgnoreCase("null"))) {
					// movecursorpostion(browser);
					if (toolbarstatus.equalsIgnoreCase("B")) {
						adminProperties.implicitWait();
						adminProperties.findAndClick("toolbar_Advance");
						toolbarstatus = "A";
					}
					adminProperties.implicitWait();
					adminProperties.addNewlines();
					adminProperties.addTable(tabledata, Checkbox_same_width, Checkbox_table_first_row_heading,
							Checkbox_table_first_column_heading, Checkbox_table_occupy_all_avaiable_width);
					adminProperties.implicitWait();
					adminProperties.addNewlines();
				}

				if (!(giphy_url).equalsIgnoreCase("null")) {
					// movecursorpostion(browser);
					if (toolbarstatus.equalsIgnoreCase("B")) {
						adminProperties.implicitWait();
						adminProperties.findAndClick("toolbar_Advance");
						toolbarstatus = "A";
					}
					adminProperties.implicitWait();
					adminProperties.addNewlines();
					if (giphy_caption.equalsIgnoreCase("null")) {
						giphy_caption = "";
					}
					adminProperties.insertGIPHY(giphy_url, giphy_layout, giphy_caption, browser);
					adminProperties.implicitWait();
					adminProperties.addNewlines();
				}

				if (!Inforgram_datawrapper_URL.equalsIgnoreCase("null")) {
					// movecursorpostion(browser);
					if (toolbarstatus.equalsIgnoreCase("B")) {
						adminProperties.implicitWait();
						adminProperties.findAndClick("toolbar_Advance");
						toolbarstatus = "A";
					}
					adminProperties.implicitWait();
					adminProperties.addNewlines();
					if (infographCaption.equalsIgnoreCase("null")) {
						infographCaption = "";
					}
					adminProperties.infograph(Inforgram_datawrapper_URL, infographLayout, infographCaption, browser);
					adminProperties.implicitWait();
					adminProperties.addNewlines();
				}

				if (!(ficha_technica).equalsIgnoreCase("null")) {
					// movecursorpostion(browser);
					if (toolbarstatus.equalsIgnoreCase("B")) {
						adminProperties.implicitWait();
						adminProperties.findAndClick("toolbar_Advance");
						toolbarstatus = "A";
					}
					adminProperties.implicitWait();
					adminProperties.addNewlines();
					adminProperties.fichaTechnica(ficha_technica);
				}

				if (!Recipe_name.equalsIgnoreCase("null")) {
					adminProperties.addRecipe(Recipe_name, Recipe_Person, Recipe_level, Recipe_ingredients,
							Recipe_ingredients_Cantidad, Recipe_ingredients_units, Recipe_ingredients_Detalles,
							Preparation_time_hours, Preparation_time_Mintues, Cooking_time_hours, Cooking_time_minutes,
							Rest_time_hours, Rest_time_mintues, Recipe_postcontent, Recipe_Image,
							Recipe_More_postcontent, Recipe_Youtube_Video, Recipe_Youtube_Video_layout, Vine_Video,
							Recipe_Vine_Video_layout, Vimeo_Video, Recipe_Vimeo_Video_layout, FB_Video,
							Recipe_FB_Video_layout, Recipe_summary, Recipe_summary_layout, browser);
				}

				if (!Gallery_photos.equalsIgnoreCase("null")) {
					adminProperties.implicitWait();
					adminProperties.addNewlines();
					adminProperties.galleryPost(Gallery_name, Gallery_description, Gallery_tag, Gallery_ShowHeader,
							Gallery_photos, browser);
				}
				adminProperties.implicitWait();
				((JavascriptExecutor) driver).executeScript("scroll(0, -800);");
				/*
				 * if(blogrole.equalsIgnoreCase("Branded Collaborator")) { System.out.
				 * println("Branded Collaborator don't have access to publish a post" ); } else{
				 */
				adminProperties.findAndClick("publish_tab");
				adminProperties.handleAuthenticationDialog(browser);
				Thread.sleep(4000);

				if (!Branded_club.equalsIgnoreCase("null")) {
					adminProperties.insertBrandedClub(Branded_club, tag);

				} else {
					adminProperties.insertTagAndCategory(category, tag);
				}
				
				
				if(!Future_time.equalsIgnoreCase("null")) {
					adminProperties.findAndClick("futuretxt");
					adminProperties.findElement(prop.getProperty("futuretxt")).clear();;
					adminProperties.implicitWait();				
					adminProperties.findAndWrite("futuretxt", Future_time);
				}
				

				if ((!homecontent.equalsIgnoreCase("null"))) {
					adminProperties.findAndWrite("homepage_content", homecontent);

				}

				if (specialpost.equalsIgnoreCase("Y")) {
					adminProperties.specialPost(specialpost);
				}

				if (comment_closed.equalsIgnoreCase("Y")) {
					adminProperties.closeComments(comment_closed);
				}

				if (!author.equalsIgnoreCase("null")) {
					adminProperties.Author(author);
				}
				System.out.println(adminProperties.imageCropper());

							
				if (category.equalsIgnoreCase("basics")) {
					adminProperties.addFbTwitterText("null", "null",Future_time);
				} else {
					adminProperties.addFbTwitterText(fbtext, Twittertext,Future_time);
				}
				
				adminProperties.implicitWait();

				if (Republish.equalsIgnoreCase("Y")) {

					adminProperties.republish();
				}
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
