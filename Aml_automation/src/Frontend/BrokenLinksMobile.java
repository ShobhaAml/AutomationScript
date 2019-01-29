package Frontend;

import java.io.IOException;
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
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Adminproperty;
import Common.Frontend;

public class BrokenLinksMobile
{
    
    String[] arrlinks;
    String sitelinks = "";
    Frontend frontendProperties = new Frontend();
    Properties prop = new Properties();
    String server, blogname, path, device, testserver, browser;
    String usuariolink = "", author = "";
  	String Mobileurl="https://guest:guest@mtest.xataka.com";
    WebDriver driver = new HtmlUnitDriver();
    
    @BeforeMethod
    public void Setup() throws Exception
    {
        prop = frontendProperties.ReadProperties();
        browser = prop.getProperty("browser");
        server = prop.getProperty("server");
        blogname = prop.getProperty("blogname");
        device = prop.getProperty("device");
        browser = prop.getProperty("browser");
    }

    @Test
    public void verfiyLinks() throws IOException
    {

	driver.get(Mobileurl);
    	// driver = frontendProperties.frontcallproperty(url1,  prop.getProperty("browser"));
     	
    	System.out.println(Mobileurl);
	   List<WebElement> anchorTagsList = driver.findElements(By.tagName("a"));
        System.out.println("Total no. of links are " + anchorTagsList.size());
        for (WebElement anchorTagElement : anchorTagsList) {
            if (anchorTagElement != null) {
                String url = anchorTagElement.getAttribute("href");
                
               // System.out.println(url+"<bR>");
                
                               if (url != null
                        && !url.contains("javascript")
                        && (!url.contains("utm_campaign=footer") && (!url
                                .contains("#") && (!url.contains("youtu")) &&  (!url.contains("redirect?")) && ( !url.contains("mailto:?subject="))))) {
                                   if(url.contains("testing.") || url.contains("test.") || (url.contains("mtest.")))
                                   {
                                       if( url.contains("test."))
                                       {
                                        url=url.replace("test.", "guest:guest@test.");
                                       }
                                       else if( url.contains("testing."))
                                       {
                                           url=url.replace("testing.", "guest:guest@mtest.");
                                       }
                                       else if( url.contains("mtest."))
                                       {
                                           url=url.replace("mtest.", "guest:guest@mtest.");
                                       }
                                   }

                    if (url.contains("/autor/")) {
                        author = url;
                    }
                    verifyURLStatus(url);
                }
            }
        }

        // check for Respuestas pages
        System.out.println("usuariolink=" + usuariolink
                + " **********AUthor===" + author);
        VerifyInternalPages(Mobileurl + "/respuestas");
        VerifyInternalPages(Mobileurl + "/respuestas/preguntar");
        // ** check for Archivos pages
        // VerifyInternalPages(url1+"/archivos");
        // Verify Editor Pages
        VerifyInternalPages(Mobileurl + "/quienes-somos");
        // Verify Editor Pages
        VerifyInternalPages(Mobileurl + "/contacto");
        if (usuariolink != "") {
            VerifyInternalPages(usuariolink);
        }
        
        
    }

    public void VerifyInternalPages(String url) throws IOException
    {
        System.out.println("********* Verifing Internal Pages  " + url
                + " *******");
       //   driver = frontendProperties.frontcallproperty(url, prop.getProperty("browser"));
  	driver.get(url);
        List<WebElement> anchorTagsList = driver.findElements(By.tagName("a"));
        System.out.println("Total no. of links are " + anchorTagsList.size());
        for (WebElement anchorTagElement : anchorTagsList) {
            if (anchorTagElement != null) {
                String url1 = anchorTagElement.getAttribute("href");
              //  System.out.println(url1);
                if (url1 != null
                        && !url1.contains("javascript")
                        && (!url1.contains("utm_campaign=footer") && (!url1
                                .contains("#") && (!url1.contains("youtu"))))) {
                    if(url1.contains("testing.") || url1.contains("test.") || (url1.contains("mtest.")))
                    {
                        if( url.contains("test."))
                        {
                         url1=url1.replace("test.", "guest:guest@test.");
                        }
                        else if( url1.contains("testing."))
                        {
                            url1=url1.replace("testing.", "guest:guest@mtest.");
                        }
                        else if( url1.contains("mtest."))
                        {
                            url1=url1.replace("mtest.", "guest:guest@mtest.");
                        }
                    }
                    if (url.contains("/usuario/")) {
                        usuariolink = url;
                    }
                
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
    
    @AfterMethod
    public void teardown()
    {
    	driver.quit();
    	//driver.close();
    }
}
