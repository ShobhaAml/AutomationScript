package Admin;

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

public class CreatePostExcel
{
    int invalidImageCount = 0;
    WebDriver driver;
    Adminproperty adminProperties = new Adminproperty();
    Properties prop = new Properties();
    String Postdata, posttitle, postcontent, primaryimage, postcontent_more,
            images, productname, productOrder, productDesc, productLink,
            productLinktext = "";
    String productPrice, productCatagory, homecontent, homeImage, postcatagory,
            postcatagoryOther, tag, seotitle, seodesc, specialpost = "";
    String author, Twittertext, fbtext, allowHomepageImage,
            allowHomepageContent = "";
    String browser = "";
    String toolbarstatus = "B";

    @BeforeMethod
    public void Setup() throws Exception
    {
        prop = adminProperties.ReadProperties();
        driver = adminProperties.callproperty(prop.getProperty("url"),
                prop.getProperty("browser"));
        browser = prop.getProperty("browser");
    }

    @DataProvider(name = "testdata")
    public Object[][] TestDataFeed() throws Exception
    {
        Object[][] postdata = adminProperties.readExcel("Normal", 35);
        return postdata;
    }

    @Test(dataProvider = "testdata")
    public void createPost(String posttype, String posttitle,
            String postcontent, String primaryimage, String postcontent_more,
            String video, String video_layout, String gallery,
            String multiple_images, String embeded_code, String summary,
            String summary_layout, String actualizacion, String table,
            String ficha_technica, String ficha_review, String giphy,
            String info_datawrapper, String slideshowimages,
            String homecontent, String homeimage, String Branded_club,
            String category, String catagory_other, String tag,
            String seotitle, String seodesc, String specialpost,
            String comment_closed, String author, String Twittertext,
            String fbtext, String Repost, String Run, String Republish)
            throws Exception
    {
        if (Run.trim().equalsIgnoreCase("Y")) {
            adminProperties.adminLogin();
            adminProperties.findAndClick("navigation_header");

            if (!slideshowimages.equalsIgnoreCase("null")) {
                adminProperties.findAndClick("navigate_Slideshow");
            } else if (!Branded_club.equalsIgnoreCase("null")) {
                adminProperties.findAndClick("navigate_brandClub");
            } else if (category.equalsIgnoreCase("basics")) {
                adminProperties.findAndClick("Basic_post");
            } else {
                adminProperties.findAndClick("create_post_link");
            }

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

            if (!video.equalsIgnoreCase("null")) {
                adminProperties.implicitWait();
                adminProperties.addNewlines();
                adminProperties.videoHandle(video, video_layout, browser);
                adminProperties.implicitWait();
                adminProperties.findAndClick("post_content");
                adminProperties.addNewlines();
            }

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
                adminProperties.addNewlines();
            }

            if (embeded_code.equalsIgnoreCase("null")) {
                // adminProperties.findAndWrite("post_content", embeded_code);
            }

            if (!slideshowimages.equalsIgnoreCase("null")) {
                adminProperties.addslides(slideshowimages, browser);
            }

            if ((!summary.equalsIgnoreCase("null"))
                    || (!actualizacion.equalsIgnoreCase("null"))) {
                adminProperties.implicitWait();
                adminProperties.addNewlines();
                adminProperties.summaryActuallization(summary, actualizacion,
                        summary_layout);
                if (!(actualizacion.equalsIgnoreCase("null"))) {
                    toolbarstatus = "A";
                } else {
                    if (browser.trim().equalsIgnoreCase("Chrome")) {
                        Actions action = new Actions(driver);
                        action.sendKeys(Keys.PAGE_DOWN);
                        adminProperties.implicitWait();
                        action.click(
                                driver.findElement(By
                                        .partialLinkText("Escribir")))
                                .perform();
                        adminProperties.implicitWait();
                        adminProperties.findAndClick("post_title");
                        adminProperties.implicitWait();
                    } else {
                        adminProperties.implicitWait();
                        adminProperties.findAndClick("post_title");
                        ((JavascriptExecutor) driver)
                                .executeScript("window.scrollBy(0,0)");
                        adminProperties.findAndClick("post_title");
                    }
                }
            }

            if (!(ficha_review).equalsIgnoreCase("null")) {
                if (toolbarstatus.equalsIgnoreCase("B")) {
                    adminProperties.implicitWait();
                    adminProperties.findAndClick("toolbar_Advance");
                }
                adminProperties.implicitWait();
                adminProperties.addNewlines();
                adminProperties.fichaDeReview(ficha_review);
                adminProperties.implicitWait();
                adminProperties.addNewlines();
            } else {
                adminProperties.addNewlines();
            }

            adminProperties.implicitWait();
            adminProperties.moveToPublishTab(browser);

            if (!Branded_club.equalsIgnoreCase("null")) {
                adminProperties.insertBrandedClub(Branded_club, tag);

            } else {
                adminProperties.insertTagAndCategory(category, tag);
            }

            if ((!homecontent.equalsIgnoreCase("null"))) {
                adminProperties.findAndWrite("homepage_content", homecontent);
            }

            if (category.equalsIgnoreCase("basics")) {
                adminProperties.addFbTwitterText("null", "null");

            } else {
                adminProperties.addFbTwitterText(fbtext, Twittertext);
            }
            adminProperties.implicitWait();

            if (Republish.equalsIgnoreCase("Y")) {

                adminProperties.republish();
            }
        }
    }
}