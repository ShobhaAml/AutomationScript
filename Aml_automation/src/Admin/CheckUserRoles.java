package Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class CheckUserRoles {

	String blogrole="",uname="";
	WebDriver driver;
    Adminproperty adminProperties = new Adminproperty();
    Properties prop = new Properties();
    String browser = "";
    Connection conn;
    String username="admin", pwd="tst4wslp2";
    
    @BeforeClass
    public void Setup() throws Exception
    {
      /*  prop = adminProperties.ReadProperties();
        driver = adminProperties.callproperty(prop.getProperty("url"),
                prop.getProperty("browser"));
        browser = prop.getProperty("browser");
        */
    }

    @Test
    public void openConnection() throws Exception 
    {
    	conn = adminProperties.connectDb();
        String arrlogin = adminProperties.checkuserlogintype(conn ,username,pwd);
        if(arrlogin!=null)
        {
	        String[] logintypes=arrlogin.split("@##@");
	        uname= logintypes[0];
	        blogrole= logintypes[1];
	        
	        if(blogrole==null)
	        {
	        	blogrole="Admin";
	        }
	       System.out.println(blogrole+" account");
	       
        }
        
        
    }
    
    

    
	
}
