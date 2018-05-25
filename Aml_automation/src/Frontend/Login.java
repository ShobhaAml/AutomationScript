package Frontend;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Common.Frontend;

public class Login {
	
	Frontend frontendProperties = new Frontend();    
	String browser="";
	String url="";
	WebDriver driver ;
	Properties prop=new Properties();
 
	@BeforeMethod
	    public void openbrowser() throws Exception
	    {
	        prop = frontendProperties.ReadProperties();
	        driver = frontendProperties.frontcallproperty(prop.getProperty("url"),
	                prop.getProperty("browser"));
	        browser = prop.getProperty("browser");
	        url=prop.getProperty("url");
	       
	    }
	 
	 	@DataProvider(name = "testdata")
		public Object[][] TestDataFeed() throws Exception {
			Object[][] postdata = frontendProperties.readExcel("Login", 2);
			return postdata;
		}

	   @Test(dataProvider = "testdata")
	   public void StandardLogin(String username, String password) throws Exception
	   {
		   frontendProperties.login(username, password,url,"std");
		}
	   
	   @Test(dataProvider = "testdata")
		public void facebookLogin(String username, String password) throws Exception
		{
			frontendProperties.login(username, password,url,"fb");
		}
		
	    @Test(dataProvider = "testdata") public void twitterLogin(String username, String password) throws Exception
		{
			frontendProperties.login("agiletest111@gmail.com", "amita123",url,"twitter");
		}
}
