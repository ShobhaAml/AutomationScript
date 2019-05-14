package Frontend.respuestas;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import Common.Frontend;

public class respuestasComment
{

	String logintype = "Std"; // Standard= Std , Fb=fb, twitter=twitter
	String username = "sumit@agilemedialab.in";
	String password = "qwerty011";
	String usersession = "2"; // 1-homepage, 2- login from post
	String comment = "automate A por el tercer intento de abc sdasd new test new 3434 ";

	Properties prop = new Properties();
	Frontend frontendProperties = new Frontend();
	String browser = "", url = "";
	WebDriver driver;

	@Test(priority = 1)
	public void openbrowser() throws Exception {
		prop = frontendProperties.ReadProperties();
		driver = frontendProperties.frontcallproperty(prop.getProperty("url"), prop.getProperty("browser"));
		browser = prop.getProperty("browser");
		url = prop.getProperty("url");
	}

	@Test(priority = 2, dependsOnMethods = "openbrowser")
	public void calllogin() {

		if (usersession == "2") {
			frontendProperties.findAndClick("cookie");
		} else {
			//usersession = frontendProperties.login(username, password, url, usersession, logintype);
		}
	}

	@Test(priority = 3, dependsOnMethods = "calllogin")
	public void MovetoRespuestaComment() {
		// frontendProperties.implicitWait();
		// Click 1st post
		frontendProperties.findAndClick("Menu");
		frontendProperties.implicitWait();
		frontendProperties.findAndClick("respuesta");
		frontendProperties.implicitWait();
		frontendProperties.findAndClick("respestasLink");
		if (usersession == "2") {
			// login
			frontendProperties.findAndClick("regístrateCommentEntra");
			//frontendProperties.login(username, password, url, usersession, logintype);

			usersession = frontendProperties.findElement(prop.getProperty("resCommentloogeduser")).getText();
			System.out.println(usersession + " successfully logged in from post page");
		}
	}

	@Test(priority = 4, dependsOnMethods = "MovetoRespuestaComment")
	public void addRcomments() {
		System.out.println("Let's add Comment");

		frontendProperties.addRescomments(comment);
		System.out.println(" Comment has been added");

	}
}
