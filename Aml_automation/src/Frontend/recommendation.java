package Frontend;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Common.Frontend;

public class recommendation {
	 Frontend frontendProperties = new Frontend();    
	    String browser="";
	    WebDriver driver ;
	    Properties prop=new Properties();
	    String url="https://www.genbeta.com/actualidad/edx-la-prestigiosa-web-de-cursos-online-gratuitos-ahora-esta-disponible-en-espanol";
	    List<String> list = new LinkedList<String>();
    	
	     @BeforeTest()
	    public void openbrowser() throws Exception
	    {
	        prop = frontendProperties.ReadProperties();
	        driver = frontendProperties.frontcallproperty(url,
	                prop.getProperty("browser"));
	        browser = prop.getProperty("browser");
	       
	    }
	    @Test (priority=1)
	    public void getPostTitle()
	    {
	    	frontendProperties.findAndClick("cookie");	    	
	    	list.add(frontendProperties.findElement(prop.getProperty("post_title")).getText());
	    }
	    @Test (priority=2)
	    public void GetSideRecommendation()
	    { 
	    	
	    	List<WebElement> siderecom= frontendProperties.findElementsByXpath(prop.getProperty("side_recommendations"));
	    	System.out.println(siderecom.size());
	    	for(WebElement posttitle: siderecom)
	    	{
	    		System.out.println(posttitle.getText());
	    		list.add(String.valueOf(posttitle.getText()));
	    	}
	     }
		
	    @Test (priority=3)
	    public void getBottomRecommendation()
	    {
	    	List<WebElement> bottomrecom= frontendProperties.findElementsByXpath(prop.getProperty("bottom_recommendations"));
	    	System.out.println(bottomrecom.size());
	    	for(WebElement posttitle: bottomrecom)
	    	{
	    		System.out.println(posttitle.getText());
	    		list.add(String.valueOf(posttitle.getText()));
	    	}
	    }
	    
	    @Test  (priority=4)
	    public void getDuplicateRecommendations() throws IOException
	    {
	    	System.out.println(list);
	    	
	    	System.out.println("Duplicate Recommendations : " + frontendProperties.findDuplicates(list));
	    	if(frontendProperties.findDuplicates(list).size()>0)
	      	{
	    		//frontendProperties.FullScreenshot(driver,"r1.png");
	    		
	      	}
	     }
}