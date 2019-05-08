package Frontend;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import Common.Frontend;

public class HTMLvalidation {
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
			driver = frontendProperties.frontcallproperty(blogname[i], prop.getProperty("browser"));
			List<WebElement> list = driver.findElements(By.xpath(".//h2[@class='abstract-title']/a"));
	        //System.out.println(list.size());
	        
	        int cnt=1;
	            for (WebElement element1 : list) {
		           // System.out.println(element1.getAttribute("href"));
		            if((cnt<5) && (!element1.getAttribute("href").contains("utm_campaign=repost"))) { 
		            	LocalerrorMap=getHTMLerror(element1.getAttribute("href"));
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
	public Map<String, Integer> getHTMLerror(String posturl) throws Exception  {
		Map<String, Integer> errorMap = new HashMap<String, Integer>();
		
		String url = "https://validator.w3.org/";
		/*WebDriver driver = new HtmlUnitDriver();
		driver.get(url);*/
		driver = frontendProperties.frontcallproperty(url, prop.getProperty("browser"));
		System.out.println("Post URL: "+posturl);
	
     	driver.findElement(By.xpath(".//input[@id='uri']")).sendKeys(posturl);
		driver.findElement(By.xpath(".//a[@class='submit']")).click();
		
		
		
	System.out.println(driver.findElements(By.xpath(".//ol/li[@class='error']")).size());
		
		
		 if (driver.findElements(By.xpath(".//ol/li[@class='error']")).size()==0)
		  {
		 System.out.println("No HTML errors");
		  
		 } else {
			 System.out.println("Here are the HTML errors");
			 List<WebElement> list=driver.findElements(By.xpath(".//ol/li[@class='error']"));
		  
			 for (WebElement element1 : list) { 
			  
			  if ( errorMap.get(element1.getText()) == null ) {
			  errorMap.put(element1.getText(),1); }
			  
			  }
			 errorMap.forEach((k,v)->System.out.println("Item : " + k + " Count : " + v));
		  
		 }
		 	driver.close();

		return errorMap;
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
