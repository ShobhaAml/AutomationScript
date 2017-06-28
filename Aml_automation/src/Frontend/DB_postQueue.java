package Frontend;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import Common.Adminproperty;
import Common.Frontend;


public class DB_postQueue {
	Frontend frontendProperties = new Frontend();
	Adminproperty adminProperties = new Adminproperty();
	Properties prop = new Properties();
	String browser = "";
	WebDriver driver;
	String url = "https://guest:guest@testing.xataka.com";
	String title;
	List<String> list = new LinkedList<String>();
	Connection conn;
	Statement stmt;
	List<String> DB_postList = new LinkedList<String>();
	List<String> homeList = new LinkedList<String>();

	@Test
	public void openDB() throws Exception {
		String query = "select ID, post_title, post_date_gmt from wp_posts ORDER BY post_date_gmt desc limit 4";
		conn = adminProperties.connectDb();
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		System.out.println("ID " + "  " + " Title " + "        " + "    post_date_gmt ");
		while (rs.next()) {
			String ID = rs.getString(1);
			title = rs.getString(2);
			String date = rs.getString(3);
			System.out.println(ID + "" + title + "" + date);
			DB_postList.add(title);
		}
		System.out.println("\n" + DB_postList);
		conn.close();
	}

	@Test
	public void openbrowser() throws Exception {
		prop = frontendProperties.ReadProperties();
		driver = frontendProperties.frontcallproperty(url, prop.getProperty("browser"));
		browser = prop.getProperty("browser");
		frontendProperties.implicitWait();
		java.util.List<WebElement> list = driver.findElements(By.xpath(prop.getProperty("postcommentlink")));
		for (WebElement post : list) {
			String postList = post.getText();
			homeList.add(postList);
		}
		System.out.println("\n" + homeList + "\n");
		if (DB_postList.equals(homeList)) {
			System.out.println("success");
		} else
			System.out.println("failure");
	}

}
