package Common;

import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
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
                System.getProperty("user.dir") + "\\src\\Common\\admin.properties");
        prop.load(inStream);
        return prop;
    }

    public WebDriver callproperty(String url, String browser) throws IOException
    {
        if (browser.trim().equalsIgnoreCase("Chrome")) {
            System.setProperty("webdriver.chrome.driver",
                    System.getProperty("user.dir") + "//src//Driverfiles//" + "chromedriver.exe");
            driver = new ChromeDriver();
        } else {
            System.setProperty("webdriver.gecko.driver",
                    System.getProperty("user.dir") + "//src//Driverfiles//" + "geckodriver.exe");
            driver = new FirefoxDriver();
        }
        driver.get(url);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        return driver;
    }

    public void uploadPrimaryImage(String primaryimage, String browser) throws Exception
    {
        String primaryimagearr[] = primaryimage.split("@#@");
        for (int i = 0; i < primaryimagearr.length; i++) {
            if ((primaryimagearr[i].contains(".gif")) || (primaryimagearr[i].contains(".GIF"))) {
                findAndClick("toolbar_more");
                implicitWait();
            }
            findAndClick("toolbar_image");
            if (browser.trim().equalsIgnoreCase("firefox")) {
                findAndClick("post_content");
            }
            addNewlines();
            implicitWait();
            System.out.println(primaryimagearr[i]);
            findAndWrite("primary_image_insert",
                    System.getProperty("user.dir") + prop.getProperty("image_path") + "\\" + primaryimagearr[i]);
            findAndClick("primary_image_upload");
        }
        WebElement element1 = findElement(
                prop.getProperty("product_image_bulkupload") + prop.getProperty("product_image_bulkupload1"));

        if (element1.getAttribute("href") != null) {
            isLinkBroken(new URL(element1.getAttribute("href")));
            System.out.println(isLinkBroken(new URL(element1.getAttribute("href"))));
        }
        implicitWait();
        findAndClick("primary_noraml_insert");
        implicitWait();
    }

    public void uploadMultipleImage(String primaryimage, String browser) throws Exception
    {
        findAndClick("toolbar_image");
        if (browser.trim().equalsIgnoreCase("firefox")) {
            findAndClick("post_content");
        }
        addNewlines();
        implicitWait();
        String primaryimagearr[] = primaryimage.split("@#@");
        for (int i = 0; i < primaryimagearr.length; i++) {
            findAndWrite("primary_image_insert",
                    System.getProperty("user.dir") + prop.getProperty("image_path") + "\\" + primaryimagearr[i]);
            findAndClick("primary_image_upload");
        }
        WebElement element1 = findElement(
                prop.getProperty("product_image_bulkupload") + prop.getProperty("product_image_bulkupload1"));

        if (element1.getAttribute("href") != null) {
            isLinkBroken(new URL(element1.getAttribute("href")));
            System.out.println(isLinkBroken(new URL(element1.getAttribute("href"))));
        }
        implicitWait();
        findAndClick("primary_noraml_insert");
        implicitWait();
    }

    public void findAndClick(String element)
    {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        WebElement element1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty(element))));
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
            findElement(prop.getProperty("twitter_text")).sendKeys(twitter_text);
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
            List<WebElement> optionlist = findElementByClass(prop.getProperty("catagory_ecommerce_by_ClassName"));
            for (WebElement options : optionlist) {
                if (options.getText().equalsIgnoreCase(postcatagory)) {
                    options.click();
                    break;
                }
            }
        }
        if (tag != "") {
            findAndWrite("tag_input", tag);
            List<WebElement> Tagoptionlist = findElementByClass(prop.getProperty("tag_list_Byclassname"));
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
            System.out.println(isLinkBroken(new URL(element.getAttribute("href"))));
        }
    }

    public void Conditionalwait(String xpath)
    {
        WebDriverWait wait = new WebDriverWait(driver, 50);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty(xpath))));
    }

    public Boolean clickButton(String row, String Column, String Postname, String listtype)
    {
        int cnt = 1;
        Boolean status = false;
        List<WebElement> postlist = null;
        if (listtype == "Dashboardlist") {
            postlist = findElementsByXpath(prop.getProperty("Dashboardlist"));
        } else if (listtype == "Draft") {
            postlist = findElementsByXpath(prop.getProperty("Draftlist_dashboard"));

        }

        for (WebElement list : postlist) {
            System.out.println(list.getText());
            if (list.getText().equalsIgnoreCase(Postname)) {
                if (listtype == "Dashboardlist") {
                    findElement(prop.getProperty(row) + "[" + cnt + "]" + prop.getProperty(Column)).click();
                } else if (listtype == "Draft") {

                    Actions action = new Actions(driver);
                    action.moveToElement(findElement(prop.getProperty(row) + "[" + cnt + "]" + "/a")).perform();

                    findElement(prop.getProperty(row) + "[" + cnt + "]" + prop.getProperty(Column)).click();
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
        List<WebElement> postlist = findElementsByXpath(prop.getProperty("Dashboardlist"));
        for (WebElement list : postlist) {
            System.out.println(list.getText());
            if (list.getText().equalsIgnoreCase(Postname)) {

                catgoryname = findElement(prop.getProperty(row) + "[" + cnt + "]" + prop.getProperty(Column)).getText();
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
            WebElement CheckBox1 = driver.findElement(By.cssSelector("input[value='" + Selector[i] + "']"));
            CheckBox1.click();
        }

    }

    public void summaryActuallization(String summary_data, String actuallization_data, String summary_layout)
            throws Exception
    {

        if (!summary_data.equalsIgnoreCase("null")) {

            findElement(prop.getProperty("toolbar_summary")).click();
            findElement(prop.getProperty("summary_input_field")).sendKeys(summary_data);
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
            addNewlines();
        }

        if (!actuallization_data.equalsIgnoreCase("null")) {
            implicitWait();
            addNewlines();
            findElement(prop.getProperty("toolbar_Advance")).click();
            findElement(prop.getProperty("toolbar_actuallization")).click();

            findElement(prop.getProperty("actuallization_input_field")).sendKeys(actuallization_data);

            implicitWait();
            findElement(prop.getProperty("actuallization_insert_button")).click();
            implicitWait();

            findAndSendkey("post_content", Keys.END);
            findAndSendkey("post_content", Keys.ENTER);
            findAndClick("post_content");
            implicitWait();
            addNewlines();
        }
    }

    public void repost_By_Difundir(String Selector1, String Selector2, String tittle_data, String navigate_blog)
            throws Exception
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
        List<WebElement> postlist = findElementByClass(prop.getProperty("notification_list_by_ClassName"));
        for (WebElement list : postlist) {
            String sender = driver.findElement(By.className(prop.getProperty("notify_sender"))).getText();
            String text = list.getText().replace(sender, "");
            if (text.trim().equalsIgnoreCase(tittle_data)) {
                System.out.println(cnt + "hi");
                Actions act = new Actions(driver);
                act.doubleClick(driver.findElement(By.className(prop.getProperty("notify_sender")))).build().perform();
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

    public void videoHandle(String videoURL, String layout, String browser)
            throws InterruptedException
    {
        WebElement element;
        findAndClick("post_content");
        findAndSendkey("post_content", Keys.ENTER);
        findAndClick("toolbar_video");
        if (browser.trim().equalsIgnoreCase("firefox")) {
            findAndClick("post_content");
            findAndSendkey("post_content", Keys.ENTER);
        }
        findAndClick("Video_URL");
        implicitWait();
        findAndWrite("Video_URL", videoURL);
        implicitWait();
        if (layout.equalsIgnoreCase("normal")) {
            findElement(prop.getProperty("Video_NormalLayout")).click();
        } else {
            findElement(prop.getProperty("Video_Biglayout")).click();
        }
        implicitWait();
        if (videoURL.contains("youtube")) {
            findAndClick("Youtube_button");
        } else if (videoURL.contains("vimeo")) {
            findAndClick("Vimeo_button");
        } else if (videoURL.contains("facebook")) {
            findAndClick("Facebook_button");
           else {
            findAndClick("Vine_button");
        }
        findAndClick("post_content");
        implicitWait();
        addNewlines();
    }

    public void insertBrandedClub(String BrandedClubName, String tag)
    {
        findAndClick("BrandedClub_Click");
        findAndWrite("BrandedClub_InputBox", BrandedClubName);
        List<WebElement> optionlist = findElementByClass(prop.getProperty("BrandedClub_List_by_ClassName"));
        for (WebElement options : optionlist) {
            if (options.getText().equalsIgnoreCase(BrandedClubName)) {
                options.click();
                break;
            }
        }
        findAndWrite("tag_input", tag);
        List<WebElement> Tagoptionlist = findElementByClass(prop.getProperty("tag_list_Byclassname"));
        for (WebElement options : Tagoptionlist) {
            if (options.getText().equalsIgnoreCase(tag)) {
                options.click();
                break;
            }
        }
    }

    public void addslides(String slides, String browser) throws IOException, Exception
    {
        String slidesarr[] = slides.split("~");
        findAndClick("toolbar_slideshow");
        implicitWait();

        for (int i = 0; i < slidesarr.length; i++) {
            String slidedetails[] = slidesarr[i].split("@##@");
            System.out.println(System.getProperty("user.dir") + "\\src\\DriverFiles\\fileupload.exe" + " "
                    + System.getProperty("user.dir") + "\\src\\Images\\" + slidedetails[0]);
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
                    Runtime.getRuntime().exec(System.getProperty("user.dir") + "\\src\\DriverFiles\\fileupload.exe"
                            + " " + System.getProperty("user.dir") + "\\src\\Images\\" + slidedetails[0]);
                } else {
                    Runtime.getRuntime()
                            .exec(System.getProperty("user.dir") + "\\src\\DriverFiles\\firefoxfileupload.exe" + " "
                                    + System.getProperty("user.dir") + "\\src\\Images\\" + slidedetails[0]);
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
            action.click(driver.findElement(By.partialLinkText("Publicar"))).perform();

            implicitWait();
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
                String fichreviewdatasheetdetails[] = fichreviewdatasheet[j].split("@###@");
                for (int z = 0; z < fichreviewdatasheetdetails.length; z++) {
                    findElement(prop.getProperty("fichareview_datasheet_row") + "[" + (j + 1) + "]" + "/td[" + (z + 1)
                            + "]/input").sendKeys(fichreviewdatasheetdetails[z]);
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
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.elementToBeClickable(findElement(path)));
    }

    public Object[][] readExcel(String excelsheetname, int columns) throws IOException
    {
        String filepath = System.getProperty("user.dir") + "\\src\\Common\\";
        String filename = "excel.xlsx";
        FileInputStream instream = new FileInputStream(filepath + "\\" + filename);
        System.out.println(filepath + "\\" + filename);
        Workbook wb = new XSSFWorkbook(instream);
        Sheet sheet = wb.getSheet(excelsheetname);
        int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();
        int cnt = 0;
        System.out.println(rows + "===" + columns);

        Object[][] postdata = new Object[rows][columns];
        for (int i = 1; i <= rows; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                sheet.getRow(i).getCell(j).setCellType(sheet.getRow(i).getCell(j).CELL_TYPE_STRING);
                if (sheet.getRow(i).getCell(j).getStringCellValue() != "") {
                    postdata[cnt][j] = sheet.getRow(i).getCell(j).getStringCellValue();
                }
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

    public void dialogBoxOk()
    {
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

    public void fichaTechnica(String fichatechnica)
    {
        findAndClick("toolbar_fichatechnica");
        String[] arrfichatechnica = fichatechnica.split("@##@");
        findAndWrite("Ficha_name", arrfichatechnica[0]);
        findAndWrite("Ficha_details", arrfichatechnica[1]);
        findAndWrite("Ficha_mainImage", arrfichatechnica[2]);
        findAndWrite("Ficha_optionalImage", arrfichatechnica[3]);
        String[] arrfichapricebar = arrfichatechnica[4].split("~##~");
        findAndWrite("Ficha_price", arrfichapricebar[0]);
        findAndWrite("Ficha_text", arrfichapricebar[1]);
        findAndWrite("Ficha_URL", arrfichapricebar[2]);

        String[] arrDataSheet = arrfichatechnica[5].split("@~@");

        if (arrDataSheet.length > 3) {
            findAndWrite("Ficha_add_newrows", "" + arrDataSheet.length + "");
            findAndClick("Ficha_add_newrows_button");
        }

        for (int y = 0; y < arrDataSheet.length; y++) {
            String[] arritems = arrDataSheet[y].split("~##~");
            for (int k = 0; k < arritems.length; k++) {
                System.out.println(prop.getProperty("List_Row") + "[" + (y + 1) + "]" + "/td[" + (k + 1) + "]/input");
                System.out.println(arritems[k]);

                if (arritems[k].equalsIgnoreCase("null")) {
                    findElement(prop.getProperty("List_Row") + "[" + (y + 1) + "]" + "/td[" + (k + 1) + "]/input")
                            .sendKeys("");
                } else {
                    findElement(prop.getProperty("List_Row") + "[" + (y + 1) + "]" + "/td[" + (k + 1) + "]/input")
                            .sendKeys(arritems[k]);
                }

            }
        }
        findAndWrite("Ficha_otherDetails", arrfichatechnica[6]);
        findAndClick("Ficha_insertButton");
        implicitWait();
        findAndClick("post_content");
        addNewlines();
    }

    public void specialPost(String status)
    {
        if (status.equalsIgnoreCase("Y")) {
            findAndClick("specialCheckbox");
        }
    }

    public void closeComments(String status)
    {
        if (status.equalsIgnoreCase("Y")) {
            findAndClick("commentsCheckbox");
        }
    }

    public void insertGIPHY(String URL, String layout, String caption, String browser)
    {
        List<WebElement> items = findElementsByXpath(prop.getProperty("header"));
        for (WebElement item : items) {
            if (item.getText().equalsIgnoreCase("GIF")) {
                item.click();
                break;
            }
        }
        findAndWrite("giphyURL", URL);
        implicitWait();
        WebElement element1 = findElement(prop.getProperty("smallLeft"));
        WebElement element2 = findElement(prop.getProperty("smallCenter"));
        WebElement element3 = findElement(prop.getProperty("smallRight"));
        WebElement element4 = findElement(prop.getProperty("normal"));
        WebElement element5 = findElement(prop.getProperty("large"));
        if (layout.equalsIgnoreCase("smallLeft")) {
            JavascriptExecutor executor1 = (JavascriptExecutor) driver;
            executor1.executeScript("arguments[0].click();", element1);
        } else if (layout.equalsIgnoreCase("smallCenter")) {
            JavascriptExecutor executor2 = (JavascriptExecutor) driver;
            executor2.executeScript("arguments[0].click();", element2);
        } else if (layout.equalsIgnoreCase("smallRight")) {
            JavascriptExecutor executor3 = (JavascriptExecutor) driver;
            executor3.executeScript("arguments[0].click();", element3);
        } else if (layout.equalsIgnoreCase("normal")) {
            JavascriptExecutor executor4 = (JavascriptExecutor) driver;
            executor4.executeScript("arguments[0].click();", element4);
        } else {
            JavascriptExecutor executor5 = (JavascriptExecutor) driver;
            executor5.executeScript("arguments[0].click();", element5);
        }
        implicitWait();
        findAndWrite("captionGIPHY", caption);
        findAndClick("insertButtonGIPHY");
        findAndClick("post_content");
    }

    public void Author(String authorName)
    {
        if (!authorName.equalsIgnoreCase("null")) {
            implicitWait();
            implicitWait();
            findAndClick("authorBox_click");
            implicitWait();
            findAndWrite("author", authorName);
            implicitWait();
            List<WebElement> optionlist = findElementByClass(prop.getProperty("Author_by_ClassName"));
            implicitWait();
            for (WebElement options : optionlist) {
                implicitWait();
                if (options.getText().equalsIgnoreCase(authorName)) {
                    System.out.println(options.getText());
                    implicitWait();
                    options.click();
                    implicitWait();
                    break;
                }
            }
        }

    }

    public void infograph(String infographURL, String infographLayout, String infographCaption, String browser)
    {
        System.out.println(infographURL + " " + infographLayout + " " + infographCaption);
        List<WebElement> items = findElementsByXpath(prop.getProperty("header"));
        for (WebElement item : items) {
            if (item.getText().equalsIgnoreCase("Graphs")) {
                item.click();
                break;
            }
        }
        findAndWrite("infographURLPath", infographURL);
        implicitWait();

        if (infographLayout.equalsIgnoreCase("small")) {
            implicitWait();
            WebElement element = findElement(prop.getProperty("graph_SmallLayout"));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
            implicitWait();
        } else if (infographLayout.equalsIgnoreCase("normal")) {
            implicitWait();
            WebElement element = findElement(prop.getProperty("graph_Normallayout"));
            JavascriptExecutor executor = (JavascriptExecutor) driver;

            executor.executeScript("arguments[0].click();", element);
            implicitWait();
        } else {
            implicitWait();
            WebElement element = findElement(prop.getProperty("graph_Largelayout"));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
            implicitWait();
        }
        implicitWait();
        findAndWrite("infographCaptionPath", infographCaption);
        implicitWait();
        findAndClick("Add_infograph");
        implicitWait();
        findAndClick("post_content");
    }

    public void addTable(String tabledata, String Checkbox_same_width, String Checkbox_table_first_row_heading,
            String Checkbox_table_first_column_heading, String Checkbox_table_occupy_all_avaiable_width)
    {
        String rows = "2", columns = "2";
        implicitWait();
        findAndClick("toolbar_table");
        System.out.println("rows: " + rows);
        String tablerow[] = tabledata.split("@##@");
        rows = String.valueOf(tablerow.length);
        for (int k = 0; k < tablerow.length; k++) {
            String tablecolumn1[] = tablerow[k].split("~##~");
            columns = String.valueOf(tablecolumn1.length);
            System.out.println("columns: " + columns);
        }
        implicitWait();
        implicitWait();
        implicitWait();
        findElement(prop.getProperty("table_row_filter")).click();
        implicitWait();
        findElement(prop.getProperty("table_row_filter")).clear();
        implicitWait();
        findAndWrite("table_row_filter", rows);
        findElement(prop.getProperty("table_column_filter")).click();
        findElement(prop.getProperty("table_column_filter")).clear();
        implicitWait();
        findAndWrite("table_column_filter", columns);
        findAndClick("table_generate_table_button");
        implicitWait();
        for (int i = 0; i < tablerow.length; i++) {
            String tablecolumn[] = tablerow[i].split("~##~");
            System.out.println(tablerow[i]);
            for (int j = 1; j <= tablecolumn.length; j++) {
                if (tablecolumn[(j - 1)].equalsIgnoreCase("null")) {
                    findElement(prop.getProperty("table_tr") + "[" + (i + 1) + "]/td[" + (j + 1) + "]"
                            + prop.getProperty("table_td")).sendKeys("");
                } else {
                    findElement(prop.getProperty("table_tr") + "[" + (i + 1) + "]/td[" + (j + 1) + "]"
                            + prop.getProperty("table_td")).sendKeys(tablecolumn[(j - 1)]);
                }
            }
        }
        implicitWait();
        if (Checkbox_same_width.equalsIgnoreCase("Y")) {
            findAndClick("table_checkbox_same_width");
        }
        if (Checkbox_table_first_row_heading.equalsIgnoreCase("Y")) {
            findAndClick("table_first_row_heading");
        }
        if (Checkbox_table_first_column_heading.equalsIgnoreCase("Y")) {
            findAndClick("table_first_column_heading");
        }
        if (Checkbox_table_occupy_all_avaiable_width.equalsIgnoreCase("Y")) {
            findAndClick("table_occupy_all_avaiable_width");
        }
        implicitWait();
        findAndClick("table_insert_button");
        findAndClick("post_content");
    }

    public void RecipeSummary(String summary_data, String summary_layout) throws Exception
    {
        if (!summary_data.equalsIgnoreCase("null")) {

            findElement(prop.getProperty("Recipe_summary")).click();
            findElement(prop.getProperty("summary_input_field")).sendKeys(summary_data);
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
            findAndSendkey("Recipe_Post_content", Keys.END);
            findAndSendkey("Recipe_Post_content", Keys.ENTER);
            findAndSendkey("Recipe_Post_content", Keys.END);
            findAndSendkey("Recipe_Post_content", Keys.ENTER);
        }

    }

    public void RecipeuploadImage(String primaryimage, String browser) throws Exception
    {
        String primaryimagearr[] = primaryimage.split("@#@");
        for (int i = 0; i < primaryimagearr.length; i++) {
            if (browser.trim().equalsIgnoreCase("firefox")) {
                findAndClick("Recipe_Post_content");
            }
            findAndSendkey("Recipe_Post_content", Keys.END);
            findAndSendkey("Recipe_Post_content", Keys.ENTER);
            findAndSendkey("Recipe_Post_content", Keys.END);
            findAndSendkey("Recipe_Post_content", Keys.ENTER);
            implicitWait();
            System.out.println(primaryimagearr[i]);
            System.out.println(
                    System.getProperty("user.dir") + prop.getProperty("image_path") + "\\" + primaryimagearr[i]);
            findAndWrite("primary_image_insert",
                    System.getProperty("user.dir") + prop.getProperty("image_path") + "\\" + primaryimagearr[i]);
            findAndClick("primary_image_upload");
        }
        WebElement element1 = findElement(
                prop.getProperty("product_image_bulkupload") + prop.getProperty("product_image_bulkupload1"));

        if (element1.getAttribute("href") != null) {
            isLinkBroken(new URL(element1.getAttribute("href")));
            System.out.println(isLinkBroken(new URL(element1.getAttribute("href"))));
        }
        implicitWait();
        findAndClick("Recipe_normal_insert");
        implicitWait();
    }

    public void RecipeAddVideo(String videoURL, String layout, String browser) throws InterruptedException
    {
        WebElement element;
        findAndClick("Recipe_Post_content");
        findAndSendkey("Recipe_Post_content", Keys.ENTER);
        findAndClick("Recipe_video");
        if (browser.trim().equalsIgnoreCase("firefox")) {
            findAndClick("Recipe_Post_content");
            findAndSendkey("Recipe_Post_content", Keys.ENTER);
        }
        findAndClick("Video_URL");
        implicitWait();
        findAndWrite("Video_URL", videoURL);
        implicitWait();
        if (layout.equalsIgnoreCase("normal")) {
            findElement(prop.getProperty("Video_NormalLayout")).click();
        } else {
            findElement(prop.getProperty("Video_Biglayout")).click();
        }
        implicitWait();
        if (videoURL.contains("youtube")) {
            findAndClick("Youtube_button");
        } else if (videoURL.contains("vimeo")) {
            findAndClick("Vimeo_button");
        } else {
            findAndClick("Vine_button");
        }
        findAndClick("Recipe_Post_content");
        implicitWait();
        findAndSendkey("Recipe_Post_content", Keys.END);
        findAndSendkey("Recipe_Post_content", Keys.ENTER);
        findAndSendkey("Recipe_Post_content", Keys.END);
        findAndSendkey("Recipe_Post_content", Keys.ENTER);

    }

    public void addRecipe(String name, String persons, String level, String ingredients,
            String Recipe_ingredients_Cantidad, String Recipe_ingredients_units, String Recipe_ingredients_Detailes,
            String Preparation_time_hours, String Preparation_time_Mintues, String Cooking_time_hours,
            String Cooking_time_minutes, String Rest_time_hours, String Rest_time_mintues, String Recipe_postcontent,
            String RecipeImage, String Recipe_More_postcontent, String Youtube_Video,
            String Recipe_Youtube_Video_layout, String Vine_Video, String Recipe_Vine_Video_layout, String Vimeo_Video,
            String Recipe_Vimeo_Video_layout, String FB_Video, String Recipe_FB_Video_layout, String Recipe_summary,
            String Recipe_summary_layout, String browser) throws Exception
    {
        System.out.println("Recipe_name " + name);
        findAndWrite("Recipe_name", name);
        implicitWait();
        String[] personarr = persons.split("@##@");
        implicitWait();
        if (level.equalsIgnoreCase("high")) {
            findElement(prop.getProperty("Recipe_high")).click();
        } else if (level.equalsIgnoreCase("Low")) {
            findElement(prop.getProperty("Recipe_low")).click();
        } else {
            findElement(prop.getProperty("Recipe_medium")).click();
        }
        findAndWrite("Recipe_Person", personarr[0]);
        findAndClick("Recipe_person_unit_dropdown");
        implicitWait();
        List<WebElement> lists3 = driver.findElements(By.xpath("//*//*[text()='" + personarr[1] + "']"));
        System.out.println(lists3.size());
        for (WebElement test3 : lists3) {
            if (test3.getText().equalsIgnoreCase(personarr[1].trim())) {
                System.out.println("Matched: " + test3.getText());
                test3.click();
                implicitWait();
                break;
            }
        }
        implicitWait();
        implicitWait();
        findAndWrite("preparation_time_hours", Preparation_time_hours);
        findAndWrite("preparation_time_mnts", Preparation_time_Mintues);
        findAndWrite("cooking_time_hours", Cooking_time_hours);
        findAndWrite("cooking_time_mnts", Cooking_time_minutes);
        findAndWrite("rest_time_hours", Rest_time_hours);
        findAndWrite("rest_time_mnts", Rest_time_mintues);
        implicitWait();

        String[] arringredients = ingredients.split("@##@");
        String[] arrRecipe_ingredients_Cantidad = Recipe_ingredients_Cantidad.split("@##@");
        String[] arrRecipeingredientsdetails = Recipe_ingredients_Detailes.split("@##@");
        String[] arrRecipeingredientsunits = Recipe_ingredients_units.split("@##@");
        int cnt = 2;
        for (int i = 0; i < arringredients.length; i++) {
            if (i > 2) {
                findAndClick("Recipe_more_row_button");
            }

            implicitWait();
            findElement(prop.getProperty("Recipe_ingredient_row_p1") + "[" + cnt + "]"
                    + prop.getProperty("Recipe_ingredient_row_p2")).sendKeys(arringredients[i]);
            List<WebElement> lists1 = driver.findElements(By.xpath("//*[text()='" + arringredients[i] + "']"));
            for (WebElement test : lists1) {
                System.out.println(test.getText() + "===" + arringredients[i].trim());

                if (test.getText().equalsIgnoreCase(arringredients[i].trim())) {
                    System.out.println("Matched: " + test.getText());
                    test.click();
                    implicitWait();
                    break;
                }
            }

            System.out.println(arrRecipe_ingredients_Cantidad[i]);
            implicitWait();
            findElement(prop.getProperty("Recipe_ingredient_quantity_p1") + "[" + cnt + "]"
                    + prop.getProperty("Recipe_ingredient_quantity_p2")).sendKeys(arrRecipe_ingredients_Cantidad[i]);
            implicitWait();
            findElement(prop.getProperty("Recipe-unit_p1") + "[" + cnt + "]" + prop.getProperty("Recipe-unit_p2"))
                    .click();
            implicitWait();

            List<WebElement> lists2 = driver
                    .findElements(By.xpath("//*//*[last()][text()='" + arrRecipeingredientsunits[i] + "']"));
            for (WebElement test1 : lists2) {
                if (test1.getText().equalsIgnoreCase(arrRecipeingredientsunits[i].trim())) {
                    System.out.println("Matched Unit: " + test1.getText());
                    test1.click();
                    implicitWait();
                    break;
                }
            }
            implicitWait();
            System.out.println(arrRecipeingredientsdetails[i].toString() + "==" + Recipe_ingredients_Detailes);
            findElement(prop.getProperty("Recipe_details_p1") + "[" + cnt + "]" + prop.getProperty("Recipe_details_p2"))
                    .sendKeys(Keys.SHIFT);
            Thread.sleep(10000);
            findElement(prop.getProperty("Recipe_details_p1") + "[" + cnt + "]" + prop.getProperty("Recipe_details_p2"))
                    .sendKeys(arrRecipeingredientsdetails[i]);

            implicitWait();
            implicitWait();

            cnt++;
        }
        findAndWrite("Recipe_Post_content", Recipe_postcontent);
        implicitWait();
        findAndSendkey("Recipe_Post_content", Keys.END);
        findAndSendkey("Recipe_Post_content", Keys.ENTER);
        findAndSendkey("Recipe_Post_content", Keys.END);
        findAndSendkey("Recipe_Post_content", Keys.ENTER);

        if (!RecipeImage.equalsIgnoreCase("null")) {
            findAndClick("Recipe_image");
            RecipeuploadImage(RecipeImage, browser);
            implicitWait();
            findAndSendkey("Recipe_Post_content", Keys.END);
            findAndSendkey("Recipe_Post_content", Keys.ENTER);
            findAndSendkey("Recipe_Post_content", Keys.END);
            findAndSendkey("Recipe_Post_content", Keys.ENTER);
        }

        if (!Youtube_Video.equalsIgnoreCase("null")) {
            recipemovecursorpostion(1);
            RecipeAddVideo(Youtube_Video, Recipe_Youtube_Video_layout, browser);
            recipemovecursorpostion(2);
        }

        if (!Recipe_More_postcontent.equalsIgnoreCase("null")) {
            recipemovecursorpostion(1);
            findAndWrite("Recipe_Post_content", Recipe_More_postcontent);
            recipemovecursorpostion(2);
        }

        if (!Vine_Video.equalsIgnoreCase("null")) {
            recipemovecursorpostion(1);
            RecipeAddVideo(Vine_Video, Recipe_Vine_Video_layout, browser);
            recipemovecursorpostion(2);
        }

        if (!Vimeo_Video.equalsIgnoreCase("null")) {
            recipemovecursorpostion(1);
            RecipeAddVideo(Vimeo_Video, Recipe_Vimeo_Video_layout, browser);
            recipemovecursorpostion(2);
        }
        if (!FB_Video.equalsIgnoreCase("null")) {
            recipemovecursorpostion(1);
            RecipeAddVideo(FB_Video, Recipe_FB_Video_layout, browser);
            recipemovecursorpostion(2);
        }

        if (!Recipe_summary.equalsIgnoreCase("null")) {
            recipemovecursorpostion(1);
            RecipeSummary(Recipe_summary, Recipe_summary_layout);
            recipemovecursorpostion(2);
        }

        Thread.sleep(3000);
        implicitWait();
        findAndClick("Recipe_Save_button");
        Thread.sleep(1000);

    }

    public void recipemovecursorpostion(int stats)
    {
        if (stats == 1) {
            implicitWait();
            implicitWait();
            implicitWait();
        } else {
            implicitWait();
            findAndSendkey("Recipe_Post_content", Keys.END);
            findAndSendkey("Recipe_Post_content", Keys.ENTER);
            findAndSendkey("Recipe_Post_content", Keys.END);
            findAndSendkey("Recipe_Post_content", Keys.ENTER);
        }
    }
}
