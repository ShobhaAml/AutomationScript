package Frontend;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;


public class headless {
	
	@Test
	public void testbrowser()
	{
		
		System.setProperty("webdriver.chrome.driver", "//var//lib//jenkins/jobs//AMPvalidations//workspace//Aml_automation///src//Driverfiles//linux//chromedriver");
     
	
	    WebDriver driver = new ChromeDriver();
		   
	
	     
	    
	}

}
