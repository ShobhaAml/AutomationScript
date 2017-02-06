package Admin;

import java.util.List;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class CreateEcommerceExcel
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

        Object[][] postdata = adminProperties.readExcel("Ecommerce", 25);
        return postdata;
    }

    @Test(dataProvider = "testdata")
    public void Ecommerce(String posttitle, String postcontent,
            String primaryimage, String images, String productname,
            String productOrder, String productDesc, String productLink,
            String productLinktext, String productPrice,
            String productCatagory, String homecontent, String homeImage,
            String postcatagory, String postcatagoryOther, String tag,
            String seotitle, String seodesc, String specialpost, String author,
            String Twittertext, String fbtext, String Repost, String Run,
            String Republish) throws Exception
    {
        if (Run.trim().equalsIgnoreCase("Y")) {
            adminProperties.adminLogin();
            adminProperties.findAndClick("navigation_header");
            adminProperties.findAndClick("create_post_link");
            adminProperties.findAndWrite("post_title", posttitle);
            adminProperties.findAndWrite("post_content", postcontent);
            adminProperties.findAndClick("post_title");
            adminProperties.implicitWait();
            adminProperties.addNewline();
            adminProperties.uploadPrimaryImage(primaryimage, browser);
            adminProperties.addNewlines();
            adminProperties.findAndClick("toolbar_Advance");
            adminProperties.implicitWait();
            adminProperties.addNewlines();
            adminProperties.implicitWait();
            if (browser.trim().equalsIgnoreCase("firefox")) {
                adminProperties.findAndClick("toolbar_shopping");
            } else {
                Actions actions = new Actions(driver);
                actions.moveToElement(
                        adminProperties.findElement(prop
                                .getProperty("toolbar_shopping"))).click()
                        .perform();
            }
            adminProperties.implicitWait();
            driver.switchTo().window(driver.getWindowHandle());
            adminProperties.implicitWait();

            int cnt = 1;
            String arr[] = images.split("@##@");
            for (String image : arr) {
                adminProperties
                        .findAndWrite(
                                "Multiupload",
                                System.getProperty("user.dir")
                                        + prop.getProperty("image_path") + "\\"
                                        + image);
                adminProperties.findAndClick("add_Allimage");
                adminProperties.implicitWait();
                driver.findElement(By.tagName("img"));
                WebElement element = adminProperties.findElement(prop
                        .getProperty("product_image_bulkupload")
                        + "["
                        + cnt
                        + "]" + prop.getProperty("product_image_bulkupload1"));

                adminProperties.Imagestatus(element);

                cnt++;
            }
            adminProperties.findAndClick("insert_images");
            cnt = 1;
            int cnt1 = 0;
            String arrproductname[] = productname.split("@##@");
            String arrproductOrder[] = productOrder.split("@##@");
            String arrproductDesc[] = productDesc.split("@##@");
            String arrproductLink[] = productLink.split("@##@");
            String arrproductLinktext[] = productLinktext.split("@##@");
            String arrproductPrice[] = productPrice.split("@##@");
            String arrproductCatagory[] = productCatagory.split("@##@");

            for (String name : arrproductname) {

                adminProperties.findElement(
                        prop.getProperty("insert_product_name") + "[" + cnt
                                + "]"
                                + prop.getProperty("insert_product_name1"))
                        .sendKeys(name);
                adminProperties.findElement(
                        prop.getProperty("insert_product_order") + "[" + cnt
                                + "]"
                                + prop.getProperty("insert_product_order1"))
                        .sendKeys(String.valueOf(arrproductOrder[cnt1]));
                adminProperties.findElement(
                        prop.getProperty("insert_product_desc") + "[" + cnt
                                + "]"
                                + prop.getProperty("insert_product_desc1"))
                        .sendKeys(arrproductDesc[cnt1]);
                adminProperties.findElement(
                        prop.getProperty("insert_product_link") + "[" + cnt
                                + "]"
                                + prop.getProperty("insert_product_link1"))
                        .sendKeys(arrproductLink[cnt1]);
                adminProperties.findElement(
                        prop.getProperty("insert_product_text") + "[" + cnt
                                + "]"
                                + prop.getProperty("insert_product_text1"))
                        .sendKeys(arrproductLinktext[cnt1]);
                adminProperties.findElement(
                        prop.getProperty("insert_product_price") + "[" + cnt
                                + "]"
                                + prop.getProperty("insert_product_price1"))
                        .sendKeys(arrproductPrice[cnt1]);
                adminProperties
                        .findElement(
                                prop.getProperty("insert_product_cat")
                                        + "["
                                        + cnt
                                        + "]"
                                        + prop.getProperty("insert_product_cat1"))
                        .sendKeys(arrproductCatagory[cnt1]);

                cnt++;
                cnt1++;
            }
            adminProperties.findAndClick("insert_submit_button");
            adminProperties.addNewlines();
            adminProperties.findAndClick("publish_tab");
            adminProperties.implicitWait();

            adminProperties.insertTagAndCategory(postcatagory, tag);

            if (!homecontent.equalsIgnoreCase("null")) {
                adminProperties.findAndWrite("homepage_content", homecontent);
            }

            if (!homeImage.equalsIgnoreCase("null")) {

                adminProperties.findAndWrite(
                        "ecommerce_homepage_image",
                        System.getProperty("user.dir")
                                + prop.getProperty("image_path") + "\\"
                                + homeImage);
                adminProperties.findAndClick("ecommerce_upload_homepage_image");
                List<WebElement> imagesList = driver.findElements(By
                        .tagName("img"));
                for (WebElement imgElement : imagesList) {
                    if (imgElement != null) {
                        adminProperties.verifyImageActive(imgElement);
                    }
                }

            }

            adminProperties.addFbTwitterText(fbtext, Twittertext);
            adminProperties.implicitWait();

            if (Republish.equalsIgnoreCase("Y")) {

                adminProperties.republish();
            }

        }
    }

}
