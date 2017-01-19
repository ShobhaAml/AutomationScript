package PostCreation;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class EditEcommercePost
{

    Adminproperty adminproperties = new Adminproperty();
    Properties prop = new Properties();
    WebDriver driver;
    int cnt = 1;

    @Test(groups = { "EcommerceMovetoDraft", "EcommercewithoutHomeMovetoDraft" }, priority = 1)
    public void Draftpublishedpost() throws Exception
    {
        String Postname = "Automated eCommerce Post with homepage Image/content";
        prop = adminproperties.ReadProperties();
        driver = adminproperties.callproperty(prop.getProperty("url"),
                prop.getProperty("browser"));
        adminproperties.adminLogin();
        List<WebElement> postlist = adminproperties.findElementsByXpath(prop
                .getProperty("Dashboardlist"));
        for (WebElement list : postlist) {
            System.out.println(list.getText());
            if (list.getText().equalsIgnoreCase(Postname)) {

                adminproperties.findElement(
                        prop.getProperty("DashboardEditbuttontr") + "[" + cnt
                                + "]"
                                + prop.getProperty("DashboardDraftEcomtd"))
                        .click();
                break;
            }
            cnt++;
        }

        driver.switchTo().alert().accept();
    }

    @Test(groups = { "CreateAndEdit", "CreateAndEditWithouthomapageImage" }, priority = 0)
    public void Editpublishedpost() throws Exception
    {
        String Postname = "Automated eCommerce Post without homepage Image/content";

        prop = adminproperties.ReadProperties();
        driver = adminproperties.callproperty(prop.getProperty("url"),
                prop.getProperty("browser"));
        adminproperties.adminLogin();
        List<WebElement> postlist = adminproperties.findElementsByXpath(prop
                .getProperty("Dashboardlist"));
        for (WebElement list : postlist) {
            System.out.println(list.getText());
            if (list.getText().equalsIgnoreCase(Postname)) {
                System.out.println(cnt);
                adminproperties.findElement(
                        prop.getProperty("DashboardEditbuttontr") + "[" + cnt
                                + "]"
                                + prop.getProperty("DashboardEditbuttontd"))
                        .click();
                break;
            }
            cnt++;
        }

        adminproperties.implicitWait();
        adminproperties.findAndClick(prop.getProperty("post_content"));
        adminproperties.findAndSendkey(prop.getProperty("post_content"),
                Keys.END);
        adminproperties.findAndSendkey(prop.getProperty("post_content"),
                Keys.ENTER);
        adminproperties.findAndWrite(prop.getProperty("post_content"),
                "Added new Data by Shobha  to test Edit feature");
        adminproperties.findAndClick(prop.getProperty("post_title"));
    }

}
