package Common;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class Setup {
	
	WebDriver driver;
	
	public WebDriver callDesiredCaps(String Platformname, String Devicename, String version, String siteurl) throws MalformedURLException
	{		
		DesiredCapabilities capabilities=DesiredCapabilities.android();
		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME,BrowserType.CHROME);
		capabilities.setCapability(MobileCapabilityType.PLATFORM,Platform.ANDROID);
	    capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,Platformname);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,Devicename);
	    capabilities.setCapability(MobileCapabilityType.VERSION,version);
		URL url= new URL("http://0.0.0.0:4723/wd/hub");
		driver = new RemoteWebDriver(url, capabilities);
		driver.get(siteurl);
		return driver;
	}
	
	/*public WebDriver callEmulatorDesiredCaps(String Platformname, String Devicename, String version, String siteurl) throws MalformedURLException
	{	
		 DesiredCapabilities capabilities = new DesiredCapabilities();
		 capabilities.setCapability("deviceName", "Android Emulator");
		 capabilities.setCapability("platformName", "Android");
		 capabilities.setCapability("platformVersion", "4.4"); 
		 capabilities.setCapability("browserName", "Chrome");
		 driver = new AndroidDriver(
		           new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		 driver.get(siteurl);
		 return driver;
	}*/
	
}
