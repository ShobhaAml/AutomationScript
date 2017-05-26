package Frontend;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Frontend;

public class breakingNewshome {
	    WebDriver driver;
	    Frontend frontendProperties = new Frontend();    
	    Properties prop = new Properties();
	    String browser = "";
	    String status="Y";
	   
	    @BeforeMethod
	    public void Setup() throws Exception
	    {
	        prop = frontendProperties.ReadProperties();
	        driver = frontendProperties.frontcallproperty(prop.getProperty("url"),
	                prop.getProperty("browser"));
	        browser = prop.getProperty("browser");
	    }
	    @Test
	    public void openconnection()
	    {
	    	try
		    {
		    	WebDriverWait wait = new WebDriverWait(driver, 60);// 1 minute 
		        if (new WebDriverWait(driver, 10).until(ExpectedConditions
		                    .visibilityOfElementLocated(By.xpath(prop
		                            .getProperty("breaking_section")))) != null)
		    	{
			   		//System.out.println(frontendProperties.findElement(prop.getProperty("breaking_section")).getText());
			   		status=frontendProperties.findElement(prop.getProperty("breaking_section")).getText();
		    	}
		     
		    }
		    catch(Exception e)
			{
				
			}
    		
	    	if(status.equalsIgnoreCase("Y"))
	    	{
	    		System.out.println("No Breaking News is Enabled.");
	    	}  
	    	else
	        {
	        	System.out.println(status);
	        }
	    	
	    }
}
