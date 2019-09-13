package AdminDashboardsanity;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import com.mysql.jdbc.Connection;
import Admin.CheckUserRoles;
import Common.Adminproperty;

public class Dashboard_TODO_search {
	String search_keyword = "Cf post";
	int pageno=1;
	
	
	Connection conn;
	WebDriver driver;
	Adminproperty adminProperties = new Adminproperty();
	// Properties prop = new Properties();
	String browser = "", blogroleName = "", Authorname = "", postauthor = "", posttitle = "", blogrole = "";
	ExcelDashboardButtonSanity es = new ExcelDashboardButtonSanity();
	Properties prop = new Properties();
	String postcontent = "";
	String titleMatch = "0", postcontentMatch = "0";

	@Test
	public void Setup() throws Exception {

		prop = adminProperties.ReadProperties();
		driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
		browser = prop.getProperty("browser");
		adminProperties.implicitWait();
		
		/*WITH DATABASE
		 CheckUserRoles roleType = new CheckUserRoles();
		roleType.openConnection(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
		System.out.println("Welcome " + roleType.blogrole + " " + roleType.blogroleName);
		adminProperties.adminLogin();
		blogroleName = roleType.blogroleName;
		Authorname = roleType.Authorname;
		blogrole = roleType.blogrole;*/
		
		
		/*WITHOUT DB*/
		adminProperties.LoginAdmin(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
		blogroleName="admin";
		Authorname=	"admin";
		blogrole="";
		
	
	}

	@Test
	public void Todosearch() throws Exception {
		
			
		adminProperties.findElement(prop.getProperty("search_field_path")).sendKeys(search_keyword);
		adminProperties.implicitWait();
		adminProperties.findAndClick("search_button");
		Thread.sleep(3000);

		String stype="";
			
			 for(int k=0;k<pageno;k++)
		        {
				 System.out.println("LOOP Count==="+k);
				 

		            if((k > 0))
		            {
		            	System.out.println("hiiii Click NEXT button" + String.valueOf(k+1));
		            	driver.findElement(By.xpath("*//ul[@id='pagination']/li/a[contains(text(),\"2\")]")).click();

		            	Thread.sleep(4000);
		            	WebDriverWait wait = new WebDriverWait(driver, 50);
		        		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='posts_list']/tr")));
		        		
		            }
		           
		            adminProperties.implicitWait();
		            System.out.println(k +"==="+ pageno);
		            List<WebElement> list = adminProperties.findElementsByXpath(".//*[@id='posts_list']/tr");
		    		String category = "";

		    		System.out.println("SIze" + list.size());
		    		if (list.size() > 0) {
		    		
			for (int i = 0; i < list.size(); i++) {
				String posttypeviaID = "normal";
				String Comparebutton = "";

				if ((blogroleName.equalsIgnoreCase("UbCol") && (!Authorname.equalsIgnoreCase(postauthor))
						&& (!adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[1]")
								.getAttribute("class").contains("td-republished") == true))) {
					List<WebElement> Futurebuttonlist = adminProperties
							.findElementsByXpath(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[6]/ul/li");
					for (int j = 0; j < Futurebuttonlist.size(); j++) {
						if (Comparebutton == "") {
							Comparebutton = Futurebuttonlist.get(j).getText();
						} else {
							Comparebutton = Comparebutton + "," + Futurebuttonlist.get(j).getText();
						}
					}
				} else {
					List<WebElement> Futurebuttonlist = adminProperties
							.findElementsByXpath(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[6]/ul/li");
					for (int j = 0; j < Futurebuttonlist.size(); j++) {
						if (Comparebutton == "") {
							Comparebutton = Futurebuttonlist.get(j).getText();
						} else {
							Comparebutton = Comparebutton + "," + Futurebuttonlist.get(j).getText();
						}
					}
				}
				posttitle = adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]/td[4]/h2/a")
						.getText();
				String postid = adminProperties
						.findElement(
								"html/body/div[1]/div[2]/div[2]/div[2]/div/div/div[2]/table/tbody/tr[" + (i + 1) + "]")
						.getAttribute("id").replace("row-", "");

				if (!list.get(i).getAttribute("class").equalsIgnoreCase("tr-repost-incoming")) {
					postauthor = adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[3]/a")
							.getText();
					System.out.println("hii====" +postid);
					posttypeviaID = adminProperties.getID(postid);
					category = adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[4]/p/a[1]")
							.getText(); // Ecommerce
					
					System.out.println(category);
					// System.out.println(category,titleMatch,postcontentMatch,status);
				
					System.out.println(adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[1]").getAttribute("class"));	
					if(adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[1]").getAttribute("class").contains("td-respuesta"))
					{
						stype="respuesta";
					}
					else if(category.equalsIgnoreCase("Ecommerce"))
					{
						stype="ecom";
					}
					else
					{
						stype="normal";
					}
				}
				else 
				{
					stype="repost";
				}
				
				if (category.equalsIgnoreCase("Especial Branded")) {
					posttypeviaID = "Club";
				}
				
				String tresult ="",presult="",titleMatch="0",postcontentMatch="0";
				if(adminProperties.findMatch(search_keyword, posttitle)!="")
				{
					String[] titlematch1=adminProperties.findMatch(search_keyword, posttitle).split(",");
					titleMatch = String.valueOf(titlematch1.length);
					if(titlematch1.length>0)
					{
					 tresult = adminProperties.findMatch(search_keyword, posttitle);
					}
				}
				
				
				
				postcontent = adminProperties.getPostcontent(postid, stype,
						adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]/td[4]/h2/a").getAttribute("href"));
			
				if(adminProperties.findMatch(search_keyword, postcontent)!="")
				{
					String[] postcontentMatch1=adminProperties.findMatch(search_keyword, postcontent).split(",");
					 postcontentMatch = String.valueOf(postcontentMatch1.length);
					if(Integer.parseInt(postcontentMatch)>0)
					{
						presult= adminProperties.findMatch(search_keyword, postcontent);
					}
				}
				
				System.out
						.println("postid==" + postid + "  posttypeviaID==" + posttypeviaID + "  category=" + category);
				System.out.println(list.get(i).getAttribute("class"));

				// For future Scheduled post
				if ((list.get(i).getAttribute("class").equalsIgnoreCase("scheduled"))
						|| (list.get(i).getAttribute("class").equalsIgnoreCase("scheduled tomorrow"))
						|| (list.get(i).getAttribute("class").equalsIgnoreCase("scheduled today"))) {
					if ((adminProperties.findElement(".//*[@id='posts_list']/tr[" + (i + 1) + "]//td[1]")
							.getAttribute("class").contains("td-republished") == true)) {// Scheduled
																							// Republished
																							// Post
						Getactualresult(Comparebutton, "scheduleRepublish", posttypeviaID, category, titleMatch,
								postcontentMatch,tresult,presult);
					} else {
						Getactualresult(Comparebutton, "schedule", posttypeviaID, category, titleMatch,
								postcontentMatch,tresult,presult);
					}

				} else if (list.get(i).getAttribute("class").equalsIgnoreCase("republished")) {// Republished Post
					Getactualresult(Comparebutton, "republish", posttypeviaID, category, titleMatch, postcontentMatch,tresult,presult);
				} else if (list.get(i).getAttribute("class").equalsIgnoreCase("tr-repost-incoming")) {// Repost
																										// posts

					Getactualresult(Comparebutton, "Repost", posttypeviaID, category, titleMatch, postcontentMatch,tresult,presult);
				} else {
					//System.out.println("NORMAL" + Comparebutton + "====" + postcontentMatch);
					Comparebutton = Comparebutton.replace("Pasar a borrador", "Rechazar");
					Getactualresult(Comparebutton, "normal", posttypeviaID, category, titleMatch, postcontentMatch,tresult,presult);
				}
				System.out.println(" ");
				
					
			}
			
		    		}else {
		    			System.out.println("No result found");
		    		}
		} 
			 
		
		
	}

	public void Getactualresult(String Comparebutton, String posttype, String ptype, String category, String titleMatch,
			String postcontentMatch,String tresult,String presult) throws IOException {
		String[] valueToWrite;
		String fileName = "DashboardButton.xlsx";
		String sheetName = "TODOSearch";
		//System.out.println("Result Match"+ "===" + (tresult) +"===="+presult);
		String status = "Fail";

		int total = Integer.parseInt(titleMatch) + Integer.parseInt(postcontentMatch);
		if (total > 0) {
			status = "Pass";
		}

		if (Authorname.equalsIgnoreCase(postauthor)) {
			if (ptype.equalsIgnoreCase("Club")) // Club
			{
				// System.out.println("Club post");
				if (posttype.equalsIgnoreCase("schedule")) {
					System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
							+ postcontentMatch + "===" + status + "===" + status);
					valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype, Comparebutton,
							titleMatch, postcontentMatch,tresult,presult, status };
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
				} else if (posttype.equalsIgnoreCase("scheduleRepublish")) {
					System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
							+ postcontentMatch + "===" + status);
					valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype, Comparebutton,
							titleMatch, postcontentMatch,tresult,presult, status };
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
				} else if (posttype.equalsIgnoreCase("republish")) {
					System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
							+ postcontentMatch + "===" + status);
					valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype, Comparebutton,
							titleMatch, postcontentMatch,tresult,presult, status };
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
				} else if (posttype.equalsIgnoreCase("Repost")) {
					System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
							+ postcontentMatch + "===" + status);
					valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype, Comparebutton,
							titleMatch, postcontentMatch,tresult,presult, status };
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
				} else if (posttype.equalsIgnoreCase("normal")) {
					System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
							+ postcontentMatch + "===" + status);
					valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype, Comparebutton,
							titleMatch, postcontentMatch,tresult,presult, status };
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
				}

			} else {

				if ((category.equalsIgnoreCase("Ecommerce")) || (category.equalsIgnoreCase("default"))) {
					if (posttype.equalsIgnoreCase("schedule")) {
						System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
								+ postcontentMatch + "===" + status);
						valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype, ptype,
								Comparebutton, titleMatch, postcontentMatch,tresult,presult, status };
					} else {

						System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
								+ postcontentMatch + "===" + status);
						valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype,
								Comparebutton, titleMatch, postcontentMatch,tresult,presult, status };
					}
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);

				} else {

					if (posttype.equalsIgnoreCase("schedule")) {
						System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
								+ postcontentMatch + "===" + status);
						valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype,
								Comparebutton, titleMatch, postcontentMatch,tresult,presult, status };
						adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					} else if (posttype.equalsIgnoreCase("scheduleRepublish")) {
						System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
								+ postcontentMatch + "===" + status);
						valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype,
								Comparebutton, titleMatch, postcontentMatch,tresult,presult, status };
						adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					} else if (posttype.equalsIgnoreCase("republish")) {
						System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
								+ postcontentMatch + "===" + status);
						valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype,
								Comparebutton, titleMatch, postcontentMatch,tresult,presult, status };
						adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					} else if (posttype.equalsIgnoreCase("Repost")) {
						System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
								+ postcontentMatch + "===" + status);
						valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype,
								Comparebutton, titleMatch, postcontentMatch,tresult,presult, status };
						adminProperties.writeExcel(fileName, sheetName, valueToWrite);

					} else if (posttype.equalsIgnoreCase("normal")) {
						System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
								+ postcontentMatch + "===" + status);
						valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype,
								Comparebutton, titleMatch, postcontentMatch,tresult,presult, status };
						adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					}
				}

			}
		} else {
			if (ptype.equalsIgnoreCase("Club")) // Club
			{

				if (posttype.equalsIgnoreCase("schedule")) {
					System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
							+ postcontentMatch + "===" + status);
					valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype, Comparebutton,
							titleMatch, postcontentMatch,tresult,presult, status };
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
				} else if (posttype.equalsIgnoreCase("scheduleRepublish")) {
					System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
							+ postcontentMatch + "===" + status);
					valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype, Comparebutton,
							titleMatch, postcontentMatch,tresult,presult, status };
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
				} else if (posttype.equalsIgnoreCase("republish")) {
					System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
							+ postcontentMatch + "===" + status);
					valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype, Comparebutton,
							titleMatch, postcontentMatch,tresult,presult, status };
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
				} else if (posttype.equalsIgnoreCase("Repost")) {
					System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
							+ postcontentMatch + "===" + status);
					valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype, Comparebutton,
							titleMatch, postcontentMatch,tresult,presult, status };
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
				} else if (posttype.equalsIgnoreCase("normal")) {
					System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
							+ postcontentMatch + "===" + status);
					valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype, Comparebutton,
							titleMatch, postcontentMatch,tresult,presult, status };
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);
				}

			} else {
				if ((category.equalsIgnoreCase("Ecommerce")) || (category.equalsIgnoreCase("default"))) {
					if (posttype.equalsIgnoreCase("schedule")) {
						System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
								+ postcontentMatch + "===" + status);
						valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype,
								Comparebutton, titleMatch, postcontentMatch,tresult,presult, status };

					} else {

						System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
								+ postcontentMatch + "===" + status);
						valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype,
								Comparebutton, titleMatch, postcontentMatch,tresult,presult, status };
					}
					adminProperties.writeExcel(fileName, sheetName, valueToWrite);

				} else {
					if (posttype.equalsIgnoreCase("schedule")) {
						System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
								+ postcontentMatch + "===" + status);
						valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype,
								Comparebutton, titleMatch, postcontentMatch,tresult,presult, status };
						adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					} else if (posttype.equalsIgnoreCase("scheduleRepublish")) {
						System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
								+ postcontentMatch + "===" + status);
						valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype,
								Comparebutton, titleMatch, postcontentMatch,tresult,presult, status };
						adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					} else if (posttype.equalsIgnoreCase("republish")) {
						System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
								+ postcontentMatch + "===" + status);
						valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype,
								Comparebutton, titleMatch, postcontentMatch,tresult,presult, status };
						adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					} else if (posttype.equalsIgnoreCase("Repost")) {
						System.out.println(posttitle + "=====" + Comparebutton + "====" + titleMatch + "==="
								+ postcontentMatch + "===" + status);
						valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype,
								Comparebutton, titleMatch, postcontentMatch,tresult,presult, status };
						adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					} else if (posttype.equalsIgnoreCase("normal")) {
						System.out.println(posttitle + "====" + Comparebutton + "====" + titleMatch + "==="
								+ postcontentMatch + "===" + status);
						valueToWrite = new String[] { blogrole, posttitle, Authorname, postauthor, posttype,
								Comparebutton, titleMatch, postcontentMatch,tresult,presult, status };
						adminProperties.writeExcel(fileName, sheetName, valueToWrite);
					}
				}
			}
		}

	}

}
