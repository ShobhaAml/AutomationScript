package Admin;

import java.util.Properties;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class DraftActions {
	WebDriver driver;
	Adminproperty adminProperties = new Adminproperty();
	Properties prop = new Properties();
	String post_title = "pw longform post";
	String listtype = "Dashboardlist";
	String Selectors = "xatakandroid,xatakamovil";
	String tittle_data = "En infinidad de ocasiones hemos comentado cómo influyen las modas en los entrenamientos";
	String FurturePostname = "Tacos de pescado al chipotle. Receta mexicana fácil";
	String fbText = "fb testing";
	String twitterText = "";
	String DateTime = "27/12/2027 12:27";
	String FuturePostDate = "12:30,21/12/2026";
	String catagoryText = "Especiales";
	String tag = "Encuesta";

	public void Setup() throws Exception {
		prop = adminProperties.ReadProperties();
		driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
		adminProperties.adminLogin();
	}

	@Parameters({ "posttitle" })
	@Test
	public void draft(String posttitle) throws InterruptedException {
		Thread.sleep(6000);
		adminProperties.pickDraft(posttitle, listtype, "DashboardEditforotherpost", "1");
		adminProperties.dialogBoxOk();
	}

	@Parameters({ "posttitle" })
	@Test
	public void futurePost(String posttitle) throws Exception {
		Setup();
		Thread.sleep(10000);
		adminProperties.pickDraft(posttitle, listtype, "DashboardDraftEcomtd", "2");
		Thread.sleep(5000);
		adminProperties.findAndClick("publish_tab");
		adminProperties.findElement(prop.getProperty("future_publish_date")).clear();
		adminProperties.findAndWrite("future_publish_date", DateTime);
		WebElement categoryBox = adminProperties.findElement(prop.getProperty("Catagory_click"));
		String tagBox = adminProperties.findElement(prop.getProperty("tag_input")).getText();
		String fbText = adminProperties.findElement(prop.getProperty("fb_text")).getText();
		if (((categoryBox.isSelected()) == false) || ((tagBox.isEmpty()) == false) || (fbText.isEmpty()) == false) {
			adminProperties.implicitWait();
			adminProperties.insertTagAndCategory(catagoryText, tag);
			adminProperties.addFbTwitterText("fb_text", "");
		}
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(0,-250)", "");
		adminProperties.findAndClick("future_post_button");
		if (adminProperties.findElement(prop.getProperty("futureValidation")).getText()
				.contains("Tu post está programado")) {
			System.out.println(adminProperties
					.findElement("Message for future post:- " + prop.getProperty("futureValidation")).getText());
		}

	}

	@Test(enabled = false)
	public void repost() throws Exception {
		adminProperties.clickButton("DashboardEditbuttontr", "DashboardDraftReptd", post_title, listtype);
		adminProperties.implicitWait();
		Thread.sleep(3000);
		adminProperties.repostCheckbox(Selectors);
		adminProperties.implicitWait();
		adminProperties.findAndClick("repost_post_button");
		Thread.sleep(3000);
		// adminProperties.findAndClick("clear_repost_popup");
		driver.navigate().to("https://testing.xatakafoto.com/admin");
		adminProperties.adminLogin();
		adminProperties.implicitWait();
		Thread.sleep(5000);
		driver.navigate().refresh();
		driver.navigate().refresh();
		adminProperties.implicitWait();
		adminProperties.findAndClick("notification_button");
		adminProperties.implicitWait();
		adminProperties.clickNotificationButton(tittle_data);
	}

	@Test(enabled = false)
	public void futurePostDashboard() {
		adminProperties.clickButton("DashboardEditbuttontr", "Dashboard_future", FurturePostname, "Dashboardlist");
		adminProperties.implicitWait();
		adminProperties.futurePostDashborad(FuturePostDate);
	}

}
