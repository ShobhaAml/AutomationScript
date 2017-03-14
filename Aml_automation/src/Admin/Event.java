package Admin;

import java.util.List;
import java.util.Properties;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import Common.Adminproperty;

public class Event
{
    WebDriver driver;
    Properties prop = new Properties();
    String videoURL = "https://www.youtube.com/watch?v=5Qjfp93sIz0";
    Adminproperty property = new Adminproperty();
    String eventData = "new event,21//6//2019//15//10,test lugar,test hashtag,https://www.youtube.com,test event content";
    String editEvent = "updated title,11,3,2020,25,20,test lugar1,test hashtag1,https://www.youtube.com/sbc,test event content test event content";
    String elements = "eventTitle,eventDay,eventMonth,eventYear,eventHour,eventMinute,eventLugar,eventHashtag,eventVideo,eventContent";
    String eventName = "event5";

    @BeforeTest
    public void Initial() throws Exception
    {
        prop = property.ReadProperties();
        driver = property.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
        property.adminLogin();
        property.implicitWait();
        property.findAndClick("navigation_header");
        property.findAndClick("eventLink");
    }

    @Test(priority = 0)
    public void createEvent()
    {
        String[] eventArr = eventData.split(",");
        property.findAndClick("createEvent");
        property.findAndWrite("eventTitle", eventArr[0]);
        String timeArr[] = eventArr[1].split("//");
        Select day = new Select(property.findElement(prop.getProperty("eventDay")));
        day.selectByValue(timeArr[0]);
        Select month = new Select(property.findElement(prop.getProperty("eventMonth")));
        month.selectByValue(timeArr[1]);
        Select year = new Select(property.findElement(prop.getProperty("eventYear")));
        year.selectByValue(timeArr[2]);
        Select hour = new Select(property.findElement(prop.getProperty("eventHour")));
        hour.selectByValue(timeArr[3]);
        Select minute = new Select(property.findElement(prop.getProperty("eventMinute")));
        minute.selectByValue(timeArr[4]);
        property.findAndWrite("eventLugar", eventArr[2]);
        property.findAndWrite("eventHashtag", eventArr[3]);
        property.findAndWrite("eventBrowse",
                System.getProperty("user.dir") + prop.getProperty("image_path") + "\\car.jpg");
        property.findAndWrite("eventVideo", eventArr[4]);
        property.findAndWrite("eventContent", eventArr[5]);
        property.findAndClick("eventSave");
    }

    @Test(priority = 1, enabled = false)
    public void deleteEvent()
    {
        int cnt = 1;
        List<WebElement> list = property.findElementsByXpath(prop.getProperty("eventList") + "/td[1]");
        for (WebElement element : list) {
            if (element.getText().equalsIgnoreCase(eventName)) {
                property.findElement(prop.getProperty("eventList") + "[" + cnt + "]" + prop.getProperty("eventActions")
                        + prop.getProperty("eventDel")).click();
                break;
            }
            cnt++;
        }
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

    @Test(priority = 2, enabled = false)
    public void editEvent()
    {

        int cnt = 1;
        List<WebElement> list = property.findElementsByXpath(prop.getProperty("eventList") + "/td[1]");
        for (WebElement element : list) {
            if (element.getText().equalsIgnoreCase(eventName)) {
                property.findElement(prop.getProperty("eventList") + "[" + cnt + "]" + prop.getProperty("eventActions")
                        + prop.getProperty("eventEdit")).click();
                break;
            }
            cnt++;
        }
        String EventArr[] = editEvent.split(",");
        String elementsArr[] = elements.split(",");
        for (int i = 0; i < elementsArr.length; i++) {
            if (property.findElement(prop.getProperty(elementsArr[i])).getAttribute("value").length() < 1) {
                property.findAndWrite(elementsArr[i], EventArr[i]);
            }
        }
        if (property.findElement(prop.getProperty(("eventBackgroundImage"))).getText().contains("Borrar archivo")) {
            System.out.println("Image present");
            property.findAndClick("eventImageDel");
            property.findAndWrite("editEventBrowse",
                    System.getProperty("user.dir") + prop.getProperty("image_path") + "\\car.jpg");
            property.findAndClick("eventImageUpload");
            property.implicitWait();
        } else {
            System.out.println("not present");
            property.findAndWrite("editEventBrowse",
                    System.getProperty("user.dir") + prop.getProperty("image_path") + "\\slide1.jpg");
            property.findAndClick("eventImageUpload");
        }
        property.Conditionalwait("eventSaveValidation");
        if (property.findElement(prop.getProperty("eventSaveValidation")).getText().contains("Guardado")) {
            System.out.println("event is saved");
        }

    }
}
