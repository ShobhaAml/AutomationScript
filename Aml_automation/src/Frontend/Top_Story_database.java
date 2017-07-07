package Frontend;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Common.Adminproperty;
import Common.Frontend;

import java.sql.ResultSet;

public class Top_Story_database {

	Frontend frontendProperties = new Frontend();
	Adminproperty adminProperties = new Adminproperty();

	Statement stmt;
	Properties prop = new Properties();
	ResultSet rs;
	Connection con;
	String browser = "chrome";
	WebDriver driver;
	String url = "https://guest:guest@testing.xataka.com";
	String title;
	String FrontResult;
	String result;

	List<WebElement> section1;
	List<WebElement> section2;

	@BeforeClass
	public void getconnection() throws Exception {
		con = adminProperties.connectDb();
	}

	@Test(priority = 1)
	public void getTopStory() throws Exception {
		String Topquery = "select post_title from wp_posts where special_post_priority = 5 and special_post > now() order by post_date_gmt desc limit 1";
		stmt = con.createStatement();
		Statement stmt = con.createStatement();

		ResultSet rs = stmt.executeQuery(Topquery);

		while (rs.next()) {
			result = rs.getString(1);

			System.out.println(result);
		}

	}

	@Test(priority = 2)
	public void openbrowser() throws Exception {
		prop = frontendProperties.ReadProperties();
		driver = frontendProperties.frontcallproperty(url, prop.getProperty("browser"));
		browser = prop.getProperty("browser");
		frontendProperties.implicitWait();
		driver.findElement(By.xpath(prop.getProperty("top_story_text_homepage"))).getText();
		FrontResult = driver.findElement(By.xpath(prop.getProperty("top_story_text_homepage"))).getText();
		System.out.println(FrontResult);	
	}

	@AfterClass
	public void compareMethod() {

		String expected = result;
		String actual = FrontResult;

		if (expected.equalsIgnoreCase(actual)) {
			System.out.println("Matched");

		} else {
			System.out.println("Mismatch");
		}

	}
}
