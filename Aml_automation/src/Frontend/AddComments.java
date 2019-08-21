package Frontend;

import java.util.Properties;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Common.Frontend;

public class AddComments {
	
		Frontend frontendProperties = new Frontend();    
		String browser="";
		String url="";
		WebDriver driver ;
		Properties prop=new Properties();
		String username = "shobha@agilemedialab.in";
		String password = "shobha";
		String sousername = "amlqa@outlook.com";
		String sopassword = "Aml@12345";
		String comment = "";
		
	    
	    	@BeforeMethod
	    	public void RandomComment() {
	    		int length = 20;
	    	    boolean useLetters = true;
	    	    boolean useNumbers = false;
	    	    String random_comment = RandomStringUtils.random(length, useLetters, useNumbers);
	    	    System.out.println("Comment to be added " + random_comment);
	    	    comment = random_comment;
	    	}
	    	
	    	@BeforeMethod
		    public void openbrowser() throws Exception
		    {
		      prop = frontendProperties.ReadProperties();
		      //driver = frontendProperties.headlessbrowser(prop.getProperty("url"), "headless");
		      driver = frontendProperties.frontcallproperty(prop.getProperty("url"),	  
		      prop.getProperty("browser"));
			  browser = prop.getProperty("browser");
			  url=prop.getProperty("url");
		       
		    }
			
		   
		    @Test(priority=1)
		    public void StdPostComment() throws Exception
			   {
		    	frontendProperties.login(username, password, "Std");
			    frontendProperties.findAndClick("cookie");
			    frontendProperties.findAndClick("postcommentlink");
			    System.out.println("Let's add Comment");			    
			    frontendProperties.addcomments(comment);
			    frontendProperties.addRescomments(comment, url);
			    frontendProperties.implicitWait();
			    driver.quit();
			    
		    }
		   //@Test(priority=2)
		    public void FBPostComment() throws Exception
			   {
		    	frontendProperties.login(sousername, sopassword, "fb");
			    frontendProperties.findAndClick("cookie");
			    frontendProperties.implicitWait();
			    frontendProperties.findAndClick("postcommentlink");
			    System.out.println("Let's add Comment");
			    frontendProperties.addcomments(comment);
			    frontendProperties.addRescomments(comment, url);
			    frontendProperties.implicitWait();
			    driver.quit();
		    }
		   // @Test(priority=3)
		    public void TWPostComment() throws Exception
			   {
		    	frontendProperties.login(sousername, sopassword, "twitter");
			    frontendProperties.findAndClick("cookie");
			    frontendProperties.implicitWait();
			    frontendProperties.findAndClick("postcommentlink");
			    System.out.println("Let's add Comment");
			    frontendProperties.addcomments(comment);
			    frontendProperties.addRescomments(comment, url);
			    frontendProperties.implicitWait();
			    driver.quit();
			   }
		   
		   
}


