import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageDownloader {

	/*
	 * public static String downloadPage(String link) { URL url; InputStream is
	 * = null; BufferedReader br; String line; String page = ""; try { url = new
	 * URL(link); is = url.openStream(); // throws an IOException br = new
	 * BufferedReader(new InputStreamReader(is));
	 *
	 * while ((line = br.readLine()) != null) { page += line; } } catch
	 * (MalformedURLException mue) { mue.printStackTrace(); } catch (IOException
	 * ioe) { ioe.printStackTrace(); } finally { try { if (is != null)
	 * is.close(); } catch (IOException ioe) { // nothing to see here } } return
	 * page; }
	 */

	public static void downloadPagina12() {
		String linkPagina12 = "http://www.pagina12.com.ar/diario/economia/index-";// yyyy-MM-dd;
		String pathAGuardar = "//home//pruebahadoop//Documentos//DescargasPeriodicos//Original//Pagina12//Economia/";

		Date fechaAnterior = new Date();
		int diasARecopilar = 3650;

		for (int i = diasARecopilar; i > 0; i--) {
			fechaAnterior = Utils.sumarRestarDiasFecha(fechaAnterior, -1);
			String fecha = Utils.dtoYYYY_MM_DD(fechaAnterior);
			System.out.println(fecha);
			String linkActual = linkPagina12 + fecha + ".html";
			Document page = null;
			try {
				page = Jsoup.connect(linkActual).timeout(0).get();
			} catch (IOException e) {
				System.out.println("AL PARECER NO EXISTE EL DIARIO DEL DIA: " + fecha);
				e.printStackTrace();
				System.out.println();
				System.out.println();
				continue;
			}
			String nombreArchivo = "Pagina12_" + fecha;
			try {
				StoreFile sf = new StoreFile(pathAGuardar, ".html", page.html(), nombreArchivo, "iso-8859-1");
				sf.store();
			} catch (IOException e) {
				System.out.println("ERROR AL QUERER GUARDAR EL ARCHIVO " + nombreArchivo);
				e.printStackTrace();
				System.out.println();
				continue;
			}
			// Sin HTML
			// guardarPageHtml(Jsoup.parse(page).text(), "pageHTML_" + fecha);
		}
	}

	public static void downloadLaNacion() {
		String linkLaNacion = "http://servicios.lanacion.com.ar/archivo-f";// 04/09/2014-c30"
		// (-c30 = politica);
		// (-c272 = economia)
		String pathAGuardar = "//home//pruebahadoop//Documentos//DescargasPeriodicos//Original//LaNacion//Economia//";
		Date fechaAnterior = new Date();
		int diasARecopilar = 3560;

		for (int i = diasARecopilar; i > 0; i--) {
			fechaAnterior = Utils.sumarRestarDiasFecha(fechaAnterior, -1);
			String fecha = Utils.dtoDD_MM_YYYY(fechaAnterior);
			System.out.println(fecha);
			String linkActual = linkLaNacion + fecha + "-c272";
			Document page = null;
			try {
				page = Jsoup.connect(linkActual).timeout(0).get();
			} catch (IOException e) {
				System.out.println("AL PARECER NO EXISTE EL DIARIO DEL DIA: " + fecha);
				continue;
			}
			String nombreArchivo = "LaNacion_" + fecha.replace("/", "-");
			try {
				StoreFile sf = new StoreFile(pathAGuardar, ".html", page.html(), nombreArchivo, "utf-8");
				sf.store();
			} catch (IOException e) {
				System.out.println("ERROR AL QUERER GUARDAR EL ARCHIVO: " + nombreArchivo);
				e.printStackTrace();
			}
			// Sin HTML
			// guardarPageHtml(Jsoup.parse(page).text(), "pageHTML_" + fecha);
		}
	}

	public static void main(String[] args) {

		 downloadPagina12();
		// downloadLaNacion();
//			prueba();
	}

	public static void prueba() throws IOException{
		File a = new File("test.html");

		Elements elem = Jsoup.parse(a, "utf-8").getElementById("archivo-notas-272").getElementsByTag("a")
				.select("[href]");

		for (Element E : elem) {
			Document doc = Jsoup.connect(E.attr("href")).timeout(0).get();
			System.out.println(doc.getElementById("encabezado").getAllElements().select("h1").text());
			System.out.println(doc.getElementById("encabezado").getAllElements().select("p").text());
			System.out.println(doc.getElementById("cuerpo").getAllElements().select("p").text());
			System.out.println();
		}
	}

}