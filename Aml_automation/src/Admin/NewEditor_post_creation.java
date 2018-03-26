package Admin;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class NewEditor_post_creation {

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

	@Test
	public void MVPattachImage() throws Exception
	{	
		adminProperties.LoginAdmin(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
		//driver.navigate().to("http://test.admin.weblogssl.com/escribir/-L6uvRAZ8dhDlfxzQnTg?blog=xataka&userid=1");
		driver.manage().timeouts().implicitlyWait(10000,TimeUnit.SECONDS);
		adminProperties.CreateMVPpost();
		adminProperties.implicitWait();
		adminProperties.AddMVPTitle("Testing title");
		adminProperties.implicitWait();

		//**add Title
		/*adminProperties.findAndClick("MVPtitle");
		adminProperties.findAndWrite("MVPtitle", "Testing title");*/
		/*adminProperties.AddMVPTitle("Testing title");*/
		List<WebElement> list = adminProperties.findElementsByXpath(prop.getProperty("MVPSectionList"));
		System.out.println(list.size() +"list count");
		System.out.println(list.get(list.size()-1).getAttribute("id"));
		/*adminProperties.findElement("//div[@id='"+list.get(list.size()-1).getAttribute("id")+"']").click();
	    adminProperties.implicitWait();*/
       /* Actions action = new Actions(driver);
		action.sendKeys(Keys.PAGE_DOWN);
		action.sendKeys(Keys.END);
		action.sendKeys(Keys.SHIFT);
		action.sendKeys(Keys.END);
		action.sendKeys(Keys.ENTER);
		adminProperties.implicitWait();*/
		/*adminProperties.findAndClick("Clickplus");
		adminProperties.implicitWait();
		Thread.sleep(1000);
		adminProperties.findAndClick("MVPImage");*/
		adminProperties.ClickImageICON(driver);
		adminProperties.implicitWait();
        adminProperties.findAndClick("MVPResourceImage1");
		adminProperties.implicitWait();
		adminProperties.implicitWait();
		driver.switchTo().activeElement();
        adminProperties.findAndWrite("MVPImageAlt","testing alternate text");
        adminProperties.findAndClick("MVPInsertButton");
		
	}
	
	
}
