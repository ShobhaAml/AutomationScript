package Admin;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class Create_Roles_Excel {

	WebDriver driver;
	Adminproperty adminProperties = new Adminproperty();
	Properties prop = new Properties();
	String browser = "";
	String Userdata, user, username, userrole, userpassword, useremail, usertwitter, usernormalpost, userspecialpost,
			userdescription;

	@BeforeMethod
	public void Setup() throws Exception {
		prop = adminProperties.ReadProperties();
		driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
		browser = prop.getProperty("browser");

		adminProperties.LoginAdmin(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
		adminProperties.findAndClick("navigation_header");
		adminProperties.findAndClick("navigate_usuarios");
		adminProperties.findAndClick("create_User");
	}

	@DataProvider(name = "testdata")
	public Object[][] TestDataFeed() throws Exception {
		Object[][] userdata = adminProperties.readExcel_roleCreation("NewUser", 10);
		return userdata;
	}

	@Test(dataProvider = "testdata")
	public void createUser(String user, String username, String userrole, String userpassword, String useremail,
			String usertwitter, String usernormalpost, String userspecialpost, String userdescription, String Run)
			throws Exception {

		if (Run.trim().equalsIgnoreCase("Y")) {
			if (userrole.equalsIgnoreCase("Lead Editor")) {

				adminProperties.findAndWrite("user_fullname", user);
				adminProperties.findAndWrite("user_name", username);
				// adminProperties.findAndWrite("user_role", userrole);

				adminProperties.findAndClick("user_role");
				Select select = new Select(driver.findElement(By.xpath("//*[@id=\"editor-blogrole\"]")));
				select.selectByValue("Lead Editor");

				enter_data(userpassword, useremail, usertwitter, usernormalpost, userspecialpost, userdescription);
			}

			if (userrole.equalsIgnoreCase("Editor Senior")) {

				adminProperties.findAndWrite("user_fullname", user);
				adminProperties.findAndWrite("user_name", username);

				adminProperties.findAndClick("user_role");
				Select select = new Select(driver.findElement(By.xpath("//*[@id=\"editor-blogrole\"]")));
				select.selectByValue("Editor Senior");

				enter_data(userpassword, useremail, usertwitter, usernormalpost, userspecialpost, userdescription);
			}

			if (userrole.equalsIgnoreCase("Editor")) {

				adminProperties.findAndWrite("user_fullname", user);
				adminProperties.findAndWrite("user_name", username);

				adminProperties.findAndClick("user_role");
				Select select = new Select(driver.findElement(By.xpath("//*[@id=\"editor-blogrole\"]")));
				select.selectByValue("Editor");

				enter_data(userpassword, useremail, usertwitter, usernormalpost, userspecialpost, userdescription);
			}

			if (userrole.equalsIgnoreCase("Collaborator")) {

				adminProperties.findAndWrite("user_fullname", user);
				adminProperties.findAndWrite("user_name", username);

				adminProperties.findAndClick("user_role");
				Select select = new Select(driver.findElement(By.xpath("//*[@id=\"editor-blogrole\"]")));
				select.selectByValue("Collaborator");

				enter_data(userpassword, useremail, usertwitter, usernormalpost, userspecialpost, userdescription);
			}

			if (userrole.equalsIgnoreCase("Coordinator")) {

				adminProperties.findAndWrite("user_fullname", user);
				adminProperties.findAndWrite("user_name", username);

				adminProperties.findAndClick("user_role");
				Select select = new Select(driver.findElement(By.xpath("//*[@id=\"editor-blogrole\"]")));
				select.selectByValue("Coordinator");

				enter_data(userpassword, useremail, usertwitter, usernormalpost, userspecialpost, userdescription);
			}

			if (userrole.equalsIgnoreCase("Director")) {

				adminProperties.findAndWrite("user_fullname", user);
				adminProperties.findAndWrite("user_name", username);

				adminProperties.findAndClick("user_role");
				Select select = new Select(driver.findElement(By.xpath("//*[@id=\"editor-blogrole\"]")));
				select.selectByValue("Director");

				enter_data(userpassword, useremail, usertwitter, usernormalpost, userspecialpost, userdescription);
			}

			if (userrole.equalsIgnoreCase("Branded Coordinator")) {

				adminProperties.findAndWrite("user_fullname", user);
				adminProperties.findAndWrite("user_name", username);

				adminProperties.findAndClick("user_role");
				Select select = new Select(driver.findElement(By.xpath("//*[@id=\"editor-blogrole\"]")));
				select.selectByValue("Branded Coordinator");

				enter_data(userpassword, useremail, usertwitter, usernormalpost, userspecialpost, userdescription);
			}

			if (userrole.equalsIgnoreCase("Branded Collaborator")) {

				adminProperties.findAndWrite("user_fullname", user);
				adminProperties.findAndWrite("user_name", username);

				adminProperties.findAndClick("user_role");
				Select select = new Select(driver.findElement(By.xpath("//*[@id=\"editor-blogrole\"]")));
				select.selectByValue("Branded Collaborator");

				enter_data(userpassword, useremail, usertwitter, usernormalpost, userspecialpost, userdescription);
			}

			System.out.println("User=" + username + " of role=" + userrole + " " + "has been created");

		}

		else {
			System.out.println("Column 'Run' is not YES in excel sheet");
		}
	}

	public void enter_data(String userpassword, String useremail, String usertwitter, String usernormalpost,
			String userspecialpost, String userdescription) {
		adminProperties.findAndWrite("user_pass", userpassword);
		adminProperties.findAndWrite("user_email", useremail);
		adminProperties.findAndWrite("user_twt", usertwitter);
		adminProperties.findAndWrite("user_normalpost", usernormalpost);
		adminProperties.findAndWrite("user_specialpost", userspecialpost);
		adminProperties.findAndWrite("user_desc", userdescription);
		adminProperties.findAndClick("SaveUser_btn");
	}
}
