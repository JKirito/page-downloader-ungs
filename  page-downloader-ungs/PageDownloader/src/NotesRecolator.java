import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NotesRecolator {

	private final static Logger logger = LogManager.getLogger(NotesRecolator.class.getName());

	public static void main(String[] args) {
		String pathAGuardar = "//home//pruebahadoop//Documentos//DescargasPeriodicos//Procesado//LaNacion//Economia//";

		// Obtener la carpeta donde se encuentran todos los archivos
		File carpeta = new File("//home//pruebahadoop//Documentos//DescargasPeriodicos//Original//LaNacion//Economia//");
		int i = 1;
		if (carpeta.isDirectory()) {
			ExecutorService executor = Executors.newFixedThreadPool(5);
			logger.info("Cantidad total de archivos a procesar: "+carpeta.list().length);
			logger.info("PUBLICACION, NOTA, TIEMPO PROCESAMIENTO(ms), TIEMPO DESCARGA(ms)");
			// Recorrer cada archivo de la carpeta
			for (String archivo : carpeta.list()) {
				File file = new File(carpeta.getAbsolutePath() + "//" + archivo);
				if (file.isFile()) {
					long init = new Date().getTime();
					// Obtener los links asociados a las notas de cada archivo
					try {
						Elements elem = Jsoup.parse(file, "utf-8").getElementById("archivo-notas-272")
								.getElementsByTag("a").select("[href]");

						for (Element E : elem) {
							NoteProcessor np = new NoteProcessor(archivo, E, pathAGuardar);
							executor.execute(np);
						}
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("Se estaba procesando el archivo " + archivo);
						continue;
					}
					long now = new Date().getTime();
					i++;
				}
			}
		}

	}

}
