package Admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

public class hellotest {

	@Test
	public void test1()
	{
		System.out.println("hi test 1");
		WebDriver driver= new ChromeDriver();
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + "//src//Driverfiles//" + "chromedriver.exe");
		driver.get("https://www.xataka.com/");
		
		
	}
	
	@Test
	public void test2()
	{
		System.out.println("hi test 2");
		WebDriver driver= new ChromeDriver();
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + "//src//Driverfiles//" + "chromedriver.exe");
		driver.get("https://www.xatakafoto.com/");
	}

}
