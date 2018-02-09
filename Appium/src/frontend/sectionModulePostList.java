package frontend;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import java.awt.AWTException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class sectionModulePostList {
	String baseurl1 = "http://mtest.xataka.com";
	AppiumDriver<MobileElement> driver;
	String device = "Android";
	String deviceName = "ZY2242L8M3";
	String platformVersion = "7.0";
	String platformName = "Android";	
	List<String> list1 = new ArrayList<String>();
	List<String> list2 = new ArrayList<String>();

	@BeforeClass
	public void setUp() throws InterruptedException, AWTException, Exception {
		DesiredCapabilities capabilites = new DesiredCapabilities();         
		capabilites.setCapability("device", device);
		capabilites.setCapability("deviceName", deviceName);
		capabilites.setCapability("platformVersion", platformVersion);
		capabilites.setCapability("platformName", platformName);
		capabilites.setCapability("browserName", "chrome");
		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilites);
		driver.manage().timeouts().implicitlyWait(20L, TimeUnit.SECONDS);
		System.out.println(baseurl1);
		Thread.sleep(1000);
		driver.get(baseurl1);
		Thread.sleep(10000);
			}

	@Test
	public void sectionModule() throws InterruptedException {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,7550)");
		Thread.sleep(1000);
		List<MobileElement> sectionList1 = driver.findElementByXPath("html/body/div[1]/div[3]/div/section[10]/div").findElementsByTagName("li");
		List<MobileElement> sectionList2 = driver.findElementByXPath("html/body/div[1]/div[3]/div/section[11]/div").findElementsByTagName("li");
		System.out.println("Size of section1 -->" + sectionList1.size());
		for (MobileElement element1 : sectionList1) {
			list1.add(String.valueOf(element1.getText()));
		}
		System.out.println(list1);
		System.out.println("Size of section2 -->" + sectionList2.size());
		for (MobileElement element2 : sectionList2) {
			list2.add(String.valueOf(element2.getText()));
		}
		System.out.println(list2);
	}
}
