package Frontend;



import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.ChromeDriverManager;


public class headless {
	
	@Test
	public void testbrowser()
	{	
		
		ChromeOptions options = new ChromeOptions();
    	 //options.addArguments("windows-size=1400,800");
    	 //options.addArguments("–disable-dev-shm-usage");
		options.addArguments("--start-maximized");
		ChromeDriverManager.getInstance().setup();
		options.addArguments("--dns-prefetch-disable");
		options.addArguments("--always-authorize-plugins");

      	 options.addArguments("headless");
      	 options.addArguments("–disable-gpu");
      	 options.setBinary(System.getProperty("user.dir") + "//src//Driverfiles//chromedriver");
 		//System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "//src//Driverfiles//chromedriver");

        WebDriver driver = new ChromeDriver(options);
        driver.get("http://www.google.com");
         System.out.println("test");    
	}

}
