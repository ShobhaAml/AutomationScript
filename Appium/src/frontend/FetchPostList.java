package frontend;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import Common.Setup;
import io.appium.java_client.android.AndroidDriver;

public class FetchPostList {
	
	Setup set= new Setup();
	String version="", Platformname= "";
	String Devicename="";	
	
	String siteurl="http://guest:guest@mtest.xataka.com";
	WebDriver driver;
	int pageno=2;
	
	
	@Test
	public void fetchPosts() throws MalformedURLException
	{
		Setup set= new Setup();
		
		//REAL Device Paramaters
		version="7.0";
	    Platformname= "Android";
		Devicename="33001e543a45a277";
		
		driver =  set.callDesiredCaps(Platformname, Devicename, version, siteurl);
		driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);
	
		
		 for(int i=0;i<pageno;i++)
	     {
	            if(i>0)
	            {
	            	//driver.scrollTo("SAVE");
	            	driver.findElement(By.xpath(".//*[@class='btn btn-next']")).click();
	                driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);
	            }

	            List<WebElement> optionlist = driver.findElements(By.xpath("//*[@class=\"abstract-title\"]"));
	    		for (WebElement options : optionlist) {
	    			System.out.println(options.getText());
	    			
	    		}
	      }
		
		
		
		
		

		
	}

}
