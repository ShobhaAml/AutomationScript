package Frontend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.imageio.IIOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Common.Frontend;

public class Postlist
{

    Frontend frontendProperties = new Frontend();    
    String browser="";
    String url="";
    WebDriver driver ;
    Properties prop=new Properties();
    int pageno=2;
    int columnCount =1;  //Change columnn to add data in new COULUMN for comparision
    int rowCount = 1;
    @BeforeTest()
    public void openbrowser() throws Exception
    {
        prop = frontendProperties.ReadProperties();
        driver = frontendProperties.frontcallproperty(prop.getProperty("url"),
                prop.getProperty("browser"));
        browser = prop.getProperty("browser");
        url=prop.getProperty("url");
    }
    
    @Test
    public void fetchposts() throws IOException, Exception
    {
        String filepath=System.getProperty("user.dir") + "\\src\\Common\\PostListing.xls";

        File file= new File(filepath);
       try
       {
           if(!file.exists())
        {
            file.createNewFile();
        }
       }
       catch(IIOException e)
       {
           
       }
      
      
        frontendProperties.findAndClick("cookie");       
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet =null;
        for(int i=0;i<pageno;i++)
        {
            if(i>0)
            {
                frontendProperties.findAndClick("PageNextbutton");
                frontendProperties.implicitWait();
            }
            else
            {
                sheet=workbook.createSheet("Post List");
            }
            List<WebElement> list = driver.findElements(By.className("abstract-title"));
            System.out.println(list.size());
            
            Iterator rows = sheet.rowIterator();

            System.out.println("Row count: "+rows.hasNext());
            
            for (WebElement element1 : list) {
            System.out.println(element1.getText());
                Row row = sheet.createRow(++rowCount);
               // Cell cell = row.createCell(columnCount);
                Cell cell = row.createCell(5);
                cell.setCellValue((String) element1.getText());
               /* Cell cell2 = row.createCell(2);
                cell2.setCellValue((String) element1.getText());*/
                 try (FileOutputStream outputStream = new FileOutputStream(filepath,false)) {
                    workbook.write(outputStream);
                    outputStream.close();
                }
           }
        }
    }
}
