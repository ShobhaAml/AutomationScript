package Frontend;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Frontend;

public class consolemessage
{
    WebDriver driver;
    String browser="";
    Properties prop = new Properties(); 
    Frontend frontProperties= new Frontend();
    String url="https://guest:guest@testing.xataka.com/videos/automate-excel-lead-video-post-gopro-karma-primeras-impresiones-erase-una-vez-un-dron-a-un-gran-estabilizador-pegado-1";
    
    @BeforeMethod
    public void setUp() throws Exception {
        prop = frontProperties.ReadProperties();
        driver = frontProperties.callproperty(prop.getProperty("url"),
                prop.getProperty("browser"));
        browser = prop.getProperty("browser");
        
    }
    @Test
    public void testMethod() {
        driver.get(url);
        frontProperties.ExtractJSLogs(url);
    }
}
