package Admin;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class ImageCroper {
	String url1="";
	String url="https://ti.blogs.es/02d51c/afgepazgbpa/original.jpg";
	Adminproperty adminProperties = new Adminproperty();

	@Test
	public void CheckImageCropperStatus()
	{
		
			adminProperties.CheckImageCropperStatus(url);
		
	}
	
}
