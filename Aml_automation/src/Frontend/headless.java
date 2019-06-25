package Frontend;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

public class headless {
	
	@Test
	public void testbrowser()
	{
		
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/usr/bin/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBinary("/usr/bin/chromedriver");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
      //  chromeOptions.setExperimentalOption("useAutomationExtension", false);
      	WebDriver driver = new ChromeDriver(chromeOptions);
		driver.get("http://www.google.com");     
		System.out.println("<-->");
	}
}
