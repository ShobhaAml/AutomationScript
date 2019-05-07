package Admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class test {
	Adminproperty adminProperties = new Adminproperty();

	@Test
	public void TestScript() throws Exception
	{
		System.setProperty("webdriver.chrome.driver","E:\\driver\\chromedriver.exe");
		// Initialize browser
		WebDriver driver=new ChromeDriver();
		driver.get("http://www.facebook.com");
		// Maximize browser
		driver.manage().window().maximize();
		System.out.println("Successfull");
		
	
	}
	
	
}
