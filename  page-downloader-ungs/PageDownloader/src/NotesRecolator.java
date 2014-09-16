import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NotesRecolator {

	public static void main(String[] args) {
		String pathAGuardar = "//home//pruebahadoop//Documentos//DescargasPeriodicos//Procesado//LaNacion//Economia//";

		// Obtener la carpeta donde se encuentran todos los archivos
		File carpeta = new File("//home//pruebahadoop//Documentos//DescargasPeriodicos//Original//LaNacion//Economia//");
		int i = 1;
		if (carpeta.isDirectory()) {
			// Recorrer cada archivo de la carpeta
			for (String archivo : carpeta.list()) {
				File file = new File(carpeta.getAbsolutePath() + "//" + archivo);
				if (file.isFile()) {
					System.out.print("//PROCESANDO ARCHIVO N°: " + i);
					long init = new Date().getTime();
					// Obtener los links asociados a las notas de cada archivo
					try {
						Elements elem = Jsoup.parse(file, "utf-8").getElementById("archivo-notas-272")
								.getElementsByTag("a").select("[href]");

						for (Element E : elem) {
							Document doc = Jsoup.connect(E.attr("href")).timeout(0).get();
							String titulo = doc.getElementById("encabezado").getAllElements().select("h1").text();
							String descripcion = doc.getElementById("encabezado").getAllElements().select("p").text();
							String cuerpo = doc.getElementById("cuerpo").getAllElements().select("p").text();
							String articulo = titulo + "\n" + descripcion + "\n" + cuerpo;
							String nombreArchivo = archivo.replace(".html", "_" + titulo);
							StoreFile sf = new StoreFile(pathAGuardar, ".txt", articulo, nombreArchivo, "iso-8859-1");
							sf.store();
						}
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
					long now = new Date().getTime();
					System.out.println(" - Tardó: " + (now - init) / 1000 + "seg.");
					i++;
				}
			}
		}

	}

	public void guardarNotasLaNacion() {

	}

}
