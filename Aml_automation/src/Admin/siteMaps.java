package Admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.ParserConfigurationException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

public class siteMaps {

	WebDriver driver;
	List<String> list = new ArrayList<String>();
	String siteMap_URL = "https://guest:guest@testing.xataka.com/sitemap_index.xml";

	@Test
	public void fetch_siteMap_URL() throws InterruptedException, ParserConfigurationException, SAXException, IOException {
		ChromeOptions opt = new ChromeOptions();
		System.setProperty("webdriver.chrome.driver", "//usr//local//bin//chromedriver 1.48.21 PM");
		driver = new ChromeDriver(opt);
		opt.addArguments("start-maximized");
		driver.get(siteMap_URL);
		driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);

		String src = driver.getPageSource();

		Elements parse_element = org.jsoup.Jsoup.parse(src).getElementsByClass("text");
		for (org.jsoup.nodes.Element item : parse_element) {
			if (item.text().contains("https"))
				list.add(item.unwrap().toString().replace("https://", ""));
		}
		driver.close();
		/*Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
			}*/
		driver = new ChromeDriver();
		
		for(int i=0; i<list.size();i++)
		{
		driver.get("https://guest:guest@"+list.get(i));
		Thread.sleep(2000);
		}
		System.out.println();
	}
}
