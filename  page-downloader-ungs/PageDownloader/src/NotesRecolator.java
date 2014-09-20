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

	private final static Logger logger = LogManager.getLogger(NotesRecolator.class);

	public static void main(String[] args) {
		String pathAGuardar = "//home//pruebahadoop//Documentos//DescargasPeriodicos//Procesado//LaNacion//Economia//";

		// Obtener la carpeta donde se encuentran todos los archivos
		File carpeta = new File("//home//pruebahadoop//Documentos//DescargasPeriodicos//Original//LaNacion//Economia//");
		int i = 1;
		if (carpeta.isDirectory()) {
			ExecutorService executor = Executors.newFixedThreadPool(1);
			// Recorrer cada archivo de la carpeta
			for (String archivo : carpeta.list()) {
				File file = new File(carpeta.getAbsolutePath() + "//" + archivo);
				if (file.isFile()) {
					logger.info("ARCHIVO PROCESADO " + archivo);
					long init = new Date().getTime();
					// Obtener los links asociados a las notas de cada archivo
					try {
						Elements elem = Jsoup.parse(file, "utf-8").getElementById("archivo-notas-272")
								.getElementsByTag("a").select("[href]");

						System.out.println("Este archivo tiene " + elem.size() + "notas/artículos.");

						for (Element E : elem) {
							long initNoteProcessor = new Date().getTime();
							NoteProcessor np = new NoteProcessor(archivo, E, pathAGuardar);
							executor.execute(np);
							long finNoteProcessor = new Date().getTime();
							System.out.println(" - Tardó en procesar una Nota: " + (initNoteProcessor - finNoteProcessor) + "ms");
						}
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("Se estaba procesando el archivo " + archivo);
						continue;
					}
					long now = new Date().getTime();
					System.out.println(" - Tardó en total en procesar un archivo aprox: " + (now - init) / 1000 + "seg.");

					i++;
				}
			}
		}

	}

	public void guardarNotasLaNacion() {

	}

}
