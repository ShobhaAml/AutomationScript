package Admin_Dashboard_Sanity;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import Admin.CheckUserRoles;
import Common.Adminproperty;

public class Longform_Dashboard_Sanity {
	WebDriver driver;
	Adminproperty adminProperties = new Adminproperty();
	CheckUserRoles roleType = new CheckUserRoles();
	Properties prop = new Properties();
	String blogname, browser, button, id, longformType, longformAutor;
	int count=1;
	
	@Test
	public void Setup() throws Exception  {
		prop = adminProperties.ReadProperties();
		driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
		browser = prop.getProperty("browser");
		adminProperties.adminLogin();
		roleType.openConnection(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
		System.out.println("authorname and blogrole is:-----" + roleType.Authorname + "------"+roleType.blogrole);
		longformDraft(roleType.blogrole);
	}	
	
	    public void longformDraft(String role) throws Exception {
	    	
		Actions action = new Actions(driver);
		List<String> longformArray = new ArrayList<String>();	
		List<String> longformPostTypeArray = new ArrayList<String>();	
		List<String> longformAutorArray = new ArrayList<String>();	
		List<WebElement> longformDraftlist = adminProperties.findElementsByXpath(prop.getProperty("longformDraftList"));
		List<WebElement> longformDraftListBCollaborator = adminProperties.findElementsByXpath(prop.getProperty("longformDraftListBCollaborator"));
    
		if(role.equalsIgnoreCase("Branded Collaborator")){
    	for(int j = 0; j<longformDraftListBCollaborator.size();j++){
       	String[] getIDfromURL = adminProperties.findElement(prop.getProperty("longformDraftListBCollaborator") + "["+ (j + 1) + "]"+"/a").getAttribute("href").split("/"); 
        longformArray.add(getIDfromURL[4]);}}
        else{
		for(int j = 0; j<longformDraftlist.size();j++)
        {
     	String[] getIDfromURL = adminProperties.findElement(prop.getProperty("longformDraftList") + "["+ (j + 1) + "]"+"/a").getAttribute("href").split("/");
        longformArray.add(getIDfromURL[4]);}}  
		
        for(int l=0;l<longformArray.size();l++){
        String[] splitText = "http://test.admin.weblogssl.com/edit/post/-L-1Tbjq0ClYVUddYrII?blog=xataka&userid=1".split("/");
        String newString ="http://test.admin.weblogssl.com/edit/post/-L-1Tbjq0ClYVUddYrII?blog=xataka&userid=1".replaceAll(splitText[5].substring(0, 20), longformArray.get(l)); 
        ((JavascriptExecutor)driver).executeScript("window.open();");
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        driver.get(newString);
        Thread.sleep(1000);
        
        if(role.equalsIgnoreCase("Branded Collaborator")){
           	 Thread.sleep(1000);
        	 adminProperties.findElement(prop.getProperty("longformPublicar")).click();
        	 Thread.sleep(2000);
        	 if(adminProperties.findElement(prop.getProperty("longformBrandLabelBCollaborator")).getText().equalsIgnoreCase("Detalles de publicación Completa toda la información para que el equipo de WSL Branded Content pueda publicar el artículo")==true)
             {longformType = "Brand";
              longformPostTypeArray.add(longformType);}}
        
        else if(role.equalsIgnoreCase("Branded Coordinator")){
        	adminProperties.findElement(prop.getProperty("longformPublicar")).click();
        	Thread.sleep(2000);
        	((JavascriptExecutor) driver).executeScript("window.scrollBy(0,600)");        	
        	adminProperties.findElement(prop.getProperty("longformBCoordinator"));           
            Thread.sleep(2000);
            longformType = "Brand";
            longformPostTypeArray.add(longformType);            
            longformAutor = adminProperties.findElement(prop.getProperty("longformAutor")).getAttribute("value");
            longformAutorArray.add(longformAutor);}
        
        else if(role.equalsIgnoreCase("Unbranded Coordinator")||(role.equalsIgnoreCase("Editor"))||(role.equalsIgnoreCase("Unbranded collaboraor")))    
    	    {Thread.sleep(2000);   
	  	    if(driver.getCurrentUrl().contains("notAuthorized")){
	        longformType = "Brand";           
            longformPostTypeArray.add(longformType);
	  	    }
    	    else if(driver.getCurrentUrl().contains("notAuthorized")==false){
    	    Thread.sleep(1000);
    	    adminProperties.findElement(prop.getProperty("longformPublicar")).click();
    		Thread.sleep(1000);
    	    if(adminProperties.findElement(prop.getProperty("longformNormal")).getText().equalsIgnoreCase("Portada y redes sociales")==true);
    	    {Thread.sleep(1000);
            longformType = "Normal";           
            longformPostTypeArray.add(longformType);  
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,600)");
    	    longformAutor = adminProperties.findElement(prop.getProperty("longformAutor")).getAttribute("value");
            longformAutorArray.add(longformAutor); }}}
            driver.close();
            driver.switchTo().window(tabs.get(0));}
             
        for(int btn=0;btn<longformArray.size();btn++)
        {
        	if(role.equalsIgnoreCase("Branded Collaborator") && (longformType.equalsIgnoreCase("brand"))){ //B Collaborator
            action.moveToElement(adminProperties.findElement(prop.getProperty("longformDraftListBCollaborator") + "[" + count + "]")).perform();
            System.out.println("<<--PostTitle-->>    "+adminProperties.findElement(prop.getProperty("longformDraftListBCollaborator")+ "[" + count + "]" +"/a").getText()+"        <<--PostType-->>"     +longformPostTypeArray.get(btn) +"        <<--Buttons-->>      "+adminProperties.findElement(prop.getProperty("longformDraftListBCollaborator")+"["+ count + "]"+ prop.getProperty("longformDraftListEditButton")).getText() +"     <<****PASS****>>");
            }
            
        	if(role.equalsIgnoreCase("Branded Coordinator") && (longformType.equalsIgnoreCase("brand"))) { // B Coordinator
        	((JavascriptExecutor) driver).executeScript("window.scrollBy(0,400)");
           	 action.moveToElement(adminProperties.findElement(prop.getProperty("longformDraftList") + "[" + count + "]")).perform();
      	    System.out.println("<<--PostTitle-->>    "+adminProperties.findElement(prop.getProperty("longformDraftList")+ "[" + count + "]" +"/a").getText()+"        <<--PostType-->>"     +longformPostTypeArray.get(btn) +"        <<--Buttons-->>      "+adminProperties.findElement(prop.getProperty("longformDraftList")+"["+ count + "]"+ prop.getProperty("longformDraftListEditButton")).getText() +"     <<****PASS****>>");
      	    }
      	             
            if(role.equalsIgnoreCase("Unbranded Coordinator")){  //UB Coordinator
    	     if(longformPostTypeArray.get(btn).equalsIgnoreCase("normal")){
          	((JavascriptExecutor) driver).executeScript("window.scrollBy(0,600)");
          	action.moveToElement(adminProperties.findElement(prop.getProperty("longformDraftList") + "[" + count + "]")).perform();
         	System.out.println("<<--PostTitle-->>    " +adminProperties.findElement(prop.getProperty("longformDraftList")+ "[" + count + "]" +"/a").getText()  +"        <<--PostType-->>"     +longformPostTypeArray.get(btn) +"        <<--Buttons-->>      "+adminProperties.findElement(prop.getProperty("longformDraftList")+"["+ count + "]"+ prop.getProperty("longformDraftListEditButton")).getText()+"     <<****PASS****>>");}
             else
            System.out.println("<<--PostTitle-->>    " +adminProperties.findElement(prop.getProperty("longformDraftList")+ "[" + count + "]" +"/a").getText()   +"        <<--PostType-->>"     +longformPostTypeArray.get(btn) +"        <<----No action buttons on branded posts---->>          <<****PASS****>>");
            }
       
            if(role.equalsIgnoreCase("Unbranded collaboraor")&& (longformPostTypeArray.get(btn).equalsIgnoreCase("normal"))){ //UB Collaborator
             if(longformAutorArray.get(btn).equalsIgnoreCase(roleType.Authorname)){
    	    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,400)");
    	    action.moveToElement(adminProperties.findElement(prop.getProperty("longformDraftList") + "[" + count + "]")).perform();
        	System.out.println("<<--PostTitle-->>    " +adminProperties.findElement(prop.getProperty("longformDraftList")+ "[" + count + "]" +"/a").getText()  +"        <<--PostType-->>"     +longformPostTypeArray.get(btn) +"        <<--Buttons-->>      "+adminProperties.findElement(prop.getProperty("longformDraftList")+"["+ count + "]"+ prop.getProperty("longformDraftListEditButton")).getText()+"     <<****PASS****>>");}
         	 else
            System.out.println("<<--PostTitle-->>    " +adminProperties.findElement(prop.getProperty("longformDraftList")+ "[" + count + "]" +"/a").getText()   +"        <<--PostType-->>"     +longformPostTypeArray.get(btn) +"        <<----No action buttons on other's post---->>         ");
            }
        		
        	if(role.equalsIgnoreCase("Editor")&&(longformPostTypeArray.get(btn).equalsIgnoreCase("normal"))){ //Editor
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,600)");
    	    action.moveToElement(adminProperties.findElement(prop.getProperty("longformDraftList") + "[" + count + "]")).perform();
          	System.out.println("<<--PostTitle-->>    " +adminProperties.findElement(prop.getProperty("longformDraftList")+ "[" + count + "]" +"/a").getText()  +"        <<--PostType-->>"     +longformPostTypeArray.get(btn) +"        <<--Buttons-->>      "+adminProperties.findElement(prop.getProperty("longformDraftList")+"["+ count + "]"+ prop.getProperty("longformDraftListEditButton")).getText()+"     <<****PASS****>>");
          	}
            count++; }       
        }

}
