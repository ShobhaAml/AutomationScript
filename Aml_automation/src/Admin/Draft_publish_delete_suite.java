package Admin;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import Common.Adminproperty;
public class Draft_publish_delete_suite {
    WebDriver driver;
    Adminproperty adminProperties = new Adminproperty();
    Properties prop = new Properties();
    String postName = "Normal longform xataka trend";
    String listtype = "Dashboardlist";
    String Post_Title= "Canon EOS 77D, toda la y toma de contacto con la nuevavanzada";
    String Selectors = "xatakandroid,xatakamovil";
    String tittle_data = "Las siete claves para elegir bien un segundo objetivo para tu";
    String FurturePostname = "Tacos de pescado al chipotle. Receta mexicana";
    String fbText = "fb testing";
    String twitterText = "";
    String DateTime = "27/12/2027 12:27";
    String FuturePostDate = "12:30,21/12/2026";
    String catagoryText = "Especiales";
    String tag = "Encuesta";
    String posttitleDelete= "Normal Post";
    
    public void Setup() throws Exception {
        prop = adminProperties.ReadProperties();
        driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
        adminProperties.adminLogin();
    }
   
    @Parameters({"posttitle"})
    @Test
    public void openPublishDraft(String posttitle) throws Exception {
    	Setup();
        Thread.sleep(5000);
        driver.navigate().refresh();
        Select dropdown = new Select(driver.findElement(By.id("postStatus")));
        dropdown.selectByValue("1");
        adminProperties.implicitWait();

        adminProperties.clickButton("DashboardEditbuttontr", "DashboardEditbuttontd", posttitle, listtype);
        adminProperties.implicitWait();
        adminProperties.findAndClick("publish_tab");
Thread.sleep(5000);       
adminProperties.findAndClick("publishDraftButton");
    }
  
    @Parameters({"posttitleDraft"})
    @Test
    public void openPublishdraft2(String posttitleDraft) throws Exception {
    	Setup();
        Thread.sleep(5000);
        driver.navigate().refresh();
        Select dropdown = new Select(driver.findElement(By.id("postStatus")));
        dropdown.selectByValue("1");
        adminProperties.implicitWait();

        adminProperties.clickButton("DashboardEditbuttontr", "DashboardEditbuttontd", posttitleDraft, listtype);
        adminProperties.implicitWait();
        adminProperties.findAndClick("publish_tab");
Thread.sleep(5000);       
adminProperties.findAndClick("publishDraftButton");
    }
    
    @Test
    public void openPublishDelete() throws Exception {
    	Setup();
        Thread.sleep(5000);
        driver.navigate().refresh();
        Select dropdown = new Select(driver.findElement(By.id("postStatus")));
        dropdown.selectByValue("1");
        adminProperties.implicitWait();

        adminProperties.clickButton("DashboardEditbuttontr", "DashboardEditbuttontd", posttitleDelete, listtype);
        adminProperties.implicitWait();             
        adminProperties.implicitWait();
    
    }
    
  
    }
