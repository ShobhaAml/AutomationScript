package Common;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Common.Adminproperty;

public class Frontend extends Adminproperty
{

	public Properties ReadProperties() throws IOException {
		FileInputStream inStream = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\Common\\frontend.properties");
		prop.load(inStream);
		return prop;
	}

	public WebDriver frontcallproperty(String url, String browser) throws IOException {
		if (browser.trim().equalsIgnoreCase("Chrome")) {
			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + "//src//Driverfiles//" + "chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("start-maximized");
			driver = new ChromeDriver(options);
		} else {
			System.setProperty("webdriver.gecko.driver",
					System.getProperty("user.dir") + "//src//Driverfiles//" + "geckodriver.exe");
			driver = new FirefoxDriver();
		}
		driver.get(url);
		if (browser.trim().equalsIgnoreCase("firefox")) {
			driver.switchTo().alert().accept();
			driver.manage().window().maximize();
		}

		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		return driver;
	}

	public void clickMenu(String Linktext) {
		super.findAndClick("cookie");
		driver.findElement(By.className(prop.getProperty("Menu"))).click();
		super.implicitWait();
		if (Linktext.equalsIgnoreCase("EntraORegistrate")) {
			super.findAndClick("login");
		}
		super.implicitWait();
	}

	public String checkifuserloggedin() {
		String name = "";
		driver.findElement(By.className(prop.getProperty("Menu"))).click();
		super.implicitWait();
		if (driver.findElement(By.className(prop.getProperty("loggedusername"))) != null) {
			name = driver.findElement(By.className(prop.getProperty("loggedusername"))).getText()
					+ " Logged in successfully";
		} else {
			name = "User not logged";
		}

		return name;
	}

	public String StandardLogin(String username, String password) {
		implicitWait();
		findAndWrite("standard_email", username);
		findAndWrite("standard_password", password);
		findAndClick("standard_button");
		String invalidmessage = "";
		try {
			if (new WebDriverWait(driver, 10).until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(prop.getProperty("standard_invalid_validation")))) != null) {
				invalidmessage = new WebDriverWait(driver, 10)
						.until(ExpectedConditions
								.visibilityOfElementLocated(By.xpath(prop.getProperty("standard_invalid_validation"))))
						.getText();
				invalidmessage = "Invalid Login credentials: " + invalidmessage;
			}
		} catch (Exception e) {

		}
		System.out.println(invalidmessage);
		return invalidmessage;
	}

	public void addcomments(String comment) {
		String error = "";
		findAndWrite("commentbox", comment);
		implicitWait();
		findAndClick("commentsubmit");
		try {

			/*
			 * WebDriverWait wait = new WebDriverWait(driver, 5); // The int
			 * here is the maximum time in seconds the element can wait. error =
			 * wait.until(
			 * ExpectedConditions.visibilityOfElementLocated(By.xpath(prop
			 * .getProperty("commenterror")))).getText();
			 */
			if (findElement(prop.getProperty("commenterror")) != null) {
				error = findElement(prop.getProperty("commenterror")).getText();
				System.out.println("ERROR: " + error);
			}

		} catch (Exception e) {

		}
		System.out.println("ERROR: " + error);
		if (error == "") {
			/*
			 * List<WebElement> lst = findElementsByXpath(prop
			 * .getProperty("commentlist")); System.out.println(lst.size()); for
			 * (WebElement list : lst) { if
			 * (list.findElement(By.className("comment-content")).getText()
			 * .equalsIgnoreCase(comment)) {
			 * System.out.println("<b>Added comment: </b>" + comment); } }
			 */
		}
	}

	public Boolean IsElementPresent(By by, WebDriver driver) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}

	}

	public String login(String username, String password, String url, String usersession, String logintype) {
		String message = "";
		if (url.contains("testing.") || url.contains("mtest.") || url.contains("test.")) {
			if (usersession == "1") {
				clickMenu("EntraORegistrate");
			}

			if (logintype == "twiiter") {
			} else if (logintype == "fb") {
			} else {
				message = StandardLogin(username, password);
				if (usersession == "1") {
					if (message == "") {
						message = checkifuserloggedin();
					}
					findAndClick("CloseMenu");
				}
				System.out.println(message);
			}
			usersession = message;
		} else {
			System.out.println("Production site restricted");
			throw new InvalidPathException("Production site restricted", "Production site restricted");
		}

		return usersession;
	}

	public void addRescomments(String comment) {

		findAndWrite("resCommentEntra", comment);
		findAndClick("publishRespuestasComment");

	}

	public String twitterLogin(String username, String password) throws Exception {
		String message = "";
		String winHandleBefore = driver.getWindowHandle();

		findAndClick("twitter_registor_button");
		implicitWait();

		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}

		implicitWait();
		findAndWrite("twitter_email", username);
		findAndWrite("twitter_password", password);
		findAndClick("twitter_button");
		implicitWait();

		boolean searchIconPresence = driver.findElement(By.className("message-text")).isDisplayed();
		if (searchIconPresence == true) {
			driver.findElement(By.className("message-text")).getText();
			System.out.println(driver.findElement(By.className("message-text")).getText());
			message = (driver.findElement(By.className("message-text")).getText());

		}

		else {
			System.out.println("Logged in successfully");

		}
		implicitWait();
		driver.switchTo().window(winHandleBefore);
		return message;
	}

	public String facebookLogin(String username, String password) throws Exception {
		String message = "";
		findAndClick("facebook_registor_button");
		implicitWait();
		findAndWrite("facebook_email", username);
		findAndWrite("facebook_password", password);
		findAndClick("facebook_button");
		implicitWait();

		if (driver.findElement(By.xpath(".//*[@id='globalContainer']/div[3]/div/div/div")).isEnabled() == true) {
			System.out.println("Exit");
		}

		return message;

	}
	public static Set<String> findDuplicates(List<String> listContainingDuplicates) {
		 
		final Set<String> setToReturn = new HashSet<String>();
		final Set<String> set1 = new HashSet<String>();
 
		for (String yourInt : listContainingDuplicates) {
			if (!set1.add(yourInt)) {
				setToReturn.add(yourInt);
			}
		}
		return setToReturn;
	}
}
