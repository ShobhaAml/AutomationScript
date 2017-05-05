package Frontend;


import java.util.Properties;
import org.openqa.selenium.WebDriver;
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
