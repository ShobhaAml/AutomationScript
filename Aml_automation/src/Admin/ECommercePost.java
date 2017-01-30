package Admin;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class ECommercePost
{

    int invalidImageCount = 0;
    WebDriver driver;
    Adminproperty adminProperties = new Adminproperty();
    Properties prop = new Properties();
    String postcatagory = "Ecommerce";
    String posttitle = "Automated eCommerce Post";
    String postcontent = "hey shobha testing ecommerce post.\n";
    String primaryimage = "\\primary.jpg";
    String tag = "Apple";
    String homecontent="Homepage content for ecommerce post";
    String fbtext = "Hi testing for FB content";
    String allowHomepageImage = "Y";
    String allowHomepageContent = "Y";

    @BeforeMethod(alwaysRun = true)
    public void Setup() throws IOException
    { 
        prop = adminProperties.ReadProperties();
        driver = adminProperties.callproperty(prop.getProperty("url"),
                prop.getProperty("browser"));
        
        System.out.println( prop.getProperty("browser"));
    }

    public void Writeboard() throws Exception
    {
        adminProperties.adminLogin();
        adminProperties.findAndClick("navigation_header");
        adminProperties.findAndClick("create_post_link");
        adminProperties.findAndWrite("post_title", posttitle);
        adminProperties.findAndWrite("post_content", postcontent);
        adminProperties.findAndClick("post_title");
        adminProperties.implicitWait();
        adminProperties.findAndClick("toolbar_image");
        adminProperties.uploadPrimaryImage(primaryimage);
        adminProperties.findAndSendkey("post_content", Keys.END);
        adminProperties.findAndSendkey("post_content", Keys.ENTER);
        adminProperties.findAndSendkey("post_content", Keys.END);
        adminProperties.findAndSendkey("post_content", Keys.ENTER);
        // Click advance from toolbar
        adminProperties.findAndClick("toolbar_Advance");
        adminProperties.implicitWait();
        adminProperties.findAndClick("post_title");
        adminProperties.implicitWait();
        adminProperties.findAndClick("toolbar_shopping");
        adminProperties.implicitWait();
        driver.switchTo().window(driver.getWindowHandle());
        adminProperties.implicitWait();
        String images = prop.getProperty("image_array");
        int cnt = 1;
        String arr[] = images.split(",");
        for (String image : arr) {
            adminProperties.findAndWrite(
                    "Multiupload",
                    System.getProperty("user.dir")
                            + prop.getProperty("image_path") + "\\" + image);
            adminProperties.findAndClick("add_Allimage");
            adminProperties.implicitWait();
            driver.findElement(By.tagName("img"));
            WebElement element = adminProperties.findElement(prop
                    .getProperty("product_image_bulkupload")
                    + "["
                    + cnt
                    + "]"
                    + prop.getProperty("product_image_bulkupload1"));

            adminProperties.Imagestatus(element);

            cnt++;
        }
        adminProperties.findAndClick("insert_images");
        String catagory = "";
        cnt = 1;
        int price = 0;
        for (String image : arr) {
            String name = image;
            price = Calendar.MINUTE + Calendar.MILLISECOND;
            adminProperties.findElement(
                    prop.getProperty("insert_product_name") + "[" + cnt + "]"
                            + prop.getProperty("insert_product_name1"))
                    .sendKeys(name);
            adminProperties.findElement(
                    prop.getProperty("insert_product_order") + "[" + cnt + "]"
                            + prop.getProperty("insert_product_order1"))
                    .sendKeys(String.valueOf(cnt));
            adminProperties.findElement(
                    prop.getProperty("insert_product_desc") + "[" + cnt + "]"
                            + prop.getProperty("insert_product_desc1"))
                    .sendKeys(name);
            adminProperties.findElement(
                    prop.getProperty("insert_product_link") + "[" + cnt + "]"
                            + prop.getProperty("insert_product_link1"))
                    .sendKeys("http://yahoo.com");
            adminProperties.findElement(
                    prop.getProperty("insert_product_text") + "[" + cnt + "]"
                            + prop.getProperty("insert_product_text1"))
                    .sendKeys(name);
            adminProperties.findElement(
                    prop.getProperty("insert_product_price") + "[" + cnt + "]"
                            + prop.getProperty("insert_product_price1"))
                    .sendKeys("4500");
            if (cnt % 2 == 0) {
                catagory = "abc";
            } else {
                catagory = "xyz";
            }
            adminProperties.findElement(
                    prop.getProperty("insert_product_cat") + "[" + cnt + "]"
                            + prop.getProperty("insert_product_cat1"))
                    .sendKeys(catagory);
            cnt++;
        }
        adminProperties.findAndClick("insert_submit_button");
        adminProperties.findAndClick("post_title");
        adminProperties.findAndClick("publish_tab");
        adminProperties.implicitWait();

    }

    @Test
    public void createEcommercePost() throws Exception
    {
        Writeboard();
        adminProperties.insertTagAndCategory(postcatagory, tag);
        if (allowHomepageContent == "Y") {
            adminProperties.findAndWrite("homepage_content",
                    homecontent);
        }

        if (allowHomepageImage == "Y") {

            adminProperties.findAndWrite(
                    "ecommerce_homepage_image",
                    System.getProperty("user.dir")
                            + prop.getProperty("image_path") + "\\"
                            + prop.getProperty("homepage_image"));
            adminProperties.findAndClick("ecommerce_upload_homepage_image");
            List<WebElement> imagesList = driver
                    .findElements(By.tagName("img"));
            for (WebElement imgElement : imagesList) {
                if (imgElement != null) {
                    adminProperties.verifyImageActive(imgElement);
                }
            }

        }

        adminProperties.addFbTwitterText(fbtext);

    }

    @AfterMethod
    public void teardown()
    {
        driver.close();
    }
}
