package Frontend;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;
import Common.Frontend;

public class Amptest {
	WebDriver driver;
	Frontend frontendProperties = new Frontend();
	Properties prop = new Properties();
	String browser = "";
	String status = "Y";
	String PostTitle = "";
	String url="https://www.xataka.com,https://www.bebesymas.com,https://www.directoalpaladar.com,https://www.vitonica.com,https://www.espinof.com";
	
	
	Map<String, Integer> FinalerrorMap = new HashMap<String, Integer>();
	
	@Test
	public void Setup() throws Exception {
		prop = frontendProperties.ReadProperties();
		Map<String, Integer> LocalerrorMap = new HashMap<String, Integer>();
		
		String[] blogname= url.split(",");
		for(int i=0;i<blogname.length;i++)
		{
			System.out.println(blogname[i]);
			driver=headlessbrowser(blogname[i]);
			List<WebElement> list = driver.findElements(By.xpath(".//h2[@class='abstract-title']/a"));
	         
	        int cnt=1;
	            for (WebElement element1 : list) {
		           if((cnt<4) && (!element1.getAttribute("href").contains("utm_campaign=repost"))) { 
		            	LocalerrorMap=getAMPerror(element1.getAttribute("href")+"/amp");
		            	LocalerrorMap.forEach((k,v)->
            			{
		        			if ( FinalerrorMap.get(k) == null ) {
		        				FinalerrorMap.put(k,v);
		    				}
		        			else
		        			{
		        				FinalerrorMap.put(k, FinalerrorMap.get(k)+1);
		        			}
            			}
        					);
		            		
   	         	cnt=cnt+1;}
		         }
	        
		}
		
		 if ( FinalerrorMap.size()>0 ) { 
	            System.out.println("FINAL result");
	    		FinalerrorMap=sortByValue(FinalerrorMap);
	         	FinalerrorMap.forEach((k,v)->System.out.println("Final Result : " + k + " Count : " + v));
	         	}
	            else
	            {
	            	System.out.println("Final Result: PASS");
	            }
		
		driver.quit();
	}

	
	public Map<String, Integer> getAMPerror(String ampurl) throws Exception  {
		Map<String, Integer> errorMap = new HashMap<String, Integer>();
		
		String url = "https://validator.ampproject.org/";
	   	driver=headlessbrowser(url);  
				System.out.println("AMP URL: "+ampurl);
	
     	driver.findElement(By.xpath(".//input[@id='input']")).sendKeys(ampurl);
		driver.findElement(By.id("validateButton")).click();
		
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		
		if(driver.findElement(By.xpath(".//div[@class='ampproject-result style-scope webui-statusbar']/span")).getText()=="PASS")
		{
		 System.out.println(driver.findElement(By.xpath(".//div[@class='ampproject-result style-scope webui-statusbar']/span")).getText());
		
		}
		else 
		{ 
			System.out.println(driver.findElement(By.xpath(".//div[@class='ampproject-result style-scope webui-statusbar']/span")).getText());
			List<WebElement> list=driver.findElements(By.xpath(".//div[@class='message style-scope webui-errorlist']"));
			
			for (WebElement element1 : list) {
				// System.out.println(element1.getText());
				  
				if ( errorMap.get(element1.getText()) == null ) {
				    errorMap.put(element1.getText(),1);
				}
				 
			}
			
			errorMap.forEach((k,v)->System.out.println("Item : " + k + " Count : " + v));
		
			
		}
		
	driver.close();

		return errorMap;
	}
	public WebDriver headlessbrowser(String url){
		System.setProperty("webdriver.chrome.driver", "/src/Driverfiles/chromedriver_linux");
	    ChromeOptions chromeOptions = new ChromeOptions();
	    chromeOptions.addArguments("--headless");
	    ChromeDriver driver = new ChromeDriver(chromeOptions);
		driver.get(url);     
		return driver;
		
	}
	 public static Map<String, Integer> sortByValue(Map<String, Integer> hm) 
	    { 
	       List<Map.Entry<String, Integer> > list = 
	               new LinkedList<Map.Entry<String, Integer> >(hm.entrySet()); 
	        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
	            public int compare(Map.Entry<String, Integer> o1,  
	                               Map.Entry<String, Integer> o2) 
	            { 
	                return (o2.getValue()).compareTo(o1.getValue()); 
	            } 
	        }); 
	          
	        Map<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
	        for (Map.Entry<String, Integer> aa : list) { 
	            temp.put(aa.getKey(), aa.getValue()); 
	        } 
	        return temp; 
	    } 
	  

}
