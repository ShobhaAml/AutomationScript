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

	public String blogrole="";
	String uname="";
	WebDriver driver;
    Adminproperty adminProperties = new Adminproperty();
    Properties prop = new Properties();
    String browser = "";
    Connection conn;
    String blogroleName="";
	public String Authorname="";
    
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
    public void openConnection(String username , String pwd) throws Exception 
    {
        
        
        conn = adminProperties.connectDb();
        String arrlogin = adminProperties.checkuserlogintype(conn ,username,pwd);
        if(arrlogin!=null)
        {
            String[] logintypes=arrlogin.split("@##@");
            uname= logintypes[0].trim();
            blogrole= logintypes[1].trim();
            Authorname=logintypes[2].trim();
     
               
          System.out.println(blogrole+" account");
           
          if((blogrole.equalsIgnoreCase("Editor")) || (blogrole.equalsIgnoreCase("Lead Editor")) || (blogrole.equalsIgnoreCase("Editor Senior")))
           { 
               blogroleName="Editor";
               
           }else if(blogrole.equalsIgnoreCase("Coordinator"))
           {
               blogroleName="UBC";
               
           }else if(blogrole.equalsIgnoreCase("Collaborator"))
           {
               blogroleName="UbCol";
           }else if(blogrole.equalsIgnoreCase("Branded Coordinator"))
           {
               blogroleName="BC";
           }else if(blogrole.equalsIgnoreCase("Branded Collaborator"))
           {
               blogroleName="Bcol";
           }
           else
           { blogroleName="admin";}
        }
        
        System.out.println("Welcome " + blogrole + " " + blogroleName);
        adminProperties.adminLogin();
        
        
    }
    

    
	
}
