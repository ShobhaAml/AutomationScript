package Frontend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Common.Frontend;

public class GetPostMetadata {
	WebDriver driver;
	Frontend frontendProperties = new Frontend();
	Properties prop = new Properties();
	String browser = "";
	String status = "Y";
	String PostTitle = "";
	String Post_PublishedDate = "";
	String Post_ModifiedDate = "";

	@BeforeMethod
	public void Setup() throws Exception {
		prop = frontendProperties.ReadProperties();
		driver = frontendProperties.frontcallproperty(prop.getProperty("url"), prop.getProperty("browser"));
		browser = prop.getProperty("browser");
	}

	public String[] getPost_MetaDate(String PostTitle, String Post_PublishedDate, String Post_ModifiedDate)
			throws Exception {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)");
		driver.findElement(By.xpath(prop.getProperty("HomepagePost1"))).click();
		driver.getPageSource();
		PostTitle = driver.findElement(By.xpath("//meta[@property='og:title']")).getAttribute("content");
		System.out.println("Post_title=" + PostTitle);
		Post_PublishedDate = driver.findElement(By.xpath("//meta[@property='article:published_time']"))
				.getAttribute("content");
		System.out.println("PostPublishedDate=" + Post_PublishedDate);
		Post_ModifiedDate = driver.findElement(By.xpath("//meta[@property='article:published_time']"))
				.getAttribute("content");
		System.out.println("PostModifiedDate=" + Post_ModifiedDate);
		String[] results = { PostTitle, Post_PublishedDate, Post_ModifiedDate };
		return results;
	}

	public void writeExcel(String fileName, String sheetName, String[] dataToWrite) throws IOException {
		// DashboardButton
		String filePath = System.getProperty("user.dir") + "\\src\\Common\\";
		File file = new File(filePath + "\\" + fileName); // Create an object of File class to open xlsx file
		FileInputStream inputStream = new FileInputStream(file); // Create an object of FileInputStream class to read
																	// excel file
		Workbook workbook = null;
		String fileExtensionName = fileName.substring(fileName.indexOf(".")); // Find the file extension by splitting
																				// file name in substring and getting
																				// only extension name
		if (fileExtensionName.equals(".xlsx")) { // Check condition if the file is xlsx file
			workbook = new XSSFWorkbook(inputStream); // If it is xlsx file then create object of XSSFWorkbook class
		} // Check condition if the file is xls file
		else if (fileExtensionName.equals(".xls")) {
			workbook = new HSSFWorkbook(inputStream); // If it is xls file then create object of XSSFWorkbook class
		}
		Sheet sheet = workbook.getSheet(sheetName); // Read excel sheet by sheet name
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum(); // Get the current count of rows in excel file
		Row row = sheet.getRow(0); // Get the first row from the sheet
		Row newRow = sheet.createRow(rowCount + 1); // Create a new row and append it at last of sheet
		for (int j = 0; j < row.getLastCellNum(); j++) {// Create a loop over the cell of newly created Row
			// Fill data in row
			Cell cell = newRow.createCell(j);
			cell.setCellValue(dataToWrite[j]);
		}
		inputStream.close(); // Close input stream
		FileOutputStream outputStream = new FileOutputStream(file); // Create an object of FileOutputStream class to
																	// create write data in excel file
		workbook.write(outputStream); // write data in the excel file
		outputStream.close(); // close output stream
	}

	public void GetDataInExcel(String[] results) throws Exception {
		String[] dataToWrite;
		String fileName = "PostMetaData.xlsx";
		String sheetName = "PostMetaData";
		dataToWrite = new String[] { PostTitle, Post_PublishedDate, Post_ModifiedDate };
		System.out.println(results);
		writeExcel(fileName, sheetName, results);
	}

	@Test
	public void postmetadata() throws Exception {
		String[] results = getPost_MetaDate(PostTitle, Post_PublishedDate, Post_ModifiedDate);
		GetDataInExcel(results);
	}

}
