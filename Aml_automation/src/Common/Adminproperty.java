package Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.TestListenerAdapter;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.screentaker.ViewportPastingStrategy;

public class Adminproperty extends TestListenerAdapter {

	WebDriver driver;
	Properties prop = new Properties();

public Properties ReadProperties() throws IOException {
        
        FileInputStream inStream = new FileInputStream(System.getProperty("user.dir") +"/src/Common/admin.properties");    
        System.out.print(System.getProperty("user.dir") + "\\src\\Common\\admin.properties");
prop.load(inStream);
return prop;
}



public WebDriver callproperty(String url, String browser) throws IOException {

/*LoggingPreferences loggingprefs = new LoggingPreferences();
loggingprefs.enable(LogType.BROWSER, Level.ALL);*/
if (browser.trim().equalsIgnoreCase("Chrome")) {
    System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + "//src//Driverfiles//" + "chromedriver.exe");
    DesiredCapabilities capabilities = DesiredCapabilities.chrome();
    /*capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);*/

    ChromeOptions options = new ChromeOptions();
    options.addArguments("start-maximized");
    //options.addArguments("" + capabilities + "");
    driver = new ChromeDriver(options);

} else {
    System.setProperty("webdriver.gecko.driver",
            System.getProperty("user.dir") + "//src//Driverfiles//" + "geckodriver.exe");
    DesiredCapabilities capabilities = DesiredCapabilities.firefox();
    /*capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);*/
    driver = new FirefoxDriver(capabilities);
}
driver.get(url);
driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
return driver;
}


	public void uploadPrimaryImage(String primaryimage, String browser) throws Exception {
		String primaryimagearr[] = primaryimage.split("@#@");
		for (int i = 0; i < primaryimagearr.length; i++) {
			if ((primaryimagearr[i].contains(".gif")) || (primaryimagearr[i].contains(".GIF"))) {

				findAndClick("toolbar_more");
				implicitWait();
			}
			findAndClick("toolbar_image");
			if (browser.trim().equalsIgnoreCase("firefox")) {
				findAndClick("post_content");
			}
			addNewlines();
			implicitWait();
			System.out.println(primaryimagearr[i]);
			findAndWrite("primary_image_insert",
					System.getProperty("user.dir") + prop.getProperty("image_path") + "\\" + primaryimagearr[i]);
			findAndClick("primary_image_upload");
		}
		WebElement element1 = findElement(
				prop.getProperty("product_image_bulkupload") + prop.getProperty("product_image_bulkupload1"));

		if (element1.getAttribute("href") != null) {
			isLinkBroken(new URL(element1.getAttribute("href")));
			System.out.println(isLinkBroken(new URL(element1.getAttribute("href"))));
		}
		implicitWait();
		findAndClick("primary_noraml_insert");
		implicitWait();
	}

	public void uploadMultipleImage(String primaryimage, String browser) throws Exception {
		findAndClick("toolbar_image");
		if (browser.trim().equalsIgnoreCase("firefox")) {
			findAndClick("post_content");
		}
		addNewlines();
		implicitWait();
		String primaryimagearr[] = primaryimage.split("@#@");
		for (int i = 0; i < primaryimagearr.length; i++) {
			findAndWrite("primary_image_insert",
					System.getProperty("user.dir") + prop.getProperty("image_path") + "\\" + primaryimagearr[i]);
			findAndClick("primary_image_upload");
		}
		WebElement element1 = findElement(
				prop.getProperty("product_image_bulkupload") + prop.getProperty("product_image_bulkupload1"));

		if (element1.getAttribute("href") != null) {
			isLinkBroken(new URL(element1.getAttribute("href")));
			System.out.println(isLinkBroken(new URL(element1.getAttribute("href"))));
		}
		implicitWait();
		findAndClick("primary_noraml_insert");
		implicitWait();
	}

	public void findAndClick(String element) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		WebElement element1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty(element))));
		implicitWait();
		findElement(prop.getProperty(element)).click();
	}

	public void findAndWrite(String element, String content) {
		findElement(prop.getProperty(element)).sendKeys(content);
	}

	public void findAndSendkey(String element, Keys end) {
		findElement(prop.getProperty(element)).sendKeys(end);
	}

	public void adminLogin() throws Exception {
		String username = prop.getProperty("admin_usename");
		String pwd = prop.getProperty("admin_pwd");
		findElement(prop.getProperty("login_username_txt")).sendKeys(username);
		findElement(prop.getProperty("login_pwd_txt")).sendKeys(pwd);
		findElement(prop.getProperty("login_submit_button")).click();
	}

	public WebElement findElement(String xpath) {
		WebElement element = driver.findElement(By.xpath(xpath));
		return element;
	}

	public List<WebElement> findElementByClass(String className) {
		List<WebElement> element = driver.findElements(By.className(className));
		return element;
	}

	public List<WebElement> findElementsByXpath(String xpath) {
		List<WebElement> element = driver.findElements(By.xpath(xpath));
		return element;
	}

	public void implicitWait() {
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.SECONDS);
	}

	public static String isLinkBroken(URL url) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			connection.connect();
			String response = connection.getResponseMessage();
			connection.disconnect();
			return response;
		} catch (Exception exp) {
			return exp.getMessage();
		}
	}

	public void verifyImageActive(WebElement imgElement) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(imgElement.getAttribute("src"));
			HttpResponse response = client.execute(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addFbTwitterText(String fbtext, String twitter_text,String futurepost) {
		implicitWait();
		if (!(fbtext).equalsIgnoreCase("null")) {
			findElement(prop.getProperty("fb_text")).sendKeys(fbtext);
		}

		if (!(twitter_text).equalsIgnoreCase("null")) {
			findElement(prop.getProperty("twitter_text")).clear();
			findElement(prop.getProperty("twitter_text")).sendKeys(twitter_text);
		}
		((JavascriptExecutor) driver).executeScript("scroll(0, -800);");
		
		if(!futurepost.equalsIgnoreCase("null")) {
			findElement(prop.getProperty("future_post_button")).click();
		
		}else {
			findElement(prop.getProperty("publish_post")).click();
		}
		
		implicitWait();
		System.out.println("Published post");
	}

	public void insertTagAndCategory(String postcatagory, String tag) {
		if (postcatagory != "") {
			findAndClick("Catagory_click");
			findAndWrite("catagory", postcatagory);
			List<WebElement> optionlist = findElementByClass(prop.getProperty("catagory_ecommerce_by_ClassName"));
			for (WebElement options : optionlist) {
				if (options.getText().equalsIgnoreCase(postcatagory)) {
					options.click();
					break;
				}
			}
		}
		if (tag != "") {
			findAndWrite("tag_input", tag);
			List<WebElement> Tagoptionlist = findElementByClass(prop.getProperty("tag_list_Byclassname"));
			for (WebElement options : Tagoptionlist) {
				if (options.getText().equalsIgnoreCase(tag)) {
					options.click();
					break;
				}
			}
		}

	}

	public void Imagestatus(WebElement element) throws Exception {
		if (element.getAttribute("href") != null) {
			isLinkBroken(new URL(element.getAttribute("href")));
			System.out.println(isLinkBroken(new URL(element.getAttribute("href"))));
		}
	}

	public void Conditionalwait(String xpath) {
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty(xpath))));
	}

	public Boolean clickButton(String row, String Column, String Postname, String listtype) {
		int cnt = 1;
		Boolean status = false;
		List<WebElement> postlist = null;
		if (listtype == "Dashboardlist") {
			postlist = findElementsByXpath(prop.getProperty("Dashboardlist"));
		} else if (listtype == "Draft") {
			postlist = findElementsByXpath(prop.getProperty("Draftlist_dashboard"));
		}

		for (WebElement list : postlist) {
			System.out.println(list.getText());
			if (list.getText().equalsIgnoreCase(Postname)) {
				if (listtype == "Dashboardlist") {
					findElement(prop.getProperty(row) + "[" + cnt + "]" + prop.getProperty(Column)).click();
				} else if (listtype == "Draft") {

					Actions action = new Actions(driver);
					action.moveToElement(findElement(prop.getProperty(row) + "[" + cnt + "]" + "/a")).perform();

					findElement(prop.getProperty(row) + "[" + cnt + "]" + prop.getProperty(Column)).click();
				}

				status = true;
				break;
			}

			cnt++;
		}

		return status;
	}

	public String getcatagoryname(String row, String Column, String Postname) {
		String catgoryname = "";
		int cnt = 1;
		Boolean status = false;
		List<WebElement> postlist = findElementsByXpath(prop.getProperty("Dashboardlist"));
		for (WebElement list : postlist) {
			System.out.println(list.getText());
			if (list.getText().equalsIgnoreCase(Postname)) {
				catgoryname = findElement(prop.getProperty(row) + "[" + cnt + "]" + prop.getProperty(Column)).getText();
				break;
			}

			cnt++;
		}

		return catgoryname;
	}

	public void repostCheckbox(String selectors) {
		String Selector[] = selectors.split(",");
		for (int i = 0; i < Selector.length; i++) {
			WebElement CheckBox1 = driver.findElement(By.cssSelector("input[value='" + Selector[i] + "']"));
			CheckBox1.click();
		}

	}

	public void summaryActuallization(String summary_data, String actuallization_data, String summary_layout)
			throws Exception {
		if (!summary_data.equalsIgnoreCase("null")) {

			findElement(prop.getProperty("toolbar_summary")).click();
			findElement(prop.getProperty("summary_input_field")).sendKeys(summary_data);

			Thread.sleep(3000);
			implicitWait();
			switch (summary_layout) {
			case "left":
				implicitWait();
				findElement(prop.getProperty("summary_insert_left")).click();
				break;
			case "right":
				implicitWait();
				findElement(prop.getProperty("summary_insert_right")).click();
				break;
			case "center":
				implicitWait();
				findElement(prop.getProperty("summary_insert_center")).click();
				break;
			}
			implicitWait();
			addNewlines();
			addNewlines();
		}

		if (!actuallization_data.equalsIgnoreCase("null")) {
			implicitWait();
			addNewlines();
			findElement(prop.getProperty("toolbar_Advance")).click();
			findElement(prop.getProperty("toolbar_actuallization")).click();

			findElement(prop.getProperty("actuallization_input_field")).sendKeys(actuallization_data);

			implicitWait();
			findElement(prop.getProperty("actuallization_insert_button")).click();

			implicitWait();

			findAndSendkey("post_content", Keys.END);
			findAndSendkey("post_content", Keys.ENTER);
			findAndClick("post_content");
			implicitWait();
			addNewlines();
		}
	}

	public void repost_By_Difundir(String Selector1, String Selector2, String tittle_data, String navigate_blog)
			throws Exception {
		findElement(prop.getProperty("difundir_Link")).click();
		findElement(prop.getProperty("repost_list_button")).click();
		implicitWait();
		repostCheckbox(Selector1);
		implicitWait();
		findElement(prop.getProperty("repost_post_button")).click();
		Thread.sleep(3000);
		driver.navigate().to(navigate_blog);
		adminLogin();
		implicitWait();
		Thread.sleep(5000);
		driver.navigate().refresh();
		driver.navigate().refresh();
		driver.navigate().refresh();
		implicitWait();
		findElement(prop.getProperty("notification_button")).click();
		implicitWait();
		clickNotificationButton("tittle_data");
	}

	public void clickNotificationButton(String tittle_data) {
		int cnt = 1;
		List<WebElement> postlist = findElementByClass(prop.getProperty("notification_list_by_ClassName"));
		for (WebElement list : postlist) {
			String sender = driver.findElement(By.className(prop.getProperty("notify_sender"))).getText();

			String text = list.getText().replace(sender, "");
			if (text.trim().equalsIgnoreCase(tittle_data)) {
				System.out.println(cnt + "hi");
				Actions act = new Actions(driver);
				act.doubleClick(driver.findElement(By.className(prop.getProperty("notify_sender")))).build().perform();
				driver.findElement(By.className("actions-approve")).click();
				break;
			}
			cnt++;
		}
	}

	public void HomePageContent() {
		findAndWrite("Homepagecontent", "Homepagetext");
	}

	public void videoHandle(String videoURL, String layout, String browser) throws InterruptedException {
		WebElement element;
		findAndClick("post_content");
		findAndSendkey("post_content", Keys.ENTER);
		findAndClick("toolbar_video");
		if (browser.trim().equalsIgnoreCase("firefox")) {
			findAndClick("post_content");
			findAndSendkey("post_content", Keys.ENTER);
		}
		findAndClick("Video_URL");
		implicitWait();
		findAndWrite("Video_URL", videoURL);
		implicitWait();
		if (layout.equalsIgnoreCase("normal")) {
			findElement(prop.getProperty("Video_NormalLayout")).click();
		} else {
			findElement(prop.getProperty("Video_Biglayout")).click();
		}
		implicitWait();
		if (videoURL.contains("youtube")) {
			findAndClick("Youtube_button");
		} else if (videoURL.contains("vimeo")) {
			findAndClick("Vimeo_button");
		} else if (videoURL.contains("facebook")) {
			findAndClick("Facebook_button");
		} else {
			findAndClick("Vine_button");
		}
		findAndClick("post_content");
		implicitWait();
		addNewlines();
	}

	public void insertBrandedClub(String BrandedClubName, String tag) {
		findAndClick("BrandedClub_Click");
		findAndWrite("BrandedClub_InputBox", BrandedClubName);
		List<WebElement> optionlist = findElementByClass(prop.getProperty("BrandedClub_List_by_ClassName"));
		for (WebElement options : optionlist) {
			if (options.getText().equalsIgnoreCase(BrandedClubName)) {
				options.click();
				break;
			}
		}
		findAndWrite("tag_input", tag);
		List<WebElement> Tagoptionlist = findElementByClass(prop.getProperty("tag_list_Byclassname"));
		for (WebElement options : Tagoptionlist) {
			if (options.getText().equalsIgnoreCase(tag)) {
				options.click();
				break;
			}
		}
	}

	public void addslides(String slides, String browser) throws IOException, Exception {
		String slidesarr[] = slides.split("~");
		findAndClick("toolbar_slideshow");
		implicitWait();

		for (int i = 0; i < slidesarr.length; i++) {
			String slidedetails[] = slidesarr[i].split("@##@");
			System.out.println(System.getProperty("user.dir") + "\\src\\DriverFiles\\fileupload.exe" + " "
					+ System.getProperty("user.dir") + "\\src\\Images\\" + slidedetails[0]);
			Conditionalwait("slide_button");
			implicitWait();
			if (slidedetails[0].contains("youtube")) {
				findAndClick("slide_video_button");
				findAndWrite("slide_video_url", slidedetails[0]);
			} else {
				findAndClick("slideshow_addimage");
				findAndClick("slideshow_input_image");
				implicitWait();

				if (browser.trim().equalsIgnoreCase("Chrome")) {
					Runtime.getRuntime().exec(System.getProperty("user.dir") + "\\src\\DriverFiles\\fileupload.exe"
							+ " " + System.getProperty("user.dir") + "\\src\\Images\\" + slidedetails[0]);
				} else {
					Runtime.getRuntime()
							.exec(System.getProperty("user.dir") + "\\src\\DriverFiles\\firefoxfileupload.exe" + " "
									+ System.getProperty("user.dir") + "\\src\\Images\\" + slidedetails[0]);
				}

			}

			implicitWait();
			findAndWrite("Slideshow_subtitle", slidedetails[1]);
			findAndWrite("Slideshow_desc", slidedetails[2]);
			findAndClick("slide_button");
			Thread.sleep(4000);
			findAndSendkey("post_content", Keys.ENTER);
			Thread.sleep(4000);

		}
		findAndClick("slide_button_close");
		implicitWait();

	}

	public void addNewline() {
		findAndClick("post_title");
		findAndClick("post_content");
		findAndSendkey("post_content", Keys.END);
		findAndSendkey("post_content", Keys.ENTER);
		findAndSendkey("post_content", Keys.END);
		findAndSendkey("post_content", Keys.ENTER);

	}

	public void addNewlines() {

		findAndSendkey("post_content", Keys.END);
		findAndSendkey("post_content", Keys.ENTER);
		findAndSendkey("post_content", Keys.END);
		findAndSendkey("post_content", Keys.ENTER);

	}

	public void moveToPublishTab(String browser) {
		/*
		 * if (browser.trim().equalsIgnoreCase("Chrome")) { Actions action = new
		 * Actions(driver); action.sendKeys(Keys.PAGE_DOWN); implicitWait();
		 * action.click(driver.findElement(By.partialLinkText("Publicar")))
		 * .perform(); implicitWait(); findAndClick("publish_tab");
		 * implicitWait(); } else {
		 */
		implicitWait();
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)");
		findAndClick("post_title");
		implicitWait();
		findAndClick("publish_tab");
		/* } */

		/*WebDriverWait wait = new WebDriverWait(driver, 10);
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.authenticateUsing(new UserAndPassword("guest", "guest"));*/

	}

	public void fichaDeReview(String fichareview) {
		implicitWait();
		findAndClick("toolbar_fichadereview");

		String fichareviewarr[] = fichareview.split("~##~");
		for (int i = 0; i < fichareviewarr.length; i++) {
			String fichareviewdetails[] = fichareviewarr[i].split("@##@");
			if (!(fichareviewdetails[0].equalsIgnoreCase("null"))) {
				findAndWrite("fichareview_name", fichareviewdetails[0]);
			} else {
				findAndClick("fichareview_checkbox");
			}

			findAndWrite("fichareview_best", fichareviewdetails[1]);
			findAndWrite("fichareview_worst", fichareviewdetails[2]);

			String fichreviewdatasheet[] = fichareviewdetails[3].split("@~#~@");

			for (int j = 0; j < fichreviewdatasheet.length; j++) {
				String fichreviewdatasheetdetails[] = fichreviewdatasheet[j].split("@###@");
				for (int z = 0; z < fichreviewdatasheetdetails.length; z++) {
					findElement(prop.getProperty("fichareview_datasheet_row") + "[" + (j + 1) + "]" + "/td[" + (z + 1)
							+ "]/input").sendKeys(fichreviewdatasheetdetails[z]);
				}
			}

			findAndWrite("fichareview_summary", fichareviewdetails[4]);
			findAndClick("fichreview_insert_button");
		}
		implicitWait();
		addNewlines();

	}

	public void wait(String path) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(findElement(path)));
	}

	public Object[][] readExcel(String excelsheetname, int columns) throws IOException {
		String filepath = System.getProperty("user.dir") + "\\src\\Common\\";
		String filename = "excel.xlsx";
		FileInputStream instream = new FileInputStream(filepath + "\\" + filename);
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
				sheet.getRow(i).getCell(j).setCellType(sheet.getRow(i).getCell(j).CELL_TYPE_STRING);
				if (sheet.getRow(i).getCell(j).getStringCellValue() != "") {
					postdata[cnt][j] = sheet.getRow(i).getCell(j).getStringCellValue();
				}
			}
			cnt++;
		}

		return postdata;
	}

	public void republish() {
		Conditionalwait("difundir_Link");
		findAndClick("difundir_Link");
		implicitWait();
		findAndClick("republish_diffunder");
		implicitWait();
		findAndClick("republish_click");
	}

	public void dialogBoxOk() {
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}

	public void fichaTechnica(String fichatechnica) {
		findAndClick("toolbar_fichatechnica");
		String[] arrfichatechnica = fichatechnica.split("@##@");
		findAndWrite("Ficha_name", arrfichatechnica[0]);
		findAndWrite("Ficha_details", arrfichatechnica[1]);
		findAndWrite("Ficha_mainImage", arrfichatechnica[2]);
		findAndWrite("Ficha_optionalImage", arrfichatechnica[3]);
		String[] arrfichapricebar = arrfichatechnica[4].split("~##~");
		findAndWrite("Ficha_price", arrfichapricebar[0]);
		findAndWrite("Ficha_text", arrfichapricebar[1]);
		findAndWrite("Ficha_URL", arrfichapricebar[2]);

		String[] arrDataSheet = arrfichatechnica[5].split("@~@");

		if (arrDataSheet.length > 3) {
			findAndWrite("Ficha_add_newrows", "" + arrDataSheet.length + "");
			findAndClick("Ficha_add_newrows_button");
		}

		for (int y = 0; y < arrDataSheet.length; y++) {
			String[] arritems = arrDataSheet[y].split("~##~");
			for (int k = 0; k < arritems.length; k++) {
				System.out.println(prop.getProperty("List_Row") + "[" + (y + 1) + "]" + "/td[" + (k + 1) + "]/input");
				System.out.println(arritems[k]);

				if (arritems[k].equalsIgnoreCase("null")) {
					findElement(prop.getProperty("List_Row") + "[" + (y + 1) + "]" + "/td[" + (k + 1) + "]/input")
							.sendKeys("");
				} else {
					findElement(prop.getProperty("List_Row") + "[" + (y + 1) + "]" + "/td[" + (k + 1) + "]/input")
							.sendKeys(arritems[k]);
				}

			}
		}
		findAndWrite("Ficha_otherDetails", arrfichatechnica[6]);
		findAndClick("Ficha_insertButton");
		implicitWait();
		findAndClick("post_content");
		addNewlines();
	}

	public void specialPost(String status) {
		if (status.equalsIgnoreCase("Y")) {
			findAndClick("specialCheckbox");
		}
	}

	public void closeComments(String status) {
		if (status.equalsIgnoreCase("Y")) {
			findAndClick("commentsCheckbox");
		}
	}

	public void Author(String authorName) {
		if (!authorName.equalsIgnoreCase("null")) {
			implicitWait();
			implicitWait();
			findAndClick("authorBox_click");
			implicitWait();
			findAndWrite("author", authorName);
			implicitWait();
			List<WebElement> optionlist = findElementByClass(prop.getProperty("Author_by_ClassName"));
			implicitWait();
			for (WebElement options : optionlist) {
				implicitWait();
				if (options.getText().equalsIgnoreCase(authorName)) {
					System.out.println(options.getText());
					implicitWait();
					options.click();
					implicitWait();
					break;
				}
			}
		}

	}

	public void infograph(String infographURL, String infographLayout, String infographCaption, String browser) {
		System.out.println(infographURL + " " + infographLayout + " " + infographCaption);
		List<WebElement> items = findElementsByXpath(prop.getProperty("header"));
		for (WebElement item : items) {
			if (item.getText().equalsIgnoreCase("Graphs")) {
				item.click();
				break;
			}
		}
		findAndWrite("infographURLPath", infographURL);
		implicitWait();

		if (infographLayout.equalsIgnoreCase("small")) {
			implicitWait();
			WebElement element = findElement(prop.getProperty("graph_SmallLayout"));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);
			implicitWait();
		} else if (infographLayout.equalsIgnoreCase("normal")) {
			implicitWait();
			WebElement element = findElement(prop.getProperty("graph_Normallayout"));
			JavascriptExecutor executor = (JavascriptExecutor) driver;

			executor.executeScript("arguments[0].click();", element);
			implicitWait();
		} else {
			implicitWait();
			WebElement element = findElement(prop.getProperty("graph_Largelayout"));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);
			implicitWait();
		}
		implicitWait();
		findAndWrite("infographCaptionPath", infographCaption);
		implicitWait();
		findAndClick("Add_infograph");
		implicitWait();
		findAndClick("post_content");
	}

	public void addTable(String tabledata, String Checkbox_same_width, String Checkbox_table_first_row_heading,
			String Checkbox_table_first_column_heading, String Checkbox_table_occupy_all_avaiable_width) {
		String rows = "2", columns = "2";
		implicitWait();
		findAndClick("toolbar_table");
		System.out.println("rows: " + rows);
		String tablerow[] = tabledata.split("@##@");
		rows = String.valueOf(tablerow.length);
		for (int k = 0; k < tablerow.length; k++) {
			String tablecolumn1[] = tablerow[k].split("~##~");
			columns = String.valueOf(tablecolumn1.length);
			System.out.println("columns: " + columns);
		}
		implicitWait();
		implicitWait();
		implicitWait();
		findElement(prop.getProperty("table_row_filter")).click();
		implicitWait();
		findElement(prop.getProperty("table_row_filter")).clear();
		implicitWait();
		findAndWrite("table_row_filter", rows);
		findElement(prop.getProperty("table_column_filter")).click();
		findElement(prop.getProperty("table_column_filter")).clear();
		implicitWait();
		findAndWrite("table_column_filter", columns);
		findAndClick("table_generate_table_button");
		implicitWait();
		for (int i = 0; i < tablerow.length; i++) {
			String tablecolumn[] = tablerow[i].split("~##~");
			System.out.println(tablerow[i]);
			for (int j = 1; j <= tablecolumn.length; j++) {
				if (tablecolumn[(j - 1)].equalsIgnoreCase("null")) {
					findElement(prop.getProperty("table_tr") + "[" + (i + 1) + "]/td[" + (j + 1) + "]"
							+ prop.getProperty("table_td")).sendKeys("");
				} else {
					findElement(prop.getProperty("table_tr") + "[" + (i + 1) + "]/td[" + (j + 1) + "]"
							+ prop.getProperty("table_td")).sendKeys(tablecolumn[(j - 1)]);
				}
			}
		}
		implicitWait();
		if (Checkbox_same_width.equalsIgnoreCase("Y")) {
			findAndClick("table_checkbox_same_width");
		}
		if (Checkbox_table_first_row_heading.equalsIgnoreCase("Y")) {
			findAndClick("table_first_row_heading");
		}
		if (Checkbox_table_first_column_heading.equalsIgnoreCase("Y")) {
			findAndClick("table_first_column_heading");
		}
		if (Checkbox_table_occupy_all_avaiable_width.equalsIgnoreCase("Y")) {
			findAndClick("table_occupy_all_avaiable_width");
		}
		implicitWait();
		findAndClick("table_insert_button");
		findAndClick("post_content");
	}

	public void RecipeSummary(String summary_data, String summary_layout) throws Exception {
		if (!summary_data.equalsIgnoreCase("null")) {

			findElement(prop.getProperty("Recipe_summary")).click();
			findElement(prop.getProperty("summary_input_field")).sendKeys(summary_data);
			Thread.sleep(3000);
			implicitWait();
			switch (summary_layout) {
			case "left":
				implicitWait();
				findElement(prop.getProperty("summary_insert_left")).click();
				break;
			case "right":
				implicitWait();
				findElement(prop.getProperty("summary_insert_right")).click();
				break;
			case "center":
				implicitWait();
				findElement(prop.getProperty("summary_insert_center")).click();
				break;
			}
			implicitWait();
			findAndSendkey("Recipe_Post_content", Keys.END);
			findAndSendkey("Recipe_Post_content", Keys.ENTER);
			findAndSendkey("Recipe_Post_content", Keys.END);
			findAndSendkey("Recipe_Post_content", Keys.ENTER);
		}

	}

	public void RecipeuploadImage(String primaryimage, String browser) throws Exception {
		String primaryimagearr[] = primaryimage.split("@#@");
		for (int i = 0; i < primaryimagearr.length; i++) {
			if (browser.trim().equalsIgnoreCase("firefox")) {
				findAndClick("Recipe_Post_content");
			}
			findAndSendkey("Recipe_Post_content", Keys.END);
			findAndSendkey("Recipe_Post_content", Keys.ENTER);
			findAndSendkey("Recipe_Post_content", Keys.END);
			findAndSendkey("Recipe_Post_content", Keys.ENTER);
			implicitWait();
			System.out.println(primaryimagearr[i]);
			System.out.println(
					System.getProperty("user.dir") + prop.getProperty("image_path") + "\\" + primaryimagearr[i]);
			findAndWrite("primary_image_insert",
					System.getProperty("user.dir") + prop.getProperty("image_path") + "\\" + primaryimagearr[i]);
			findAndClick("primary_image_upload");
		}
		WebElement element1 = findElement(
				prop.getProperty("product_image_bulkupload") + prop.getProperty("product_image_bulkupload1"));

		if (element1.getAttribute("href") != null) {
			isLinkBroken(new URL(element1.getAttribute("href")));
			System.out.println(isLinkBroken(new URL(element1.getAttribute("href"))));
		}
		implicitWait();
		findAndClick("Recipe_normal_insert");
		implicitWait();
	}

	public void RecipeAddVideo(String videoURL, String layout, String browser) throws InterruptedException {
		WebElement element;
		findAndClick("Recipe_Post_content");
		findAndSendkey("Recipe_Post_content", Keys.ENTER);
		findAndClick("Recipe_video");
		if (browser.trim().equalsIgnoreCase("firefox")) {
			findAndClick("Recipe_Post_content");
			findAndSendkey("Recipe_Post_content", Keys.ENTER);
		}
		findAndClick("Video_URL");
		implicitWait();
		findAndWrite("Video_URL", videoURL);
		implicitWait();
		if (layout.equalsIgnoreCase("normal")) {
			findElement(prop.getProperty("Video_NormalLayout")).click();
		} else {
			findElement(prop.getProperty("Video_Biglayout")).click();
		}
		implicitWait();
		if (videoURL.contains("youtube")) {
			findAndClick("Youtube_button");
		} else if (videoURL.contains("vimeo")) {
			findAndClick("Vimeo_button");
		} else {
			findAndClick("Vine_button");
		}
		findAndClick("Recipe_Post_content");
		implicitWait();
		findAndSendkey("Recipe_Post_content", Keys.END);
		findAndSendkey("Recipe_Post_content", Keys.ENTER);
		findAndSendkey("Recipe_Post_content", Keys.END);
		findAndSendkey("Recipe_Post_content", Keys.ENTER);

	}

	public void recipemovecursorpostion(int stats) {
		if (stats == 1) {
			implicitWait();
			implicitWait();
			implicitWait();
		} else {
			implicitWait();
			findAndSendkey("Recipe_Post_content", Keys.END);
			findAndSendkey("Recipe_Post_content", Keys.ENTER);
			findAndSendkey("Recipe_Post_content", Keys.END);
			findAndSendkey("Recipe_Post_content", Keys.ENTER);
		}
	}

	public void uploadImageInsertInArticle(String primaryimage, String browser, String layout,
			String supertitle_caption_and_postion) throws Exception {
		String primaryimagearr[] = primaryimage.split(",");
		for (int i = 0; i < primaryimagearr.length; i++) {
			if ((primaryimagearr[i].contains(".gif")) || (primaryimagearr[i].contains(".GIF"))) {
				findAndClick("toolbar_more");
				implicitWait();
			}
			findAndClick("toolbar_image");
			if (browser.trim().equalsIgnoreCase("firefox")) {
				findAndClick("post_content");
			}
			addNewlines();
			implicitWait();
			findAndWrite("primary_image_insert",
					System.getProperty("user.dir") + prop.getProperty("image_path") + "\\" + primaryimagearr[i]);
			findAndClick("primary_image_upload");
		}
		WebElement element1 = findElement(
				prop.getProperty("product_image_bulkupload") + prop.getProperty("product_image_bulkupload1"));

		if (element1.getAttribute("href") != null) {
			isLinkBroken(new URL(element1.getAttribute("href")));
			System.out.println(isLinkBroken(new URL(element1.getAttribute("href"))));
		}
		findAndClick("insert_in_article");
		implicitWait();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
		Date date = new Date();
		String FinalDate = dateFormat.format(date);
		System.out.print(FinalDate);
		String TxtBoxContent = driver.findElement(By.xpath(prop.getProperty("text_img"))).getText();
		System.out.println(TxtBoxContent);
		String captionText[] = supertitle_caption_and_postion.split("@##@");
		switch (layout) {
		case "small":
			if (TxtBoxContent != null) {
				WebElement element = findElement(prop.getProperty("small"));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", element);
				implicitWait();
			} else {
				findAndWrite("text_img", FinalDate);
				implicitWait();
			}
			break;
		case "small_left":
			if (TxtBoxContent != null) {
				findAndWrite("text_img", FinalDate);
				implicitWait();
			}
			WebElement element2 = findElement(prop.getProperty("small_left"));
			JavascriptExecutor executor2 = (JavascriptExecutor) driver;
			executor2.executeScript("arguments[0].click();", element2);
			implicitWait();
			break;
		case "small_right":
			if (TxtBoxContent != null) {
				findAndWrite("text_img", FinalDate);
				implicitWait();
			}
			WebElement element3 = findElement(prop.getProperty("small_right"));
			JavascriptExecutor executor3 = (JavascriptExecutor) driver;
			executor3.executeScript("arguments[0].click();", element3);
			implicitWait();
			break;
		case "normal":
			if (TxtBoxContent != null) {
				implicitWait();
				findAndWrite("text_img", FinalDate);
				implicitWait();
			}
			WebElement element4 = findElement(prop.getProperty("normal"));
			JavascriptExecutor executor4 = (JavascriptExecutor) driver;
			executor4.executeScript("arguments[0].click();", element4);
			findAndWrite("add_caption_img", captionText[0]);
			implicitWait();
			break;
		case "big":
			implicitWait();
			if (TxtBoxContent != null) {
				findAndWrite("text_img", FinalDate);
				implicitWait();
			}
			WebElement element5 = findElement(prop.getProperty("big"));
			JavascriptExecutor executor5 = (JavascriptExecutor) driver;
			executor5.executeScript("arguments[0].click();", element5);
			findAndWrite("add_caption_img", captionText[0]);
			implicitWait();
			if (captionText[1].equalsIgnoreCase("Y")) {
				findAndClick("check_box_img");
				implicitWait();
			}

			Select dropdown = new Select(driver.findElement(By.xpath(prop.getProperty("position"))));
			dropdown.selectByVisibleText(captionText[2]);
			Thread.sleep(3000);
			Select dropdown1 = new Select(driver.findElement(By.xpath(prop.getProperty("color"))));
			dropdown1.selectByVisibleText(captionText[3]);
			implicitWait();
		}
		Thread.sleep(20000);
		findAndClick("add_img");
		implicitWait();
		findAndClick("post_content");
	}

	public void create_ingrediente(String ingredienteName, String checkboxes) throws InterruptedException {
		findAndClick("create_ingrediente_button");
		implicitWait();
		findAndClick("ingrediente_name");
		implicitWait();
		findAndWrite("ingrediente_name", ingredienteName);
		implicitWait();
		String allCheckboxes[] = checkboxes.split(",");
		for (int i = 0; i < allCheckboxes.length; i++) {
			String CheckBox1 = driver.findElement(By.xpath(prop.getProperty("ml"))).getText();
			System.out.println(CheckBox1);
			String replaceString = CheckBox1.replace("ml/l (mililitros)", "ml");
			System.out.println(replaceString);
			if (replaceString.equalsIgnoreCase("ml")) {
				driver.findElement(By.xpath(prop.getProperty(allCheckboxes[i]))).click();
			}
			String CheckBox2 = driver.findElement(By.xpath(prop.getProperty("kg"))).getText();
			System.out.println(CheckBox2);
			String replaceString2 = CheckBox2.replace("g/kg (gramos)", "kg");
			System.out.println(replaceString2);
			if (replaceString.equalsIgnoreCase("kg")) {
				driver.findElement(By.xpath(prop.getProperty(allCheckboxes[i]))).click();
			}
			String CheckBox3 = driver.findElement(By.xpath(prop.getProperty("unit"))).getText();
			System.out.println(CheckBox3);
			String replaceString3 = CheckBox3.replace("unidades", "unit");
			System.out.println(replaceString3);

			if (replaceString.equalsIgnoreCase("unit")) {
				driver.findElement(By.xpath(prop.getProperty(allCheckboxes[i]))).click();
			}

		}
		findAndClick("save_ingrediente");
	}

	public void galleryPost(String gallery_Name, String Desp, String tag, String showinheader, String galleryimage,
			String browser) throws MalformedURLException, Exception {

		String galleryimagearr[] = galleryimage.split("@##@");
		findAndWrite("gallery_name", gallery_Name);
		implicitWait();
		findAndWrite("gallery_description", Desp);

		findAndWrite("gallery_tag", tag);
		implicitWait();
		if (showinheader.equalsIgnoreCase("Y")) {
			findAndClick("gallery_check");
			implicitWait();
		}

		for (int i = 0; i < galleryimagearr.length; i++) {
			implicitWait();
			findAndWrite("gallery_upload_button",
					System.getProperty("user.dir") + prop.getProperty("image_path") + "\\" + galleryimagearr[i]);

		}
		findAndClick("gallery_upload_bulk");

		for (int i = 0; i < galleryimagearr.length; i++) {
			WebElement element1 = findElement(
					prop.getProperty("gallery_upload_row") + prop.getProperty("gallery_image_bulkupload1"));

			if (element1.getAttribute("href") != null) {
				isLinkBroken(new URL(element1.getAttribute("href")));
				System.out.println(isLinkBroken(new URL(element1.getAttribute("href"))));
			}
		}
		Thread.sleep(10000);
		findAndClick("attach_gallery_to_post");
		implicitWait();
		addNewlines();
		addNewlines();
		addNewlines();
	}

	public void futurePost(String dateTime, String fbtext) throws Exception

	{
		driver.findElement(By.xpath(prop.getProperty("future_publish_date"))).clear();
		implicitWait();
		findAndWrite("future_publish_date", dateTime);
		findAndWrite("fb_text", fbtext);
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollBy(0,-600)", "");
		findAndClick("future_post_button");
	}

	public void futurePostDashborad(String FuturePostDate) {
		driver.findElement(By.className(prop.getProperty("future_post_time"))).clear();
		driver.findElement(By.className(prop.getProperty("future_post_date"))).clear();
		String FuturePost[] = FuturePostDate.split(",");
		driver.findElement(By.className(prop.getProperty("future_post_time"))).sendKeys(FuturePost[0]);

		driver.findElement(By.className(prop.getProperty("future_post_date"))).sendKeys(FuturePost[1]);
		driver.findElement(By.className(prop.getProperty("future_post_save"))).click();
	}

	public void ExtractJSLogs(String URL) {
		int cnt = 0;

		LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
		for (LogEntry entry : logEntries) {
			if (entry.getMessage() != null) {
				if (cnt == 0) {
					System.out.println("Console Logs:  " + URL);
					cnt++;
				}

				System.out.println(entry.getLevel() + " " + entry.getMessage());
			}
		}
	}

	public void FullScreenshot(WebDriver driver, String screenshotName) throws IOException {
		Screenshot screenshot = new AShot().shootingStrategy(new ViewportPastingStrategy(1000)).takeScreenshot(driver);
		ImageIO.write(screenshot.getImage(), "PNG",
				new File(System.getProperty("user.dir") + "\\src\\Screenshots\\" + screenshotName));
	}

	public static void captureScreenshot(WebDriver driver, String screenshotName) {

		try {
			TakesScreenshot ts = (TakesScreenshot) driver;

			File source = ts.getScreenshotAs(OutputType.FILE);

			FileUtils.copyFile(source,
					new File(System.getProperty("user.dir") + "\\src\\Screenshots\\" + screenshotName + ".png"));

			System.out.println("Screenshot taken");
		} catch (Exception e) {

			System.out.println("Exception while taking screenshot " + e.getMessage());
		}
	}

	public void editEvent(String video_Data) {
		int cnt = 1;
		List<WebElement> list = findElementsByXpath(prop.getProperty("video_list") + "/td[1]");
		for (WebElement element : list) {
			if (element.getText().equalsIgnoreCase(video_Data)) {
				findElement(prop.getProperty("video_list") + "[" + cnt + "]" + prop.getProperty("eventActions")
						+ prop.getProperty("video_list")).click();
				break;
			}
			cnt++;
		}
		implicitWait();

		if (findElement(prop.getProperty("eventImage")).isDisplayed()) {

			findAndClick("eventImageDel");
			findAndWrite("editEventBrowse",
					System.getProperty("user.dir") + prop.getProperty("image_path") + "\\headphone.jpg");
			findAndClick("eventImageUpload");
			implicitWait();
		}

		else {
			findAndWrite("editEventBrowse",
					System.getProperty("user.dir") + prop.getProperty("image_path") + "\\headphone.jpg");
			findAndClick("eventImageUpload");
		}

	}

	public void DeleteVideo(String Video_post_name) {
		WebElement tableelement = driver.findElement(By.id("video-list"));
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		});
		int cnt = 1;
		List<WebElement> list = findElementsByXpath(".//*[@id='video-list']/table/tbody/tr/td[1]");
		for (WebElement element : list) {
			System.out.println(element.getText());

			if (element.getText().equalsIgnoreCase(Video_post_name)) {
				findElement(prop.getProperty("video_list") + "[" + cnt + "]" + prop.getProperty("del_td")).click();
				implicitWait();
				Alert alert = driver.switchTo().alert();
				alert.accept();
				break;
			}
			cnt++;

		}
	}

	public void EditVideoGallery(String Video_post_name, String checkbox) throws Exception {
		WebElement tableelement = driver.findElement(By.id("video-list"));
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		});

		int cnt = 1;
		List<WebElement> list = findElementsByXpath(".//*[@id='video-list']/table/tbody/tr/td[1]");
		for (WebElement element : list) {
			System.out.println(element.getText());

			if (element.getText().equalsIgnoreCase(Video_post_name)) {
				findElement(prop.getProperty("video_list") + "[" + cnt + "]" + prop.getProperty("edit")).click();
				implicitWait();
				Alert alert = driver.switchTo().alert();
				alert.accept();

				break;
			}
			cnt++;

		}

		implicitWait();
		System.out.println("hello");
		findElement(prop.getProperty("video_post_title")).clear();
		findElement(prop.getProperty("video_post_url")).clear();
		findElement(prop.getProperty("video_video_url")).clear();

		if (checkbox.equalsIgnoreCase("Y")) {
			findElement(prop.getProperty("video_is_special")).click();
		} else {

			findElement(prop.getProperty("video_is_sponsored")).clear();
			findElement(prop.getProperty("video_sponsor_name")).clear();
			findElement(prop.getProperty("video_sponsor_logo")).clear();

		}

	}

	public void videoGallery(String post_title, String post_url, String video_url, String checkbox, String sponsor_logo,
			String sponsor_name)

	{

		findAndClick("create_new_video");
		findAndWrite("video_post_title", post_title);
		findAndWrite("video_post_url", post_url);
		findAndWrite("video_video_url", video_url);

		if (checkbox.equalsIgnoreCase("Y")) {
			findAndClick("video_is_special");
		} else {

			findAndClick("video_is_sponsored");
			findAndWrite("video_sponsor_name", sponsor_name);
			findAndWrite("video_sponsor_logo", sponsor_logo);
		}
		findAndClick("guardar_button");

	}

	public void amazonProduct(String value, String amazon_product_id, String amazon_product_name) {

		// keywords,asin

		Select oSelect = new Select(driver.findElement(By.id(prop.getProperty("checkList"))));

		oSelect.selectByValue(value);

		if (value == "asin") {

			findAndWrite("amazon_search_field", amazon_product_id);
			findAndClick("search_product_button");

			driver.findElement(By.className(prop.getProperty("add_product_button"))).click();

		}

		else {

			findAndWrite("amazon_search_field", amazon_product_name);
			findAndClick("search_product_button");

			driver.findElement(By.className(prop.getProperty("add_product_button"))).click();

		}

	}

	public void users(String Username, String Fullname, String UserPassword, String UserEmail, String userDescription,
			String value) {
		findAndClick("Usuarios");
		findAndClick("create_Usuarios_button");
		findAndWrite("user_name", Username);
		findAndWrite("full_name", Fullname);
		Select oSelect = new Select(driver.findElement(By.id(prop.getProperty("roll"))));

		oSelect.selectByValue(value);

		findAndWrite("user_password", UserPassword);
		findAndWrite("user_email", UserEmail);
		findAndWrite("user_description", userDescription);
		findAndClick("user_submit_button");

	}

	public void createSectionModule(String title_1_data, String title_2_data, String type_1, String type_2,
			String value, String tag_input_data_1, String tag_input_data_2) {
		findAndClick("section_module");
		findAndWrite("title_1", title_1_data);

		if (type_1 == "cat_1") {

			findAndClick("cat_1");
			Select oSelect = new Select(driver.findElement(By.id(prop.getProperty("publish_main_category_1_chosen"))));
			oSelect.selectByValue(value);

		}

		else {
			findAndClick("tag_1");
			findAndWrite("tag_input-box", tag_input_data_1);

		}

		findAndWrite("title_2", title_2_data);

		if (type_2 == "cat_2") {
			findAndClick("cat_2");
			Select oSelect = new Select(driver.findElement(By.id(prop.getProperty("publish_main_category_2_chosen"))));
			oSelect.selectByValue(value);
		}

		else {
			findAndClick("tag_2");
			findAndWrite("tag_input-box_2", tag_input_data_2);
		}
		findAndClick("save_Section_Module");

	}

	public Connection connectDb() throws Exception {
		Connection conn;
		String databaseURL = "jdbc:mysql://localhost:3306/xataka";
		String user = "root";
		String password = "";
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Connecting to Database...");
		conn = DriverManager.getConnection(databaseURL, user, password);
		if (conn != null) {
			System.out.println("Connected to the Database...");
		}
		return conn;
	}

	public void LoginAdmin(String username, String pwd) throws Exception {

		findElement(prop.getProperty("login_username_txt")).sendKeys(username);
		findElement(prop.getProperty("login_pwd_txt")).sendKeys(pwd);
		findElement(prop.getProperty("login_submit_button")).click();
	}

	public String checkuserlogintype(Connection conn, String username, String pwd) throws SQLException {
		String blogrole = "", uname = "", usernicename = "";
		String query = "select  user_login, user_nicename,blog_role,roles from wp_users where user_login='" + username
				+ "' and user_pass=md5('" + pwd + "')";
		System.out.println(query);

		Statement stmt = conn.createStatement();
		stmt.executeQuery(query);
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			usernicename = rs.getString("user_nicename");
			uname = rs.getString("user_login");
			blogrole = rs.getString("blog_role");
			System.out.println(uname + "====" + blogrole);
		}
		conn.close();

		System.out.println(uname);
		return (uname + "@##@" + blogrole + "@##@" + usernicename);
	}

	public void pickDraft(String name, String type, String Button, String index) {
		Select dropdown = new Select(driver.findElement(By.id("postStatus")));
		dropdown.selectByValue(index);
		implicitWait();
		clickButton("DashboardEditbuttontr", Button, name, type);
		implicitWait();
	}

	public void AddInstagram(String Instagram_embed) {
		findAndClick("toolbar_instagram");
		findAndWrite("instagram_url", Instagram_embed);
		findAndClick("instagram_button");
		addNewlines();
		addNewlines();

	}

	public Object[][] readEditorialExcel(String excelsheetname, int columns) throws IOException {
		String filepath = System.getProperty("user.dir") + "\\src\\Common\\";
		String filename = "excel.xlsx";
		FileInputStream instream = new FileInputStream(filepath + "\\" + filename);
		System.out.println(filepath + "\\" + filename);
		Workbook wb = new XSSFWorkbook(instream);
		Sheet sheet = wb.getSheet(excelsheetname);
		int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();
		int cnt = 0;
		System.out.println(rows + "===" + columns);

		Object[][] postdata = new Object[rows][columns];
		for (int i = 2; i <= rows; i++) {
			Row row = sheet.getRow(i);
			for (int j = 0; j < row.getLastCellNum(); j++) {
				sheet.getRow(i).getCell(j).setCellType(sheet.getRow(i).getCell(j).CELL_TYPE_STRING);
				if (sheet.getRow(i).getCell(j).getStringCellValue() != "") {
					postdata[cnt][j] = sheet.getRow(i).getCell(j).getStringCellValue();
				}
			}
			cnt++;
		}

		return postdata;
	}

	public String getID(String ID) throws Exception {
		String status = "";
		String url = "https://testing.xataka.com/admin/newposts/";
		url = url + ID;
		WebDriver driver = new HtmlUnitDriver();
		driver.get(url);
		// System.out.println(driver.getCurrentUrl());
		String username = prop.getProperty("Uadmin");
		String pwd = prop.getProperty("Padmin");
		driver.findElement(By.xpath(prop.getProperty("login_username_txt"))).sendKeys(username);
		driver.findElement(By.xpath(prop.getProperty("login_pwd_txt"))).sendKeys(pwd);
		driver.findElement(By.xpath(prop.getProperty("login_submit_button"))).click();
		driver.navigate().refresh();
		// System.out.println("Current url" + driver.getCurrentUrl());
		if (driver.getCurrentUrl().contains("clubposts")) {
			status = "Club";
		} else {
			status = "normal";
		}
		// System.out.println(driver.getCurrentUrl() +"==" + status);
		driver.close();

		return status;
	}

	public void checkThumbnails(String URL) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(URL);
		try {
			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() != 200)
				System.out.println(URL + ": " + response.getStatusLine().getStatusCode() + "=="
						+ response.getStatusLine().getReasonPhrase());
		} catch (Exception e) {
			// e.printStackTrace();
		}

	}

	public void CheckImageCropperStatus(String url) {
		String url1 = "";
		String thumbnails = "1366_1366,1024_1024,840_840,375_375,288_288,1024_682,840_560,1920_733,1440_550,1366_521,1024_391,500_190,375_142";
		String[] arrthumbnails = thumbnails.split(",");

		for (int i = 0; i < arrthumbnails.length; i++) {
			url1 = url.replace("original", arrthumbnails[i]).trim();
			checkThumbnails(url1);
			System.out.println(url1);
			url1 = url;
		}

	}

	public void insertGIPHY(String URL, String layout, String caption, String browser) {
		List<WebElement> items = findElementsByXpath(prop.getProperty("header"));

		for (WebElement item : items) {
			if (item.getText().equalsIgnoreCase("GIF")) {
				item.click();
				break;
			}
		}

		findAndWrite("giphyURL", URL);
		implicitWait();

		if (layout.equalsIgnoreCase("smallLeft")) {
			WebElement element1 = findElement(prop.getProperty("smallLeft"));
			JavascriptExecutor executor1 = (JavascriptExecutor) driver;
			executor1.executeScript("arguments[0].click();", element1);
		} else if (layout.equalsIgnoreCase("smallCenter")) {
			WebElement element2 = findElement(prop.getProperty("smallCenter"));
			JavascriptExecutor executor2 = (JavascriptExecutor) driver;
			executor2.executeScript("arguments[0].click();", element2);
		} else if (layout.equalsIgnoreCase("smallRight")) {
			WebElement element3 = findElement(prop.getProperty("smallRight"));
			JavascriptExecutor executor3 = (JavascriptExecutor) driver;
			executor3.executeScript("arguments[0].click();", element3);
		} else if (layout.equalsIgnoreCase("normalCenter")) {
			WebElement element4 = findElement(prop.getProperty("normalCenter"));
			JavascriptExecutor executor4 = (JavascriptExecutor) driver;
			executor4.executeScript("arguments[0].click();", element4);
		} else {
			WebElement element5 = findElement(prop.getProperty("large"));
			JavascriptExecutor executor5 = (JavascriptExecutor) driver;
			executor5.executeScript("arguments[0].click();", element5);
		}
		implicitWait();
		findAndWrite("captionGIPHY", caption);
		findAndClick("insertButtonGIPHY");
		findAndClick("post_content");
	}

	public void addRecipe(String name, String persons, String level, String ingredients,
			String Recipe_ingredients_Cantidad, String Recipe_ingredients_units, String Recipe_ingredients_Detailes,
			String Preparation_time_hours, String Preparation_time_Mintues, String Cooking_time_hours,
			String Cooking_time_minutes, String Rest_time_hours, String Rest_time_mintues, String Recipe_postcontent,
			String RecipeImage, String Recipe_More_postcontent, String Youtube_Video,
			String Recipe_Youtube_Video_layout, String Vine_Video, String Recipe_Vine_Video_layout, String Vimeo_Video,
			String Recipe_Vimeo_Video_layout, String FB_Video, String Recipe_FB_Video_layout, String Recipe_summary,
			String Recipe_summary_layout, String browser) throws Exception {
		System.out.println("Recipe_name " + name);
		findAndWrite("Recipe_name", name);
		implicitWait();
		String[] personarr = persons.split("@##@");
		implicitWait();
		if (level.equalsIgnoreCase("high")) {
			findElement(prop.getProperty("Recipe_high")).click();
		} else if (level.equalsIgnoreCase("Low")) {
			findElement(prop.getProperty("Recipe_low")).click();
		} else {
			findElement(prop.getProperty("Recipe_medium")).click();
		}
		findAndWrite("Recipe_Person", personarr[0]);
		findAndClick("Recipe_person_unit_dropdown");
		implicitWait();
		List<WebElement> lists3 = driver.findElements(By.xpath("//*//*[text()='" + personarr[1] + "']"));
		System.out.println(lists3.size());
		for (WebElement test3 : lists3) {
			if (test3.getText().equalsIgnoreCase(personarr[1].trim())) {
				System.out.println("Matched: " + test3.getText());
				test3.click();
				implicitWait();
				break;
			}
		}
		implicitWait();
		implicitWait();
		findAndWrite("preparation_time_hours", Preparation_time_hours);
		findAndWrite("preparation_time_mnts", Preparation_time_Mintues);
		findAndWrite("cooking_time_hours", Cooking_time_hours);
		findAndWrite("cooking_time_mnts", Cooking_time_minutes);
		findAndWrite("rest_time_hours", Rest_time_hours);
		findAndWrite("rest_time_mnts", Rest_time_mintues);
		implicitWait();

		String[] arringredients = ingredients.split("@##@");
		String[] arrRecipe_ingredients_Cantidad = Recipe_ingredients_Cantidad.split("@##@");
		String[] arrRecipeingredientsdetails = Recipe_ingredients_Detailes.split("@##@");
		String[] arrRecipeingredientsunits = Recipe_ingredients_units.split("@##@");
		int cnt = 2;
		for (int i = 0; i < arringredients.length; i++) {
			if (i > 2) {
				findAndClick("Recipe_more_row_button");
			}

			implicitWait();
			findElement(prop.getProperty("Recipe_ingredient_row_p1") + "[" + cnt + "]"
					+ prop.getProperty("Recipe_ingredient_row_p2")).sendKeys(arringredients[i]);

			List<WebElement> lists1 = findElementsByXpath(prop.getProperty("ingredientListRecipe"));
			// List<WebElement> lists1 =
			// driver.findElements(By.xpath("//*[text()='" + arringredients[i] +
			// "']"));
			for (WebElement test : lists1) {
				System.out.println(test.getText() + "===" + arringredients[i].trim());
				if (test.getText().equalsIgnoreCase(arringredients[i].trim())) {
					System.out.println("Matched: " + test.getText());
					test.click();
					implicitWait();
					break;
				}
			}

			System.out.println(arrRecipe_ingredients_Cantidad[i]);
			implicitWait();
			findElement(prop.getProperty("Recipe_ingredient_quantity_p1") + "[" + cnt + "]"
					+ prop.getProperty("Recipe_ingredient_quantity_p2")).sendKeys(arrRecipe_ingredients_Cantidad[i]);
			implicitWait();
			findElement(prop.getProperty("Recipe-unit_p1") + "[" + cnt + "]" + prop.getProperty("Recipe-unit_p2"))
					.click();
			implicitWait();

			List<WebElement> lists2 = driver
					.findElements(By.xpath("//*//*[last()][text()='" + arrRecipeingredientsunits[i] + "']"));
			for (WebElement test1 : lists2) {
				if (test1.getText().equalsIgnoreCase(arrRecipeingredientsunits[i].trim())) {
					System.out.println("Matched Unit: " + test1.getText());
					test1.click();
					implicitWait();
					break;
				}
			}
			implicitWait();
			System.out.println(arrRecipeingredientsdetails[i].toString() + "==" + Recipe_ingredients_Detailes);
			findElement(prop.getProperty("Recipe_details_p1") + "[" + cnt + "]" + prop.getProperty("Recipe_details_p2"))
					.sendKeys(Keys.SHIFT);
			Thread.sleep(10000);
			findElement(prop.getProperty("Recipe_details_p1") + "[" + cnt + "]" + prop.getProperty("Recipe_details_p2"))
					.sendKeys(arrRecipeingredientsdetails[i]);
			implicitWait();
			implicitWait();
			cnt++;
		}
		findAndWrite("Recipe_Post_content", Recipe_postcontent);
		implicitWait();
		findAndSendkey("Recipe_Post_content", Keys.END);
		findAndSendkey("Recipe_Post_content", Keys.ENTER);
		findAndSendkey("Recipe_Post_content", Keys.END);
		findAndSendkey("Recipe_Post_content", Keys.ENTER);

		if (!RecipeImage.equalsIgnoreCase("null")) {
			findAndClick("Recipe_image");
			RecipeuploadImage(RecipeImage, browser);
			implicitWait();
			findAndSendkey("Recipe_Post_content", Keys.END);
			findAndSendkey("Recipe_Post_content", Keys.ENTER);
			findAndSendkey("Recipe_Post_content", Keys.END);
			findAndSendkey("Recipe_Post_content", Keys.ENTER);
		}

		if (!Youtube_Video.equalsIgnoreCase("null")) {
			recipemovecursorpostion(1);
			RecipeAddVideo(Youtube_Video, Recipe_Youtube_Video_layout, browser);
			recipemovecursorpostion(2);
		}

		if (!Recipe_More_postcontent.equalsIgnoreCase("null")) {
			recipemovecursorpostion(1);
			findAndWrite("Recipe_Post_content", Recipe_More_postcontent);
			recipemovecursorpostion(2);
		}

		if (!Vine_Video.equalsIgnoreCase("null")) {
			recipemovecursorpostion(1);
			RecipeAddVideo(Vine_Video, Recipe_Vine_Video_layout, browser);
			recipemovecursorpostion(2);

		}

		if (!Vimeo_Video.equalsIgnoreCase("null")) {
			recipemovecursorpostion(1);
			RecipeAddVideo(Vimeo_Video, Recipe_Vimeo_Video_layout, browser);
			recipemovecursorpostion(2);
		}
		if (!FB_Video.equalsIgnoreCase("null")) {
			recipemovecursorpostion(1);
			RecipeAddVideo(FB_Video, Recipe_FB_Video_layout, browser);
			recipemovecursorpostion(2);
		}

		if (!Recipe_summary.equalsIgnoreCase("null")) {
			recipemovecursorpostion(1);
			RecipeSummary(Recipe_summary, Recipe_summary_layout);
			recipemovecursorpostion(2);
		}

		Thread.sleep(3000);
		implicitWait();
		findAndClick("Recipe_Save_button");
		Thread.sleep(1000);
	}

	public String imageCropper() throws InterruptedException {
		Thread.sleep(7000);
		findAndClick("Escribir");
		findAndClick("publish_tab");
		implicitWait();
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,870)");
		implicitWait();
		List<WebElement> validarButtons = findElementsByXpath(prop.getProperty("validarButtons"));
		for (WebElement validButton : validarButtons) {
			if (validButton.getText().equalsIgnoreCase("validar")) {
				validButton.click();
				Thread.sleep(2000);
			}
		}
		List<WebElement> editarButtons = findElementsByXpath(prop.getProperty("editarButtons"));
		for (int i = 0; i < editarButtons.size(); i++) {
			editarButtons.get(i).isDisplayed();
		}
		return "Images have been cropped sucessfully";
	}
	 public void searchAndEdit(String search_data) throws Exception {
			driver.findElement(By.xpath(prop.getProperty("search_field_path"))).sendKeys(search_data);
			driver.findElement(By.xpath(prop.getProperty("search_button"))).click();
			Thread.sleep(2000);
			List<WebElement> items = driver.findElements(By.xpath(prop.getProperty("Dashboardlist")));
			System.out.println(items.size());
			for (WebElement posttitle : items) {
				if (posttitle.getText().equalsIgnoreCase(search_data)) {
					System.out.println(posttitle.getText());
					findAndClick("Editbutton");
					break;
				}
			}
			
		}
	 public void scheduleRepublish(String scheduleDate){	
			findAndClick("post_title");
			Conditionalwait("difundir_Link");
			findAndClick("difundir_Link");
			implicitWait();
			findAndClick("portada_button");
			implicitWait();
			driver.findElement(By.xpath(prop.getProperty("repubDate"))).clear();
			implicitWait();
			findAndWrite("repubDate", scheduleDate);
			findAndClick("checkBoxRepub");
			findAndClick("scheduleRepubButton");

		}
		public String normalPostVarifyCropImage(String results) throws Exception {

			findAndClick("publish_tab");

			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,1300)");
			String result1="";

			String Editar="";
			if (!results.equalsIgnoreCase(Editar)) {
				result1 = driver.findElement(By.xpath(prop.getProperty("Editar"))).getText();
				System.out.println("Active button=  " + result1);
				System.out.println("********* NOTE:- If Editar button present means your Image has validated *********");
			} else {
				
				System.out.println("********* NOTE:- If Validar button present means you have to validate image first to publish post *********");
			}
			return result1;
		}
	
		public void sunMedia(String sunVideoURL, String layout, String browser, String youtubeURL) {
			findAndClick("Video_URL");
			implicitWait();
			if (layout.equalsIgnoreCase("normal")) {
				findElement(prop.getProperty("Video_NormalLayout")).click();
			} else {
				findElement(prop.getProperty("Video_Biglayout")).click();
			}
			implicitWait();
			findAndWrite("Sun_Xpath", sunVideoURL);
			findAndWrite("Sun_youtubeXpath", youtubeURL);
			if (sunVideoURL.contains("api")) {
				findAndClick("button_sunmedia");		
			}
				}
		
		 public void writeExcel(String fileName,String sheetName,String[] dataToWrite) throws IOException{
			    //DashboardButton
				    String filePath = System.getProperty("user.dir") + "\\src\\Common\\";
				    File file =    new File(filePath+"\\"+fileName); 	//Create an object of File class to open xlsx file
					FileInputStream inputStream = new FileInputStream(file); //Create an object of FileInputStream class to read excel file
					Workbook workbook = null;
					String fileExtensionName = fileName.substring(fileName.indexOf(".")); //Find the file extension by splitting  file name in substring and getting only extension name
					if(fileExtensionName.equals(".xlsx")){		//Check condition if the file is xlsx file
						workbook = new XSSFWorkbook(inputStream); //If it is xlsx file then create object of XSSFWorkbook class
					}//Check condition if the file is xls file
					else if(fileExtensionName.equals(".xls")){  
						workbook = new HSSFWorkbook(inputStream); //If it is xls file then create object of XSSFWorkbook class
			      }
				   Sheet sheet = workbook.getSheet(sheetName); //Read excel sheet by sheet name  
				   int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum(); //Get the current count of rows in excel file
				   Row row = sheet.getRow(0);  //Get the first row from the sheet
				   Row newRow = sheet.createRow(rowCount+1); //Create a new row and append it at last of sheet
				   for(int j = 0; j < row.getLastCellNum(); j++){//Create a loop over the cell of newly created Row
					 //Fill data in row
					  Cell cell = newRow.createCell(j);
					  cell.setCellValue(dataToWrite[j]);
				   }
				   inputStream.close(); //Close input stream
				   FileOutputStream outputStream = new FileOutputStream(file); //Create an object of FileOutputStream class to create write data in excel file
				   workbook.write(outputStream); //write data in the excel file
				   outputStream.close(); //close output stream
			 }
		 
		 public void handleAuthenticationDialog(String browser) throws IOException {
				
			  if (browser.trim().equalsIgnoreCase("Chrome")) {
			  Runtime.getRuntime().exec(System.getProperty("user.dir") + "\\src\\DriverFiles\\authentication_chrome.exe"); 
			  }
			else {
				Runtime.getRuntime().exec(System.getProperty("user.dir") + "\\src\\DriverFiles\\authentication_firefox.exe");
			}
			System.out.println("Sucessfully Authenticated");
		 }
		
		public Object[][] readExcel_roleCreation(String excelsheetname, int columns) throws IOException {
			String filepath = System.getProperty("user.dir") + "\\src\\Common\\";
			String filename = "Excel_roleCreation.xlsx";
			FileInputStream instream = new FileInputStream(filepath + "\\" + filename);
			System.out.println(filepath + "\\" + filename);
			Workbook wb = new XSSFWorkbook(instream);
			Sheet sheet = wb.getSheet(excelsheetname);
			int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();
			int cnt = 0;
			System.out.println(rows + "===" + columns);

			Object[][] userdata = new Object[rows][columns];
			for (int i = 1; i <= rows; i++) {
				Row row = sheet.getRow(i);
				for (int j = 0; j < row.getLastCellNum(); j++) {
					sheet.getRow(i).getCell(j).setCellType(sheet.getRow(i).getCell(j).CELL_TYPE_STRING);
					if (sheet.getRow(i).getCell(j).getStringCellValue() != "") {
						userdata[cnt][j] = sheet.getRow(i).getCell(j).getStringCellValue();
					}
				}
				cnt++;
			}

			return userdata;
		}
	 /*public List<IndexWrapper> findIndexesForKeyword(String keyword,String searchString) {
		 
		 String result= findMatch( keyword, searchString);
		
		 System.out.println("Matching result======"+result);
	        String regex = "\\b"+keyword+"\\b";
	        Pattern pattern = Pattern.compile(regex);
	        Matcher matcher = pattern.matcher(searchString);
	 
	        List<IndexWrapper> wrappers = new ArrayList<IndexWrapper>();
	 
	        while(matcher.find() == true){
	            int end = matcher.end();
	            int start = matcher.start();
	            IndexWrapper wrapper = new IndexWrapper(start, end);
	            wrappers.add(wrapper);
		        System.out.println(wrapper);

	        }
	        
	        
		
	        return wrappers;
	    }
	 */
		
		public String findMatch(String keyword,String searchString)
		{
			String arrmatch="";
	
			String[] tags=keyword.toLowerCase().split(" ");
			int cnt=0;
			int index =0;
			
			for(int j=0;j<tags.length;j++)
			{
				if(tags[j].length()>2) {
				String tagsRegex = "\\w*(" + String.join("|", tags[j].substring(0, 3)) + ")\\w*";
				Matcher matcher = Pattern.compile(tagsRegex,
                     Pattern.UNICODE_CHARACTER_CLASS).matcher(searchString.toLowerCase());
			 while (matcher.find() ) {
			     // System.out.println("MATCH======="+matcher.group() );
			     if(arrmatch!="")
			     {
			    	 arrmatch=arrmatch+","+ matcher.group();
			     }
			     else
			     {
			    	 arrmatch=matcher.group(); 
			     }
			    }
				}
			}
			 //System.out.println(arrmatch);
			 
			/*String result=  String.valueOf(cnt) ;
			result=result+"@"+arrmatch;
			*/
			return arrmatch;
		}
		
		
	 public String getPostcontent(String ID,String posttype,String sitename) {
		 System.out.println("ID==="+ID);
			String status = "";
			String url = sitename.replace("https://testing", "https://guest:guest@testing");
			//System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + "//src//Driverfiles//" + "chromedriver.exe");
			
			WebDriver driver = new HtmlUnitDriver();
			driver.get(url);
			/*driver.navigate().refresh();
			*/driver.manage().timeouts().implicitlyWait(1000,TimeUnit.SECONDS);
			if(posttype.equalsIgnoreCase("respuesta"))
			{
				status = driver.findElement(By.xpath(".//*[@class='article-summary']")).getText();
				
			}
			else if (posttype.equalsIgnoreCase("ecom"))
			{
				status = driver.findElement(By.xpath(".//*[@class='context']")).getText();
				
			}
			else {
			status = driver.findElement(By.xpath(".//*[@class='article-content']")).getText();
			}
			driver.close();
			return status;
		}
	 
	 public void CreateMVPpost() throws InterruptedException {
		 System.out.println("Move to warning screen");
		 	findAndClick("navigation_header");
		 	findAndClick("create_MVPpost_link");
		 	Thread.sleep(3000);
	        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
	        driver.switchTo().window(tabs.get(1));
	        implicitWait();
	         System.out.println("mvp_close_dialog==="+prop.getProperty("mvp_close_dialog"));
	        Conditionalwait("mvp_close_dialog");
	        findAndClick("mvp_close_dialog");
	        
	        implicitWait();
	    }


	public void ClickICON(WebDriver driver, String icon ) throws InterruptedException {
		Thread.sleep(2000);
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(By.xpath(prop.getProperty("content_section_path"))));
		action.click();
		implicitWait();
		action.sendKeys(Keys.ENTER);
		action.build().perform();
		implicitWait();
		findAndClick("Clickplus");
		implicitWait();
		if(icon.equalsIgnoreCase("image")){
			findAndClick("MVPImage");
		}else if(icon.equalsIgnoreCase("instagram")) {
			findAndClick("MVP_instagram");
		}else if(icon.equalsIgnoreCase("giphy")) {
			findAndClick("MVPgiphy");
		}
		implicitWait();
	}

	public void AddMVPTitle(String title)  {

		Actions action = new Actions(driver);
		implicitWait();
		action.moveToElement(driver.findElement(By.xpath(prop.getProperty("MVPtitle"))));
		action.click();
		action.build().perform();
	
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		action.sendKeys(title);
		action.build().perform();

		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		implicitWait();
		System.out.println("Title added to post");
	}

	public void addImageToResourcePanel(String browser) throws IOException, InterruptedException {

		findAndClick("MVP_upload_img_btn");
		if (browser.trim().equalsIgnoreCase("Chrome")) {
			Runtime.getRuntime().exec(System.getProperty("user.dir") + "\\src\\DriverFiles\\mvpImage_chrome.exe" + " "
					+ System.getProperty("user.dir") + "\\src\\Images\\" + prop.getProperty("MVPimage_locate"));
		} else {
			Runtime.getRuntime().exec(System.getProperty("user.dir") + "\\src\\DriverFiles\\mvpImage_firefox.exe" + " "
					+ System.getProperty("user.dir") + "\\src\\Images\\" + prop.getProperty("MVPimage_locate"));
		}
		System.out.println("Image sucessfully uploaded");
	}

	public void addMVP_SectionContent(String addContent) throws InterruptedException {
		Actions actions = new Actions(driver);
		if (findElement(prop.getProperty("typeSection")).getText()
				.equalsIgnoreCase("Empieza a escribir aqu�...")) {
			Thread.sleep(1000);
			actions.moveToElement(driver.findElement(By.xpath(prop.getProperty("addSection"))));
			actions.click();
			actions.sendKeys(addContent);
			actions.build().perform();
			implicitWait();
		} else {
			Thread.sleep(1000);
			List<WebElement> list = (findElementsByXpath(prop.getProperty("MVPSectionList")));
			if (list.get(list.size() - 1).findElement(By.xpath("//*[@class='node-wrapper']")) != null)
				actions.moveToElement(driver.findElement(By.xpath(".//*[@id='"
						+ list.get(list.size() - 1).getAttribute("id") + "']" + prop.getProperty("newLineMVP"))))
						.click();
			actions.sendKeys(addContent);
			//actions.sendKeys(Keys.ENTER);
			actions.build().perform();
		}
	}

	public void mvpUrlImage(String image_url) throws InterruptedException {
		Actions action = new Actions(driver);
		implicitWait();
		action.moveToElement(driver.findElement(By.className(prop.getProperty("urlUploadClass"))));
		action.click();
		action.sendKeys(image_url);
		action.build().perform();
		findAndClick("mvp_image_url_button");
		implicitWait();
	}

	public void Insertimage() {
		Conditionalwait("image_select");
		findAndClick("MVPResourceImage1");
		implicitWait();
		implicitWait();
		driver.switchTo().activeElement();
		findAndWrite("MVPImageAlt", "testing alternate text");
		findAndClick("MVPInsertButton");
	}
	
	public void getHintImageToolbar() throws InterruptedException {
		Actions actions = new Actions(driver);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@class='node-wrapper']")).click();
		List<WebElement> toolBarList = findElementsByXpath(prop.getProperty("imageToolbarList"));
		actions.moveToElement(findElement(prop.getProperty("activeImageToolbar"))).clickAndHold().build().perform();
		Thread.sleep(1000);
		System.out.println(
				"Active_image layout is   -->>" + findElement(prop.getProperty("activeImageToolbar")).getText());
		implicitWait();
		for (int i = 0; i <= toolBarList.size(); i++) {
			actions.moveToElement(toolBarList.get(i)).clickAndHold().build().perform();
			Thread.sleep(1000);
			System.out.println("Other toolbars are -->   " + toolBarList.get(i).getText());
			if (i == toolBarList.size() - 1)
			break;
			actions.moveToElement(toolBarList.get(i + 1)).clickAndHold().build().perform();
		}
	}

	public void Insertimage(String layout) throws InterruptedException {
		Conditionalwait("image_select");
		Thread.sleep(3000);
		findAndClick("image_select");
		implicitWait();
		Thread.sleep(3000);
		implicitWait();
		driver.switchTo().activeElement();
		findAndWrite("MVPImageAlt", "testing alternate text");
		if (layout.equalsIgnoreCase("")) {
			findAndClick("MVPInsertButton");
			}
		else{
			//layoutResourcePanel(layout);         //UPDATION
			common_layout_mvp("imgLayoutResourcePanel", layout);
		findAndClick("MVPInsertButton");}
		
	}

	public void editImage(String image, String layout) throws InterruptedException {
		Actions actions = new Actions(driver);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@class='node-wrapper']")).click();
		List<WebElement> toolBarList = findElementsByXpath(prop.getProperty("imageToolbarList"));
		for (int i = 0; i < toolBarList.size(); i++) {
			actions.moveToElement(toolBarList.get(i)).clickAndHold().build().perform();
			Thread.sleep(1000);
			if (toolBarList.get(i).getText().equalsIgnoreCase("Editar")) {
				toolBarList.get(i).click();
				break;
			}
		}
		if (image.equalsIgnoreCase("")) {
			Thread.sleep(1000);
			findAndClick("image_select");
			actions.moveToElement(driver.findElement(By.xpath(prop.getProperty("image_url_decp_path"))));
			actions.click();
			findAndWrite("MVPImageAlt", "testing alternate text");
			Thread.sleep(2000);
			actions.moveToElement(driver.findElement(By.xpath(prop.getProperty("imgCaptionResourcePanel"))));
			actions.click();
			// actions.sendKeys("caption");  //findAndWrite("imgCaptionResourcePanel","testing caption text");
			Thread.sleep(2000);
			layoutResourcePanel(layout);
			findAndClick("MVPInsertButton");
		} else {
			Thread.sleep(3000);
			Insertimage(layout);
		}
	}
