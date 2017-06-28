package Frontend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Common.Frontend;

public class DB_breakingTag {

	Frontend frontendProperties = new Frontend();
	String browser = "";
	WebDriver driver;
	Properties prop = new Properties();
	String url = "https://guest:guest@testing.xataka.com";
	String homePage, postPage;
	List<String> list = new LinkedList<String>();
	String title;

	@BeforeTest
	public void method_isha() throws ClassNotFoundException, SQLException {

		// Connection URL Syntax: "jdbc:mysql://ipaddress:portnumber/db_name"
		String dbUrl = "jdbc:mysql://localhost:3306/compradiccion";
		// Database Username
		String username = "root";

		// Database Password
		String password = "";
		// Query to Execute
		String query = "select * from breaking_news where active=1;";

		// Load mysql jdbc driver
		Class.forName("com.mysql.jdbc.Driver");

		// Create Connection to DB
		Connection con = DriverManager.getConnection(dbUrl, username, password);

		// Create Statement Object
		Statement stmt = con.createStatement();

		// Execute the SQL Query. Store results in ResultSet
		ResultSet rs = stmt.executeQuery(query);
		System.out.println("ID " + " Title " + "    Active " + " enddate ");

		// While Loop to iterate through all data and print results
		while (rs.next()) {
			String ID = rs.getString(1);
			title = rs.getString(2);
			String active = rs.getString(4);
			String endDate = rs.getString(5);
			System.out.println(ID + "  " + title + active + "  " + endDate);
		}
		// closing DB Connection
		con.close();
	}

	@Test
	public void openbrowser() throws Exception {
		prop = frontendProperties.ReadProperties();
		driver = frontendProperties.frontcallproperty(url, prop.getProperty("browser"));
		browser = prop.getProperty("browser");
		frontendProperties.implicitWait();
		if (driver.findElement(By.xpath(prop.getProperty("breakingTag"))).getText().equals(title)) {
			System.out.println("Success");
		}
	}
}
