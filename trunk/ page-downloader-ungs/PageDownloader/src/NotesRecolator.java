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
					System.out.println("//PROCESANDO ARCHIVO "+ archivo + ", N°: " + i);
					long init = new Date().getTime();
					// Obtener los links asociados a las notas de cada archivo
					try {
						Elements elem = Jsoup.parse(file, "utf-8").getElementById("archivo-notas-272")
								.getElementsByTag("a").select("[href]");
						long finParsearEnlacesANotas = new Date().getTime() - init;

						int n = 1;
						System.out.println("Este archivo tiene "+ elem.size() + "notas/artículos.");

						for (Element E : elem) {
							long inicioDescargarUnaNota = new Date().getTime();
							Document doc = Jsoup.connect(E.attr("href")).timeout(0).get();
							long finDescargarUnaNota = new Date().getTime() - inicioDescargarUnaNota;

							long inicioParsearUnaNota = new Date().getTime();
							//TODO: nullpointer! Aparentemente no encuentra "encabezado" (o h1??) VERIFICAR!!!
							String titulo = doc.getElementById("encabezado").getAllElements().select("h1").text();
							String descripcion = doc.getElementById("encabezado").getAllElements().select("p").text();
							String cuerpo = doc.getElementById("cuerpo").getAllElements().select("p").text();
							String articulo = titulo + "\n" + descripcion + "\n" + cuerpo;
							long finParsearUnaNota = new Date().getTime() - inicioParsearUnaNota;

							long inicioGuardarUnaNota = new Date().getTime();
							String nombreArchivo = archivo.replace(".html", "_" + titulo);
							StoreFile sf = new StoreFile(pathAGuardar, ".txt", articulo, nombreArchivo, "iso-8859-1");
							sf.store();
							long finGuardarUnaNota = new Date().getTime() - inicioGuardarUnaNota;

							System.out.println("\t buscarEnlacesEnArchivo" + n + " : " + finParsearEnlacesANotas + "ms");
							System.out.println("\t DescargarNota" + n + " : " + finDescargarUnaNota + " ms");
							System.out.println("\t ParsearNota" + n + " : " + finParsearUnaNota + "ms");
							System.out.println("\t GuardarNota" + n + " : " + finGuardarUnaNota + "ms");

							n++;
						}
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
					long now = new Date().getTime();
					System.out.println(" - Tardó en total aprox: " + (now - init) / 1000 + "seg.");

					i++;
				}
			}
		}

	}

	public void guardarNotasLaNacion() {

	}

}
