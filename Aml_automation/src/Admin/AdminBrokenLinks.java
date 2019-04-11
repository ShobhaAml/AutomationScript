package Admin;

import java.io.IOException;
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
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Common.Adminproperty;


public class AdminBrokenLinks
{
   
    String[] arrlinks;
    String sitelinks = "";
    Adminproperty adminProperties = new Adminproperty();
    Properties prop = new Properties();
    String server, blogname, path, device, testserver, browser;
    String usuariolink = "", author = "";
  //  WebDriver driver = new HtmlUnitDriver();
    WebDriver driver;
	String adminurl="https://testadmin.xataka.com";
    
    @BeforeMethod
    public void Setup() throws Exception
    {
        prop = adminProperties.ReadProperties();
        browser = prop.getProperty("browser");
        server = prop.getProperty("server");
        blogname = prop.getProperty("blogname");
        device = prop.getProperty("device");
        browser = prop.getProperty("browser");
   }

    @Test
    public void verfiyLinks() throws IOException
    {
    	
    	System.setProperty("webdriver.chrome.driver",
                System.getProperty("user.dir") + "//src//Driverfiles//" + "chromedriver.exe");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        /* capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs); */
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        // options.addArguments("" + capabilities + "");
        driver = new ChromeDriver(options);
    	
    	driver.get(adminurl);

        String username = prop.getProperty("Uadmin");
		String pwd = prop.getProperty("Padmin");
		System.out.println("Uname==" + username );
		
		driver.findElement(By.xpath(prop.getProperty("login_username_txt"))).sendKeys(username);
		driver.findElement(By.xpath(prop.getProperty("login_pwd_txt"))).sendKeys(pwd);
		driver.findElement(By.xpath(prop.getProperty("login_submit_button"))).click();
    	driver.navigate().refresh();
    	
     //   driver = adminProperties.frontcallproperty(prop.getProperty("url"),  prop.getProperty("browser"));
    	
    
    	System.out.println("Main URL----------"+ adminurl);
        List<WebElement> anchorTagsList = driver.findElements(By.tagName("a"));
        System.out.println( adminurl +" ---Total no. of links are " + anchorTagsList.size());
        for (WebElement anchorTagElement : anchorTagsList) {
            if (anchorTagElement != null) {
                String url = anchorTagElement.getAttribute("href");
               // System.out.println(url+"-------------------------------");
                if(!url.contains("/queueposts/")) {    
                	              if(url.contains("https://testing.") || url.contains("https://test.") || (url.contains("https://mtest."))) {
                                       if( url.contains("https://test."))
                                       {
                                        url=url.replace("https://test.", "https://guest:guest@test.");
                                       }
                                       else if( url.contains("https://testing."))
                                       {
                                           url=url.replace("https://testing.", "https://guest:guest@testing.");
                                       }
                                       else if( url.contains("mtest."))
                                       {
                                           url=url.replace("https://mtest.", "https://guest:guest@mtest.");
                                       }
                                   }
                
                
                

                    if (url.contains("/autor/")) {
                        author = url;
                    }
                    verifyURLStatus(url);
            	}
                	
                    
            }
          
                    
        }

        String arrurl="";
           List<WebElement> linkurl = driver.findElements(By.xpath(".//div[@id='panel-main-menu']/ul/li/a"));
	        for(WebElement e: linkurl)
	        {
	        	  System.out.println(e.getAttribute("href") +"xxxxx");
	        	//  VerifyInternalPages(e.getAttribute("href"));
	        	  
	        	  if(arrurl=="")
	        	  {
	        		  arrurl=e.getAttribute("href") ;
	        	  }
	        	  else
	        	  {
	        		   arrurl=arrurl+"@#@"+ e.getAttribute("href") ;
	        	  }
	        }
	        System.out.println(arrurl);
	        
	        String[] arrurl1= arrurl.split("@##@");
	        
	        for(int i=0;i<=arrurl1.length;i++)
	        {
	        	VerifyInternalPages(arrurl1[i]);
	        }
	        
        }

    public void VerifyInternalPages(String url) throws IOException
    {
        System.out.println("********* Verifing Internal Pages  " + url
                + "*******");
      //  driver = adminProperties.frontcallproperty(url, prop.getProperty("browser"));
     	driver.get(url);
        List<WebElement> anchorTagsList = driver.findElements(By.tagName("a"));
        System.out.println("Total no. of links are " + anchorTagsList.size());
        for (WebElement anchorTagElement : anchorTagsList) {
            if (anchorTagElement != null) {
                String url1 = anchorTagElement.getAttribute("href");
               
             //   System.out.println(url1+"--------------------------------<bR>");                
                
                if (url1 != null
                        && !url1.contains("javascript")   && !url1.contains("javascript")   && !url1.contains("javascript")
                        && (!url1.contains("utm_campaign=footer") && (!url1
                                .contains("#") && (!url1.contains("youtu"))))) {
                    if(url1.contains("https://testing.") || url1.contains("https://test.") || (url1.contains("https://mtest.")))
                    {
                        if( url.contains("https://test."))
                        {
                         url1=url1.replace("https://test.", "https://guest:guest@test.");
                        }
                        else if( url1.contains("https://testing."))
                        {
                            url1=url1.replace("https://testing.", "https://guest:guest@testing.");
                        }
                        else if( url1.contains("https://mtest."))
                        {
                            url1=url1.replace("https://mtest.", "https://guest:guest@mtest.");
                        }
                    }
                    if (url.contains("/usuario/")) {
                        usuariolink = url;
                    }
                    // System.out.println(url1);
                    verifyURLStatus(url1);
                    
                }
            }
        }
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
        
      //  frontendProperties.ExtractJSLogs(URL);
    }
}
