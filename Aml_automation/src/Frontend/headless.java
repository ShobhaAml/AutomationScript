package Frontend;

import org.testng.annotations.Test;


public class headless {
	
	@Test
	public void testbrowser()
	{
		
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "//src//Driverfiles//linux//chromedriver");
       
	     
	    
	}

}
