package Admin;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Common.Adminproperty;
import Common.Frontend;

public class Login
{
    int invalidImageCount = 0;
    WebDriver driver;
    Frontend frontendProperties = new Frontend();    
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
        prop = frontendProperties.ReadProperties();
        driver = frontendProperties.frontcallproperty(prop.getProperty("url"),
                prop.getProperty("browser"));
        browser = prop.getProperty("browser");
    }

    @DataProvider(name = "testdata")
    public Object[][] TestDataFeed() throws Exception
    {
        Object[][] postdata = frontendProperties.readExcel("Login", 2);
        return postdata;
    }
    
    @Test(dataProvider="testdata")
    public void StandardLogin(String username, String password)
    {
        
        frontendProperties.clickMenu("EntraORegistrate");
        frontendProperties.implicitWait();
 /*       frontendProperties.findAndWrite("standard_email", username);
        frontendProperties.findAndWrite("standard_password", password);
        frontendProperties.findAndClick("standard_button");
        String invalidmessage="";
        
            try
            {
                if(new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("standard_invalid_validation"))))==null)
                {
                    invalidmessage = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("standard_invalid_validation")))).getText();
                }
                else
                {
                    Assert.fail("Invalid Login credentials");
                   
                }
            }
            catch(Exception e)
            {
                
            }*/
        
 
    }
   
}
