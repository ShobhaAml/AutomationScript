package Admin;

import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class SlideShowPost
{
    WebDriver driver;
    String Text1 = "Apple";
    String postcatagory = "Especiales";
    String tag = "Encuesta";
    String tittle_data = "Slide show post testing:- shobha";
    String editor_data = "El aspecto final de Beast v2 era sobrecogedor: un enorme cilindro de 2 metros de altura y casi 150 kg de peso en el que se agrupaban esas 144 Raspberry Pi2. El proyecto se terminó en agosto y se";
    String Slideshow_subtitle_data = "test test test test";
    String Slideshow_desc_data = "test test test";
    String fbtext = "Hi testing for FB content";
    String primaryimage = "//4.jpg";
    String slideshowimages="car.jpg@##@7recogidos para conquistar en San@##@ Valentín El peinado de Emma Stone también se merecía un premio en la entrega de los Globos de Oro por su originalidad y porque la idea funciona perfectamente. Se trata de un recogido lateral que de frente simula un falso bob, que aunque no parece fácil para hacértelo tú misma, si es una buena idea para copiar.~slide1.jpg@##@Creo que nunca voy a olvidar este precioso look@##@ de Chloe Moretz. su trenza lateral un poco deshecha con los mechones sueltos al otro lado del rostro es un peinado relativamente fácil que podemos llevar tanto en el día como en la noche.~slide3.jpg@##@Una coleta es el recogido más resultón del mundo@##@basta con que te dejes unos mechones sueltos, te hagas unas ondas en las puntas y lo combines con un maquillaje bonito y tendrás un look de 10";
    int i = 1;
    int invalidImageCount = 0;
    Adminproperty adminProperties = new Adminproperty();
    Properties prop = new Properties();

    @BeforeMethod
    public void Setup() throws IOException
    {
        prop = adminProperties.ReadProperties();
        driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
    }

    public void login() throws Exception
    {
        adminProperties.adminLogin();
    }

    @Test
    public void Writeboard() throws Exception
    {
        login();
        adminProperties.implicitWait();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = wait
                .until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("navigation_header"))));
        adminProperties.findAndClick("navigation_header");
        adminProperties.findAndClick("navigate_Slideshow");
        adminProperties.findAndWrite("post_title", tittle_data);
        adminProperties.findAndWrite("post_content", editor_data);
        adminProperties.findAndClick("post_content");
        adminProperties.implicitWait();
       /* adminProperties.findAndClick("toolbar_image");
        adminProperties.implicitWait();
        adminProperties.uploadPrimaryImage(primaryimage);*/
        adminProperties.implicitWait();
        adminProperties.findAndClick("post_content");
        adminProperties.findAndWrite("post_content", editor_data);
        
        
        adminProperties.implicitWait();
        
        
        adminProperties.addslides(slideshowimages,prop.getProperty("browser"));
        
       
      /*  adminProperties.findAndClick("toolbar_slideshow");
        adminProperties.addNewlines();
        adminProperties.implicitWait();
          
       // adminProperties.addslides(slides);
     
        Thread.sleep(4000);
        adminProperties.findAndClick("slideshow_addimage");
        adminProperties.implicitWait();
        adminProperties.findAndClick("slideshow_input_image");
        adminProperties.implicitWait(); 
        
        
        Runtime.getRuntime().exec(
                System.getProperty("user.dir")
                        + "\\src\\DriverFiles\\firefoxfileupload.exe" + " "
                        + System.getProperty("user.dir")
                        + "\\src\\Images\\" +"car.jpg");
        
        adminProperties.findAndWrite("Slideshow_subtitle", Slideshow_subtitle_data);
        adminProperties.findAndWrite("Slideshow_desc", Slideshow_desc_data);
        adminProperties.findAndClick("slide_button");
        Thread.sleep(4000);
        adminProperties.findAndSendkey("post_content", Keys.ENTER);
        Thread.sleep(4000);
        adminProperties.findAndClick("slide_button_close");
        */
        
       /* adminProperties.implicitWait();
       
        adminProperties.findAndClick("slideshow_addimage");
        adminProperties.implicitWait();
        adminProperties.findAndClick("slideshow_input_image");
        adminProperties.implicitWait();
       
        Runtime.getRuntime().exec(System.getProperty("user.dir") + "\\src\\DriverFiles\\fileupload.exe" + " "
                + System.getProperty("user.dir") + "\\src\\Images\\4.jpg");
        
       
        adminProperties.findAndWrite("Slideshow_subtitle", Slideshow_subtitle_data);
        adminProperties.findAndWrite("Slideshow_desc", Slideshow_desc_data);
        adminProperties.findAndClick("slide_button");
        Thread.sleep(4000);
        adminProperties.findAndSendkey("post_content", Keys.ENTER);
        Thread.sleep(4000);
       // adminProperties.findAndClick("slide_button_close");
        adminProperties.implicitWait();
       
        adminProperties.findAndClick("slideshow_addimage");
        adminProperties.implicitWait();
        adminProperties.findAndClick("slideshow_input_image");
        adminProperties.implicitWait();
       
        Runtime.getRuntime().exec(System.getProperty("user.dir") + "\\src\\DriverFiles\\fileupload.exe" + " "
                + System.getProperty("user.dir") + "\\src\\Images\\4.jpg");
        
       
        adminProperties.findAndWrite("Slideshow_subtitle", Slideshow_subtitle_data);
        adminProperties.findAndWrite("Slideshow_desc", Slideshow_desc_data);
        adminProperties.findAndClick("slide_button");
       adminProperties.findAndClick("slide_button_close");
       Thread.sleep(4000);
       adminProperties.findAndSendkey("post_content", Keys.ENTER);
       Thread.sleep(4000);
        adminProperties.implicitWait();*/
      
        
        /*WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = wait
                .until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("publish_tab"))));
        */
       /* adminProperties.findAndClick("publish_tab");
        adminProperties.implicitWait();
        adminProperties.insertTagAndCategory(postcatagory, tag);
        adminProperties.implicitWait();
        adminProperties.addFbTwitterText(fbtext);
        adminProperties.implicitWait();
        adminProperties.findAndClick("publish_post");
        */
     
    }
        }
  