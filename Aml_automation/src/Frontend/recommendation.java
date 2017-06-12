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
	    String url="https://www.bebesymas.com/parto/ten-cuidado-con-lo-que-dices-cuando-veas-una-mariposa-lila-en-la-cuna-de-un-bebe";
	    List<String> list = new LinkedList<String>();
    	
	     @BeforeTest()
	    public void openbrowser() throws Exception
	    {
	        prop = frontendProperties.ReadProperties();
	        driver = frontendProperties.frontcallproperty(url,
	                prop.getProperty("browser"));
	        browser = prop.getProperty("browser");
	       
	    }
	    
	    @Test
	    public void GetSideRecommendation()
	    { 
	    	frontendProperties.findAndClick("cookie");	    	
	    	List<WebElement> siderecom= frontendProperties.findElementsByXpath(prop.getProperty("side_recommendations"));
	    	System.out.println(siderecom.size());
	    	for(WebElement posttitle: siderecom)
	    	{
	    		System.out.println(posttitle.getText());
	    		list.add(String.valueOf(posttitle.getText()));
	    	}
	     }
		
	    @Test
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
	    
	    @Test void getDuplicateRecommendations() throws IOException
	    {
	       
	    	System.out.println("Duplicate Recommendations : " + frontendProperties.findDuplicates(list));
	    	if(frontendProperties.findDuplicates(list).size()>0)
	      	{
	    		frontendProperties.FullScreenshot(driver,"r1.png");
	    		
	      	}
	     }
}