package Admin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc.Role;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class CheckButtonsForPosttype {
	Connection conn;
	WebDriver driver;
	Adminproperty adminProperties = new Adminproperty();
	Properties prop = new Properties();
	String browser = "";
	String editorialrole = "admin";
	String posttype = "reposted";
	String blogrole="",blogroleName="",uname="",Authorname="";
	int future=0,republish=0;
	String arrbutton="";
	String postauthor="";
	String posttitle="";
	String ScheduleRepublishbutton="",RepublishButton="",RepostButton="",NormalButton="";
	String selffuture="",Otherfuture="",selfBrandfuture="",Othersbranded="";
	String SelfScheduleRepublish="",OtherScheduleRepublish="",selfBrandScheduleRepublish="",OthersbrandedScheduleRepublish="";	
	String 	SelfRepublishButton="",OtherRepublishButton="",selfBrandRepublishButton="",OthersbrandedRepublishButton="";
	String SelfRepost="",OtherRepost="",selfBrandRepost="",OthersbrandedRepost="";
	String  SelfNormal="",OtherNormal="",selfBrandNormal="",OthersbrandedNormal="";
	
	String posttypearr="";
	 String Futurepostbutton="";
	 
	
	/* public void getPosttypes() throws Exception
	 {
		 posttypearr=adminProperties.getPostType();
	     System.out.println("Get post type");
	 }
	 */
	 
	@Test
	public void Setup() throws Exception {
		//getPosttypes();
		System.out.println("Method 2");
		prop = adminProperties.ReadProperties();
		driver = adminProperties.callproperty(prop.getProperty("url"),
				prop.getProperty("browser"));
		browser = prop.getProperty("browser");
		getuserloginRole();
	}

	@DataProvider(name = "testdata")
	 public Object[][] TestDataFeed() throws Exception { Object[][] postdata =
		 adminProperties.readExcel("Editorial Roles", 28); 
		 return postdata; 
	 }

 @Test(dataProvider = "testdata")
	public void getbutton(String types,String rowclass, String Original_Names, String Buttons,
			String admin_Self_unbranded, String admin_Others_unbranded,
			String admin_Self_branded, String admin_Others_branded,
			String Editor_Self_unbranded, String Editor_Others_unbranded,
			String Editor_Self_branded, String Editor_Others_branded,
			String UBC_Self_unbranded, String UBC_Others_unbranded,
			String UBC_Self_branded, String UBC_Others_branded,
			String UbCol_Self_unbranded, String UbCol_Others_unbranded,
			String UbCol_Self_branded, String UbCol_Others_branded,
			String BCord_Self_unbranded, String BCord_Others_unbranded,
			String BCord_Self_branded, String BCord_Others_branded,			
			String Bcol_Self_unbranded, String Bcol_Others_unbranded,
			String Bcol_Self_branded, String Bcol_Others_branded) throws SQLException {

	
	 if (editorialrole.equalsIgnoreCase("admin")) {
		
		 ExcelbuttonRolebased(types,  rowclass,  blogroleName,  Buttons, admin_Self_unbranded,  admin_Others_unbranded,
					 admin_Self_branded,  admin_Others_branded,
					 Editor_Self_unbranded,  Editor_Others_unbranded,
					 Editor_Self_branded,  Editor_Others_branded,
					 UBC_Self_unbranded,  UBC_Others_unbranded,
					 UBC_Self_branded,  UBC_Others_branded,
					 UbCol_Self_unbranded,  UbCol_Others_unbranded,
					 UbCol_Self_branded,  UbCol_Others_branded,
					 BCord_Self_unbranded,  BCord_Others_unbranded,
					 BCord_Self_branded,  BCord_Others_branded,					 
					 Bcol_Self_unbranded,  Bcol_Others_unbranded,
					 Bcol_Self_branded,  Bcol_Others_branded);
		
		} else if ((editorialrole.equalsIgnoreCase("Lead Editor")) || (editorialrole.equalsIgnoreCase("Editor Senior")) || (editorialrole.equalsIgnoreCase("Editor"))) {
			 ExcelbuttonRolebased(types,  rowclass,  blogroleName,  Buttons, admin_Self_unbranded,  admin_Others_unbranded,
					 admin_Self_branded,  admin_Others_branded,
					 Editor_Self_unbranded,  Editor_Others_unbranded,
					 Editor_Self_branded,  Editor_Others_branded,
					 UBC_Self_unbranded,  UBC_Others_unbranded,
					 UBC_Self_branded,  UBC_Others_branded,
					 UbCol_Self_unbranded,  UbCol_Others_unbranded,
					 UbCol_Self_branded,  UbCol_Others_branded,
					 BCord_Self_unbranded,  BCord_Others_unbranded,
					 BCord_Self_branded,  BCord_Others_branded,					
					 Bcol_Self_unbranded,  Bcol_Others_unbranded,
					 Bcol_Self_branded,  Bcol_Others_branded);
		} else if (editorialrole.equalsIgnoreCase("Collaborator")) {
			 ExcelbuttonRolebased(types,  rowclass,  blogroleName,  Buttons, admin_Self_unbranded,  admin_Others_unbranded,
					 admin_Self_branded,  admin_Others_branded,
					 Editor_Self_unbranded,  Editor_Others_unbranded,
					 Editor_Self_branded,  Editor_Others_branded,
					 UBC_Self_unbranded,  UBC_Others_unbranded,
					 UBC_Self_branded,  UBC_Others_branded,
					 UbCol_Self_unbranded,  UbCol_Others_unbranded,
					 UbCol_Self_branded,  UbCol_Others_branded,
					 BCord_Self_unbranded,  BCord_Others_unbranded,
					 BCord_Self_branded,  BCord_Others_branded,					 
					 Bcol_Self_unbranded,  Bcol_Others_unbranded,
					 Bcol_Self_branded,  Bcol_Others_branded);
		} else if (editorialrole.equalsIgnoreCase("Coordinator")) {
			 ExcelbuttonRolebased(types,  rowclass,  blogroleName,  Buttons, admin_Self_unbranded,  admin_Others_unbranded,
					 admin_Self_branded,  admin_Others_branded,
					 Editor_Self_unbranded,  Editor_Others_unbranded,
					 Editor_Self_branded,  Editor_Others_branded,
					 UBC_Self_unbranded,  UBC_Others_unbranded,
					 UBC_Self_branded,  UBC_Others_branded,
					 BCord_Self_unbranded,  BCord_Others_unbranded,
					 BCord_Self_branded,  BCord_Others_branded,
					 UbCol_Self_unbranded,  UbCol_Others_unbranded,
					 UbCol_Self_branded,  UbCol_Others_branded,
					 Bcol_Self_unbranded,  Bcol_Others_unbranded,
					 Bcol_Self_branded,  Bcol_Others_branded);
		} else if (editorialrole.equalsIgnoreCase("Branded Coordinator")) {
			 ExcelbuttonRolebased(types,  rowclass,  blogroleName,  Buttons, admin_Self_unbranded,  admin_Others_unbranded,
					 admin_Self_branded,  admin_Others_branded,
					 Editor_Self_unbranded,  Editor_Others_unbranded,
					 Editor_Self_branded,  Editor_Others_branded,
					 UBC_Self_unbranded,  UBC_Others_unbranded,
					 UBC_Self_branded,  UBC_Others_branded,
					 UbCol_Self_unbranded,  UbCol_Others_unbranded,
					 UbCol_Self_branded,  UbCol_Others_branded,
					 BCord_Self_unbranded,  BCord_Others_unbranded,
					 BCord_Self_branded,  BCord_Others_branded,
					 Bcol_Self_unbranded,  Bcol_Others_unbranded,
					 Bcol_Self_branded,  Bcol_Others_branded);
		} else if (editorialrole.equalsIgnoreCase("Branded Collaborator")) {
			 ExcelbuttonRolebased(types,  rowclass,  blogroleName,  Buttons, admin_Self_unbranded,  admin_Others_unbranded,
					 admin_Self_branded,  admin_Others_branded,
					 Editor_Self_unbranded,  Editor_Others_unbranded,
					 Editor_Self_branded,  Editor_Others_branded,
					 UBC_Self_unbranded,  UBC_Others_unbranded,
					 UBC_Self_branded,  UBC_Others_branded,
					 UbCol_Self_unbranded,  UbCol_Others_unbranded,
					 UbCol_Self_branded,  UbCol_Others_branded,
					 BCord_Self_unbranded,  BCord_Others_unbranded,
					 BCord_Self_branded,  BCord_Others_branded,					
					 Bcol_Self_unbranded,  Bcol_Others_unbranded,
					 Bcol_Self_branded,  Bcol_Others_branded);
		} else if (editorialrole.equalsIgnoreCase("Colaborador")) {
			// ****Do nothing
		}
	}
		// System.out.println(Roles + "===" + Editor_Self_unbranded);
 
 public void ExcelbuttonRolebased(String types, String rowclass, String rolebased, String Buttons,String admin_Self_unbranded, String admin_Others_unbranded,
			String admin_Self_branded, String admin_Others_branded,
			String Editor_Self_unbranded, String Editor_Others_unbranded,
			String Editor_Self_branded, String Editor_Others_branded,
			String UBC_Self_unbranded, String UBC_Others_unbranded,
			String UBC_Self_branded, String UBC_Others_branded,
			String UbCol_Self_unbranded, String UbCol_Others_unbranded,
			String UbCol_Self_branded, String UbCol_Others_branded,
			String BCord_Self_unbranded, String BCord_Others_unbranded,
			String BCord_Self_branded, String BCord_Others_branded,			
			String Bcol_Self_unbranded, String Bcol_Others_unbranded,
			String Bcol_Self_branded, String Bcol_Others_branded)
			{
	 
	 if(types.equalsIgnoreCase("Future") && (rowclass.equalsIgnoreCase("scheduled")))
	  {
		 System.out.println(rolebased);
		if(rolebased.equalsIgnoreCase("Editor"))
		{
			if(Editor_Self_unbranded.equalsIgnoreCase("Y"))
			{ if(selffuture==""){selffuture= Buttons ;}else{selffuture=selffuture+","+ Buttons ;} }
			 if(Editor_Others_unbranded.equalsIgnoreCase("Y"))
			{if(Otherfuture==""){Otherfuture= Buttons ;}else{Otherfuture=Otherfuture+","+ Buttons ;} }
			 if(Editor_Self_branded.equalsIgnoreCase("Y"))
			{if(selfBrandfuture==""){selfBrandfuture= Buttons ;}else{selfBrandfuture=selfBrandfuture+","+ Buttons ;} }
			 if(Editor_Others_branded.equalsIgnoreCase("Y"))
			{if(Othersbranded==""){Othersbranded= Buttons ;}else{Othersbranded=Othersbranded+","+ Buttons ;} }
			
		}else if(rolebased.equalsIgnoreCase("UBC"))	{
			
			
			if(UBC_Self_unbranded.equalsIgnoreCase("Y"))
			{if(selffuture==""){selffuture= Buttons ;}else{selffuture=selffuture+","+ Buttons ;} }
			 if(UBC_Others_unbranded.equalsIgnoreCase("Y"))
			{if(Otherfuture==""){Otherfuture= Buttons ;}else{Otherfuture=Otherfuture+","+ Buttons ;} }
			 if(UBC_Self_branded.equalsIgnoreCase("Y"))
			{if(selfBrandfuture==""){selfBrandfuture= Buttons ;}else{selfBrandfuture=selfBrandfuture+","+ Buttons ;} }
			 if(UBC_Others_branded.equalsIgnoreCase("Y"))
			{if(Othersbranded==""){Othersbranded= Buttons ;}else{Othersbranded=Othersbranded+","+ Buttons ;} }
			 System.out.println("Schedule ===="+UBC_Self_unbranded +"==" + UBC_Others_unbranded +"==" +UBC_Self_branded +"=="+  UBC_Others_branded);
		}else if(rolebased.equalsIgnoreCase("UbCol")){
			System.out.println( UbCol_Self_unbranded +" "+	UbCol_Others_unbranded	+" "+ UbCol_Self_branded	+" "+UbCol_Others_branded);
			
			if(UbCol_Self_unbranded.equalsIgnoreCase("Y"))
			{if(selffuture==""){selffuture= Buttons ;}else{selffuture=selffuture+","+ Buttons ;}   }
			 if(UbCol_Others_unbranded.equalsIgnoreCase("Y"))
			{if(Otherfuture==""){Otherfuture= Buttons ;}else{Otherfuture=Otherfuture+","+ Buttons ;} }
			 if(UbCol_Self_branded.equalsIgnoreCase("Y"))
			{if(selfBrandfuture==""){selfBrandfuture= Buttons ;}else{selfBrandfuture=selfBrandfuture+","+ Buttons ;}  }
			 if(UbCol_Others_branded.equalsIgnoreCase("Y"))
			{if(Othersbranded==""){Othersbranded= Buttons ;}else{Othersbranded=Othersbranded+","+ Buttons ;}  }
			
		}else if(rolebased.equalsIgnoreCase("BC")){
			
			
			
			if(BCord_Self_unbranded.equalsIgnoreCase("Y"))
			{if(selffuture==""){selffuture= Buttons ;}else{selffuture=selffuture+","+ Buttons ;}   }
			 if(BCord_Others_unbranded.equalsIgnoreCase("Y"))
			{if(Otherfuture==""){Otherfuture= Buttons ;}else{Otherfuture=Otherfuture+","+ Buttons ;} }
			 if(BCord_Self_branded.equalsIgnoreCase("Y"))
			{if(selfBrandfuture==""){selfBrandfuture= Buttons ;}else{selfBrandfuture=selfBrandfuture+","+ Buttons ;}  }
			 if(BCord_Others_branded.equalsIgnoreCase("Y"))
			{if(Othersbranded==""){Othersbranded= Buttons ;}else{Othersbranded=Othersbranded+","+ Buttons ;} }
					
		}else if(rolebased.equalsIgnoreCase("Bcol")){
			if(Bcol_Self_unbranded.equalsIgnoreCase("Y"))
			{if(selffuture==""){selffuture= Buttons ;}else{selffuture=selffuture+","+ Buttons ;}   }
			 if(Bcol_Others_unbranded.equalsIgnoreCase("Y"))
			{if(Otherfuture==""){Otherfuture= Buttons ;}else{Otherfuture=Otherfuture+","+ Buttons ;} }
			 if(Bcol_Self_branded.equalsIgnoreCase("Y"))
			{if(selfBrandfuture==""){selfBrandfuture= Buttons ;}else{selfBrandfuture=selfBrandfuture+","+ Buttons ;}  }
			 if(Bcol_Others_branded.equalsIgnoreCase("Y"))
			{if(Othersbranded==""){Othersbranded= Buttons ;}else{Othersbranded=Othersbranded+","+ Buttons ;}  }
		
		}else if(rolebased.equalsIgnoreCase("admin")){
			if(admin_Self_unbranded.equalsIgnoreCase("Y"))
			{if(selffuture==""){selffuture= Buttons ;}else{selffuture=selffuture+","+ Buttons ;}   }
			 if(admin_Others_unbranded.equalsIgnoreCase("Y"))
			{if(Otherfuture==""){Otherfuture= Buttons ;}else{Otherfuture=Otherfuture+","+ Buttons ;} }
			 if(admin_Self_branded.equalsIgnoreCase("Y"))
			{if(selfBrandfuture==""){selfBrandfuture= Buttons ;}else{selfBrandfuture=selfBrandfuture+","+ Buttons ;}  }
			 if(admin_Others_branded.equalsIgnoreCase("Y"))
			{if(Othersbranded==""){Othersbranded= Buttons ;}else{Othersbranded=Othersbranded+","+ Buttons ;}  }
		
		}
	  }
		else if(types.equalsIgnoreCase("Schedule republish") && (rowclass.equalsIgnoreCase("scheduled")))
		{
			if(rolebased.equalsIgnoreCase("Editor"))
			{
				if(Editor_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfScheduleRepublish==""){SelfScheduleRepublish= Buttons ;} else{SelfScheduleRepublish=SelfScheduleRepublish+","+ Buttons ;}   }
				if(Editor_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherScheduleRepublish==""){OtherScheduleRepublish= Buttons ;} else{OtherScheduleRepublish=OtherScheduleRepublish+","+ Buttons ;} }
				if(Editor_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandScheduleRepublish==""){selfBrandScheduleRepublish= Buttons ;} else{selfBrandScheduleRepublish=selfBrandScheduleRepublish+","+ Buttons ;} }
				if(Editor_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedScheduleRepublish==""){OthersbrandedScheduleRepublish= Buttons ;}else{OthersbrandedScheduleRepublish=OthersbrandedScheduleRepublish+","+ Buttons ;} }
				
			}else if(rolebased.equalsIgnoreCase("UBC"))	{
				if(UBC_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfScheduleRepublish==""){SelfScheduleRepublish= Buttons ;}else{SelfScheduleRepublish=SelfScheduleRepublish+","+ Buttons ;} }
				if(UBC_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherScheduleRepublish==""){OtherScheduleRepublish= Buttons ;}else{OtherScheduleRepublish=OtherScheduleRepublish+","+ Buttons ;} }
				if(UBC_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandScheduleRepublish==""){selfBrandScheduleRepublish= Buttons ;}else{selfBrandScheduleRepublish=selfBrandScheduleRepublish+","+ Buttons ;}  }
				if(UBC_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedScheduleRepublish==""){OthersbrandedScheduleRepublish= Buttons ;}else{OthersbrandedScheduleRepublish=OthersbrandedScheduleRepublish+","+ Buttons ;}  }
				
			}else if(rolebased.equalsIgnoreCase("UbCol")){
				if(UbCol_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfScheduleRepublish==""){SelfScheduleRepublish= Buttons ;}else{SelfScheduleRepublish=SelfScheduleRepublish+","+ Buttons ;} }
				if(UbCol_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherScheduleRepublish==""){OtherScheduleRepublish= Buttons ;}else{OtherScheduleRepublish=OtherScheduleRepublish+","+ Buttons ;}  }
				if(UbCol_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandScheduleRepublish==""){selfBrandScheduleRepublish= Buttons ;}else{selfBrandScheduleRepublish=selfBrandScheduleRepublish+","+ Buttons ;} }
				if(UbCol_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedScheduleRepublish==""){OthersbrandedScheduleRepublish= Buttons ;}else{OthersbrandedScheduleRepublish=OthersbrandedScheduleRepublish+","+ Buttons ;}  }
				
			}else if(rolebased.equalsIgnoreCase("BC")){
				if(BCord_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfScheduleRepublish==""){SelfScheduleRepublish= Buttons ;}else{SelfScheduleRepublish=SelfScheduleRepublish+","+ Buttons ;}}
				if(BCord_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherScheduleRepublish==""){OtherScheduleRepublish= Buttons ;}else{OtherScheduleRepublish=OtherScheduleRepublish+","+ Buttons ;}  }
				if(BCord_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandScheduleRepublish==""){selfBrandScheduleRepublish= Buttons ;}else{selfBrandScheduleRepublish=selfBrandScheduleRepublish+","+ Buttons ;}  }
				if(BCord_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedScheduleRepublish==""){OthersbrandedScheduleRepublish= Buttons ;}else{OthersbrandedScheduleRepublish=OthersbrandedScheduleRepublish+","+ Buttons ;} }
						
			}else if(rolebased.equalsIgnoreCase("Bcol")){
				if(Bcol_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfScheduleRepublish==""){SelfScheduleRepublish= Buttons ;}else{SelfScheduleRepublish=SelfScheduleRepublish+","+ Buttons ;} }
				if(Bcol_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherScheduleRepublish==""){OtherScheduleRepublish= Buttons ;}else{OtherScheduleRepublish=OtherScheduleRepublish+","+ Buttons ;}  }
				 if(Bcol_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandScheduleRepublish==""){selfBrandScheduleRepublish= Buttons ;}else{selfBrandScheduleRepublish=selfBrandScheduleRepublish+","+ Buttons ;}  }
				 if(Bcol_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedScheduleRepublish==""){OthersbrandedScheduleRepublish= Buttons ;}else{OthersbrandedScheduleRepublish=OthersbrandedScheduleRepublish+","+ Buttons ;}  }
			
			}else if(rolebased.equalsIgnoreCase("admin")){
				if(admin_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfScheduleRepublish==""){SelfScheduleRepublish= Buttons ;}else{SelfScheduleRepublish=SelfScheduleRepublish+","+ Buttons ;} }
				 if(admin_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherScheduleRepublish==""){OtherScheduleRepublish= Buttons ;}else{OtherScheduleRepublish=OtherScheduleRepublish+","+ Buttons ;}  }
				 if(admin_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandScheduleRepublish==""){selfBrandScheduleRepublish= Buttons ;}else{selfBrandScheduleRepublish=selfBrandScheduleRepublish+","+ Buttons ;}}
				 if(admin_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedScheduleRepublish==""){OthersbrandedScheduleRepublish= Buttons ;}else{OthersbrandedScheduleRepublish=OthersbrandedScheduleRepublish+","+ Buttons ;}  }
		}
		}
		else if(types.equalsIgnoreCase("Republish posts") && (rowclass.equalsIgnoreCase("Republish")))
		{
			
			if(rolebased.equalsIgnoreCase("Editor"))
			{
				if(Editor_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfRepublishButton==""){SelfRepublishButton= Buttons ;}else{SelfRepublishButton=SelfRepublishButton+","+ Buttons ;}  }
				 if(Editor_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherRepublishButton==""){OtherRepublishButton= Buttons ;}else{OtherRepublishButton=OtherRepublishButton+","+ Buttons ;}   }
				 if(Editor_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandRepublishButton==""){selfBrandRepublishButton= Buttons ;}else{selfBrandRepublishButton=selfBrandRepublishButton+","+ Buttons ;}  }
				 if(Editor_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedRepublishButton==""){OthersbrandedRepublishButton= Buttons ;}else{OthersbrandedRepublishButton=OthersbrandedRepublishButton+","+ Buttons ;}}
				
			}else if(rolebased.equalsIgnoreCase("UBC"))	{
				
				if(UBC_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfRepublishButton==""){SelfRepublishButton= Buttons ;}else{SelfRepublishButton=SelfRepublishButton+","+ Buttons ;}    }
				 if(UBC_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherRepublishButton==""){OtherRepublishButton= Buttons ;}else{OtherRepublishButton=OtherRepublishButton+","+ Buttons ;}  }
				 if(UBC_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandRepublishButton==""){selfBrandRepublishButton= Buttons ;}else{selfBrandRepublishButton=selfBrandRepublishButton+","+ Buttons ;}  }
				 if(UBC_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedRepublishButton==""){OthersbrandedRepublishButton= Buttons ;}else{OthersbrandedRepublishButton=OthersbrandedRepublishButton+","+ Buttons ;}  }
				
			}else if(rolebased.equalsIgnoreCase("UbCol")){
				if(UbCol_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfRepublishButton==""){SelfRepublishButton= Buttons ;}else{SelfRepublishButton=SelfRepublishButton+","+ Buttons ;}    }
				 if(UbCol_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherRepublishButton==""){OtherRepublishButton= Buttons ;}else{OtherRepublishButton=OtherRepublishButton+","+ Buttons ;}  }
				 if(UbCol_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandRepublishButton==""){selfBrandRepublishButton= Buttons ;}else{selfBrandRepublishButton=selfBrandRepublishButton+","+ Buttons ;}  }
				 if(UbCol_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedRepublishButton==""){OthersbrandedRepublishButton= Buttons ;}else{OthersbrandedRepublishButton=OthersbrandedRepublishButton+","+ Buttons ;}  }
				
			}else if(rolebased.equalsIgnoreCase("BC")){
				if(BCord_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfRepublishButton==""){SelfRepublishButton= Buttons ;}else{SelfRepublishButton=SelfRepublishButton+","+ Buttons ;}    }
				 if(BCord_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherRepublishButton==""){OtherRepublishButton= Buttons ;}else{OtherRepublishButton=OtherRepublishButton+","+ Buttons ;}  }
				 if(BCord_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandRepublishButton==""){selfBrandRepublishButton= Buttons ;}else{selfBrandRepublishButton=selfBrandRepublishButton+","+ Buttons ;}  }
				 if(BCord_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedRepublishButton==""){OthersbrandedRepublishButton= Buttons ;}else{OthersbrandedRepublishButton=OthersbrandedRepublishButton+","+ Buttons ;}   }
						
			}else if(rolebased.equalsIgnoreCase("Bcol")){
				if(Bcol_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfRepublishButton==""){SelfRepublishButton= Buttons ;}else{SelfRepublishButton=SelfRepublishButton+","+ Buttons ;}    }
				 if(Bcol_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherRepublishButton==""){OtherRepublishButton= Buttons ;}else{OtherRepublishButton=OtherRepublishButton+","+ Buttons ;} }
				 if(Bcol_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandRepublishButton==""){selfBrandRepublishButton= Buttons ;}else{selfBrandRepublishButton=selfBrandRepublishButton+","+ Buttons ;}  }
				 if(Bcol_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedRepublishButton==""){OthersbrandedRepublishButton= Buttons ;}else{OthersbrandedRepublishButton=OthersbrandedRepublishButton+","+ Buttons ;}   }
			
			}else if(rolebased.equalsIgnoreCase("admin")){
				if(admin_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfRepublishButton==""){SelfRepublishButton= Buttons ;}else{SelfRepublishButton=SelfRepublishButton+","+ Buttons ;}    }
				 if(admin_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherRepublishButton==""){OtherRepublishButton= Buttons ;}else{OtherRepublishButton=OtherRepublishButton+","+ Buttons ;} }
				 if(admin_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandRepublishButton==""){selfBrandRepublishButton= Buttons ;}else{selfBrandRepublishButton=selfBrandRepublishButton+","+ Buttons ;}  }
				 if(admin_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedRepublishButton==""){OthersbrandedRepublishButton= Buttons ;}else{OthersbrandedRepublishButton=OthersbrandedRepublishButton+","+ Buttons ;}  }
		}
		}
		else if(types.equalsIgnoreCase("Repost Posts") && (rowclass.equalsIgnoreCase("Repost")))
		{
			if(rolebased.equalsIgnoreCase("Editor"))
			{
				if(Editor_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfRepost==""){SelfRepost= Buttons ;}else{SelfRepost=SelfRepost+","+ Buttons ;} }
				if(Editor_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherRepost==""){OtherRepost= Buttons ;}else{OtherRepost=OtherRepost+","+ Buttons ;} }
				 if(Editor_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandRepost==""){selfBrandRepost= Buttons ;}else{selfBrandRepost=selfBrandRepost+","+ Buttons ;}  }
				 if(Editor_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedRepost==""){OthersbrandedRepost= Buttons ;}else{OthersbrandedRepost=OthersbrandedRepost+","+ Buttons ;} }
				
			}else if(rolebased.equalsIgnoreCase("UBC"))	{
				
				if(UBC_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfRepost==""){SelfRepost= Buttons ;}else{SelfRepost=SelfRepost+","+ Buttons ;}   }
				 if(UBC_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherRepost==""){OtherRepost= Buttons ;}else{OtherRepost=OtherRepost+","+ Buttons ;} }
				 if(UBC_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandRepost==""){selfBrandRepost= Buttons ;}else{selfBrandRepost=selfBrandRepost+","+ Buttons ;}   }
				 if(UBC_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedRepost==""){OthersbrandedRepost= Buttons ;}else{OthersbrandedRepost=OthersbrandedRepost+","+ Buttons ;}  }
				
			}else if(rolebased.equalsIgnoreCase("UbCol")){
				if(UbCol_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfRepost==""){SelfRepost= Buttons ;}else{SelfRepost=SelfRepost+","+ Buttons ;}   }
				 if(UbCol_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherRepost==""){OtherRepost= Buttons ;}else{OtherRepost=OtherRepost+","+ Buttons ;} }
				 if(UbCol_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandRepost==""){selfBrandRepost= Buttons ;}else{selfBrandRepost=selfBrandRepost+","+ Buttons ;}   }
				 if(UbCol_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedRepost==""){OthersbrandedRepost= Buttons ;}else{OthersbrandedRepost=OthersbrandedRepost+","+ Buttons ;}  }
				
			}else if(rolebased.equalsIgnoreCase("BC")){
				if(BCord_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfRepost==""){SelfRepost= Buttons ;}else{SelfRepost=SelfRepost+","+ Buttons ;}   }
				 if(BCord_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherRepost==""){OtherRepost= Buttons ;}else{OtherRepost=OtherRepost+","+ Buttons ;}  }
				 if(BCord_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandRepost==""){selfBrandRepost= Buttons ;}else{selfBrandRepost=selfBrandRepost+","+ Buttons ;}   }
				 if(BCord_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedRepost==""){OthersbrandedRepost= Buttons ;}else{OthersbrandedRepost=OthersbrandedRepost+","+ Buttons ;}  }
						
			}else if(rolebased.equalsIgnoreCase("Bcol")){
				if(Bcol_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfRepost==""){SelfRepost= Buttons ;}else{SelfRepost=SelfRepost+","+ Buttons ;}   }
				 if(Bcol_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherRepost==""){OtherRepost= Buttons ;}else{OtherRepost=OtherRepost+","+ Buttons ;} }
				 if(Bcol_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandRepost==""){selfBrandRepost= Buttons ;}else{selfBrandRepost=selfBrandRepost+","+ Buttons ;} }
				 if(Bcol_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedRepost==""){OthersbrandedRepost= Buttons ;}else{OthersbrandedRepost=OthersbrandedRepost+","+ Buttons ;}  }
			
			}else if(rolebased.equalsIgnoreCase("admin")){
				if(admin_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfRepost==""){SelfRepost= Buttons ;}else{SelfRepost=SelfRepost+","+ Buttons ;}   }
				 if(admin_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherRepost==""){OtherRepost= Buttons ;}else{OtherRepost=OtherRepost+","+ Buttons ;}  }
				 if(admin_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandRepost==""){selfBrandRepost= Buttons ;}else{selfBrandRepost=selfBrandRepost+","+ Buttons ;}   }
				 if(admin_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedRepost==""){OthersbrandedRepost= Buttons ;}else{OthersbrandedRepost=OthersbrandedRepost+","+ Buttons ;}  }
			 
		}
		}
		else if(types.equalsIgnoreCase("Normal") && (rowclass.equalsIgnoreCase("Normal")))
		{ 
			if(rolebased.equalsIgnoreCase("Editor"))
			{
				if(Editor_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfNormal==""){SelfNormal= Buttons ;}else{SelfNormal=SelfNormal+","+ Buttons ;}   }
				 if(Editor_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherNormal==""){OtherNormal= Buttons ;}else{OtherNormal=OtherNormal+","+ Buttons ;}}
				 if(Editor_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandNormal==""){selfBrandNormal= Buttons ;}else{selfBrandNormal=selfBrandNormal+","+ Buttons ;} }
				 if(Editor_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedNormal==""){OthersbrandedNormal= Buttons ;}else{OthersbrandedNormal=OthersbrandedNormal+","+ Buttons ;} }
				
			}else if(rolebased.equalsIgnoreCase("UBC"))	{
				
				if(UBC_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfNormal==""){SelfNormal= Buttons ;}else{SelfNormal=SelfNormal+","+ Buttons ;} }
				 if(UBC_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherNormal==""){OtherNormal= Buttons ;}else{OtherNormal=OtherNormal+","+ Buttons ;} }
				 if(UBC_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandNormal==""){selfBrandNormal= Buttons ;}else{selfBrandNormal=selfBrandNormal+","+ Buttons ;} }
				 if(UBC_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedNormal==""){OthersbrandedNormal= Buttons ;}else{OthersbrandedNormal=OthersbrandedNormal+","+ Buttons ;}  }
				 System.out.println(SelfNormal +"==" + OtherNormal +"==" +selfBrandNormal +"=="+  OthersbrandedNormal);
			}else if(rolebased.equalsIgnoreCase("UbCol")){
				if(UbCol_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfNormal==""){SelfNormal= Buttons ;}else{SelfNormal=SelfNormal+","+ Buttons ;}   }
				 if(UbCol_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherNormal==""){OtherNormal= Buttons ;}else{OtherNormal=OtherNormal+","+ Buttons ;} }
				 if(UbCol_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandNormal==""){selfBrandNormal= Buttons ;}else{selfBrandNormal=selfBrandNormal+","+ Buttons ;} }
				 if(UbCol_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedNormal==""){OthersbrandedNormal= Buttons ;}else{OthersbrandedNormal=OthersbrandedNormal+","+ Buttons ;} }
				
			}else if(rolebased.equalsIgnoreCase("BC")){
				if(BCord_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfNormal==""){SelfNormal= Buttons ;}else{SelfNormal=SelfNormal+","+ Buttons ;}   }
				 if(BCord_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherNormal==""){OtherNormal= Buttons ;}else{OtherNormal=OtherNormal+","+ Buttons ;} }
				 if(BCord_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandNormal==""){selfBrandNormal= Buttons ;}else{selfBrandNormal=selfBrandNormal+","+ Buttons ;} }
				 if(BCord_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedNormal==""){OthersbrandedNormal= Buttons ;}else{OthersbrandedNormal=OthersbrandedNormal+","+ Buttons ;} }
						
			}else if(rolebased.equalsIgnoreCase("Bcol")){
				if(Bcol_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfNormal==""){SelfNormal= Buttons ;}else{SelfNormal=SelfNormal+","+ Buttons ;}  }
				 if(Bcol_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherNormal==""){OtherNormal= Buttons ;}else{OtherNormal=OtherNormal+","+ Buttons ;} }
				 if(Bcol_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandNormal==""){selfBrandNormal= Buttons ;}else{selfBrandNormal=selfBrandNormal+","+ Buttons ;} }
				 if(Bcol_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedNormal==""){OthersbrandedNormal= Buttons ;}else{OthersbrandedNormal=OthersbrandedNormal+","+ Buttons ;} }
			
			}else if(rolebased.equalsIgnoreCase("admin")){
				if(admin_Self_unbranded.equalsIgnoreCase("Y"))
				{if(SelfNormal==""){SelfNormal= Buttons ;}else{SelfNormal=SelfNormal+","+ Buttons ;}   }
				 if(admin_Others_unbranded.equalsIgnoreCase("Y"))
				{if(OtherNormal==""){OtherNormal= Buttons ;}else{OtherNormal=OtherNormal+","+ Buttons ;}   }
				 if(admin_Self_branded.equalsIgnoreCase("Y"))
				{if(selfBrandNormal==""){selfBrandNormal= Buttons ;}else{selfBrandNormal=selfBrandNormal+","+ Buttons ;} }
				 if(admin_Others_branded.equalsIgnoreCase("Y"))
				{if(OthersbrandedNormal==""){OthersbrandedNormal= Buttons ;}else{OthersbrandedNormal=OthersbrandedNormal+","+ Buttons ;} }
		}
		}
		
	 
 }
 
