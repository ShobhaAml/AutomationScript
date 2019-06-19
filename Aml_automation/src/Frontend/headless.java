package Frontend;



import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;


public class headless {
	
	@Test
	public void testbrowser()
	{
		
	/*  System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "//src//Driverfiles//linux//chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
		WebDriver driver = new ChromeDriver(chromeOptions);
		driver.get("http://google.com");  */
		
    	
    		ChromeOptions options = new ChromeOptions();
    		options.addArguments("windows-size=1400,800");
    		options.addArguments("headless");
            WebDriver driver = new ChromeDriver(options);
            driver.get("http://www.google.com");
            System.out.println(driver.getTitle());

  
	
	     
	    
	}

}
