package Frontend;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import Common.Adminproperty;
import Common.Frontend;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AmazonProductsForAdblocker {
	WebDriver driver;
	Frontend frontendProperties = new Frontend();
	Properties prop = new Properties();
	String browser = "";
	String status = "Y";
	int cnt = 0;

	@BeforeMethod
	public void Setup() throws Exception {
		prop = frontendProperties.ReadProperties();
		driver = openBrowserWithExtension(prop.getProperty("url"), prop.getProperty("browser"));
		browser = prop.getProperty("browser");

	}

	@DataProvider(name = "testdata")
	public Object[][] TestDataFeed() throws Exception {
		Object[][] postdata = readExcel("Xataka", 5);
		return postdata;
	}

	@Test(dataProvider = "testdata")
	public void CompareAmazonProducts(String BlogName, String ProductNumber, String Products, String Image, String Link)
			throws Exception {
		System.out.println("Blogname is" + ">>>>>>>" + BlogName);
		System.out.println("--------------" + ProductNumber + "--------------");
		if (!Products.equalsIgnoreCase("null")) {
			List<WebElement> Productlists = driver.findElements(By.xpath("//*[@class='desvio-summary']/a"));
			System.out.println("Total products showing on homepage ===> " + Productlists.size());
			System.out.println("***PRODUCT NAME***");
			if (Products.equalsIgnoreCase(Productlists.get(cnt).getText())) {
				System.out.println(Products + "====" + cnt + "====" + Productlists.get(cnt).getText());
			} else {
				System.out.println(
						"PRODUCT NAME IN EXCEL NOT MATCHED" + "====" + cnt + "====" + Productlists.get(cnt).getText());
			}
		}

		if (!Image.equalsIgnoreCase("null")) {
			List<WebElement> Imagelist = driver.findElements(By.xpath("//*[@class='desvio-figure']/a/img"));
			System.out.println("***PRODUCT IMAGE***");
			if (Image.equalsIgnoreCase(Imagelist.get(cnt).getAttribute("src"))) {
				System.out.println(Image + "====" + cnt + "====" + Imagelist.get(cnt).getAttribute("src"));
			} else {
				System.out.println("PRODUCT IMAGE IN EXCEL NOT MATCHED" + "====" + cnt + "===="
						+ Imagelist.get(cnt).getAttribute("src"));
			}
		}

		if (!Link.equalsIgnoreCase("null")) {
			List<WebElement> Linklist = driver.findElements(By.xpath("//*[@class='desvio-summary']/a"));
			System.out.println("***PRODUCT LINK***");
			if (Link.equalsIgnoreCase(Linklist.get(cnt).getAttribute("href"))) {
				System.out.println(Link + "====" + cnt + "====" + Linklist.get(cnt).getAttribute("href"));
			} else {
				System.out.println("PRODUCT LINK IN EXCEL NOT MATCHED" + "====" + cnt + "===="
						+ Linklist.get(cnt).getAttribute("href"));
			}
		}
		cnt = cnt + 1;
	}

	public WebDriver openHeadlessBrowserWithExtension(String url) {
		String path_to_extension = System.getProperty("user.dir") + "\\src\\Driverfiles\\3.5.2_0";
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + "//src//Driverfiles//chromedriver.exe");
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--headless");
		chromeOptions.addArguments("--no-sandbox");
		chromeOptions.addArguments("load-extension=" + path_to_extension);
		driver = new ChromeDriver(chromeOptions);
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.navigate().refresh();
		return driver;
	}

	public WebDriver openBrowserWithExtension(String url, String browser) throws IOException, InterruptedException {
		String path_to_extension = System.getProperty("user.dir") + "\\src\\Driverfiles\\3.5.2_0";

		if (browser.trim().equalsIgnoreCase("Chrome")) {
			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + "//src//Driverfiles//" + "chromedriver.exe");
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("start-maximized");
			options.addArguments("load-extension=" + path_to_extension);
			driver = new ChromeDriver(options);

		} else {
			System.setProperty("webdriver.gecko.driver",
					System.getProperty("user.dir") + "//src//Driverfiles//" + "geckodriver.exe");
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			driver = new FirefoxDriver(capabilities);
		}
		driver.get(url);
		driver.navigate().refresh();
		return driver;

	}

	public Object[][] readExcel(String excelsheetname, int columns) throws IOException {
		String filepath = System.getProperty("user.dir") + "\\src\\Common\\";
		String filename = "Amazon products for adblockers.xlsx";
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
}