package PostCreation;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
    String posttitle = "Automated eCommerce Post with homepage Image/content";
    String postcontent = "hey shobha testing ecommerce post.\n";
    String primaryimage = "/primary.jpg";
    String tag = "Apple";
    String fbtext = "Hi testing for FB content";

    @BeforeMethod
    public void Setup() throws IOException
    {
        prop = adminProperties.ReadProperties();
        driver = adminProperties.callproperty(prop.getProperty("url"),
                prop.getProperty("browser"));
    }

    public void login() throws Exception
    {
        adminProperties.adminLogin();
    }

    public void Writeboard() throws Exception
    {
        login();
        adminProperties.findAndClick("navigation_header");
        adminProperties.findAndClick("create_post_link");
        adminProperties.findAndWrite("post_title", posttitle);
        adminProperties.findAndWrite("post_content", postcontent);
        adminProperties.findAndClick("post_title");
        adminProperties.implicitWait();
        adminProperties.findAndClick("toolbar_image");
        adminProperties.findAndWrite(
                "primary_image_insert",
                System.getProperty("user.dir") + "//"
                        + prop.getProperty("image_path") + primaryimage);
        adminProperties.findAndClick("primary_image_upload");
        WebElement element1 = adminProperties.findElement(prop
                .getProperty("product_image_bulkupload")
                + prop.getProperty("product_image_bulkupload1"));
        if (element1.getAttribute("href") != null) {
            adminProperties
                    .isLinkBroken(new URL(element1.getAttribute("href")));
            System.out.println(adminProperties.isLinkBroken(new URL(element1
                    .getAttribute("href"))));
        }
        adminProperties.implicitWait();
        adminProperties.findAndClick("primary_noraml_insert");
        adminProperties.implicitWait();
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
                    System.getProperty("user.dir") + "//"
                            + prop.getProperty("image_path") + "//" + image);
            adminProperties.findAndClick("add_Allimage");
            adminProperties.implicitWait();
            driver.findElement(By.tagName("img"));
            WebElement element = adminProperties.findElement(prop
                    .getProperty("product_image_bulkupload")
                    + "["
                    + cnt
                    + "]"
                    + prop.getProperty("product_image_bulkupload1"));
            if (element.getAttribute("href") != null) {
                adminProperties.isLinkBroken(new URL(element
                        .getAttribute("href")));
                System.out.println(adminProperties.isLinkBroken(new URL(element
                        .getAttribute("href"))));
            }
            cnt++;
        }
        adminProperties.findAndClick("insert_images");
        String catagory = "";
        cnt = 1;
        int price;
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

    public void insertTagAndCategory()
    {
        adminProperties.findAndClick("Catagory_click");
        adminProperties.findAndWrite("catagory", postcatagory);
        List<WebElement> optionlist = adminProperties.findElementByClass(prop
                .getProperty("catagory_ecommerce_by_ClassName"));
        for (WebElement options : optionlist) {
            if (options.getText().equalsIgnoreCase(postcatagory)) {
                options.click();
                break;
            }
        }
        adminProperties.findAndWrite("tag_input", tag);
        List<WebElement> Tagoptionlist = adminProperties
                .findElementByClass(prop.getProperty("tag_list_Byclassname"));
        for (WebElement options : Tagoptionlist) {
            if (options.getText().equalsIgnoreCase(tag)) {
                options.click();
                break;
            }
        }

    }

    @Test
    public void With_HomepageImage() throws Exception
    {
        Writeboard();
        adminProperties.findAndWrite("homepage_content",
                "Homepage content for ecommerce post");
        insertTagAndCategory();
        adminProperties.findAndWrite(
                "ecommerce_homepage_image",
                System.getProperty("user.dir") + "//"
                        + prop.getProperty("image_path") + "//"
                        + prop.getProperty("homepage_image"));
        adminProperties.findAndClick("ecommerce_upload_homepage_image");
        List<WebElement> imagesList = driver.findElements(By.tagName("img"));
        for (WebElement imgElement : imagesList) {
            if (imgElement != null) {
                adminProperties.verifyImageActive(imgElement);
            }
        }

        adminProperties.addFbTwitterText(fbtext);
    }

    @Test
    public void Without_HomepageImage() throws Exception
    {
        posttitle = "Automated eCommerce Post without homepage Image/content";
        Writeboard();
        insertTagAndCategory();
        adminProperties.addFbTwitterText(fbtext);
    }

    @AfterMethod
    public void teardown()
    {
        // driver.close();
    }
}
