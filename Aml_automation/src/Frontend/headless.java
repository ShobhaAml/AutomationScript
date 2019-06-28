package Frontend;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

public class headless {
	
	@Test
	public void testbrowser()
	{
		System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
		ChromeOptions chromeOptions = new ChromeOptions();
		//chromeOptions.setBinary("//src//Driverfiles//linux//chromedriver");
		chromeOptions.addArguments("--no-sandbox");
		chromeOptions.addArguments("--headless");
		//chromeOptions.addArguments("--disable-dev-shm-usage");
		//chromeOptions.setExperimentalOption("useAutomationExtension", false);
		ChromeDriver driver = new ChromeDriver(chromeOptions);
		driver.get("http://google.com");    
	}
}
