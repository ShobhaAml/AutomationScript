package Frontend;

import java.sql.Connection;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Common.Adminproperty;
import Common.Frontend;

import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Section_modules_database {
	Frontend frontendProperties = new Frontend();
	Adminproperty adminProperties = new Adminproperty();
	String cat_id = "", tag_id = "", query1 = "";
	Statement stmt;
	Properties prop = new Properties();
	ResultSet rs1;
	Connection con;
	String browser = "";
	WebDriver driver;
	String url = "https://guest:guest@testing.xataka.com";
	String title;
	String sec01 = "", sect1db = "";
	String sec02 = "", sect2db = "";

	List<WebElement> section1;
	List<WebElement> section2;

	@BeforeClass
	public void getconnection() throws Exception {
		con = adminProperties.connectDb();
	}

	@Test(priority = 1)
	public void verifySection1Module() throws Exception {

		String getcattag = getSectionModule(1);
		String[] arrgetcattag = getcattag.split(",");
		cat_id = arrgetcattag[0];
		tag_id = arrgetcattag[1];
		sec01 = getResults(cat_id, tag_id);
		System.out.println(sec01);

	}

	@Test(priority = 2)
	public void verifySection2Module() throws Exception {

		String getcattag = getSectionModule(2);
		String[] arrgetcattag = getcattag.split(",");
		cat_id = arrgetcattag[0];
		tag_id = arrgetcattag[1];
		sec02 = getResults(cat_id, tag_id);
		System.out.println(sec02);
	}

	@Test(priority = 3)
	public String getSectionModule(int order) throws Exception {
		query1 = "select tag_id,cat_id from section_module where module_order='" + order + "'";
		stmt = con.createStatement();
		rs1 = stmt.executeQuery(query1);
		while (rs1.next()) {
			tag_id = rs1.getString("tag_id");
			cat_id = rs1.getString("cat_id");

			// System.out.println(tag_id + "====" + cat_id);
		}

		return (cat_id + "," + tag_id);
	}

	public String getResults(String cat_id, String tag_id) throws Exception {

		String test = "";
		if (Integer.parseInt(cat_id) == 0) {

			query1 = "select distinct post_title, id,special_post_priority, post_subtype,post_type  from wp_posts p, wp_post2tag t  where t.tag_id='"
					+ tag_id
					+ "' and p.post_subtype=1 and p.id=t.post_id  and (id not in (select post_id from posts_queue where rank<21) or special_post_priority>4)"
					+ " order by post_date_gmt desc limit 4";

		} else {
			query1 = "select distinct post_title, id, category_id,special_post_priority, post_subtype,post_type  from wp_posts p, wp_post2cat c  where c.category_id='"
					+ cat_id
					+ "' and p.post_subtype=1 and p.id=c.post_id  and (id not in (select post_id from posts_queue where rank<21) or special_post_priority>4)"
					+ " order by post_date_gmt desc limit 4";
		}

		stmt = con.createStatement();
		rs1 = stmt.executeQuery(query1);

		while (rs1.next()) {
			String post_title = rs1.getString("post_title");
			// System.out.println(post_title);

			if (test != "") {
				test = test + "@##@" + post_title;
			} else {
				test = post_title;
			}

		}

		return test;

	}

	@Test(priority = 4)
	public void openbrowser() throws Exception {
		prop = frontendProperties.ReadProperties();
		driver = frontendProperties.frontcallproperty(url, prop.getProperty("browser"));
		browser = prop.getProperty("browser");
		frontendProperties.implicitWait();

		section1 = driver.findElements(By.xpath(prop.getProperty("section_module_first")));
		for (WebElement e : section1) {
			System.out.println(e.getText());

			if (sect1db != "") {
				sect1db = sect1db + "@##@" + e.getText();
			} else {
				sect1db = e.getText();
			}

		}
		System.out.println("Frontend section 1:=== " + sect1db);
		section2 = driver.findElements(By.xpath(prop.getProperty("section_module_sec")));
		for (WebElement e : section2) {
			System.out.println(e.getText());

			if (sect2db != "") {
				sect2db = sect2db + "@##@" + e.getText();
			} else {
				sect2db = e.getText();
			}
		}
		System.out.println("Frontend section 2:====" + sect2db);
	}

	@AfterClass
	public void compareMethod() {
		System.out.println("SECT01: " + sec01);
		System.out.println("sect1db: " + sect1db);

		String[] expected = sec01.split("@##@");
		String[] actual = sect1db.split("@##@");

		System.out.println(expected);

		for (int i = 0; i < expected.length; i++) {
			if (expected[i].equalsIgnoreCase(actual[i])) {
				System.out.println("Matched");

			} else {
				System.out.println("Mismatch");
			}

		}

		System.out.println("SECT02: " + sec02);
		System.out.println("sect2db: " + sect2db);

		String[] expected2 = sec02.split("@##@");
		String[] actual2 = sect2db.split("@##@");

		System.out.println(expected2);

		for (int i = 0; i < expected2.length; i++) {
			if (expected2[i].equalsIgnoreCase(actual2[i])) {
				System.out.println("Matched");

			} else {
				System.out.println("Mismatch");
			}

		}

	}
}