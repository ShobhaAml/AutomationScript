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

public class republicadoButtonsFilterSanity {
	WebDriver driver;
	Adminproperty adminProperties = new Adminproperty();
	CheckUserRoles roleType = new CheckUserRoles();
	Properties prop = new Properties();
	String blogname, browser, button, id, author_array;
	List<String> single_Buttons = new ArrayList<String>(Arrays.asList("Editar")); // Unbrand collaborator
	List<String> bcor_buttons = new ArrayList<String>(Arrays.asList("Repost", "Editar", "Quitar de portad"));
	List<String> bcol_buttons = new ArrayList<String>(Arrays.asList("Editar", "Quitar de portad"));// Brand collaborator,Ecommerce btns
	List<String> all_Buttons = new ArrayList<String>(Arrays.asList("Repost", "Editar", "Quitar de portad", "Destacar"));// Admin, Unbrandm Coordinator
	String[] author_role;

	@BeforeClass
	public void Setup() throws Exception {
		prop = adminProperties.ReadProperties();
		driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
		browser = prop.getProperty("browser");
		adminProperties.adminLogin();
		author_array = adminProperties.fetch_Role_Author();
		author_role = author_array.split("#");
		System.out.println("Republicado filter \n"+"Role-type : ->    "+ author_role[1]+ "   Author-name :->  " + author_role[0]);
	}

	@Test
	public void open_republicado_filter() throws Exception {
		Select status2 = new Select(adminProperties.findElement(".//*[@id='postStatus']"));
		status2.selectByVisibleText("Republicado");
		fetch_button_Status(author_role[1]);

	}

	public void fetch_button_Status(String role) throws Exception {
		List<WebElement> post_list = adminProperties.findElementsByXpath(prop.getProperty("listCountXpath"));
		System.out.println(post_list.size());
		if (post_list.size() > 0) {
			for (int i = 1; i <= post_list.size(); i++) {
				
				
				String postID = adminProperties.findElement(".//tr[" + i + "]" + prop.getProperty("idXpath"))
						.getAttribute("id").replace("row-", "");
				String postTitle = adminProperties
						.findElement(
								prop.getProperty("postListXpath") + "[" + (i) + "]" + prop.getProperty("titleXpath"))
						.getText();

				String postTypeStatus = adminProperties.getID(postID);

				switch (role) {
				case "admin":
					switch (postTypeStatus) {
					case "normal":
						if (adminProperties
								.findElement(prop.getProperty("rowsDashboard") + "[" + (i) + "]"
										+ prop.getProperty("categoryListing"))
								.getText().equalsIgnoreCase("Ecommerce") == true)
							display_status(postID, postTitle, postTypeStatus, bcol_buttons);
						else
							display_status(postID, postTitle, postTypeStatus, all_Buttons);

						break;
					case "club":
						display_status(postID, postTitle, postTypeStatus, bcor_buttons);
						break;
					}
					break; 

				case "Editor":
					switch (postTypeStatus) {
					case "normal":
						if (adminProperties
								.findElement(prop.getProperty("rowsDashboard") + "[" + (i) + "]"
										+ prop.getProperty("categoryListing"))
								.getText().equalsIgnoreCase("Ecommerce") == true)
							display_status(postID, postTitle, postTypeStatus, bcol_buttons);
						else
							display_status(postID, postTitle, postTypeStatus, all_Buttons);
						break;
					case "club":
						if (driver.findElement(By.xpath(".//*[@id='row-" + postID + "']/td[6]")).getAttribute("class")
								.contentEquals(" ") == false) {
							System.out.println("\npost-ID:" + postID + "\npost_title:" + postTitle + "\npost_type:-->"
									+ postTypeStatus + "\n*-----No action buttons for club post----*\n");
						}
						break;
					}
					break;
				case "UbCol":
					if ((postTypeStatus.equalsIgnoreCase("normal"))) {
						if (driver.findElement(By.xpath(".//*[@id='row-" + postID + "']/td[3]/a")).getText()
								.equalsIgnoreCase(author_role[0]))
							display_status(postID, postTitle, postTypeStatus, single_Buttons);
					} else {
						if (driver.findElement(By.xpath(".//*[@id='row-" + postID + "']/td[6]")).getAttribute("class")
								.contentEquals("") == false)
							System.out.println("post-ID:->" + postID + "\npost_title:" + postTitle + "\npost-type:"
									+ postTypeStatus + "\n*-------No action buttons------*\n");
					}

					break;
				case "Bcol":
					if (driver.findElement(By.xpath(".//*[@id='row-" + postID + "']/td[3]/a")).getText()
							.equalsIgnoreCase(author_role[0]))
						display_status(postID, postTitle, postTypeStatus, bcol_buttons);
					break;
				case "BC":
					  display_status(postID, postTitle, postTypeStatus, bcor_buttons);
					break;

				case "UBC":
				case "Dir":
					switch (postTypeStatus) {
					case "normal":
						if (adminProperties
								.findElement(prop.getProperty("rowsDashboard") + "[" + (i) + "]"
										+ prop.getProperty("categoryListing"))
								.getText().equalsIgnoreCase("Ecommerce") == true)
							display_status(postID, postTitle, postTypeStatus, bcol_buttons);
						else
							display_status(postID, postTitle, postTypeStatus, all_Buttons);
						break;
					case "club":
						if (driver.findElement(By.xpath(".//*[@id='row-" + postID + "']/td[6]")).getAttribute("class")
								.contentEquals(" ") == false) {
							System.out.println("\npost-ID:" + postID + "\npost_title:" + postTitle + "\npost_type:-->"
									+ postTypeStatus + "\n*-------No action buttons for club post-----*\n");
						}
						break;
					}
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