package Frontend;



import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

public class headless {
	
	@Test
	public void testbrowser()
	{	
 		//System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "//src//Driverfiles//chromedriver");
 		ChromeOptions options = new ChromeOptions();
      	// options.setBinary(System.getProperty("user.dir") + "//src//Driverfiles//chromedriver");
 		//options.addArguments("windows-size=1400,800");
    	 //options.addArguments("–disable-dev-shm-usage");
		options.addArguments("--start-maximized");
     	 options.addArguments("--headless");
		//ChromeDriverManager.getInstance().setup();
		//options.addArguments("--dns-prefetch-disable");
		//options.addArguments("--always-authorize-plugins");
         /*DesiredCapabilities capabilities = DesiredCapabilities.chrome();

         capabilities.setBrowserName("chrome");
         capabilities.setPlatform(Platform.LINUX);

         capabilities.setCapability(ChromeOptions.CAPABILITY, options);
         WebDriver driver =  new ChromeDriver(capabilities);*/
      	 //options.addArguments("–disable-gpu");

       WebDriver driver = new ChromeDriver(options);
       options.addArguments("--whitelist-ip *");
       options.addArguments("--proxy-server='direct://'");
       options.addArguments("--proxy-bypass-list=*");
        driver.get("http://www.google.com");
         System.out.println("test");    
	}

}
