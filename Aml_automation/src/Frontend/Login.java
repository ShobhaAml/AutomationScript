package Frontend;

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

import com.google.common.base.Verify;

import Common.Adminproperty;
import Common.Frontend;

public class Login
{
	int invalidImageCount = 0;
	WebDriver driver;
	Frontend frontendProperties = new Frontend();
	Properties prop = new Properties();
	String browser = "";

	@BeforeMethod
	public void Setup() throws Exception {
		prop = frontendProperties.ReadProperties();
		driver = frontendProperties.frontcallproperty(prop.getProperty("url"), prop.getProperty("browser"));
		browser = prop.getProperty("browser");
	}

	@DataProvider(name = "testdata")
	public Object[][] TestDataFeed() throws Exception {
		Object[][] postdata = frontendProperties.readExcel("Login", 2);
		return postdata;
	}

	@Test(dataProvider = "testdata")
	public void StandardLogin(String username, String password) {
		frontendProperties.clickMenu("EntraORegistrate");
		String message = frontendProperties.StandardLogin(username, password);
		if (message == "") {
			message = frontendProperties.checkifuserloggedin();
		}

		System.out.println(message);
	}

	@Test(dataProvider = "testdata")
	public void facebookLogin(String username, String password) throws Exception {
		frontendProperties.clickMenu("EntraORegistrate");
		String message = frontendProperties.facebookLogin(username, password);
		if (message == "") {
			message = frontendProperties.checkifuserloggedin();
		}

	}

	@Test(dataProvider = "testdata")
	public void twitterLogin(String username, String password) throws Exception {
		frontendProperties.clickMenu("EntraORegistrate");
		String message = frontendProperties.twitterLogin(username, password);
		if (message == "") {
			message = frontendProperties.checkifuserloggedin();
		}
	}
}
