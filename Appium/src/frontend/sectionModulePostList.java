package frontend;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import Common.Setup;

public class sectionModulePostList {
	String baseurl1 = "http://m.trendencias.com";
	WebDriver driver;
	String device = "Android";
	String deviceName = "ZY2242L8M3";
	String platformVersion = "7.0";
	String platformName = "Android";
	List<String> list1 = new ArrayList<String>();
	List<String> list2 = new ArrayList<String>();

	@Test
	public void sectionModule() throws InterruptedException, MalformedURLException {
		Setup set = new Setup();
		driver = set.callDesiredCaps(platformName, deviceName, platformVersion, baseurl1);
		System.out.println(baseurl1);
		Thread.sleep(5000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,11000)");
		Thread.sleep(1000);
		WebElement section1 = driver.findElement(By.xpath("html/body/div[1]/div[2]/div/section[8]/div"));
		List<WebElement> sectionList1 = section1.findElements(By.tagName("li"));

		WebElement section2 = driver.findElement(By.xpath("html/body/div[1]/div[2]/div/section[9]/div"));
		List<WebElement> sectionList2 = section2.findElements(By.tagName("li"));

		for (WebElement element1 : sectionList1) {
			list1.add(String.valueOf(element1.getText()));
		}
		System.out.println("Size of section1 -->" + sectionList1.size());
		System.out.println(list1);

		for (WebElement element2 : sectionList2) {
			list2.add(String.valueOf(element2.getText()));
		}
		System.out.println("Size of section2 -->" + sectionList2.size());
		System.out.println(list2);
	}
}
