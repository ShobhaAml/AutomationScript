package AdminDashboardsanity;

import java.io.IOException;
import java.util.List;

import Admin_Dashboard_sanity.ExcelDashboardButtonSanity;

import java.util.Properties;
import java.util.function.Function;

import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Adminproperty;
import Admin.CheckUserRoles;

public class DashboardProgramButtonSanity {
	String blogroleName;
	String blogrole = "admin";
	String Authorname;
	String posttitle="",postauthor="";
	Adminproperty adminProperties=new Adminproperty();
	ExcelDashboardButtonSanity sanity= new ExcelDashboardButtonSanity();
	
	String eCommerceprogrampostbuttons="Editar,Pasar a borrador,Destacar";
	String eCommerceRepublishprogrampostbuttons="Borrar";

	
	String Adminprogrampostbuttons_Normal="Repost,Editar,Pasar a borrador,Destacar";
	String Adminrepublishprogrampostbuttons_Normal="Borrar";
	String  Adminprogrampostbuttons_Branded="Repost,Editar,Pasar a borrador,Destacar";
	String  Adminrepublishprogrampostbuttons_Branded="Borrar";
	
	String BCprogrampostbuttons_Branded="Repost,Editar,Pasar a borrador";
	String BCrepublishprogrampostbuttons_Branded="Borrar";
	
	String BCOLprogrampostbuttons_Branded="Editar";
	String BCOLrepublishprogrampostbuttons_Branded="";//No button
	
	String UBC_programpostbuttons_Normal="Repost,Editar,Pasar a borrador,Destacar";
	String UBC_republishprogrampostbuttons_Normal="Borrar";
	String UBC_programpostbuttons_Branded="",UBC_republishprogrampostbuttons_Branded="";

	//No brand posts for unbranded collaborator
	String UBCOL_programpostbuttons_Normal_Self="Repost,Editar,Pasar a borrador";
	String UBCOL_programpostbuttons_Normal_Other="Pasar a borrador";
	String UBCOL_republishprogrampostbuttons_Normal_Other="",UBCOL_republishprogrampostbuttons_Normal_Self="Borrar";//No button
	
	String Editor_programpostbuttons_Normal="Repost,Editar,Pasar a borrador,Destacar";
	String Editor_republishprogrampostbuttons_Normal="Borrar";
	String Editor_programpostbuttons_Branded="",Editor_republishprogrampostbuttons_Branded="";
	
	CheckUserRoles ck= new CheckUserRoles();
	Properties prop = new Properties();
	String browser = "";
	WebDriver driver;
	
	@BeforeMethod
	public void Setup() throws Exception {
		// getPosttypes();
		prop = adminProperties.ReadProperties();
		driver = adminProperties.callproperty(prop.getProperty("url"),
				prop.getProperty("browser"));
		browser = prop.getProperty("browser");
		System.out.println(prop.getProperty("admin_usename"));
		   adminProperties.adminLogin();
	}
	    
	@Test 
	public void changefilter() throws Exception {   
		Select status2 = new Select(adminProperties.findElement(".//*[@id='postStatus']"));
		status2.selectByVisibleText("Programado");
		ck.openConnection(prop.getProperty("admin_usename"),prop.getProperty("admin_pwd"));
		Thread.sleep(1000);
		getProgramfilterStatus();
	}
	

	public void getProgramfilterStatus() throws Exception
	{
		 blogroleName= ck.blogroleName;
		 Authorname=ck.Authorname;
		 blogrole=ck.blogrole;
		
		List<WebElement> list = adminProperties.findElementsByXpath(".//*[@id='posts_list']/tr");
		String category = "";
		System.out.println(list.size());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String posttypeviaID = "normal";
				String Comparebutton = "";
				List<WebElement> Futurebuttonlist=adminProperties.findElementsByXpath(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[6]/ul/li");
				for(int j=0;j<Futurebuttonlist.size();j++)
				{
					if(Comparebutton=="")
					{Comparebutton= Futurebuttonlist.get(j).getText() ;}else{Comparebutton=Comparebutton+","+ Futurebuttonlist.get(j).getText() ;
					}
				}
				posttitle=adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]/td[4]/h2/a").getText();
				String postid= adminProperties.findElement("html/body/div[1]/div[2]/div[2]/div[2]/div/div/div[2]/table/tbody/tr[" + (i + 1) + "]").getAttribute("id").replace("row-", "");
			    postauthor=adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[3]/a").getText();
				posttypeviaID=adminProperties.getID(postid);
				category=adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[4]/p/a[1]").getText();
				//System.out.println(list.get(i).getAttribute("class") +"===posttitle=="+posttitle+" postid=="+postid + "  posttypeviaID==" +posttypeviaID + "   category=="+category +" Comparebutton====" + Comparebutton);
				if(category.equalsIgnoreCase("Especial Branded"))
				{
					posttypeviaID="Club";
				}
				
				
				if((adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[1]").getAttribute("class").contains("td-republished")==true))
				{//Scheduled Republished Post
					Getactualresult(Comparebutton,"scheduleRepublish",posttypeviaID,category );
				}
				else 
				{ Getactualresult(Comparebutton, "schedule" ,posttypeviaID,category);					
				}
			}
		} else {
			System.out.println("No result found");
		}
	}
	public void Getactualresult(String Comparebutton, String posttype , String ptype, String category ) throws IOException
	{
		
		System.out.println(Authorname +"==="+(postauthor));
		checkbutton( ptype,  posttype,  Comparebutton,category);
		
	}
	
