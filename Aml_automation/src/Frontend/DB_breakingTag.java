package Frontend;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import Common.Adminproperty;
import Common.Frontend;

public class DB_breakingTag {
	Frontend frontendProperties = new Frontend();
	Adminproperty adminProperties = new Adminproperty();
	Properties prop = new Properties();
	String browser = "";
	WebDriver driver;
	String url = "https://guest:guest@testing.xataka.com";
	List<String> list = new LinkedList<String>();
	String title;
	Connection conn;
	Statement stmt;

	@BeforeTest
	public void openDB() throws Exception {
		String query = "select * from breaking_news where active=1;";
		conn = adminProperties.connectDb();
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		Statement stmt = conn.createStatement();
		System.out.println("ID " + " Title " + "    Active " + " enddate ");
		while (rs.next()) {
			String ID = rs.getString(1);
			title = rs.getString(2);
			String active = rs.getString(4);
			String endDate = rs.getString(5);
			System.out.println(ID + "  " + title + active + "  " + endDate);
		}
		conn.close();
	}

	@Test
	public void openbrowser() throws Exception {
		prop = frontendProperties.ReadProperties();
		driver = frontendProperties.frontcallproperty(url, prop.getProperty("browser"));
		browser = prop.getProperty("browser");
		frontendProperties.implicitWait();
		if (driver.findElement(By.xpath(prop.getProperty("breakingTag"))).getText().equals(title)) {
			System.out.println("Success");
		} else
			System.out.println("failure");
	}
}
