import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NotesRecolator {

	private final static Logger logger = LogManager.getLogger(NotesRecolator.class.getName());
	private final static String pathAGuardar = "//home//pruebahadoop//Documentos//DescargasPeriodicos//Procesado//LaNacion//Economia//";
	private final static Integer threadsNumber = 64;

	public static void main(String[] args) {

		// Obtener la carpeta donde se encuentran todos los archivos
		File carpeta = new File("//home//pruebahadoop//Documentos//DescargasPeriodicos//Original//LaNacion//Economia//");
		if (carpeta.isDirectory()) {
			ExecutorService executor = Executors.newFixedThreadPool(threadsNumber);
			logger.info("PUBLICACION; NOTA; TIEMPO PROCESAMIENTO(ms); TIEMPO DESCARGA(ms)");
			// Recorrer cada archivo de la carpeta
			for (String archivo : carpeta.list()) {
				File file = new File(carpeta.getAbsolutePath() + "//" + archivo);
				if (file.isFile()) {
//					if(seProcesoArchivo(file)){
//						continue;
//					}

					// Obtener los links asociados a las notas de cada archivo
					try {
						Element notasABuscar = Jsoup.parse(file, "utf-8").getElementById("archivo-notas-272");
						if(notasABuscar == null)
							continue;
						Elements nota = notasABuscar.getElementsByTag("a").select("[href]");

						for (Element E : nota) {
							NoteProcessor np = new NoteProcessor(archivo, E, pathAGuardar);
							while(((ThreadPoolExecutor)executor).getActiveCount() == threadsNumber);
							executor.submit(np);
						}
					} catch (IOException e) {
						e.printStackTrace();
//						System.out.println("Se estaba procesando el archivo " + archivo);
						continue;
					}
				}
			}
		}

	}

//	private static boolean seProcesoArchivo(File file) {
//		File carpetaConProcesados = new File(pathAGuardar);
//		for(String arch : carpetaConProcesados.list()){
//			String archivoYAProcesado = arch.substring(0 , arch.lastIndexOf("_"));
//			String archivoAParsear = file.getName().substring(0 , file.getName().indexOf("."));
//			if(archivoYAProcesado.equals(archivoAParsear))
//				return true;
//		}
//		return false;
//	}

}