	public void checkbutton(String ptype, String posttype, String Comparebutton,String category) throws IOException
	{
		String[] valueToWrite;
		String fileName="DashboardButton.xlsx";
		String sheetName="DashboardButton";

		if(ptype.equalsIgnoreCase("Club")) //Club
		{
			if(posttype.equalsIgnoreCase("scheduleRepublish"))
			{
				if(blogroleName.equalsIgnoreCase("Editor")){
					System.out.println(posttitle +" == "+ ptype +"====<b>"+  Editor_republishprogrampostbuttons_Branded+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,Editor_republishprogrampostbuttons_Branded,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,Editor_republishprogrampostbuttons_Branded,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,Editor_republishprogrampostbuttons_Branded,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);	
				}
				else if((blogroleName.equalsIgnoreCase("UBC")) || (blogroleName.equalsIgnoreCase("director"))){
					System.out.println(posttitle +" == "+ ptype + "====<b>"+  UBC_republishprogrampostbuttons_Branded+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,UBC_republishprogrampostbuttons_Branded,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,UBC_republishprogrampostbuttons_Branded,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,UBC_republishprogrampostbuttons_Branded,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);	
				}
				else if( blogroleName.equalsIgnoreCase("UbCol")){
					System.out.println(posttitle +" == "+ ptype  +"====" +"Not authorised to view");
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,"Not authorised to view",Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,"",posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					}
				else if(blogroleName.equalsIgnoreCase("BC")){
					System.out.println(posttitle +" == "+ ptype +"====<b>"+  BCrepublishprogrampostbuttons_Branded+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,BCrepublishprogrampostbuttons_Branded,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,BCrepublishprogrampostbuttons_Branded,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,BCrepublishprogrampostbuttons_Branded,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);	
				}
				else if(blogroleName.equalsIgnoreCase("Bcol")){
					System.out.println(posttitle +" == "+ ptype +"====<b>"+  BCOLrepublishprogrampostbuttons_Branded+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,BCOLrepublishprogrampostbuttons_Branded,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,BCOLrepublishprogrampostbuttons_Branded,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,BCOLrepublishprogrampostbuttons_Branded,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);	
				}
				else {
					System.out.println(posttitle +" == "+ ptype  +"====<b>"+  Adminrepublishprogrampostbuttons_Branded+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,Adminrepublishprogrampostbuttons_Branded,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,Adminrepublishprogrampostbuttons_Branded,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,Adminrepublishprogrampostbuttons_Branded,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);	
					}
			}
			else
			{
				if(blogroleName.equalsIgnoreCase("Editor")){
					System.out.println(posttitle +" == "+ ptype +"====<b>"+  Editor_programpostbuttons_Branded+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,Editor_programpostbuttons_Branded,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,Editor_programpostbuttons_Branded,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,Editor_programpostbuttons_Branded,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);	
				}
				else if((blogroleName.equalsIgnoreCase("UBC")) || (blogroleName.equalsIgnoreCase("director"))){
					System.out.println(posttitle +" == "+ ptype +"====<b>"+  UBC_programpostbuttons_Branded+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,UBC_programpostbuttons_Branded,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,UBC_programpostbuttons_Branded,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,UBC_programpostbuttons_Branded,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
				}
				else if( blogroleName.equalsIgnoreCase("UbCol")){
					System.out.println(posttitle +" == "+ ptype +"====" +"Not authorised to view");
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,"Not authorised to view",Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,"",posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					}
				else if(blogroleName.equalsIgnoreCase("BC")){
					System.out.println(posttitle +" == "+ ptype +"====<b>"+  BCprogrampostbuttons_Branded+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,BCprogrampostbuttons_Branded,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,BCprogrampostbuttons_Branded,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,BCprogrampostbuttons_Branded,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
				}
				else if(blogroleName.equalsIgnoreCase("Bcol")){
					System.out.println(posttitle +" == "+ ptype +"====<b>"+  BCOLprogrampostbuttons_Branded+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,BCOLprogrampostbuttons_Branded,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,BCOLprogrampostbuttons_Branded,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,BCOLprogrampostbuttons_Branded,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
				}
				else {
					System.out.println(posttitle +" == "+ ptype  +"====<b>"+  Adminprogrampostbuttons_Branded+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,Adminprogrampostbuttons_Branded,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,Adminprogrampostbuttons_Branded,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,Adminprogrampostbuttons_Branded,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					}
		
			}
		}
		else
		{
			
			if((category.equalsIgnoreCase("Ecommerce"))||(category.equalsIgnoreCase("default")) ||(category.equalsIgnoreCase("Basics")))
			{
		
				if(posttype.equalsIgnoreCase("scheduleRepublish"))
				{
					if( (blogroleName.equalsIgnoreCase("UBC"))  || (blogroleName.equalsIgnoreCase("director"))  || (blogroleName.equalsIgnoreCase("Editor"))  || (blogroleName.equalsIgnoreCase("admin")))
					  {
						  System.out.println(posttitle +" == "+ ptype +"====<b>"+  eCommerceprogrampostbuttons+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,eCommerceprogrampostbuttons,posttype));
						   valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,eCommerceprogrampostbuttons,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,eCommerceprogrampostbuttons,posttype)};
						   adminProperties.writeExcel(fileName, sheetName, valueToWrite); 
						
					  }else if(blogroleName.equalsIgnoreCase("UbCol"))  {
						  if(Authorname.equalsIgnoreCase(postauthor)){
							  System.out.println(posttitle +" == "+ ptype +"====<b>"+  eCommerceprogrampostbuttons+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,eCommerceprogrampostbuttons,posttype));
								valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,eCommerceprogrampostbuttons,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,eCommerceprogrampostbuttons,posttype)};
						  	}else{
						  		System.out.println(posttitle +" == "+ ptype +"====<b>"+  "Pasar a borrador"+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,"Pasar a borrador",posttype));
							    valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,"Pasar a borrador",Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,"Pasar a borrador",posttype)};
						  	}
						    adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					  }else
					  {
						   System.out.println(posttitle +" == "+ ptype +"====" +"Not authorised to view");
						   valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,"Not authorised to view",Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,"",posttype)};
						   adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					  }
				}
				else
				{
					if((blogroleName.equalsIgnoreCase("Editor"))  || (blogroleName.equalsIgnoreCase("director")) || (blogroleName.equalsIgnoreCase("UBC")) || (blogroleName.equalsIgnoreCase("admin")))
					  {
						  System.out.println(posttitle +" == "+ ptype +"====<b>"+  eCommerceprogrampostbuttons+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,eCommerceprogrampostbuttons,posttype));
						  valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,eCommerceprogrampostbuttons,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,eCommerceprogrampostbuttons,posttype)};
						  adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					  }else if(blogroleName.equalsIgnoreCase("UbCol"))  {
						  if(Authorname.equalsIgnoreCase(postauthor)){
							  System.out.println(posttitle +" == "+ ptype +"====<b>"+  eCommerceprogrampostbuttons+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,eCommerceprogrampostbuttons,posttype));
								valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,eCommerceprogrampostbuttons,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,eCommerceprogrampostbuttons,posttype)};
						  	}else{
						  		System.out.println(posttitle +" == "+ ptype +"====<b>"+  "Pasar a borrador"+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,"Pasar a borrador",posttype));
							    valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,"Pasar a borrador",Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,"Pasar a borrador",posttype)};
						  	}
						    adminProperties.writeExcel(fileName, sheetName, valueToWrite);
							
							
						}else{
						  System.out.println(posttitle +" == "+ ptype +"====" +"Not authorised to view");
							valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,"Not authorised to view",Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,"",posttype)};
							adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					  }
					
				}
				
			}
			else{
				
			if(posttype.equalsIgnoreCase("scheduleRepublish"))
			{
				if(blogroleName.equalsIgnoreCase("Editor")){
					System.out.println(posttitle +" == "+ ptype +"====<b>"+  Editor_republishprogrampostbuttons_Normal+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,Editor_republishprogrampostbuttons_Normal,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,Editor_republishprogrampostbuttons_Normal,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,Editor_republishprogrampostbuttons_Normal,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					}
				else if((blogroleName.equalsIgnoreCase("UBC")) || (blogroleName.equalsIgnoreCase("director"))){
					System.out.println(posttitle +" == "+ ptype +"====<b>"+  UBC_republishprogrampostbuttons_Normal+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,UBC_republishprogrampostbuttons_Normal,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,UBC_republishprogrampostbuttons_Normal,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,UBC_republishprogrampostbuttons_Normal,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					}
				else if( blogroleName.equalsIgnoreCase("UbCol")){
					if(Authorname.equalsIgnoreCase(postauthor))
					{
					  System.out.println(posttitle +" == "+ ptype  +"====<b>"+  UBCOL_republishprogrampostbuttons_Normal_Self+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,UBCOL_republishprogrampostbuttons_Normal_Self,posttype));
					  valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,UBCOL_republishprogrampostbuttons_Normal_Self,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,UBCOL_republishprogrampostbuttons_Normal_Self,posttype)};
					}else{
					  System.out.println(posttitle +" == "+ ptype  +"====<b>"+  UBCOL_republishprogrampostbuttons_Normal_Other+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,UBCOL_republishprogrampostbuttons_Normal_Other,posttype));
					   valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,UBCOL_republishprogrampostbuttons_Normal_Other,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,UBCOL_republishprogrampostbuttons_Normal_Other,posttype)};
					}
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					}
				else if(blogroleName.equalsIgnoreCase("BC")){
					System.out.println(posttitle +" == "+ ptype  +"====" +"Not authorised to view");
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,"Not authorised to view",Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,"",posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					}
				else if(blogroleName.equalsIgnoreCase("Bcol")){
					System.out.println(posttitle +" == "+ ptype  +"====" +"Not authorised to view");
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,"Not authorised to view",Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,"",posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					}
				else {
					System.out.println(posttitle +" == "+ ptype +"====<b>"+  Adminrepublishprogrampostbuttons_Normal+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,Adminrepublishprogrampostbuttons_Normal,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,Adminrepublishprogrampostbuttons_Normal,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,Adminrepublishprogrampostbuttons_Normal,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					}
			}
			else
			{
				if(blogroleName.equalsIgnoreCase("Editor")){
					System.out.println(posttitle +" == "+ ptype +"====<b>"+  Editor_programpostbuttons_Normal+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,Editor_programpostbuttons_Normal,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,Editor_programpostbuttons_Normal,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,Editor_programpostbuttons_Normal,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					}
				else if((blogroleName.equalsIgnoreCase("UBC")) || (blogroleName.equalsIgnoreCase("director"))){
					System.out.println(posttitle +" == "+ ptype +"====<b>"+  UBC_programpostbuttons_Normal+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,UBC_programpostbuttons_Normal,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,UBC_programpostbuttons_Normal,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,UBC_programpostbuttons_Normal,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					}
				else if( blogroleName.equalsIgnoreCase("UbCol")){
					if(Authorname.equalsIgnoreCase(postauthor)){
						System.out.println(posttitle +" == "+ ptype +"====<b>"+  UBCOL_programpostbuttons_Normal_Self+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,UBCOL_programpostbuttons_Normal_Self,posttype));
						valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,UBCOL_programpostbuttons_Normal_Self,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,UBCOL_programpostbuttons_Normal_Self,posttype)};
						}else{
						System.out.println(posttitle +" == "+ ptype +"====<b>"+  UBCOL_programpostbuttons_Normal_Other+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,UBCOL_programpostbuttons_Normal_Other,posttype));
						valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,UBCOL_programpostbuttons_Normal_Other,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,UBCOL_programpostbuttons_Normal_Other,posttype)};
					}
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					}
				else if(blogroleName.equalsIgnoreCase("BC")){
					System.out.println(posttitle +" == "+ ptype +"====" +"Not authorised to view");
				    valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,"Not authorised to view",Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,"",posttype)};
				    adminProperties.writeExcel(fileName, sheetName, valueToWrite);
				}
				else if(blogroleName.equalsIgnoreCase("Bcol")){
					System.out.println(posttitle +" == "+ ptype +"====" +"Not authorised to view");
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,"Not authorised to view",Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,"",posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
				}
				else {
					System.out.println(posttitle +" == "+ ptype +"====<b>"+  Adminprogrampostbuttons_Normal+"</b>====" +Comparebutton +"====" + sanity.getbuttonstatus( posttitle,Comparebutton,Adminprogrampostbuttons_Normal,posttype));
					valueToWrite = new String[] {blogrole,posttitle,Authorname,postauthor,posttype,ptype,Adminprogrampostbuttons_Normal,Comparebutton,sanity.getbuttonstatus(posttitle,Comparebutton,Adminprogrampostbuttons_Normal,posttype)};
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					}
			}
		 }
		}
	}
}


