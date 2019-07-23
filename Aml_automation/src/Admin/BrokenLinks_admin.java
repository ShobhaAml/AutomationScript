package Admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Common.Adminproperty;

public class BrokenLinks_admin {
	WebDriver driver;
	String[] arrlinks;
	String sitelinks = "";
	Adminproperty adminProperties = new Adminproperty();
	Properties prop = new Properties();
	String server, blogname, path, device, testserver, browser;

   @BeforeMethod
	public void Setup() throws Exception {
		prop = adminProperties.ReadProperties();
		//driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
		//browser = prop.getProperty("browser");
		driver = adminProperties.headlessBrowser_admin(prop.getProperty("url"));
		adminProperties.LoginAdmin(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
	}

	@Test
	public void verfiyLinks() throws Exception {

		List<WebElement> anchorTagsList = driver.findElements(By.tagName("a"));
		System.out.println("Total no. of links are " + anchorTagsList.size());
		for (WebElement anchorTagElement : anchorTagsList) {
			if (anchorTagElement != null) {
				String url = anchorTagElement.getAttribute("href");
				if ((url != null && !url.contains("javascript") && (!url.contains("#")))) {
					if (url.contains("testing.") || url.contains("test.") || (url.contains("testadmin."))) {
						{
							if (url.contains("test.")) {
								url = url.replace("test.", "guest:guest@test.");
							} else if (url.contains("testing.")) {
								url = url.replace("testing.", "guest:guest@testing.");
							} else if (url.contains("mtest.")) {
								url = url.replace("mtest.", "guest:guest@mtest.");
							}
						}
						verifyURLStatus(url);
					}
				}
			}
		}
		driver.close();

		VerifyInternalPages(prop.getProperty("url") + "/newposts/new");
		VerifyInternalPages(prop.getProperty("url") + "/slideposts/new");
		VerifyInternalPages(prop.getProperty("url") + "/videoposts/new");
		VerifyInternalPages(prop.getProperty("url") + "/manage_posts");
		VerifyInternalPages(prop.getProperty("url") + "/manage_comments");
		VerifyInternalPages(prop.getProperty("url") + "/manage_categories");
		VerifyInternalPages(prop.getProperty("url") + "/manage_tags");
		VerifyInternalPages(prop.getProperty("url") + "/events");
		VerifyInternalPages(prop.getProperty("url") + "/manage_respuestas");
		VerifyInternalPages(prop.getProperty("url") + "/manage_videos");
		VerifyInternalPages(prop.getProperty("url") + "/poststats");
		VerifyInternalPages(prop.getProperty("url") + "/manage_breaking_news");
		VerifyInternalPages(prop.getProperty("url") + "/manage_section_module");
		VerifyInternalPages(prop.getProperty("url") + "/manage_blog_users");
		VerifyInternalPages(prop.getProperty("url") + "/blog_info");
		VerifyInternalPages(prop.getProperty("url") + "/clubcampaigns");
		VerifyInternalPages(prop.getProperty("url") + "/clublinks");
		VerifyInternalPages(prop.getProperty("url") + "/manage_users");
		VerifyInternalPages(prop.getProperty("url") + "/invalid_routes");
		VerifyInternalPages(prop.getProperty("url") + "/widgets");

		VerifyInternalPages_LFE(prop.getProperty("url"));
		VerifyInternalPages_brandedLFE(prop.getProperty("url"));
		VerifyInternalPages_MVP(prop.getProperty("url"));
		VerifyInternalPages_brandedMVP(prop.getProperty("url"));
	}

	public void VerifyInternalPages(String url) throws Exception {
		System.out.println("********* Verifing Internal Pages  " + url + "*******");
		//driver = adminProperties.callproperty(url, prop.getProperty("browser"));
		driver = adminProperties.headlessBrowser_admin(prop.getProperty("url"));
		adminProperties.LoginAdmin(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
		List<WebElement> anchorTagsList = driver.findElements(By.tagName("a"));
		System.out.println("Total no. of links are " + anchorTagsList.size());
		for (WebElement anchorTagElement : anchorTagsList) {
			if (anchorTagElement != null) {
				String url1 = anchorTagElement.getAttribute("href");

				if ((url1 != null && !url1.contains("javascript") && (!url1.contains("#")))) {
					if (url1.contains("testing.") || url1.contains("test.") || (url1.contains("testadmin."))) {
						if (url.contains("test.")) {
							url1 = url1.replace("test.", "guest:guest@test.");
						} else if (url1.contains("testing.")) {
							url1 = url1.replace("testing.", "guest:guest@testing.");
						} else if (url1.contains("mtest.")) {
							url1 = url1.replace("mtest.", "guest:guest@mtest.");
						}
					}
					verifyURLStatus(url1);

				}
			}
		}
		driver.close();
	}

	public void VerifyInternalPages_LFE(String url) throws Exception {
		System.out.println("********* Verifing Internal Pages_LongformPost *******");
		//driver = adminProperties.callproperty(url, prop.getProperty("browser"));
		driver = adminProperties.headlessBrowser_admin(prop.getProperty("url"));
		adminProperties.LoginAdmin(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
		adminProperties.findAndClick("navigation_header");
		adminProperties.findAndClick("create_normallongform_link");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		adminProperties.implicitWait();
		List<WebElement> anchorTagsList = driver.findElements(By.tagName("link"));
		System.out.println("Total no. of links are " + anchorTagsList.size());
		for (WebElement anchorTagElement : anchorTagsList) {
			if (anchorTagElement != null) {
				String url2 = anchorTagElement.getAttribute("href");
				if ((url2 != null && !url2.contains("javascript") && (!url2.contains("#")))) {
					if (url2.contains("testing.") || url2.contains("test.") || (url2.contains("testadmin."))) {
						{
							if (url2.contains("test.")) {
								url2 = url2.replace("test.", "guest:guest@test.");
							} else if (url2.contains("testing.")) {
								url2 = url2.replace("testing.", "guest:guest@testing.");
							} else if (url.contains("mtest.")) {
								url2 = url2.replace("mtest.", "guest:guest@mtest.");
							}
						}
						verifyURLStatus(url2);
					}
				}
			}
		}
		driver.close();
	}

	public void VerifyInternalPages_brandedLFE(String url) throws Exception {
		System.out.println("********* Verifing Internal Pages_BrandedLongformPost *******");
		//driver = adminProperties.callproperty(url, prop.getProperty("browser"));
		driver = adminProperties.headlessBrowser_admin(prop.getProperty("url"));
		adminProperties.LoginAdmin(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
		adminProperties.findAndClick("navigation_header");
		adminProperties.findAndClick("create_brandlongform_link");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		adminProperties.implicitWait();
		List<WebElement> anchorTagsList = driver.findElements(By.tagName("link"));
		System.out.println("Total no. of links are " + anchorTagsList.size());
		for (WebElement anchorTagElement : anchorTagsList) {
			if (anchorTagElement != null) {
				String url2 = anchorTagElement.getAttribute("href");
				if ((url2 != null && !url2.contains("javascript") && (!url2.contains("#")))) {
					if (url2.contains("testing.") || url2.contains("test.") || (url2.contains("testadmin."))) {
						{
							if (url2.contains("test.")) {
								url2 = url2.replace("test.", "guest:guest@test.");
							} else if (url2.contains("testing.")) {
								url2 = url2.replace("testing.", "guest:guest@testing.");
							} else if (url.contains("mtest.")) {
								url2 = url2.replace("mtest.", "guest:guest@mtest.");
							}
						}
						verifyURLStatus(url2);
					}
				}
			}
		}
		driver.close();
	}

	public void VerifyInternalPages_MVP(String url) throws Exception {
		System.out.println("********* Verifing Internal MVPPost *******");
		//driver = adminProperties.callproperty(url, prop.getProperty("browser"));
		driver = adminProperties.headlessBrowser_admin(prop.getProperty("url"));
		adminProperties.LoginAdmin(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
		adminProperties.findAndClick("navigation_header");
		adminProperties.findAndClick("create_MVPpost_link");
		Thread.sleep(3000);
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		adminProperties.implicitWait();
		System.out.println("mvp_close_dialog===" + prop.getProperty("mvp_close_dialog"));
		adminProperties.Conditionalwait("mvp_close_dialog");
		adminProperties.findAndClick("mvp_close_dialog");
		adminProperties.implicitWait();
		List<WebElement> anchorTagsList = driver.findElements(By.tagName("link"));
		System.out.println("Total no. of links are " + anchorTagsList.size());
		for (WebElement anchorTagElement : anchorTagsList) {
			if (anchorTagElement != null) {
				String url2 = anchorTagElement.getAttribute("href");
				if ((url2 != null && !url2.contains("javascript") && (!url2.contains("#")))) {
					if (url2.contains("testing.") || url2.contains("test.") || (url2.contains("testadmin."))) {
						{
							if (url2.contains("test.")) {
								url2 = url2.replace("test.", "guest:guest@test.");
							} else if (url2.contains("testing.")) {
								url2 = url2.replace("testing.", "guest:guest@testing.");
							} else if (url.contains("mtest.")) {
								url2 = url2.replace("mtest.", "guest:guest@mtest.");
							}
						}
						verifyURLStatus(url2);
					}
				}
			}
		}
		driver.close();
	}

	public void VerifyInternalPages_brandedMVP(String url) throws Exception {
		System.out.println("********* Verifing Internal BrandedMVPPost *******");
		//driver = adminProperties.callproperty(url, prop.getProperty("browser"));
		driver = adminProperties.headlessBrowser_admin(prop.getProperty("url"));
		adminProperties.LoginAdmin(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
		adminProperties.findAndClick("navigation_header");
		adminProperties.findAndClick("create_MVPpost_link");
		Thread.sleep(3000);
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		adminProperties.implicitWait();
		System.out.println("mvp_close_dialog===" + prop.getProperty("mvp_close_dialog"));
		adminProperties.Conditionalwait("mvp_close_dialog");
		adminProperties.findAndClick("mvp_close_dialog");
		adminProperties.implicitWait();
		List<WebElement> anchorTagsList = driver.findElements(By.tagName("link"));
		System.out.println("Total no. of links are " + anchorTagsList.size());
		for (WebElement anchorTagElement : anchorTagsList) {
			if (anchorTagElement != null) {
				String url2 = anchorTagElement.getAttribute("href");
				if ((url2 != null && !url2.contains("javascript") && (!url2.contains("#")))) {
					if (url2.contains("testing.") || url2.contains("test.") || (url2.contains("testadmin."))) {
						{
							if (url2.contains("test.")) {
								url2 = url2.replace("test.", "guest:guest@test.");
							} else if (url2.contains("testing.")) {
								url2 = url2.replace("testing.", "guest:guest@testing.");
							} else if (url.contains("mtest.")) {
								url2 = url2.replace("mtest.", "guest:guest@mtest.");
							}
						}
						verifyURLStatus(url2);
					}
				}
			}
		}
		driver.close();
	}

	public void verifyURLStatus(String URL) {

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(URL);
		try {
			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() != 200)
				System.out.println(URL + ": " + response.getStatusLine().getStatusCode() + "=="
						+ response.getStatusLine().getReasonPhrase());
		} catch (Exception e) {
			// e.printStackTrace();
		}

		adminProperties.ExtractJSLogs(URL);
	}
}