package Frontend;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Common.Frontend;

public class editorialRecommendation {
	Frontend frontendProperties = new Frontend();
	String browser = "";
	WebDriver driver;
	Properties prop = new Properties();
	String url = "https://guest:guest@testing.xataka.com";
	String postTitle = "ddsfds fds ffsdgf sgsgsgsdg sgds";
	String homeRecom, postRecom;
	List<String> list = new LinkedList<String>();

	@BeforeTest()
	public void openbrowser() throws Exception {
		prop = frontendProperties.ReadProperties();
		driver = frontendProperties.frontcallproperty(url, prop.getProperty("browser"));
		browser = prop.getProperty("browser");
	}

	@Test
	public void getEditRec() throws InterruptedException

	{
		frontendProperties.findAndClick("cookie");
		List<WebElement> editRecom1 = driver.findElements(By.xpath(prop.getProperty("editRec")));
		for (WebElement Recom1 : editRecom1) {
			homeRecom = Recom1.getText();
			System.out.println("\n" + homeRecom);
		}
		System.out.println("........................................................");
		List<WebElement> lists = driver.findElements(By.xpath(prop.getProperty("postcommentlink")));
		for (WebElement post : lists) {
			String title = post.getText();
			if (title.equals(postTitle)) {
				post.click();
				break;
			}
		}
		List<WebElement> editRecom2 = driver.findElements(By.xpath(prop.getProperty("editRec")));
		for (WebElement Recom2 : editRecom2) {
			postRecom = Recom2.getText();
			System.out.println("\n" + postRecom);
		}
		if (homeRecom.contentEquals(postRecom)) {
			System.out.println("success");
		}
	}
}
