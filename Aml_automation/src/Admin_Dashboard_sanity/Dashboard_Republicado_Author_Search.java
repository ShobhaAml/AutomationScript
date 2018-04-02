package Admin_Dashboard_sanity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Common.Adminproperty;
import Admin.CheckUserRoles;

public class Dashboard_Republicado_Author_Search {
	WebDriver driver;
	Adminproperty adminProperties = new Adminproperty();
	CheckUserRoles connect = new CheckUserRoles();
	Properties prop = new Properties();
	String filePath = System.getProperty("user.dir") + "\\src\\Common\\";
	List<String> titleList = new ArrayList<String>();
	List<String> contentList = new ArrayList<String>();
	List<String> array = new ArrayList<String>();
	String buttonArray = "";
	int titleSum = 0;
	int contentSum = 0;

	String fileName = "DashboardButton.xlsx";
	File file = new File(filePath + "\\" + fileName);
	String sheetName = "republishFilter";
	String searchText = "sheryl sandberg isha";
	String filterName = "Republicado";
	String authorName = "";
	// String authorName = "UB coordinator";

	@BeforeClass
	public void SetUp() throws Exception {
		prop = adminProperties.ReadProperties();
		driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
		adminProperties.adminLogin();
	}

	@Test
	public void Search() throws Exception {
		setFilter();
		adminProperties.findElement(prop.getProperty("search_field_path")).sendKeys(searchText);
		adminProperties.findElement(prop.getProperty("search_button")).click();
		connect.openConnection(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
		postList();
	}

	public void openOtherTab(String ID) throws InterruptedException {
		((JavascriptExecutor) driver).executeScript("window.open();");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		driver.get("https://guest:guest@testing.xataka.com/p/" + ID);
	}

	public void closeOtherTab() {
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.close();
		driver.switchTo().window(tabs.get(0));
	}

	public void getSearchStatus() throws Exception {
		String[] testString;
		List<WebElement> postListing = adminProperties.findElementsByXpath(prop.getProperty("PostListing"));
		for (int p = 0; p < postListing.size(); p++) {
			String postID = adminProperties.findElement(prop.getProperty("rowsDashboard") + "[" + (p + 1) + "]")
					.getAttribute("id").replace("row-", "");
			buttonArray = getButtons(postID);
			if (adminProperties.findElement(
					prop.getProperty("rowsDashboard") + "[" + (p + 1) + "]" + prop.getProperty("categoryListing"))
					.getText().equalsIgnoreCase("ecommerce") == true) {
				openOtherTab(postID);
				String type = "Ecommerce Post";
				System.out.println("***Post name*** "
						+ adminProperties.findElement(prop.getProperty("ecommercePostTitle")).getText());
				testString = searchPostPage("ecommercePostContent", "ecommercePostTitle", type, buttonArray);
				closeOtherTab();
			} else {
				openOtherTab(postID);
				Thread.sleep(2000);
				String getValue = adminProperties.findElement(prop.getProperty("getClassValue")).getAttribute("class")
						.split(" ")[adminProperties.findElement(prop.getProperty("getClassValue")).getAttribute("class")
								.split(" ").length - 1];
				if (getValue.equalsIgnoreCase("article-normal")) {
					System.out.println(
							"***Post name*** " + adminProperties.findElement(prop.getProperty("postTitle")).getText());
					String type = "Normal Post";
					testString = searchPostPage("postContent", "postTitle", type, buttonArray);
				} else if (getValue.equalsIgnoreCase("article-branded")) {
					System.out.println("***Post name*** "
							+ adminProperties.findElement(prop.getProperty("BarticlePostTitle")).getText());
					String type = "Brand-Article Post";
					testString = searchPostPage("postContent", "BarticlePostTitle", type, buttonArray);
				} else if (getValue.equalsIgnoreCase("article-longform")) {
					System.out.println("***Post name*** "
							+ adminProperties.findElement(prop.getProperty("postTitleLongform")).getText());
					String type = "Longform Post";
					testString = searchPostPage("postContentLongform", "postTitleLongform", type, buttonArray);
				} else if (getValue.equalsIgnoreCase("article-slideshow")) {
					Thread.sleep(2000);
					adminProperties.findElement(prop.getProperty("closeSlide")).click();
					System.out.println(
							"***Post name*** " + adminProperties.findElement(prop.getProperty("postTitle")).getText());
					String type = "Slideshow Post";
					testString = searchPostPage("postContentSlide", "postTitle", type, buttonArray);
				} else if (getValue.equalsIgnoreCase("article-featured")) {
					System.out.println("***Post name*** "
							+ adminProperties.findElement(prop.getProperty("postTitleSpecial")).getText());
					String type = "Special Post";
					testString = searchPostPage("postContent", "postTitleSpecial", type, buttonArray);
				}
				closeOtherTab();
			}
		}
	}

	public void setFilter() {
		Select filter = new Select(adminProperties.findElement(prop.getProperty("select_filter")));
		filter.selectByVisibleText(filterName);
		if (authorName.equalsIgnoreCase(""))
			System.out.println("No author selected");
		else {
			Select author = new Select(adminProperties.findElement(prop.getProperty("authorFilter")));
			author.selectByVisibleText(authorName);
		}
	}

	public void postList() throws Exception {
		List<WebElement> postListing = adminProperties.findElementsByXpath(prop.getProperty("PostListing"));
		System.out.println("No. of search results-->" + postListing.size());
		for (int p = 0; p < postListing.size(); p++)
			System.out.println(postListing.get(p).getText());
		if (postListing.size() > 0)
			getSearchStatus();
		else if (adminProperties.findElement(prop.getProperty("emptySearch")).getText()
				.equalsIgnoreCase("No se ha encontrado el contenido."))
			System.out.println("********Search giving empty posts******");
	}

	public String getButtons(String postID) {
		List<WebElement> buttonXpath = driver
				.findElements(By.xpath(".//*[@id='row-" + postID + "']" + prop.getProperty("buttonXpath")));
		for (int b = 0; b < buttonXpath.size(); b++) {
			String button = buttonXpath.get(b).getText();
			array.add(button);
		}
		String buttonString = StringUtils.join(array);
		array.clear();
		return buttonString;
	}

	public String[] searchPostPage(String postContent, String postTitle, String postType, String button)
			throws IOException, InterruptedException {
		String Status = "";
		int count = 0;
		String[] postTitleArray = adminProperties.findElement(prop.getProperty(postTitle)).getText().split(" ");
		String[] searchArray = searchText.split(" ");
		for (int p = 0; p < postTitleArray.length; p++) {
			for (int h = 0; h < searchArray.length; h++) {
				if ((postTitleArray[p].toLowerCase().contains(searchArray[h]) == true)
						&& (titleList.contains(postTitleArray[p]) != true)) {
					count += 1;
					titleList.add(postTitleArray[p]);
					titleSum += count;
					count = 0;
					Status = "Pass";
				}
			}
		}
		String titleKeywords = StringUtils.join(titleList);
		titleList.clear();
		System.out.println(" Count of matching words in post-title-->>    " + titleSum + "       " + titleKeywords);
		for (int h = 0; h < searchArray.length; h++) {
			String newString1 = searchArray[h].substring(0, 2);
			for (int r = 2; r < searchArray[h].length(); r++) {
				String newString2 = newString1 + searchArray[h].charAt(r);
				newString1 = newString2;
				String[] postContentArray = adminProperties.findElement(prop.getProperty(postContent)).getText()
						.split(" ");
				for (int l = 0; l < postContentArray.length; l++) {
					if (postContentArray[l].toLowerCase().contains(newString2.toLowerCase()) == true)
						if (contentList.contains(postContentArray[l]) != true) {
							{
								count += 1;
								contentList.add(postContentArray[l]);
								contentSum += count;
								count = 0;
								Status = "Pass";
							}
						}
				}
			}
		}
		String contentKeywords = StringUtils.join(contentList);
		contentList.clear();
		System.out.println(" Count of matching words in post-content -->>    " + contentSum);
		System.out.println("");
		String[] excelString = new String[] { connect.blogrole,
				adminProperties.findElement(prop.getProperty(postTitle)).getText(), connect.blogroleName,
				connect.Authorname, postType, button, titleKeywords, contentKeywords, Integer.toString(titleSum),
				Integer.toString(contentSum), Status };
		adminProperties.writeExcel(fileName, sheetName, excelString);
		contentSum = Integer.parseInt(Integer.toString(contentSum));
		contentSum = 0;
		titleSum = Integer.parseInt(Integer.toString(titleSum));
		titleSum = 0;
		return excelString;
	}
}
