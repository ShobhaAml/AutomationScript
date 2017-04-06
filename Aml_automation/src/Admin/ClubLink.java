package Admin;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class ClubLink
{
    WebDriver driver;
    Properties prop = new Properties();
    String videoURL = "https://www.youtube.com/watch?v=5Qjfp93sIz0";
    Adminproperty property = new Adminproperty();
    String elements = "clubDisclaimer,clubURL,clubTitle,clubDescription,clubImageURL,clubStartDate,clubStartMonth,clubStartYear,clubStartHour,clubStartMinute,clubEndDate,clubEndMonth,clubEndYear,clubEndHour,clubEndMinute,clubCountry";
    String createData = ",https://www.xataka.com,test title5,test description,40298c/old-scale-1706780_1920/650_1200.jpg,18,2,2018,7,27,19,4,2020,6,29,Argentina@Ecuador@India@Argentina@Colombia";
    String editData = "updated disclaimer5,https://www.gmail.com,updated title2,updated description,https://i.blogs.es/e9c72a/galaxy-s8-composicion/2560_3000.jpg,29,6,2017,8,21,29,8,2017,10,25,India@Colombia";;
    String elementsArr[] = elements.split(",");
    String createArr[] = createData.split(",");
    String editArr[] = editData.split(",");
    String clubName = "Un mes con un HP Envy Ultrabook™";
    String[] country = createArr[15].split("@");
    String[] editCountry = editArr[15].split("@");

    @BeforeTest
    public void Initial() throws Exception
    {
        prop = property.ReadProperties();
        driver = property.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
        property.adminLogin();
        property.implicitWait();
        property.findAndClick("navigation_header");
        property.findAndClick("clubLink");
    }

    public void commonTime(String xpath, String value)
    {
        Select date = new Select(property.findElement(prop.getProperty(xpath)));
        date.selectByValue(value);
    }

    public void commonCountry(String CountryArray[], String element)
    {
        for (int i = 0; i < CountryArray.length; i++) {
            if (CountryArray[i].equals("Todos")) {
                Select countryList = new Select(property.findElement(prop.getProperty(element)));
                countryList.selectByVisibleText("Todos");
                break;
            } else {
                Select countryList = new Select(property.findElement(prop.getProperty(element)));
                countryList.selectByVisibleText(CountryArray[i]);
            }
        }
    }

    @Test(priority = 0, enabled = false)
    public void createClub()
    {
        property.findAndClick("clubCreate");
        property.findAndClick("clubSave");
        List<WebElement> list = driver.findElements(By.cssSelector(".message.error"));
        for (WebElement val : list) {
            System.out.println("List of validation messages-->" + val.getText());
        }
        property.findAndWrite(elementsArr[0], createArr[0]);
        property.findAndWrite(elementsArr[1], createArr[1]);
        property.findAndWrite(elementsArr[2], createArr[2]);
        property.findAndWrite(elementsArr[3], createArr[3]);
        property.findAndWrite(elementsArr[4], createArr[4]);
        commonTime(elementsArr[5], createArr[5]);
        commonTime(elementsArr[6], createArr[6]);
        commonTime(elementsArr[7], createArr[7]);
        commonTime(elementsArr[8], createArr[8]);
        commonTime(elementsArr[9], createArr[9]);
        commonTime(elementsArr[10], createArr[10]);
        commonTime(elementsArr[11], createArr[11]);
        commonTime(elementsArr[12], createArr[12]);
        commonTime(elementsArr[13], createArr[13]);
        commonTime(elementsArr[14], createArr[14]);
        commonCountry(country, "clubCountry");
        property.findAndClick("clubSave");
        property.implicitWait();
        List<WebElement> list1 = driver.findElements(By.cssSelector(".message.error"));
        if ((createArr[1].contains("http://") == false) || (createArr[1].contains("https://") == false)) {
            for (WebElement val1 : list1) {
                if (val1.getText().equalsIgnoreCase("Enlace destino no válida")) {
                    String URL = property.findElement(prop.getProperty(elementsArr[1])).getAttribute("value");
                    property.findElement(prop.getProperty(elementsArr[1])).clear();
                    property.implicitWait();
                    property.findAndWrite(elementsArr[1], "http://" + URL);
                }
            }
        }
        property.findAndClick("clubSave");
        List<WebElement> list2 = driver.findElements(By.cssSelector(".message.error"));
        if (createArr[4].contains("https://i.blogs.es") == false) {
            for (WebElement val2 : list2) {
                if (val2.getText().equalsIgnoreCase("No se permiten imágenes externas")) {
                    property.implicitWait();
                    property.findAndClick(elementsArr[4]);
                    property.implicitWait();
                    property.findElement(prop.getProperty(elementsArr[4])).clear();
                    property.implicitWait();
                    property.findAndWrite(elementsArr[4], editArr[4]);
                }
            }
        }
        property.implicitWait();
        List<WebElement> textList = property.findElementsByXpath(prop.getProperty("textList"));
        for (int r = 0; r < textList.size(); r++) {
            if (property.findElement(prop.getProperty(elementsArr[r])).getAttribute("value").length() < 1) {
                property.findAndWrite(elementsArr[r], editArr[r]);
            }
        }
        property.findAndClick("clubSave");
    }

    @Test(enabled = false)
    public void deleteClub()
    {
        List<WebElement> list = property.findElementsByXpath(prop.getProperty("clubList") + prop.getProperty("clubtd"));
        int cnt = 1;
        for (WebElement element : list) {
            if (element.getText().equalsIgnoreCase(clubName)) {
                property.findElement(prop.getProperty("clubList") + "[" + cnt + "]" + prop.getProperty("clubDelete"))
                        .click();
            }
            cnt++;
        }
    }

    @Test()
    public void editClub() throws AWTException
    {
        List<WebElement> list = property.findElementsByXpath(prop.getProperty("clubList") + prop.getProperty("clubtd"));
        int cnt = 1;
        for (WebElement element : list) {
            if (element.getText().equalsIgnoreCase(clubName)) {
                property.findElement(prop.getProperty("clubList") + "[" + cnt + "]" + prop.getProperty("clubEdit"))
                        .click();
                break;
            }
            cnt++;
        }
        property.implicitWait();
        List<WebElement> textboxes = property.findElementsByXpath(prop.getProperty("textList"));
        for (int ij = 0; ij < textboxes.size(); ij++) {
            property.findAndClick(elementsArr[ij]);
            property.findElement(prop.getProperty(elementsArr[ij])).clear();
            property.findAndWrite(elementsArr[ij], editArr[ij]);
        }
        property.implicitWait();
        Select countryList1 = new Select(property.findElement(prop.getProperty("clubCountry")));
        Robot r = new Robot();
        r.keyPress(KeyEvent.VK_CONTROL);
        r.keyRelease(KeyEvent.VK_CONTROL);
        property.implicitWait();
        countryList1.deselectAll();
        property.implicitWait();
        commonCountry(editCountry, "clubCountry");
        property.implicitWait();
        commonTime(elementsArr[5], editArr[5]);
        commonTime(elementsArr[6], editArr[6]);
        commonTime(elementsArr[7], editArr[7]);
        commonTime(elementsArr[8], editArr[8]);
        commonTime(elementsArr[9], editArr[9]);
        commonTime(elementsArr[10], editArr[10]);
        commonTime(elementsArr[11], editArr[11]);
        commonTime(elementsArr[12], editArr[12]);
        commonTime(elementsArr[13], editArr[13]);
        commonTime(elementsArr[14], editArr[14]);
    }
}
