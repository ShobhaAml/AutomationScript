package Admin;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DashBoard_Draft_And_Repost {
    WebDriver driver;
    Adminproperty adminProperties = new Adminproperty();
    Properties prop = new Properties();
    String Postname = "En infinidad de ocasiones hemos comentado cómo influyen las modas en los entrenamientos";
    String listtype ="Dashboardlist";
    String Selectors = "xatakandroid,xatakamovil";
    String tittle_data = "En infinidad de ocasiones hemos comentado cómo influyen las modas en los entrenamientos";
    @BeforeMethod
    public void Setup() throws IOException
    {
        prop = adminProperties.ReadProperties();
        driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
    }

    public void login() throws Exception
    {
        adminProperties.adminLogin();
    }

    @Test
    public void editposts() throws Exception{
   
        login();
        //Draft
        adminProperties.clickButton("DashboardEditbuttontr","DashboardDrafttd",Postname,listtype);
        adminProperties.implicitWait();
        adminProperties.dialogBoxOk();
        //Repost
       adminProperties.clickButton("DashboardEditbuttontr","DashboardDraftReptd",Postname,listtype);
        adminProperties.implicitWait();
        Thread.sleep(3000);
        adminProperties.repostCheckbox(Selectors);
        adminProperties.implicitWait();
        adminProperties.findAndClick("repost_post_button");
        Thread.sleep(3000);
        //adminProperties.findAndClick("clear_repost_popup");
        driver.navigate().to("https://testing.xatakafoto.com/admin");
        adminProperties.adminLogin();
        adminProperties.implicitWait();
        Thread.sleep(5000);
        driver.navigate().refresh();
        driver.navigate().refresh();     
        adminProperties.implicitWait();
        adminProperties.findAndClick("notification_button");
        adminProperties.implicitWait();
        adminProperties.clickNotificationButton(tittle_data);
  
}

}