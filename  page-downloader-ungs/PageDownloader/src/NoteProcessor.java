import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class NoteProcessor implements Runnable {

	private final static Logger logger = LogManager.getLogger(NoteProcessor.class);
	private Element elem;
	private String archivo;
	private String pathAGuardar;
	ExecutorService pool;

	public NoteProcessor(String archivo, Element elem, String pathAGuardar) {
		super();
		this.archivo = archivo;
		this.elem = elem;
		this.pathAGuardar = pathAGuardar;
	}

	@Override
	public void run() {
		logger.info("COMIENZO A DESCARGAR UNA NOTA!!!");
		long init = new Date().getTime();
		long inicioDescargarUnaNota = new Date().getTime();
		Document doc = null;
		try {
			doc = Jsoup.connect(elem.attr("href")).timeout(0).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long finDescargarUnaNota = new Date().getTime() - inicioDescargarUnaNota;

		long inicioParsearUnaNota = new Date().getTime();
		Note nota = getNotaFromDocument(doc);
		long tardoEnParsearUnaNota = new Date().getTime() - inicioParsearUnaNota;

		long inicioGuardarUnaNota = new Date().getTime();
		guardarNota(nota);
		long tardoEnGuardarUnaNota = new Date().getTime() - inicioGuardarUnaNota;

		System.out.println("\t DescargarNota" + " : " + finDescargarUnaNota + " ms");
		System.out.println("\t ParsearNota" + " : " + tardoEnParsearUnaNota + "ms");
		System.out.println("\t GuardarNota" + " : " + tardoEnGuardarUnaNota + "ms");
		long now = new Date().getTime();
		System.out.println(" - Tardó en total aprox: " + (now - init) + "ms.");
		logger.info("TERMINO DE DESCARGAR UNA NOTA!!!");
	}

	public Note getNotaFromDocument(Document doc) {
		if (doc.getElementById("encabezado") == null) {
			logger.error("Fail to process file {}");
			return null;
		}
		String titulo = doc.getElementById("encabezado").getAllElements().select("h1").text();
		String descripcion = doc.getElementById("encabezado").getAllElements().select("p").text();
		String cuerpo = doc.getElementById("cuerpo").getAllElements().select("p").text();

		return new Note("", titulo, descripcion, cuerpo, "", null);
	}

	public void guardarNota(Note nota) {
		String nombreArchivo = archivo.replace(".html", "_" + nota.getTitulo());
		StoreFile sf = new StoreFile(pathAGuardar, ".txt", nota.toString(), nombreArchivo, "iso-8859-1");
		try {
			sf.store();
		} catch (IOException e) {
			logger.error("Error al querer guardar en disco la nota {}", nota.getTitulo());
			e.printStackTrace();
		}
	}

}