// NOT WORKING
	public void layoutResourcePanel(String layout) throws InterruptedException {
		Actions actions = new Actions(driver);
		List<WebElement> imagePanel = findElementsByXpath(prop.getProperty("imgLayoutResourcePanel"));
		for (int j = 0; j < imagePanel.size(); j++) {
			actions.moveToElement(imagePanel.get(j)).clickAndHold().build().perform();
			Thread.sleep(1000);
			if (imagePanel.get(j).getText().equalsIgnoreCase(layout)) {
				imagePanel.get(j).click();
				Thread.sleep(2000);
			}
		}
	}

	public void deleteMVPImage() throws InterruptedException {
		Thread.sleep(2000);
		findAndClick("for_delete_image_click");
		implicitWait();
		findAndClick("delete_img_button");
	}

	public void warnningPopup() {
		String warnning = "Editar Este Art�culo en Alfa";
		WebElement element = driver.findElement(By.xpath(prop.getProperty("matchTextMVP")));
		element.getText();
		System.out.println(element.getText());
		if (element.getText().equalsIgnoreCase(warnning)) {
			Conditionalwait("warnningPopup1");
			findAndClick("warnningPopup1");
			implicitWait();
		} else {
			Conditionalwait("warnningPopup2");
			findAndClick("warnningPopup2");
			implicitWait();
		}
	}
	
	public void addImageCaption() throws InterruptedException {
		Thread.sleep(2000);
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(By.xpath(prop.getProperty("imgCaption_path"))));
		action.click();
		action.sendKeys("image caption xyz");
		action.sendKeys(Keys.TAB);
		action.build().perform();
		System.out.println("Caption successfully inserted");

	}
	
	public void selectImageLayout(String layout) throws InterruptedException {

		Thread.sleep(2000);
		driver.findElement(By.xpath(prop.getProperty("imgToolbar_path"))).click();
		if (layout.equalsIgnoreCase("smallLeft")) {
			driver.findElement(By.xpath(prop.getProperty("img_smallLeft"))).click();
		} else if (layout.equalsIgnoreCase("smallCenter")) {
			driver.findElement(By.xpath(prop.getProperty("img_smallCenter"))).click();
		} else if (layout.equalsIgnoreCase("smallRight")) {
			driver.findElement(By.xpath(prop.getProperty("img_smallRight"))).click();
		} else if (layout.equalsIgnoreCase("normalCenter")) {
			driver.findElement(By.xpath(prop.getProperty("img_normalCenter"))).click();
		} else {
			driver.findElement(By.xpath(prop.getProperty("img_big"))).click();
		}

		System.out.println("Layout selected successfully");
	}
	
	public void MVPmodules(String url)
	{
		  Actions action = new Actions(driver);
		  implicitWait();
			action.moveToElement(driver.findElement(By.xpath(prop.getProperty("MVPmoduletextbox"))));
			action.click();
			action.sendKeys(url);
			action.build().perform();
			findAndClick("MVPInsertButton");	
			WebElement element1 = driver.findElement(By.xpath(prop.getProperty("MVPmoduletextbox")));
			if (new WebDriverWait(driver, 10).until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(prop.getProperty("MVPmoduletextbox")))) != null) {
				System.out.println(element1.getText());
			}
		implicitWait();
	}
	
	public void addTwitterMVP(String twURL) throws InterruptedException {
        implicitWait();
		findAndClick("mvp_twitter");
		implicitWait();
		if (twURL.contains("twitter") == true) {
			findAndWrite("mvp_tw_url", twURL);
			implicitWait();
			findAndClick("mvp_insertar");
		} else {
			findAndWrite("mvp_tw_url", twURL);
			findAndClick("mvp_insertar");
			implicitWait();
			if (findElement(prop.getProperty("mvp_tw_validation")).isDisplayed() == true)
				System.out.println("invalid twitter URL");
		}
	}

	public void addSumarioMVP(String layout, String sumarioText) throws InterruptedException {
        implicitWait();
		findAndClick("mvp_sumario");
		Actions action = new Actions(driver);
        //action.moveToElement(driver.findElement(By.xpath("//*[@id=\"summarymodal\"]/div[1]/div/div/div/div/div")));
        action.moveToElement(findElement(prop.getProperty("mvp_sumario_url")));
        action.click();
        action.sendKeys(sumarioText);
        action.build().perform();
        implicitWait();
        common_layout_mvp("mvp_layout1", layout);
		findAndClick("mvp_insertar");
    }

	
	public void MVPaddInfogram(String infogramUrl) {
		findAndClick("MVP_infogramIcon");
		Actions action = new Actions(driver);
		implicitWait();
		action.moveToElement(driver.findElement(By.xpath(prop.getProperty("MVPInfogramtextbox"))));
		action.click();
		action.sendKeys(infogramUrl);
		action.build().perform();
		findAndClick("MVPInsertButton");
		System.out.println("Infogram INSERTED successfully");
		implicitWait();
	}

	public void MVPaddVideo(String videoUrl) {
		findAndClick("MVP_videoIcon");
		Actions action = new Actions(driver);
		implicitWait();
		action.moveToElement(driver.findElement(By.xpath(prop.getProperty("MVPVideotextbox"))));
		action.click();
		action.sendKeys(videoUrl);
		action.build().perform();
		findAndClick("MVPInsertButton");
		System.out.println("Video INSERTED successfully");
		implicitWait();
	}

	public void MVP_editInfogram(String layout) throws InterruptedException {
		Thread.sleep(5000);
		Actions action = new Actions(driver);
		implicitWait();
		driver.findElement(By.xpath("//*[@class='node-wrapper']")).click();
		implicitWait();
		// action.moveToElement(driver.findElement(By.xpath(prop.getProperty("edit_toolbarIcon"))));
		action.moveToElement(findElement(prop.getProperty("toolbar_icon") + "[4]"));
		action.click().build().perform();
		implicitWait();
		if (layout.equalsIgnoreCase("Normal"))
			findElement(prop.getProperty("MVP_Layout_ResourcePanel") + "[1]").click();
		else if (layout.equalsIgnoreCase("Grande"))
			findElement(prop.getProperty("MVP_Layout_ResourcePanel") + "[2]").click();
		findAndClick("MVPInsertButton");
		System.out.println("Inforgram EDITED successfully");
		implicitWait();

	}

	public void MVP_deleteInfogram() throws InterruptedException {
		Thread.sleep(2000);
		Actions action = new Actions(driver);
		implicitWait();
		driver.findElement(By.xpath("//*[@class='node-wrapper']")).click();
		implicitWait();
		// action.moveToElement(driver.findElement(By.xpath(prop.getProperty("delete_toolbarIcon"))));
		action.moveToElement(findElement(prop.getProperty("toolbar_icon") + "[5]"));
		action.click().build().perform();
		System.out.println("Inforgram DELETED successfully");
		implicitWait();

	}

	public void MVP_editVideo(String layout) throws InterruptedException {
		Thread.sleep(2000);
		Actions action = new Actions(driver);
		implicitWait();
		driver.findElement(By.xpath("//*[@class='node-wrapper']")).click();
		implicitWait();
		// action.moveToElement(driver.findElement(By.xpath(prop.getProperty("edit_toolbarIcon"))));
		action.moveToElement(findElement(prop.getProperty("toolbar_icon") + "[4]"));
		action.click().build().perform();
		implicitWait();
		if (layout.equalsIgnoreCase("Normal"))
			findElement(prop.getProperty("MVP_Layout_ResourcePanel") + "[1]").click();
		else if (layout.equalsIgnoreCase("Grande"))
			findElement(prop.getProperty("MVP_Layout_ResourcePanel") + "[2]").click();
		findAndClick("MVPInsertButton");
		System.out.println("Video EDITED successfully");
		implicitWait();
	}

	public void MVP_DeleteVideo() throws InterruptedException {
		Thread.sleep(2000);
		Actions action = new Actions(driver);
		implicitWait();
		driver.findElement(By.xpath("//*[@class='node-wrapper']")).click();
		implicitWait();
		// action.moveToElement(driver.findElement(By.xpath(prop.getProperty("delete_toolbarIcon"))));
		action.moveToElement(findElement(prop.getProperty("toolbar_icon") + "[5]"));
		action.click().build().perform();
		System.out.println("Video DELETED successfully");
		implicitWait();
	}
	
	public void common_layout_mvp(String listXpath, String takeAction) throws InterruptedException {
		Actions actions = new Actions(driver);
		Thread.sleep(1000);
		List<WebElement> toolList = findElementsByXpath(prop.getProperty(listXpath));
		for (int i = 1; i <= toolList.size(); i++) {
			WebElement element = findElement(prop.getProperty(listXpath) + "[" + i + "]");
			actions.moveToElement(element).clickAndHold().build();
			Thread.sleep(1000);
			if (element.getAttribute("title").equalsIgnoreCase(takeAction)) {
				element.click();
				break;
			}
		}
	}

	public void edit_URL_mvp(String xPath, String URL) throws InterruptedException{
		Thread.sleep(2000);
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(By.xpath(prop.getProperty(xPath))));
		action.click();
		implicitWait();
		driver.findElement(By.xpath(prop.getProperty(xPath))).clear();
		action.moveToElement(driver.findElement(By.xpath(prop.getProperty(xPath)))).perform();
		implicitWait();
		action.moveToElement(driver.findElement(By.xpath(prop.getProperty(xPath)))).sendKeys(URL).perform();
	}

	public void edit_delete_mvp(String Module, String takeAction, String newURL, String layout)
			throws InterruptedException {
		switch (Module.toLowerCase()) {
		case "image":
		case "gif":
		case "video":
		case "infogram":
			driver.findElement(By.xpath("//*[@class='node-wrapper']")).click();
			break;
		case "sumario":
			findAndClick("mvp_sumario_click");
			break;
		case "twitter":
			findAndClick("mvp_tw_click");
			break;
		case "instagram":
			findAndClick("mvp_instagram_click");
			break;
		}
		common_layout_mvp("toolbar_icon", takeAction);
		implicitWait();
		if (takeAction.equalsIgnoreCase("Editar")) {
			switch (Module.toLowerCase()) {
			case "gif":
				edit_URL_mvp("MVPmoduletextbox", newURL);
				common_layout_mvp("mvp_layout3", layout);
				break;
			case "infogram":
			case "video":
				edit_URL_mvp("MVPmoduletextbox", newURL);
				common_layout_mvp("mvp_layout2", layout);
				break;
			case "image":
				Insertimage(layout);
				break;
			case "sumario":
				Thread.sleep(2000);
				edit_URL_mvp("mvp_sumario_url", newURL);
				common_layout_mvp("mvp_layout1", layout);
				break;
			case "twitter":
			case "instagram":
				edit_URL_mvp("MVPmoduletextbox", newURL);
				break;
			}
			findAndClick("mvp_insertar");
		}
	}
	
	public void addpivot(String Pivot_dropdown)
	{
		findAndClick("pivoticon");
		implicitWait();
		findAndClick("pivotproducttab");
		
		if(!Pivot_dropdown.equalsIgnoreCase("null"))
		{
		  Select drpCountry = new Select(driver.findElement(By.xpath(prop.getProperty("pivot_dropdown"))));
		   drpCountry.selectByValue(Pivot_dropdown);
			System.out.println("Dropdown Value selected");
			
		}
		
		/*findAndWrite("pivotsearchinputtext", product);
		findAndClick("pivotsearchbutton");
		findAndClick("pivotselect1");
		findAndClick("Addpivotproduct");*/
		
		
	}
	
	public void mvp_addRichContent(String alternativo, String text, String url)
			throws InterruptedException, IOException {
		findAndClick("mvp_richIcon");
		Thread.sleep(2000);
		findAndWrite("mvp_richText", "hello testing");
		common_layout_mvp("mvp_layout1", "Grande");
		Thread.sleep(2000);
		mvp_add_AMPAlternativo(alternativo, text, url);
	}

	public void mvp_add_AMPAlternativo(String alternativo, String text, String url) throws InterruptedException {// url
																													// can
																													// be
																													// video/image
		Actions actions = new Actions(driver);
		List<WebElement> buttons = findElementsByXpath(prop.getProperty("mvp_rich_buttons"));
		for (int i = 1; i <= buttons.size(); i++) {
			WebElement element = findElement(prop.getProperty("mvp_rich_buttons") + "[" + i + "]");
			if ((element.getText().equalsIgnoreCase(alternativo))
					|| (element.getText().equalsIgnoreCase(alternativo + " (en uso)"))) {
				element.click();
				break;
			}
		}
		switch (alternativo.toLowerCase()) {
		case "imagen alternativa":
			Thread.sleep(1000);
			mvpUrlImage(url);
			implicitWait();
			Insertimage("");
			break;
		case "texto alternativo":
			Thread.sleep(1000);
			actions.moveToElement(findElement(prop.getProperty("mvp_text_insert"))).click().perform();
			actions.sendKeys(text).perform();
			implicitWait();
			findAndClick("MVPInsertButton");
			break;
		case "vídeo alternativo":
			implicitWait();
			actions.sendKeys(url).build().perform();
			findAndClick("MVPInsertButton");
			break;
		}
	}

	public void mvp_edit_richContent(String newAlternativo, String takeAction, String richContent, String layout,
			String text, String url) throws InterruptedException {
		driver.findElement(By.xpath("//*[@class='node-wrapper block-highlight']")).click();
		common_layout_mvp("toolbar_icon", takeAction);
		implicitWait();
		if (richContent.isEmpty() == false) {
			findElement(prop.getProperty("mvp_richText")).clear();
			findAndWrite("mvp_richText", richContent);
			common_layout_mvp("mvp_layout1", layout);
			implicitWait();
			if (findElement(prop.getProperty("mvp_amp_div")).getAttribute("class")
					.equalsIgnoreCase("amp-img-container"))
				System.out.println("Old AMP alternativo: IMAGE");
			else if (findElement(prop.getProperty("mvp_amp_div")).getAttribute("class")
					.equalsIgnoreCase("video-play-icon"))
				System.out.println("Old AMP alternativo: "
						+ driver.findElement(By.xpath(".//*[@class='amp-content']/img")).getAttribute("alt"));
			else
				System.out.println("Old AMP alternativo: TEXTO");
			Thread.sleep(1000);
			mvp_add_AMPAlternativo(newAlternativo, text, url);
			implicitWait();
			driver.findElement(By.xpath("//*[@class='node-wrapper block-highlight']")).click();
			common_layout_mvp("toolbar_icon", takeAction);
			Thread.sleep(1000);
			System.out.println("Alternativo is successfully updated.");
		}
	}
	public void add_Otra_tienda(String Pivot_otherStoreProductTitle, String Pivot_otherStoreProductImage,String Pivot_otherStorevalues) throws IOException, InterruptedException
	{
		findAndClick("otherStoreProductTitle");
		findAndWrite("otherStoreProductTitle", Pivot_otherStoreProductTitle);
		findAndClick("otherStoreProductImage");
		Thread.sleep(1000);
		Runtime.getRuntime().exec(System.getProperty("user.dir") + "\\src\\DriverFiles\\fileupload.exe"
			+ " " + System.getProperty("user.dir") + "\\src\\Images\\" + Pivot_otherStoreProductImage);
	
		implicitWait();		
		
		
		implicitWait();
		System.out.println("Image uploaded ");
		String[] arrmultiplestore= Pivot_otherStorevalues.split("@###@");
		
		System.out.println("Total array " +arrmultiplestore.length);
		for(int i=0;i<arrmultiplestore.length;i++)
		{
			String[] arrstores=arrmultiplestore[i].split("@#@");
			System.out.println(arrstores[0]);
			System.out.println(arrstores[1]);
			System.out.println(arrstores[2]);
		
			driver.findElement(By.xpath(".//*[@id='url"+i+"']")).sendKeys(arrstores[0]);
			driver.findElement(By.xpath(".//*[@id='store"+i+"']")).sendKeys(arrstores[1]);
			driver.findElement(By.xpath(".//*[@id='price"+i+"']")).sendKeys(arrstores[2]);
		 
			if((arrmultiplestore.length>1) && ((i+1)<arrmultiplestore.length))
			{
			findAndClick("Addotherstorebutton");
			}	

		}
		findAndClick("Pivot_addOtrabutton");
		System.out.println("Otra Pivot added successfully");
	}
	
	public void add_pivot_newsletter(String site_name) throws InterruptedException
	{
		findAndClick("pivoticon");
		implicitWait();
		findAndClick("pivot_newsletter_tab");
		implicitWait();
		driver.switchTo().defaultContent();
		List<WebElement> radioButton = driver.findElements(By.name("newsletter-radio"));
		for(int i=0; i<radioButton.size();i++)
		{
		implicitWait();
		if(radioButton.get(i).getAttribute("value").equalsIgnoreCase(site_name))
		{
	    JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView(true);", radioButton.get(i));
		radioButton.get(i).click();
		break;
		}
		}
		Thread.sleep(1000);
	    JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView(true);", findElement(prop.getProperty("pivot_anadirPivot")));
		findAndClick("pivot_anadirPivot");
	}
	
	public void add_pivotAmazon(String URL, String storeData) throws InterruptedException {
		findAndClick("pivoticon");
		implicitWait();
		findAndClick("pivot_product_tab");
		findAndWrite("pivot_textarea", URL);
		findAndClick("pivot_buscar");
		if (findElement(prop.getProperty("pivot_amazon_available")).getText()
				.equalsIgnoreCase("Error: producto no encontrado"))
			System.out.println("Amazon product not found....");
		else {
			if (findElement(prop.getProperty("pivot_section")) != null)
				implicitWait();
			findAndClick("pivot_elegir");
			implicitWait();
			System.out.println("Amazon product added successfully....");
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].scrollIntoView(true);", findElement(prop.getProperty("Pivot_addOtrabutton")));
			implicitWait();
			add_OtherStores(storeData):
}
	}

	public void add_OtherStores(String storeData) throws InterruptedException {
		findAndClick("pivot_otherStoreAnadir");
		implicitWait();
		String row1[] = storeData.split("@##@");
		for (int i = 0; i < row1.length; i++) {
			String row2[] = row1[i].split("#");
			driver.findElement(By.xpath(".//*[@id='url" + (i + 1) + "']")).sendKeys(row2[0]);
			driver.findElement(By.xpath(".//*[@id ='store" + (i + 1) + "']")).sendKeys(row2[1]);
			driver.findElement(By.xpath(".//*[@id ='price" + (i + 1) + "']")).sendKeys(row2[2]);
			implicitWait();
			Actions ob = new Actions(driver);
			ob.click(driver.findElement(By.xpath(".//*[@class='article-section']/div"))).perform();
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].scrollIntoView(true);",
					findElement(prop.getProperty("Addotherstorebutton")));
			Thread.sleep(1000);
			if (i < row1.length - 1)
				driver.findElement(By.xpath(prop.getProperty("Addotherstorebutton"))).click();
		}
		findAndClick("Pivot_addOtrabutton");
	}
}

