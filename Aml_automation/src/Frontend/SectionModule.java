package Frontend;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Common.Adminproperty;
import Common.Frontend;

public class SectionModule {
	Frontend frontendProperties = new Frontend();    
    String browser="";
    WebDriver driver ;
    Properties prop=new Properties();
    List<String> list = new LinkedList<String>();
	
     @BeforeTest()
    public void openbrowser() throws Exception
    {
        prop = frontendProperties.ReadProperties();
        driver = frontendProperties.frontcallproperty(prop.getProperty("url"),
                prop.getProperty("browser"));
        browser = prop.getProperty("browser");
        
        frontendProperties.findAndClick("cookie");        
        frontendProperties.ExtractJSLogs(prop.getProperty("url"));
    }
    
    @Test
    public void getSection1Posts()
    { 
    		
    	List<WebElement> Section1= frontendProperties.findElementByClass(prop.getProperty("Section_module1"));
    	System.out.println(Section1.size());
    	for(WebElement posttitle: Section1)
    	{
    		System.out.println(posttitle.getText());
    		list.add(String.valueOf(posttitle.getText()));
    	}
     }
	
    @Test
    public void getSection2Posts()
    { 
       	
    	List<WebElement> Section2= frontendProperties.findElementByClass(prop.getProperty("Section_module2"));
    	System.out.println(Section2.size());
    	for(WebElement posttitle: Section2)
    	{
    		System.out.println(posttitle.getText());
    		list.add(String.valueOf(posttitle.getText()));
    	}
     }
    
    @Test
    public void getDuplicateRecommendations() throws IOException
    {
       
    	System.out.println("Duplicate Recommendations : " + frontendProperties.findDuplicates(list));
    	if(frontendProperties.findDuplicates(list).size()>0)
      	{
    		frontendProperties.FullScreenshot(driver,"r1.png");
    		
      	}
     }
    
}
