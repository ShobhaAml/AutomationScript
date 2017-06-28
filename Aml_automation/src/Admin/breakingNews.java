package Admin;

import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;

import Common.Adminproperty;
import Common.Frontend;

public class breakingNews {

	WebDriver driver;
	Adminproperty adminProperties = new Adminproperty();
	Properties prop = new Properties();
	String browser = "";
	String title = "Automate Breaking news1";
	String link = "https://testing.xataka.com/automovil/nokia-here-se-instala-en-el-salpicadero-de-los-nuevos-toyota";
	String edittitle = "Automate Breaking news2";
	String editlink = "https://www.youtube.com/watch?v=7AVdCvG6eIU";
	String activate_news_title_name = "werwer vwreewrwer";
	String news_title_name = "qweqrew rgertertge";
	String DateTime = "2018/06/16 09:49";

	@BeforeClass
	public void Setup() throws Exception {
		prop = adminProperties.ReadProperties();
		driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
		browser = prop.getProperty("browser");
	}

	@Test(priority = 1)
	public void Login() throws Exception {
		adminProperties.adminLogin();
		adminProperties.findAndClick("navigation_header");
		adminProperties.findAndClick("Navigate_breaking_news");
	}

	@Test(priority = 2)
	public void goToCreate() {
		adminProperties.findAndClick("create_breaking_news");
	}

	@Test(priority = 3)
	public void createBreakingNews() {

		adminProperties.findAndWrite("breaking_news_title", title);
		adminProperties.findAndWrite("breaking_news_link", link);
		adminProperties.findAndClick("breaking_news_Submit");

	}

	@Test(priority = 4)
	public void Edit_news(String news_title_name, String edittitle, String editlink) {
		WebElement tableelement = driver.findElement(By.id("BreakingNewsListing"));
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(new Predicate<WebDriver>() {
			public boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		});
		int cnt = 1;
		new Actions(driver).moveToElement(driver.findElement(By.className("table-actions"))).perform();
		List<WebElement> list = adminProperties
				.findElementsByXpath(".//*[@id='BreakingNewsListing']/table/tbody/tr/td[1]");
		for (WebElement element : list) {
			System.out.println(element.getText());
			adminProperties.implicitWait();

			if (element.getText().equalsIgnoreCase(news_title_name)) {
				new Actions(driver)
						.moveToElement(adminProperties.findElement(
								prop.getProperty("news_list_row") + "[" + cnt + "]" + prop.getProperty("edit_news")))
						.click().perform();
				adminProperties.implicitWait();

				break;
			}
			cnt++;

		}
		adminProperties.findElement(prop.getProperty("breaking_news_title_e")).clear();
		adminProperties.findElement(prop.getProperty("breaking_news_link_e")).clear();
		adminProperties.findAndWrite("breaking_news_title_e", edittitle);
		adminProperties.findAndWrite("breaking_news_link_e", editlink);
		adminProperties.findAndClick("breaking_news_Submit");
		System.out.println("Breaking News edited successfully");
	}

	@Test(priority = 6)
	public void delete_news(String delete_news_title_name) {
		WebElement tableelement = driver.findElement(By.id("BreakingNewsListing"));
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(new Predicate<WebDriver>() {
			public boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		});
		int cnt = 1;
		new Actions(driver).moveToElement(driver.findElement(By.className("table-actions"))).perform();
		List<WebElement> list = adminProperties
				.findElementsByXpath(".//*[@id='BreakingNewsListing']/table/tbody/tr/td[1]");
		for (WebElement element : list) {
			System.out.println(element.getText());
			adminProperties.implicitWait();

			if (element.getText().equalsIgnoreCase(delete_news_title_name)) {
				new Actions(driver)
						.moveToElement(adminProperties.findElement(
								prop.getProperty("news_list_row") + "[" + cnt + "]" + prop.getProperty("delete_news")))
						.click().perform();
				adminProperties.implicitWait();

				break;
			}
			cnt++;

		}
	}

	@Test(priority = 5)
	public void activate_news() throws InterruptedException {
		WebElement tableelement = driver.findElement(By.id("BreakingNewsListing"));
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(new Predicate<WebDriver>() {
			public boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		});
		int cnt = 1;
		new Actions(driver).moveToElement(driver.findElement(By.className("table-actions"))).perform();
		List<WebElement> list = adminProperties
				.findElementsByXpath(".//*[@id='BreakingNewsListing']/table/tbody/tr/td[1]");
		for (WebElement element : list) {
			System.out.println(element.getText());
			adminProperties.implicitWait();

			if (element.getText().equalsIgnoreCase(activate_news_title_name)) {

				String dataid = adminProperties
						.findElement(
								prop.getProperty("news_list_row") + "[" + cnt + "]" + prop.getProperty("activate_news"))
						.getAttribute("data-id");

				if (driver.findElements(By.xpath(".//*[@id='breakingNews']/td[4]/div/button")).size() != 0) {

					adminProperties.findElement(
							prop.getProperty("news_list_row") + "[" + cnt + "]" + prop.getProperty("activate_news"))
							.click();

					System.out.println(
							prop.getProperty("news_list_row") + "[" + cnt + "]" + prop.getProperty("activate_news"));

					WebElement element2 = driver
							.findElement(By.xpath(".//*[@id='submitBreakingTag'][@data-id=" + dataid + "]"));

					System.out.println("driver.findElement(By.xpath(.//*[@id='endDateInput-" + dataid + "']'))");

					JavascriptExecutor executor1 = (JavascriptExecutor) driver;
					executor1.executeScript("arguments[0].setAttribute('value', '')", element2);
					System.out.println(element2.getAttribute("value"));
					adminProperties.implicitWait();

					executor1.executeScript("arguments[0].setAttribute('value', '2018/06/16 09:49')", element2);
					System.out.println(element2.getAttribute("value"));

					adminProperties.implicitWait();
					WebElement element1 = driver
							.findElement(By.xpath(".//*[@id='submitBreakingTag'][@data-id=" + dataid + "] "));
					executor1.executeScript("arguments[0].click();", element1);
					adminProperties.implicitWait();

				} else {
					System.out.print("News is already in active mode");

				}
				break;
			}
			cnt++;

		}

	}

}