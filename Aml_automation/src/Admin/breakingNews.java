package Admin;

import java.util.Calendar;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Adminproperty;
import Common.Frontend;

public class breakingNews {
	
	WebDriver driver;
    Adminproperty adminProperties = new Adminproperty();
    Properties prop = new Properties();
    String browser = "";
    String title="Automate Breaking news";
    String link="https://testing.xataka.com/automovil/nokia-here-se-instala-en-el-salpicadero-de-los-nuevos-toyota";
    String active="Y";
    int day=12;
    int month=6;
    int year=2012;
    int hrs=13;
    int mnts=10;
    String status="Y";
    
    @BeforeClass
    public void Setup() throws Exception
    {
        prop = adminProperties.ReadProperties();
        driver = adminProperties.callproperty(prop.getProperty("url"),
                prop.getProperty("browser"));
        browser = prop.getProperty("browser");
    }

    @Test (priority=1)
    public void Login() throws Exception
    {
    	 adminProperties.adminLogin();
         adminProperties.findAndClick("navigation_header");
         adminProperties.findAndClick("Navigate_breaking_news");
   }
    @Test  (priority=2)
    public void goToCreate()
    {
    	adminProperties.findAndClick("create_breaking_news");
    }
   
    @Test  (priority=3)
    public void createBreakingNews()
    {
    	//adminProperties.ExtractJSLogs();
    	
    	 Calendar now = Calendar.getInstance();
         
         System.out.println(now.get(Calendar.YEAR));
         System.out.println(now.get(Calendar.MONTH)+ 1);
         System.out.println(now.get(Calendar.DAY_OF_WEEK));
         System.out.println("Current Date is : " + now.get(Calendar.DATE));
         
         if((year < now.get(Calendar.YEAR)) || ( month< now.get(Calendar.MONTH)+ 1) || (day <now.get(Calendar.DATE)))
         {
        	 status="N";
         }
        
                
    	
    	adminProperties.findAndWrite("breaking_news_title", title);
    	adminProperties.findAndWrite("breaking_news_link", link);
    	if(active.equalsIgnoreCase("Y"))
    	{
    	 adminProperties.findAndClick("breaking_news_active");
    	}
    	adminProperties.findAndWrite("breaking_news_day", Integer.toString(day));
    	adminProperties.findAndWrite("breaking_news_month",Integer.toString(month));
    	adminProperties.findAndWrite("breaking_news_year", Integer.toString(year));
    	adminProperties.findAndWrite("breaking_news_hrs", Integer.toString(hrs));
    	adminProperties.findAndWrite("breaking_news_mnts", Integer.toString(mnts));
        adminProperties.findAndClick("breaking_news_Submit");
     
       
        if(status.equalsIgnoreCase("Y"))
        {
        	System.out.println("Breaking News created successfully");
        }
        else
        {
        	adminProperties.findElement(prop.getProperty("breaking_validation")).getText();
        	System.out.println("Invalid date: " + adminProperties.findElement(prop.getProperty("breaking_validation")).getText());
        	adminProperties.captureScreenshot(driver, "breakingnews.png");
        }
        
    	
    }
}
