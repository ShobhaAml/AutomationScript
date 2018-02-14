package Admin_Dashboard_sanity;

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

import Admin.CheckUserRoles;

public class republicadoButtonsFilterSanity {
        WebDriver driver;
		Adminproperty adminProperties = new Adminproperty();
		CheckUserRoles roleType = new CheckUserRoles();
		Properties prop = new Properties();
		String blogname, browser, button, id;
		List<String> singleButtons = new ArrayList<String>(Arrays.asList("Editar"));
		List<String> commonButtons = new ArrayList<String>(Arrays.asList("Editar", "Borrar"));//Unbrand collaborator,Brand coordinator, Brand collaborator
		List<String> brandButtons = new ArrayList<String>(Arrays.asList("Editar", "Borrar", "Destacar"));//Unbrand Coordinator
		List<String> editorcorButtons = new ArrayList<String>(Arrays.asList("Repost","Editar", "Borrar", "Destacar"));
		@BeforeClass
		public void Setup() throws Exception {
		prop = adminProperties.ReadProperties();
		driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
		browser = prop.getProperty("browser");
		adminProperties.adminLogin();
		}

		@Test
		public void draft() throws Exception {
		Select status2 = new Select(adminProperties.findElement(".//*[@id='postStatus']"));
		status2.selectByVisibleText("Republicado");
		roleType.openConnection(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
		System.out.println("authorname and blogrole is:-----" + roleType.Authorname + "------"+roleType.blogrole);
		buttonStatus(roleType.blogrole);
		}

		public void buttonStatus(String role) throws Exception {
		List<WebElement> list = adminProperties.findElementsByXpath(prop.getProperty("postListXpath"));
		if (list.size() > 0) {
		for (int i = 0; i < list.size(); i++) {
		String postID = adminProperties.findElement(prop.getProperty("idXpath") + (i + 1) + "]").getAttribute("id").replace("row-", "");
		String postTitle =adminProperties.findElement(prop.getProperty("postListXpath") + "["+ (i + 1) + "]"+prop.getProperty("titleXpath")).getText();
		String postTypeStatus = adminProperties.getID(postID);				

		if (role.equalsIgnoreCase("Unbranded collaboraor") && (postTypeStatus.equalsIgnoreCase("normal"))) {					
	    if (driver.findElement(By.xpath(".//*[@id='row-" + postID + "']/td[3]/a")).getText().equalsIgnoreCase(roleType.Authorname)) {						
		List<WebElement> UColButtons = driver.findElements(By.xpath(".//*[@id='row-" + postID + "']" +prop.getProperty("buttonXpath")));
		System.out.println("postID is-->>" + postID + "   postTitle is-->>" + postTitle	+ "   postStatus is-->>" + postTypeStatus +button(commonButtons, UColButtons));
	    } else {
		if (driver.findElement(By.xpath(".//*[@id='row-" + postID + "']/td[6]")).getAttribute("class").contentEquals("") == false)
		System.out.println("postID is-->>" + postID + "   postType is-->>" + postTypeStatus	+ "   postTitle is-->>" +postTitle + "    <<-----No action buttons---->>");
		}}
		if (role.equalsIgnoreCase("Unbranded Coordinator")) {
		if (postTypeStatus.equalsIgnoreCase("normal")) {
		List<WebElement> UCorButtons = driver.findElements(By.xpath(".//*[@id='row-" + postID + "']" +prop.getProperty("buttonXpath")));
		System.out.println("postID is-->>" + postID + "   postTitle is-->>" + postTitle	+ "   postStatus is-->>" + postTypeStatus +button(brandButtons, UCorButtons));			
		}
		if (postTypeStatus.equalsIgnoreCase("Club")) {
		if (driver.findElement(By.xpath(".//*[@id='row-" + postID + "']/td[6]")).getAttribute("class").contentEquals(" ") == false)
		System.out.println("postID is-->>" + postID + "   postType is-->>" + postTypeStatus	+ "   postTitle is-->>" +postTitle + "    <<-----No action buttons for club post---->>");
		}}
	    if (role.equalsIgnoreCase("Branded Collaborator") && (postTypeStatus.equalsIgnoreCase("Club"))&& (driver.findElement(By.xpath(".//*[@id='row-" + postID + "']/td[3]/a")).getText().equalsIgnoreCase(roleType.Authorname))) {List<WebElement> BColButtons = driver.findElements(By.xpath(".//*[@id='row-" + postID + "']" +prop.getProperty("buttonXpath")));
		System.out.println("postID is-->>" + postID + "   postTitle is-->>" + postTitle	+ "   postStatus is-->>" + postTypeStatus +button(singleButtons, BColButtons));			
		}
		if (role.equalsIgnoreCase("Branded Coordinator") && (postTypeStatus.equalsIgnoreCase("Club"))) {
		List<WebElement> BCorButtons = driver.findElements(By.xpath(".//*[@id='row-"+postID+"']" +prop.getProperty("buttonXpath")));
		System.out.println("postID is-->>" + postID + "   postTitle is-->>" + postTitle	+ "   postStatus is-->>" + postTypeStatus +button(brandButtons, BCorButtons));			
		}
		if (role.equalsIgnoreCase("Editor")) {
		if (postTypeStatus.equalsIgnoreCase("normal")) {
		List<WebElement> UCorButtons = driver.findElements(By.xpath(".//*[@id='row-"+postID+"']" +prop.getProperty("buttonXpath")));
		System.out.println("postID is-->>" + postID + "   postTitle is-->>" + postTitle	+ "   postStatus is-->>" + postTypeStatus +button(editorcorButtons, UCorButtons));			
						}
					}
				}
			}	
		}
		public String button(List<String> buttonArray, List<WebElement> buttonList) throws Exception {
			List<String> array = new ArrayList<String>();
		String	status="";
			for (int b = 0; b < buttonList.size(); b++) {
				button = buttonList.get(b).getText();
				array.add(button);
			}
					if (array.equals(buttonArray))
						 status = "  ****  "+  array+" ****  <<-------Pass----->>";
					else
						status = "  ****  "+array+"  ****   <<-------Fail----->>";
								return status;
			
		}
	}