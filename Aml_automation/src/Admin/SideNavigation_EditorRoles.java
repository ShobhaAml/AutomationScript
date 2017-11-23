package Admin;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Common.Adminproperty;

public class SideNavigation_EditorRoles {

	WebDriver driver;
	String browser = "";
	Connection conn;
	String blogrole = "", blogroleName = "", uname = "", Authorname = "";
	Adminproperty adminProperties = new Adminproperty();
	Properties prop = new Properties();

	@BeforeClass
	public void Setup() throws Exception {

		prop = adminProperties.ReadProperties();
		driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));
		browser = prop.getProperty("browser");

		System.out.println(prop.getProperty("admin_usename"));
		CheckUserRoles ck = new CheckUserRoles();
		ck.openConnection(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
		blogrole = ck.blogrole;
		adminProperties.LoginAdmin(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));

		adminProperties.findAndClick("navigation_header");

	}

	@Test(priority = 1)
	public String sidelinks() throws Exception {

		String result = "";
		int cnt = 1;
		List<WebElement> list = driver.findElements(By.xpath(".//*[@id='panel-main-menu']/ul/li/a"));
		List<WebElement> list1 = driver.findElements(By.xpath(".//*[@class='menu_level_1']/li"));
		for (WebElement element : list) {

			if (cnt > 2) {
				System.out.println(element.getText());
				if (result == "") {
					result = element.getText();

				}

				else {
					result = result + "," + element.getText();
				}

			}

			cnt = cnt + 1;
		}

		for (WebElement element : list1) {

			if (cnt > 2) {
				System.out.println(element.getText());

				if (result == "") {
					result = element.getText();

				}

				else {
					result = result + "," + element.getText();
				}

			}

			cnt = cnt + 1;

		}

		System.out.println(result);

		return result;

	}

	public String[] getactuallinks(String role) {
		String[] links = new String[0];

		if (role.equalsIgnoreCase("Admin")) {
			links = new String[] { "Escritorio", "Escribir", "Escribir en club", "Slideshow", "Longform", "Video post",
					"Basics post", "Entradas", "Comentarios", "Categorías", "Tags", "Eventos", "Preguntas",
					"Vídeos en portada", "Resultados de la jornada", "Estadísticas", "Breaking Tag",
					"Secciones en portada", "Comunidad", "Administrador", "Cerrar sesión ?", "Crear Longform",
					"Crear Branded Longform", "Información del blog", "Branded en contenidos relacionados",
					"Destacados club", "Usuarios", "Enlaces rotos", "Destacado Club Tag" };
		} 
		else if (role.equalsIgnoreCase("Editor") || role.equalsIgnoreCase("Lead Editor")
				|| role.equalsIgnoreCase("Editor Senior")) {
			links = new String[] { "Escritorio", "Escribir", "Escribir en club", "Slideshow", "Longform", "Video post",
					"Basics post", "Entradas", "Comentarios", "Categorías", "Tags", "Eventos", "Preguntas",
					"Vídeos en portada", "Resultados de la jornada", "Estadísticas", "Breaking Tag",
					"Secciones en portada", "Comunidad", "Cerrar sesión", "Crear Longform" };
		}
		else if (role.equalsIgnoreCase("Branded Coordinator")) {
			links = new String[] { "Escritorio", "Escribir en club", "Longform", "Entradas", "Comentarios",
					"Vídeos en portada", "Administrador", "Destacados club", "Cerrar sesión",
					"Crear Branded Longform" };
		}
		else if (role.equalsIgnoreCase("Coordinator")) {
			links = new String[] { "Escritorio", "Escribir", "Slideshow", "Longform", "Video post", "Basics post",
					"Entradas", "Comentarios", "Categorías", "Tags", "Eventos", "Preguntas", "Vídeos en portada",
					"Resultados de la jornada", "Estadísticas", "Breaking Tag", "Secciones en portada", "Comunidad",
					"Cerrar sesión", "Crear Longform" };
		}
		else if (role.equalsIgnoreCase("Branded Collaborator")) {
			links = new String[] { "Escritorio", "Escribir en club", "Longform", "Entradas", "Cerrar sesión",
					"Crear Branded Longform" };
		}
		else if (role.equalsIgnoreCase("Collaborator")) {
			links = new String[] { "Escritorio", "Escribir", "Slideshow", "Longform", "Video post", "Basics post",
					"Entradas", "Cerrar sesión", "Crear Longform" };
		} 
		else if (role.equalsIgnoreCase("director")) {
			links = new String[] { "Escritorio", "Escribir", "Escribir en club", "Slideshow", "Longform", "Video post",
					"Basics post", "Entradas", "Comentarios", "Categorías", "Tags", "Eventos", "Preguntas",
					"Vídeos en portada", "Resultados de la jornada", "Estadísticas", "Breaking Tag",
					"Secciones en portada", "Comunidad", "Cerrar sesión ?", "Crear Longform" };
		}

		return links;
	}

	@Test(priority = 2)
	public void compare() throws Exception {
		System.out.println("blogrole=============" + blogrole);
		String[] expected = sidelinks().split(",");
		String[] actual = getactuallinks(blogrole);

		System.out.println(actual.length + "===" + expected.length);

		for (int i = 0; i < expected.length; i++) {

			System.out.println(actual[i] + "==" + expected[i]);

			if (actual[i].equalsIgnoreCase(expected[i])) {

				System.out.println("Pass");
			} else {
				System.out.println("Fail");
			}

		}

		verfiyLinks();

	}

	public void verfiyLinks() throws Exception {

		driver = adminProperties.callproperty(prop.getProperty("url"), prop.getProperty("browser"));

		adminProperties.findElement(prop.getProperty("login_username_txt")).sendKeys(prop.getProperty("admin_usename"));
		adminProperties.findElement(prop.getProperty("login_pwd_txt")).sendKeys(prop.getProperty("admin_pwd"));
		adminProperties.findElement(prop.getProperty("login_submit_button")).click();

		List<WebElement> anchorTagsList = driver.findElements(By.xpath(".//*[@id='panel-main-menu']/ul/li/a"));

		List<WebElement> anchorTagsList1 = driver.findElements(By.xpath(".//*[@class='menu_level_1']/li"));

		int a = anchorTagsList.size();
		int b = anchorTagsList1.size();
		int value = a + b;

		System.out.println("Total no. of links are " + value);

		for (WebElement anchorTagElement : anchorTagsList) {
			if (anchorTagElement != null) {
				String url = anchorTagElement.getAttribute("href");
				if (url != null && !url.contains("javascript")
						&& (!url.contains("utm_campaign=footer") && (!url.contains("#") && (!url.contains("redirect?"))
								&& (!url.contains("mailto:?subject="))))) {
					if (url.contains("testing.")) {
						url = url.replace("testing.", "guest:guest@testing.");
					}

					verifyURLStatus(url);
				}
			}
		}

	}

	public void verifyURLStatus(String URL) {

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(URL);
		try {
			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() != 200)
				System.out.println(URL + ": " + response.getStatusLine().getStatusCode() + "=="
						+ response.getStatusLine().getReasonPhrase());
		} catch (Exception e) {

		}

		adminProperties.ExtractJSLogs(URL);
	}

}