@Test
 public void getstatus() throws Exception
 {	
	
		List<WebElement> list = adminProperties.findElementsByXpath(".//*[@id='posts_list']/tr");
		
		if(list.size()>0)
		{
		for (int i =0; i < list.size(); i++) {
			String posttypeviaID= "normal";
			String Comparebutton="";
			
		if((blogroleName.equalsIgnoreCase("UbCol") && (!Authorname.equalsIgnoreCase(postauthor)) && (!adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[1]").getAttribute("class").contains("td-republished")==true)))
		{	List<WebElement> Futurebuttonlist=adminProperties.findElementsByXpath(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[6]/ul/li");
			for(int j=0;j<Futurebuttonlist.size();j++)
			{
				if(Comparebutton=="")
				{Comparebutton= Futurebuttonlist.get(j).getText() ;}else{Comparebutton=Comparebutton+","+ Futurebuttonlist.get(j).getText() ;
				}
			}
		}
		else
		{
			List<WebElement> Futurebuttonlist=adminProperties.findElementsByXpath(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[6]/ul/li");
			for(int j=0;j<Futurebuttonlist.size();j++)
			{
				if(Comparebutton=="")
				{Comparebutton= Futurebuttonlist.get(j).getText() ;}else{Comparebutton=Comparebutton+","+ Futurebuttonlist.get(j).getText() ;
				}
			}
		}
			posttitle=adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]/td[4]/h2/a").getText();
			String postid= adminProperties.findElement("html/body/div[1]/div[2]/div[2]/div[2]/div/div/div[2]/table/tbody/tr[" + (i + 1) + "]").getAttribute("id").replace("row-", "");
			
		    if(!list.get(i).getAttribute("class").equalsIgnoreCase("tr-repost-incoming")){
			 postauthor=adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[3]/a").getText();
				 posttypeviaID=adminProperties.getID(postid);
			 }
	
			System.out.println("postid=="+postid + "  posttypeviaID==" +posttypeviaID);
			System.out.println(list.get(i).getAttribute("class"));
			//For future Scheduled post
			if((list.get(i).getAttribute("class").equalsIgnoreCase("scheduled"))   ||  (list.get(i).getAttribute("class").equalsIgnoreCase("scheduled tomorrow"))  ||  (list.get(i).getAttribute("class").equalsIgnoreCase("scheduled today")))
			{
				if((adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[1]").getAttribute("class").contains("td-republished")==true))
				{//Scheduled Republished Post
				Getactualresult(Comparebutton,"scheduleRepublish",posttypeviaID );
				}
				else 
				{ Getactualresult(Comparebutton, "schedule" ,posttypeviaID);					
				}
				
			}
			else if(list.get(i).getAttribute("class").equalsIgnoreCase("republished"))
			{//Republished Post
			 	Getactualresult(Comparebutton, "republish" ,posttypeviaID);					
			}
			else if(list.get(i).getAttribute("class").equalsIgnoreCase("tr-repost-incoming"))
			{//Repost posts
				if(blogroleName.equalsIgnoreCase("BC")){
				OthersbrandedRepost="Editar,Borrar";
				OtherRepost="Editar,Borrar";
				}
				Getactualresult(Comparebutton, "Repost",posttypeviaID );					
			}
			else 
			{   Comparebutton=Comparebutton.replace("Pasar a borrador", "Rechazar");
				Getactualresult(Comparebutton, "normal",posttypeviaID );					
			}
			System.out.println(" ");
		}
	}	
		else
		{
			System.out.println("No result found");
		}
 } 


public void Getactualresult(String Comparebutton, String posttype , String ptype )
{
	
	System.out.println(Authorname +"==="+(postauthor));
	
	if(Authorname.equalsIgnoreCase(postauthor))
	{
		if(ptype.equalsIgnoreCase("Club")) //Club
		{
			System.out.println("Club post");
			if(posttype.equalsIgnoreCase("schedule"))
			{
				System.out.println(posttitle +"====<b>"+  selfBrandfuture+"</b>====" +Comparebutton +"====" + getbuttonstatus( posttitle,Comparebutton,selfBrandfuture,posttype));
			}else if(posttype.equalsIgnoreCase("scheduleRepublish"))
			{
				System.out.println(posttitle +"====<b>" + selfBrandScheduleRepublish+"</b>====" + Comparebutton +"===="+getbuttonstatus( posttitle,Comparebutton,selfBrandScheduleRepublish,posttype));
			}
			else if(posttype.equalsIgnoreCase("republish"))
			{System.out.println(posttitle +"====<b>" + selfBrandRepublishButton+"</b>====" + Comparebutton +"===="+getbuttonstatus( posttitle,Comparebutton,selfBrandRepublishButton,posttype));
				
			}
			else if(posttype.equalsIgnoreCase("Repost"))
			{System.out.println(posttitle +"====<b>"+ selfBrandRepost+"</b>====" + Comparebutton +"====" +getbuttonstatus( posttitle,Comparebutton,selfBrandRepost,posttype));
				
			}else if(posttype.equalsIgnoreCase("normal"))
			{System.out.println(posttitle +"====<b>" + selfBrandNormal+"</b>====" + Comparebutton +"===="+getbuttonstatus( posttitle,Comparebutton,selfBrandNormal,posttype));
			}
			
	   	}
		else{
			
			if(posttype.equalsIgnoreCase("schedule"))
			{
				System.out.println(posttitle +"====<b>"+ selffuture+"</b>===="+ Comparebutton +"===="  +getbuttonstatus( posttitle,Comparebutton,selffuture,posttype));
			}else if(posttype.equalsIgnoreCase("scheduleRepublish"))
			{
				System.out.println(posttitle +"====<b>" + SelfScheduleRepublish+"</b>====" + Comparebutton +"===="+getbuttonstatus( posttitle,Comparebutton,SelfScheduleRepublish,posttype));
			}
			else if(posttype.equalsIgnoreCase("republish"))
			{System.out.println(posttitle +"====<b>" + SelfRepublishButton+"</b>====" + Comparebutton +"===="+getbuttonstatus( posttitle,Comparebutton,SelfRepublishButton,posttype));
				
			}
			else if(posttype.equalsIgnoreCase("Repost"))
			{System.out.println(posttitle +"====<b>" + SelfRepost+"</b>====" + Comparebutton +"===="+getbuttonstatus( posttitle,Comparebutton,SelfRepost,posttype));
				
			}else if(posttype.equalsIgnoreCase("normal"))
			{
				System.out.println(posttitle +"====<b>"+ SelfNormal+"</b>====" + Comparebutton +"====" +getbuttonstatus( posttitle,Comparebutton,SelfNormal,posttype));
			}
			
			
			}
	}
	else
	{
		if(ptype.equalsIgnoreCase("Club")) //Club
		{
		
			if(posttype.equalsIgnoreCase("schedule"))
			{
				System.out.println(posttitle +"====<b>" + Othersbranded+"</b>====" + Comparebutton +"===="+getbuttonstatus( posttitle,Comparebutton, Othersbranded,posttype));
			}else if(posttype.equalsIgnoreCase("scheduleRepublish"))
			{
				System.out.println(posttitle +"====<b>" + OthersbrandedScheduleRepublish+"</b>===="+ Comparebutton +"====" +getbuttonstatus( posttitle,Comparebutton,OthersbrandedScheduleRepublish,posttype));
			}
			else if(posttype.equalsIgnoreCase("republish"))
			{System.out.println(posttitle +"====<b>" + OthersbrandedRepublishButton+"</b>====" + Comparebutton +"===="+getbuttonstatus( posttitle,Comparebutton,OthersbrandedRepublishButton,posttype));
				
			}
			else if(posttype.equalsIgnoreCase("Repost"))
			{System.out.println(posttitle +"====<b>"+ OthersbrandedRepost+"</b>===="+ Comparebutton +"===="  + getbuttonstatus( posttitle,Comparebutton,OthersbrandedRepost,posttype));
				
			}else if(posttype.equalsIgnoreCase("normal"))
			{
				System.out.println(posttitle +"====<b>" + OthersbrandedNormal+"</b>====" + Comparebutton +"===="+getbuttonstatus( posttitle,Comparebutton,OthersbrandedNormal,posttype));
			}

		}
		else
		{
				
			if(posttype.equalsIgnoreCase("schedule"))
			{
				System.out.println(posttitle +"====<b>" + Otherfuture+"</b>====" + Comparebutton +"===="+getbuttonstatus( posttitle,Comparebutton, Otherfuture,posttype));
			}else if(posttype.equalsIgnoreCase("scheduleRepublish"))
			{
				System.out.println(posttitle +"====<b>"+ OtherScheduleRepublish+"</b>====" + Comparebutton +"====" +getbuttonstatus( posttitle,Comparebutton,OtherScheduleRepublish,posttype));
			}
			else if(posttype.equalsIgnoreCase("republish"))
			{System.out.println(posttitle +"====<b>"+ OtherRepublishButton+"</b>====" + Comparebutton +"====" +getbuttonstatus( posttitle,Comparebutton,OtherRepublishButton,posttype));
				
			}
			else if(posttype.equalsIgnoreCase("Repost"))
			{System.out.println(posttitle +"====<b>" + OtherRepost+"</b>====" + Comparebutton +"===="+getbuttonstatus( posttitle,Comparebutton,OtherRepost,posttype));
				
			}else if(posttype.equalsIgnoreCase("normal"))
			{
				System.out.println(posttitle +"====<b>"+ OtherNormal+"</b>====" + Comparebutton +"====" +getbuttonstatus( posttitle,Comparebutton,OtherNormal,posttype));
			}

		}
	}

}

   public String getbuttonstatus(String posttitle,String Comparebutton, String ExcelComparebutton, String postype )
	{
	   String Status="";
	   String[] arrComparebutton=Comparebutton.split(",");
	   String[] arrExcelComparebutton=ExcelComparebutton.split(",");
	   int invalid=0;
	   int valid=0;
	   int cntmatch=0;
	  if(arrExcelComparebutton.length==arrComparebutton.length)
	  {
	   for(int i=0;i<arrExcelComparebutton.length;i++)
	   {
		   for(int j=0; j<arrComparebutton.length;j++)
		   {
			   if(arrExcelComparebutton[i].equalsIgnoreCase(arrComparebutton[j]))
				{
				   cntmatch=1;
				   break;
				}
		   }
			if(cntmatch==1)
			{valid=valid+1;	}
			else
			{invalid=invalid+1;}
	   }
	  }
	  else
	  {
		  Status="Fail";  
	  }
	  
	   if((invalid==0) && (valid==(arrExcelComparebutton.length)))
	   { Status="Pass";
	   }else
	   {   Status="Fail";
	   }
		//System.out.print( posttitle+"=>");
		
	   return Status;
	}


	public void getuserloginRole() throws Exception
	{
		conn = adminProperties.connectDb();
	    String arrlogin = adminProperties.checkuserlogintype(conn ,prop.getProperty("admin_usename"),
				prop.getProperty("admin_pwd"));
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
