package Admin;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class CreateTables
{
    WebDriver driver;
    Adminproperty adminProperties = new Adminproperty();
    Properties prop = new Properties();
    String browser = "";

    @BeforeMethod
    public void Setup() throws Exception
    {
        prop = adminProperties.ReadProperties();
        driver = adminProperties.callproperty(prop.getProperty("url"),
                prop.getProperty("browser"));
        browser = prop.getProperty("browser");
    }

    @DataProvider(name = "tabledata")
    public Object[][] TestDataFeed() throws Exception
    {
        Object[][] postdata = adminProperties.readExcel("tables", 6);
        return postdata;
    }

    @Test(dataProvider = "tabledata")
    public void createTable(String tablecnt, String tabledata,
            String Checkbox_same_width,
            String Checkbox_table_first_row_heading,
            String Checkbox_table_first_column_heading,
            String Checkbox_table_occupy_all_avaiable_width) throws Exception
    {
        adminProperties.adminLogin();
        adminProperties.findAndClick("navigation_header");
        adminProperties.findAndClick("create_post_link");
        adminProperties.findAndWrite("post_title", "Table testing");
        adminProperties.findAndWrite("post_content", "Table testing");
        adminProperties.findAndClick("post_title");
        adminProperties.implicitWait();
        System.out.println(tabledata);
        adminProperties.addTable(tabledata, Checkbox_same_width,
                Checkbox_table_first_row_heading,
                Checkbox_table_first_column_heading,
                Checkbox_table_occupy_all_avaiable_width);
    }
}
