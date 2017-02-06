package Common;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Adminproperty
{

    WebDriver driver;
    Properties prop = new Properties();

    public Properties ReadProperties() throws IOException
    {
        FileInputStream inStream = new FileInputStream(
                System.getProperty("user.dir")
                        + "\\src\\Common\\admin.properties");
        prop.load(inStream);
        return prop;
    }

    public WebDriver callproperty(String url, String browser)
            throws IOException
    {
        if (browser.trim().equalsIgnoreCase("Chrome")) {
            System.setProperty("webdriver.chrome.driver",
                    System.getProperty("user.dir") + "//src//Driverfiles//"
                            + "chromedriver.exe");
            driver = new ChromeDriver();
        } else {
            System.setProperty("webdriver.gecko.driver",
                    System.getProperty("user.dir") + "//src//Driverfiles//"
                            + "geckodriver.exe");
            driver = new FirefoxDriver();
        }
        driver.get(url);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        return driver;
    }

    public void uploadPrimaryImage(String primaryimage, String browser)
            throws Exception
    {

        String primaryimagearr[] = primaryimage.split(",");
        for (int i = 0; i < primaryimagearr.length; i++) {
            if ((primaryimagearr[i].contains(".gif"))
                    || (primaryimagearr[i].contains(".GIF"))) {
                findAndClick("toolbar_more");
                implicitWait();
            }
            findAndClick("toolbar_image");
            if (browser.trim().equalsIgnoreCase("firefox")) {
                findAndClick("post_content");
            }
            addNewlines();
            implicitWait();
            findAndWrite("primary_image_insert", System.getProperty("user.dir")
                    + prop.getProperty("image_path") + "\\"
                    + primaryimagearr[i]);
            findAndClick("primary_image_upload");
        }

        WebElement element1 = findElement(prop
                .getProperty("product_image_bulkupload")
                + prop.getProperty("product_image_bulkupload1"));
        if (element1.getAttribute("href") != null) {
            isLinkBroken(new URL(element1.getAttribute("href")));
            System.out.println(isLinkBroken(new URL(element1
                    .getAttribute("href"))));
        }
        implicitWait();
        findAndClick("primary_noraml_insert");
        implicitWait();

    }

    public void findAndClick(String element)
    {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        WebElement element1 = wait.until(ExpectedConditions
                .elementToBeClickable(By.xpath(prop.getProperty(element))));
        implicitWait();
        findElement(prop.getProperty(element)).click();
    }

    public void findAndWrite(String element, String content)
    {
        findElement(prop.getProperty(element)).sendKeys(content);
    }

    public void findAndSendkey(String element, Keys end)
    {
        findElement(prop.getProperty(element)).sendKeys(end);
    }

    public void adminLogin() throws Exception
    {
        String username = prop.getProperty("admin_usename");
        String pwd = prop.getProperty("admin_pwd");
        findElement(prop.getProperty("login_username_txt")).sendKeys(username);
        findElement(prop.getProperty("login_pwd_txt")).sendKeys(pwd);
        findElement(prop.getProperty("login_submit_button")).click();
    }

    public WebElement findElement(String xpath)
    {
        WebElement element = driver.findElement(By.xpath(xpath));
        return element;
    }

    public List<WebElement> findElementByClass(String className)
    {
        List<WebElement> element = driver.findElements(By.className(className));
        return element;
    }

    public List<WebElement> findElementsByXpath(String xpath)
    {
        List<WebElement> element = driver.findElements(By.xpath(xpath));
        return element;
    }

    public void implicitWait()
    {
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.SECONDS);
    }

    public static String isLinkBroken(URL url) throws Exception
    {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            connection.connect();
            String response = connection.getResponseMessage();
            connection.disconnect();
            return response;
        } catch (Exception exp) {
            return exp.getMessage();
        }
    }

    public void verifyImageActive(WebElement imgElement)
    {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(imgElement.getAttribute("src"));
            HttpResponse response = client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFbTwitterText(String fbtext, String twitter_text)
    {
        implicitWait();
        if (!(fbtext).equalsIgnoreCase("null")) {
            findElement(prop.getProperty("fb_text")).sendKeys(fbtext);
        }

        if (!(twitter_text).equalsIgnoreCase("null")) {
            findElement(prop.getProperty("twitter_text")).clear();
            findElement(prop.getProperty("twitter_text"))
                    .sendKeys(twitter_text);
        }

        findElement(prop.getProperty("publish_post")).click();
        implicitWait();
        System.out.println("Published post");
    }

    public void insertTagAndCategory(String postcatagory, String tag)
    {
        if (postcatagory != "") {
            findAndClick("Catagory_click");
            findAndWrite("catagory", postcatagory);
            List<WebElement> optionlist = findElementByClass(prop
                    .getProperty("catagory_ecommerce_by_ClassName"));
            for (WebElement options : optionlist) {
                if (options.getText().equalsIgnoreCase(postcatagory)) {
                    options.click();
                    break;
                }
            }
        }
        if (tag != "") {
            findAndWrite("tag_input", tag);
            List<WebElement> Tagoptionlist = findElementByClass(prop
                    .getProperty("tag_list_Byclassname"));
            for (WebElement options : Tagoptionlist) {
                if (options.getText().equalsIgnoreCase(tag)) {
                    options.click();
                    break;
                }
            }
        }

    }

    public void Imagestatus(WebElement element) throws Exception
    {
        if (element.getAttribute("href") != null) {
            isLinkBroken(new URL(element.getAttribute("href")));
            System.out.println(isLinkBroken(new URL(element
                    .getAttribute("href"))));
        }
    }

    public void Conditionalwait(String xpath)
    {
        WebDriverWait wait = new WebDriverWait(driver, 50);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop
                .getProperty(xpath))));
    }

    public Boolean clickButton(String row, String Column, String Postname,
            String listtype)
    {
        int cnt = 1;
        Boolean status = false;
        List<WebElement> postlist = null;
        if (listtype == "Dashboardlist") {
            postlist = findElementsByXpath(prop.getProperty("Dashboardlist"));
        } else if (listtype == "Draft") {
            postlist = findElementsByXpath(prop
                    .getProperty("Draftlist_dashboard"));

        }

        for (WebElement list : postlist) {
            System.out.println(list.getText());
            if (list.getText().equalsIgnoreCase(Postname)) {
                if (listtype == "Dashboardlist") {
                    findElement(
                            prop.getProperty(row) + "[" + cnt + "]"
                                    + prop.getProperty(Column)).click();
                } else if (listtype == "Draft") {

                    Actions action = new Actions(driver);
                    action.moveToElement(
                            findElement(prop.getProperty(row) + "[" + cnt + "]"
                                    + "/a")).perform();

                    findElement(
                            prop.getProperty(row) + "[" + cnt + "]"
                                    + prop.getProperty(Column)).click();
                }

                status = true;
                break;
            }

            cnt++;
        }

        return status;
    }

    public String getcatagoryname(String row, String Column, String Postname)
    {
        String catgoryname = "";
        int cnt = 1;
        Boolean status = false;
        List<WebElement> postlist = findElementsByXpath(prop
                .getProperty("Dashboardlist"));
        for (WebElement list : postlist) {
            System.out.println(list.getText());
            if (list.getText().equalsIgnoreCase(Postname)) {

                catgoryname = findElement(
                        prop.getProperty(row) + "[" + cnt + "]"
                                + prop.getProperty(Column)).getText();
                break;
            }

            cnt++;
        }

        return catgoryname;
    }

    public void repostCheckbox(String selectors)
    {
        String Selector[] = selectors.split(",");
        for (int i = 0; i < Selector.length; i++) {
            WebElement CheckBox1 = driver.findElement(By
                    .cssSelector("input[value='" + Selector[i] + "']"));
            CheckBox1.click();
        }

    }

    public void summaryActuallization(String summary_data,
            String actuallization_data, String summary_layout) throws Exception
    {

        if (!summary_data.equalsIgnoreCase("null")) {

            findElement(prop.getProperty("toolbar_summary")).click();
            findElement(prop.getProperty("summary_input_field")).sendKeys(
                    summary_data);
            Thread.sleep(3000);
            implicitWait();
            switch (summary_layout) {
            case "left":
                implicitWait();
                findElement(prop.getProperty("summary_insert_left")).click();
                break;
            case "right":
                implicitWait();
                findElement(prop.getProperty("summary_insert_right")).click();
                break;
            case "center":
                implicitWait();
                findElement(prop.getProperty("summary_insert_center")).click();
                break;
            }
            implicitWait();
            addNewlines();
        }

        if (!actuallization_data.equalsIgnoreCase("null")) {

            findElement(prop.getProperty("toolbar_Advance")).click();
            findElement(prop.getProperty("toolbar_actuallization")).click();

            findElement(prop.getProperty("actuallization_input_field"))
                    .sendKeys(actuallization_data);

            implicitWait();
            findElement(prop.getProperty("actuallization_insert_button"))
                    .click();
            implicitWait();
            addNewlines();
        }
    }

    public void repost_By_Difundir(String Selector1, String Selector2,
            String tittle_data, String navigate_blog) throws Exception
    {
        findElement(prop.getProperty("difundir_Link")).click();
        findElement(prop.getProperty("repost_list_button")).click();
        implicitWait();
        repostCheckbox(Selector1);
        implicitWait();
        findElement(prop.getProperty("repost_post_button")).click();
        Thread.sleep(3000);
        driver.navigate().to(navigate_blog);
        adminLogin();
        implicitWait();
        Thread.sleep(5000);
        driver.navigate().refresh();
        driver.navigate().refresh();
        driver.navigate().refresh();
        implicitWait();
        findElement(prop.getProperty("notification_button")).click();
        implicitWait();
        clickNotificationButton("tittle_data");
    }

    public void clickNotificationButton(String tittle_data)
    {
        int cnt = 1;
        List<WebElement> postlist = findElementByClass(prop
                .getProperty("notification_list_by_ClassName"));
        for (WebElement list : postlist) {
            String sender = driver.findElement(
                    By.className(prop.getProperty("notify_sender"))).getText();
            String text = list.getText().replace(sender, "");
            if (text.trim().equalsIgnoreCase(tittle_data)) {
                System.out.println(cnt + "hi");
                Actions act = new Actions(driver);
                act.doubleClick(
                        driver.findElement(By.className(prop
                                .getProperty("notify_sender")))).build()
                        .perform();
                driver.findElement(By.className("actions-approve")).click();
                break;
            }
            cnt++;
        }
    }

    public void HomePageContent()
    {
        findAndWrite("Homepagecontent", "Homepagetext");
    }

    public void videoHandle(String videoURL, String layout)
    {
        findAndClick("toolbar_video");
        findAndWrite("Video_URL", videoURL);
        if (videoURL.contains("youtube")) {
            switch (layout) {
            case "normal":
                findAndClick("Video_NormalLayout");
                break;
            case "big":
                findAndClick("Video_Biglayout");
                break;
            }
            findAndClick("Youtube_button");

        } else if (videoURL.contains("vimeo")) {
            switch (layout) {
            case "normal":
                findAndClick("Video_NormalLayout");
                break;
            case "big":
                findAndClick("Video_Biglayout");
                break;
            }
            findAndClick("Vimeo_button");

        } else {
            switch (layout) {
            case "normal":
                findAndClick("Video_NormalLayout");
                break;
            case "big":
                findAndClick("Video_Biglayout");
                break;
            }
            findAndClick("Vine_button");

        }
        implicitWait();
        addNewline();
    }

    public void insertBrandedClub(String BrandedClubName, String tag)
    {
        findAndClick("BrandedClub_Click");
        findAndWrite("BrandedClub_InputBox", BrandedClubName);
        List<WebElement> optionlist = findElementByClass(prop
                .getProperty("BrandedClub_List_by_ClassName"));

        for (WebElement options : optionlist) {
            if (options.getText().equalsIgnoreCase(BrandedClubName)) {
                options.click();
                break;
            }
        }
        findAndWrite("tag_input", tag);
        List<WebElement> Tagoptionlist = findElementByClass(prop
                .getProperty("tag_list_Byclassname"));
        for (WebElement options : Tagoptionlist) {
            if (options.getText().equalsIgnoreCase(tag)) {
                options.click();
                break;
            }
        }
    }

    public void addslides(String slides, String browser) throws IOException,
            Exception
    {
        String slidesarr[] = slides.split("~");
        findAndClick("toolbar_slideshow");
        implicitWait();

        for (int i = 0; i < slidesarr.length; i++) {
            String slidedetails[] = slidesarr[i].split("@##@");
            System.out.println(slidedetails[0]);
            Conditionalwait("slide_button");
            implicitWait();
            if (slidedetails[0].contains("youtube")) {
                findAndClick("slide_video_button");
                findAndWrite("slide_video_url", slidedetails[0]);
            } else {
                findAndClick("slideshow_addimage");
                findAndClick("slideshow_input_image");
                implicitWait();

                if (browser.trim().equalsIgnoreCase("Chrome")) {
                    Runtime.getRuntime().exec(
                            System.getProperty("user.dir")
                                    + "\\src\\DriverFiles\\fileupload.exe"
                                    + " " + System.getProperty("user.dir")
                                    + "\\src\\Images\\" + slidedetails[0]);
                } else {
                    Runtime.getRuntime()
                            .exec(System.getProperty("user.dir")
                                    + "\\src\\DriverFiles\\firefoxfileupload.exe"
                                    + " " + System.getProperty("user.dir")
                                    + "\\src\\Images\\" + slidedetails[0]);
                }

            }

            implicitWait();
            findAndWrite("Slideshow_subtitle", slidedetails[1]);
            findAndWrite("Slideshow_desc", slidedetails[2]);
            findAndClick("slide_button");
            Thread.sleep(4000);
            findAndSendkey("post_content", Keys.ENTER);
            Thread.sleep(4000);

        }
        findAndClick("slide_button_close");
        implicitWait();

    }

    public void addNewline()
    {
        findAndClick("post_title");
        findAndClick("post_content");
        findAndSendkey("post_content", Keys.END);
        findAndSendkey("post_content", Keys.ENTER);
        findAndSendkey("post_content", Keys.END);
        findAndSendkey("post_content", Keys.ENTER);

    }

    public void addNewlines()
    {

        findAndSendkey("post_content", Keys.END);
        findAndSendkey("post_content", Keys.ENTER);
        findAndSendkey("post_content", Keys.END);
        findAndSendkey("post_content", Keys.ENTER);

    }

    public void moveToPublishTab(String browser)
    {
        if (browser.trim().equalsIgnoreCase("Chrome")) {
            Actions action = new Actions(driver);
            action.sendKeys(Keys.PAGE_DOWN);
            implicitWait();
            action.click(driver.findElement(By.partialLinkText("Publicar")))
                    .perform();
            findAndClick("publish_tab");
            implicitWait();
        } else {
            implicitWait();
            findAndClick("post_title");
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,0)");
            findAndClick("publish_tab");
        }

    }

    public void fichaDeReview(String fichareview)
    {
        addNewlines();
        findAndClick("toolbar_Advance");
        implicitWait();
        findAndClick("toolbar_fichadereview");

        String fichareviewarr[] = fichareview.split("~##~");
        for (int i = 0; i < fichareviewarr.length; i++) {
            String fichareviewdetails[] = fichareviewarr[i].split("@##@");
            if (!(fichareviewdetails[0].equalsIgnoreCase("null"))) {
                findAndWrite("fichareview_name", fichareviewdetails[0]);
            } else {
                findAndClick("fichareview_checkbox");
            }

            findAndWrite("fichareview_best", fichareviewdetails[1]);
            findAndWrite("fichareview_worst", fichareviewdetails[2]);

            String fichreviewdatasheet[] = fichareviewdetails[3].split("@~#~@");

            for (int j = 0; j < fichreviewdatasheet.length; j++) {
                String fichreviewdatasheetdetails[] = fichreviewdatasheet[j]
                        .split("@###@");
                for (int z = 0; z < fichreviewdatasheetdetails.length; z++) {
                    findElement(
                            prop.getProperty("fichareview_datasheet_row") + "["
                                    + (j + 1) + "]" + "/td[" + (z + 1)
                                    + "]/input").sendKeys(
                            fichreviewdatasheetdetails[z]);
                }
            }

            findAndWrite("fichareview_summary", fichareviewdetails[4]);
            findAndClick("fichreview_insert_button");
        }
        implicitWait();
        addNewlines();

    }

    public void wait(String path)
    {
        WebDriverWait wait = new WebDriverWait(driver, 30); // this is explicit
                                                            // wait
        wait.until(ExpectedConditions.elementToBeClickable(findElement(path)));
    }

    public Object[][] readExcel(String excelsheetname, int columns)
            throws IOException
    {
        String filepath = System.getProperty("user.dir") + "\\src\\Common\\";
        String filename = "excel.xlsx";
        FileInputStream instream = new FileInputStream(filepath + "\\"
                + filename);

        System.out.println(filepath + "\\" + filename);
        Workbook wb = new XSSFWorkbook(instream);
        Sheet sheet = wb.getSheet(excelsheetname);
        int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();
        int cnt = 0;

        Object[][] postdata = new Object[rows][columns];
        for (int i = 1; i <= rows; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                postdata[cnt][j] = sheet.getRow(i).getCell(j)
                        .getStringCellValue();
            }
            cnt++;
        }

        return postdata;
    }

    public void republish()
    {
        Conditionalwait("difundir_Link");
        findAndClick("difundir_Link");
        implicitWait();
        findAndClick("republish_diffunder");
        implicitWait();
        findAndClick("republish_click");

    }
}
