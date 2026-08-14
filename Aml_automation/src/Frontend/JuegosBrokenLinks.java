package Frontend;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Frontend;

public class JuegosBrokenLinks
{
	 
    String[] arrlinks;
    String sitelinks = "";
    Frontend frontendProperties = new Frontend();
    Properties prop = new Properties();
    String server, blogname, path, device, testserver, browser;
    String usuariolink = "", author = "";
    WebDriver driver = null;
    String desktopurl="https://guest:newguest@testing.3djuegos.com/juegos/ghost-recon-advanced-warfighter/noticias/grin-sera-el-desarrollador-de-ghost-recon-3-050627-9"
    		+ ", https://guest:newguest@testing.3djuegos.com/juegos/bad-day-la/noticias/publicada-la-web-de-bad-day-la-050707-27"
    		+ ", https://guest:newguest@testing.3djuegos.com/juegos/falcon-40-allied-force/noticias/disponible-falcon-40-allied-force-051128-280"
    		+", https://guest:newguest@testing.3djuegos.com/juegos/age-of-empires-iii/noticias/cambia-el-destino-del-mundo-con-age-of-empires-iii-051104-245"
    		+ ", https://guest:newguest@testing.3djuegos.com/juegos/dungeon-siege-ii-plains-of-tears/noticias/dungeon-siege-ii-plains-of-tears-avance-050713-11"
    		+ ", https://guest:newguest@testing.3djuegos.com/juegos/el-senor-de-los-anillos-la-batalla-por-la-tierra-media-2/noticias/la-batalla-por-la-tierra-media-2-avance-051225-70"
    		+ ", https://guest:newguest@testing.3djuegos.com/juegos/call-of-duty-2/noticias/call-of-duty-2-avance-050715-12"
    		+ ", https://guest:newguest@testing.3djuegos.com/juegos/el-senor-de-los-anillos-la-batalla-por-la-tierra-media-2/noticias/la-batalla-por-la-tierra-media-2-avance-051225-70"
    		+ ", https://guest:newguest@testing.3djuegos.com/juegos/northland/guias-y-trucos/juego-rapido-y-mapas-050719-15";
    		
	
	    
  
    
    @Test
    public void verfiyLinks() throws Exception
    { 
    	String[] blogname= desktopurl.split(",");
		for(int i=0;i<blogname.length;i++)
		{
			System.out.println(blogname[i]);
			driver=headlessbrowser(blogname[i]);
	    	
	    	System.out.println("Main URL----------"+ blogname[i]);
	        List<WebElement> anchorTagsList = driver.findElements(By.tagName("a"));
	        System.out.println( blogname[i] +" ---Total no. of links are " + anchorTagsList.size());
	        for (WebElement anchorTagElement : anchorTagsList) {
	            if ((anchorTagElement != null)) {
	                String url = anchorTagElement.getAttribute("href");
	                if((url != null) && (!url.contains("https://popup.taboola.com/es/?")) && (!url.contains("taboola")) )
	                {
	                	if((url.contains("https://test."))) { 
	                	url=url.replace("https://test.", "https://guest:newguesttest@testing.");
                           
	                }
            	 else if( url.contains("https://testing."))
                    {
                        url=url.replace("https://testing.", "https://guest:newguesttest@testing");
                    }
	
	                
	                verifyURLStatus(url);
	            }
	            }
	            
	        }
		}
		
		
    	
    	
    
    
    }
    
    
	
   
	public WebDriver headlessbrowser(String url){

	     // System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "//src//Driverfiles//chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "//src//Driverfiles//chromedriver");
	         
	        ChromeOptions chromeOptions = new ChromeOptions();
	        chromeOptions.addArguments("--headless");
		    chromeOptions.addArguments("--no-sandbox");
	      
		    ChromeDriver driver = new ChromeDriver(chromeOptions);
			driver.get(url);     
		    driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

			return driver;	
	  }
	
	
	 public void verifyURLStatus(String URL)
	    {
	        
	        HttpClient client = HttpClientBuilder.create().build();
	        HttpGet request = new HttpGet(URL);
	        try {
	            HttpResponse response = client.execute(request);

	            if (response.getStatusLine().getStatusCode() != 200)
	                System.out.println(URL + ": "
	                        + response.getStatusLine().getStatusCode()   +"=="+ response.getStatusLine().getReasonPhrase() );
	        } catch (Exception e) {
	            // e.printStackTrace();
	        }
	        
	      ExtractJSLogs(URL);
	    }
	    public void ExtractJSLogs(String URL) {
			int cnt = 0;

			LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
			for (LogEntry entry : logEntries) {
				if (entry.getMessage() != null) {
					if (cnt == 0) {
						System.out.println("Console Logs:  " + URL);
						cnt++;
					}

					System.out.println(entry.getLevel() + " " + entry.getMessage());
				}
			}
		}
   
}
