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

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
		
		FileInputStream inStream = new FileInputStream("/Users/isha/AML/Automation/Aml_automation/src/Common/frontend.properties");
		prop.load(inStream);
		return prop;
}

    public WebDriver frontcallproperty(String url, String browser) throws IOException {

	if (browser.trim().equalsIgnoreCase("Chrome")) {
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		driver = new ChromeDriver(options);

	} else {
		System.setProperty("webdriver.gecko.driver",
				System.getProperty("user.dir") + "//src//Driverfiles//" + "geckodriver.exe");
		driver = new FirefoxDriver();
	}
	driver.get(url);
	driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	return driver;
}


	public void clickMenu(String Linktext, String Rtype) {
		//super.findAndClick("cookie");
		driver.findElement(By.xpath(prop.getProperty("Menu"))).click();
		super.implicitWait();
		if (Linktext.equalsIgnoreCase("EntraORegistrate")) {
			if(Rtype.equalsIgnoreCase("login"))
			{
			super.findAndClick("login");
			}
			else
			{
				
			}
		}
		super.implicitWait();
	}

	public String checkifuserloggedin() {
		String name = "";
		driver.findElement(By.xpath(prop.getProperty("Menu"))).click();
		super.implicitWait();
		if (driver.findElement(By.xpath(prop.getProperty("loggedusername"))) != null) {
			name = driver.findElement(By.xpath(prop.getProperty("loggedusername"))).getText()
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
		String invalidmessage = "Success";
		try {
			
			if (new WebDriverWait(driver, 10).until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(prop.getProperty("standard_invalid_validation")))) != null) {
				invalidmessage = new WebDriverWait(driver, 10)
						.until(ExpectedConditions
								.visibilityOfElementLocated(By.xpath(prop.getProperty("standard_invalid_validation"))))
						.getText();
				invalidmessage = "Invalid Login credentials: " + invalidmessage;
			}
			
			//System.out.println(prop.getProperty("deactivated_user"));
		} catch (Exception e) {

		}
		
			try {
					
				if(new WebDriverWait(driver, 10).until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath(prop.getProperty("deactivated_user")))) != null) {
					invalidmessage = new WebDriverWait(driver, 10)
					.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath(prop.getProperty("deactivated_user"))))
					.getText();
					invalidmessage = "Deactivated User :  " + invalidmessage;
				}
				//System.out.println(prop.getProperty("deactivated_user"));
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

	public String login(String username, String password, String url,  String logintype) throws Exception {
		String message = "";
			clickMenu("EntraORegistrate","login");
			if (logintype == "twitter") {
				message = twitterLogin(username, password);
				if (message == "Success") {
				message = checkifuserloggedin();
				findAndClick("CloseMenu");
				}
		
			System.out.println(message);
				
			} else if (logintype == "fb") {
				message = facebookLogin(username, password);
				 if (message.equalsIgnoreCase("Success")) {
						message = checkifuserloggedin();
					    findAndClick("CloseMenu");
					}
				 System.out.println(message);
				
			} else {
				message = StandardLogin(username, password);
				if (message == "Success") {
						message = checkifuserloggedin();
						findAndClick("CloseMenu");
				}
				System.out.println(message);
			}
	
		return message;
	}

	public void addRescomments(String comment) {

		findAndWrite("resCommentEntra", comment);
		findAndClick("publishRespuestasComment");

	}

	public String twitterLogin(String username, String password) throws Exception {
		String message = "Success";
		String winHandleBefore = driver.getWindowHandle();
		findAndClick("twitter_registor_button");
		implicitWait();

		if(driver.getWindowHandles().size()>1){
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}

		implicitWait();
		findAndWrite("twitter_email", username);
		findAndWrite("twitter_password", password);
		findAndClick("twitter_button");
		implicitWait();
		try {
		if(driver.getCurrentUrl().contains("https://twitter.com/login/error")){	
			message="Invalid login credentials";
			System.out.println("Invalid login credentials");}
		} 
		catch (Exception e) {
			
		}
		driver.switchTo().window(winHandleBefore);
		
		try {
			
			if(new WebDriverWait(driver, 10).until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(prop.getProperty("deactivated_user")))) != null) {
				message = new WebDriverWait(driver, 10)
				.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath(prop.getProperty("deactivated_user"))))
				.getText();
				message = "Deactivated User :  " + message;
			}
		} catch (Exception e) {
		
		}
		
		}
		return message;
	}

	public String facebookLogin(String username, String password) throws Exception {
		String message = "Success";
		findAndClick("facebook_registor_button");
		implicitWait();
		findAndWrite("facebook_email", username);
		findAndWrite("facebook_password", password);
		implicitWait();
		findAndClick("facebook_button");
		implicitWait();
		String currenturl= driver.getCurrentUrl();

	if(currenturl.contains("www.facebook.com/")){	
			try {
					
				if(new WebDriverWait(driver, 10).until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("*//div[@class='uiContextualLayer uiContextualLayerRight']"))) != null) {
					message = new WebDriverWait(driver, 10)
					.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath("*//div[@class='uiContextualLayer uiContextualLayerRight']")))
					.getText();
					message = "Invalid User :  " + message;
				}
				
			} catch (Exception e) {
			
			}
	}
	else{
			implicitWait();
			try {
				
				if(new WebDriverWait(driver, 10).until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath(prop.getProperty("deactivated_user")))) != null) {
					message = new WebDriverWait(driver, 10)
					.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath(prop.getProperty("deactivated_user"))))
					.getText();
					message = "Deactivated User :  " + message;
				}
			} catch (Exception e) {
			
			}
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
	public Object[][] readExcelPost(String excelsheetname, int columns)
			throws IOException {
		String filepath = System.getProperty("user.dir") + "\\src\\Common\\";
		String filename = "PostListing.xlsx";
		FileInputStream instream = new FileInputStream(filepath + "\\"
				+ filename);
		System.out.println(filepath + "\\" + filename);
		Workbook wb = new XSSFWorkbook(instream);
		Sheet sheet = wb.getSheet(excelsheetname);
		int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();
		int cnt = 0;
		System.out.println(rows + "===" + columns);

		Object[][] postdata = new Object[rows][columns];
		for (int i = 1; i <= rows; i++) {
			Row row = sheet.getRow(i);
			for (int j = 0; j < row.getLastCellNum(); j++) {
				sheet.getRow(i)
						.getCell(j)
						.setCellType(
								sheet.getRow(i).getCell(j).CELL_TYPE_STRING);
				if (sheet.getRow(i).getCell(j).getStringCellValue() != "") {
					postdata[cnt][j] = sheet.getRow(i).getCell(j)
							.getStringCellValue();
				}
			}
			cnt++;
		}

		return postdata;
	}
	
	public void deactivation(String loginType, String username,  String password) throws Exception
	   {
		implicitWait();
		findAndClick("Menu");
		findAndClick("userDeactivate_Button");
		implicitWait();
		findAndClick("userDeactivate_acceptButton");
		implicitWait();
		findAndClick("userDeactivate_acceptButton");
		implicitWait();
		if (loginType.equalsIgnoreCase("standard"))
			StandardLogin(username, password);
		else if (loginType.equalsIgnoreCase("twitter"))
			findAndClick("twitter_registor_button");
		else {
			findAndClick("facebook_registor_button");
			implicitWait();
			findAndWrite("facebook_password", password);
			implicitWait();
			findAndClick("facebook_button");
		}
		findAndClick("userDeactivate_acceptButton");
		Thread.sleep(2000);
		findAndClick("userDeactivate_cross");
		System.out.println("<<--------------Try to re-login with same credential------------->>");
		implicitWait();
		if (findElement(prop.getProperty("Entra")).getText().equalsIgnoreCase("Entra") == true)
			findAndClick("Entra");

		if (loginType.equalsIgnoreCase("Standard"))
			StandardLogin(username, password);
		else if (loginType.equalsIgnoreCase("twitter"))
			findAndClick("twitter_registor_button");
		else if (loginType.equalsIgnoreCase("facebook")) {
			findAndClick("Menu");
			findAndClick("Entra");
			implicitWait();
			facebookLogin(username, password);
		}
		implicitWait();
		if (driver.findElement(By.xpath(".//*[@id = 'wsl_invalid_user_deactivated']")).isDisplayed() == true)
			System.out.println("************You are successfully deactivated**************");
}
	
	public void DeactivateUserFromNewsletter(String loginType, String username, String password, String Linktext, String Rtype) throws Exception {
		clickMenu(Linktext, Rtype);
		if (loginType.equalsIgnoreCase("standard"))
			StandardLogin(username, password);
		else if (loginType.equalsIgnoreCase("twitter"))
			twitterLogin(username, password);
		else {
			facebookLogin(username, password);
		}
		driver.findElement(By.xpath(prop.getProperty("Menu"))).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath(prop.getProperty("Configuration"))).click();
		Thread.sleep(2000);
		if (driver.findElement(By.xpath(prop.getProperty("NewsletterCheckbox"))).isSelected()) {
			driver.findElement(By.xpath(prop.getProperty("NewsletterCheckbox"))).click();
			driver.findElement(By.xpath(prop.getProperty("Guardar_btn"))).click();
			System.out.println("**********User is successfully unsubscribed from newsletter subscription**********");
			implicitWait();
		} else if (!driver.findElement(By.xpath(prop.getProperty("NewsletterCheckbox"))).isSelected()) {
			System.out.println("*****User is already unsubscribed*****");
		}
		implicitWait();

	}

}
