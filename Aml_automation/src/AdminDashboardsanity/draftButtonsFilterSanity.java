package AdminDashboardsanity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Common.Adminproperty;
import Admin.CheckUserRoles;

public class draftButtonsFilterSanity {

	WebDriver driver;
	String blogname, browser, button, id, author_array;
	String[] author_role;
	Adminproperty adminProperties = new Adminproperty();
	CheckUserRoles roleType = new CheckUserRoles();
	Properties prop = new Properties();
	List<String> buttonList = new ArrayList<String>(Arrays.asList("Editar", "Borrar"));

	@BeforeClass
	public void Setup() throws Exception {
		prop = adminProperties.ReadProperties();
		driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
		browser = prop.getProperty("browser");
		adminProperties.adminLogin();
		author_array = adminProperties.fetch_Role_Author();
		author_role = author_array.split("#");
		System.out.println(
				"*----------Borrador filter-------* \n" + "Role-type : ->    " + author_role[1] + "   Author-name :->  " + author_role[0]);
	}

	@Test
	public void open_borrador_filter() throws Exception {
		Select status2 = new Select(adminProperties.findElement(".//*[@id='postStatus']"));
		status2.selectByValue("2");
		Thread.sleep(2000);
		fetch_button_Status(author_role[1]);
	}

	public void fetch_button_Status(String role) throws Exception {
		List<WebElement> post_list = adminProperties.findElementsByXpath(prop.getProperty("listCountXpath"));
		System.out.println(post_list.size());
		if (post_list.size() > 0) {
			for (int i = 1; i <= post_list.size(); i++) {
				String postID = driver.findElement(By.xpath(".//tbody[@id='posts_list']/tr[" + i + "]"))
						.getAttribute("id").replace("row-", "");
				String postTitle = adminProperties
						.findElement(
								prop.getProperty("postListXpath") + "[" + (i) + "]" + prop.getProperty("titleXpath"))
						.getText();
				String postTypeStatus = adminProperties.getID(postID);

				switch (role) {
				case "admin":
					display_status(postID, postTitle, postTypeStatus, buttonList);
					break;
				case "Editor":
					if (postTypeStatus.equalsIgnoreCase("normal"))
						display_status(postID, postTitle, postTypeStatus, buttonList);
					break;
				case "UbCol":
					switch (postTypeStatus) {
					case "normal":
						if (driver.findElement(By.xpath(".//*[@id='row-" + postID + "']/td[3]/a")).getText()
								.equalsIgnoreCase(author_role[0]))
							display_status(postID, postTitle, postTypeStatus, buttonList);
						break;
					}
					break;
				case "UBC":
				case "Dir":
					switch (postTypeStatus) {
					case "normal":
						display_status(postID, postTitle, postTypeStatus, buttonList);
						break;
					case "Club":
						if (driver.findElement(By.xpath(".//*[@id='row-" + postID + "']/td[6]")).getAttribute("class")
								.contentEquals(" ") == false)
							System.out.println("post-ID:->" + postID + "\npost_title:" + postTitle + "\npost-type:"
									+ postTypeStatus + "\n*-------No action buttons------*\n");
						break;
					}
					break;
				case "Bcol":
					if (driver.findElement(By.xpath(".//*[@id='row-" + postID + "']/td[3]/a")).getText()
							.equalsIgnoreCase(author_role[0]))
						display_status(postID, postTitle, postTypeStatus, buttonList);
					break;
				case "BC":
					display_status(postID, postTitle, postTypeStatus, buttonList);
					break;
				}
			}
		}
	}

	public String compareButtons(List<String> buttonArray, List<WebElement> buttonList) throws Exception {
		List<String> array = new ArrayList<String>();
		String status = "";
		for (int b = 0; b < buttonList.size(); b++) {
			button = buttonList.get(b).getText();
			array.add(button);
		}

		if (array.equals(buttonArray))
			status = array + "\n *---Status:- Pass---*\n";
		else
			status = array + "\n*---Status: Fail---*\n";
		return status;
	}

	public void display_status(String id, String title, String type, List<String> expected_btns) throws Exception {
		List<WebElement> actual_btns = driver
				.findElements(By.xpath(".//*[@id='row-" + id + "']" + prop.getProperty("buttonXpath")));
		System.out.println("post-ID:->" + id + "\npost-Title:  " + title + "\npost_type:" + type + "\nbuttons:"
				+ compareButtons(expected_btns, actual_btns));
	}
}