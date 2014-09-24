import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NoteProcessor implements Runnable {

	private final static Logger logger = LogManager.getLogger(NoteProcessor.class.getName());
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
		long inicioDescargarUnaNota = new Date().getTime();
		Document doc = null;
		try {
			doc = Jsoup.connect(elem.attr("href")).timeout(0).get();
		} catch (IOException e) {
			System.out.println("!"+e.getMessage());
			run();
		}
		long tardoEnDescargarUnaNota = new Date().getTime() - inicioDescargarUnaNota;
		if (doc == null) {
			return;
		}
		long inicioParsearUnaNota = new Date().getTime();
		Note nota = getNotaFromDocument(doc);
		long tardoEnParsearUnaNota = new Date().getTime() - inicioParsearUnaNota;

		long inicioGuardarUnaNota = new Date().getTime();
		guardarNota(nota);
		long tardoEnGuardarUnaNota = new Date().getTime() - inicioGuardarUnaNota;

		logger.info(archivo + ";  " + nota.getTitulo() + "; " + tardoEnParsearUnaNota + "; " + tardoEnDescargarUnaNota
				+ "; " + tardoEnGuardarUnaNota);
	}

	public Note getNotaFromDocument(Document doc) {
		if (doc.getElementById("encabezado") == null) {
			logger.error("Fail to process file {}");
			return null;
		}
		Element encabezado = doc.getElementById("encabezado");
		// Elements firma = encabezado.getElementsByAttributeValue("class",
		// "firma");
		encabezado.getElementsByClass("firma").remove();
		encabezado.getElementsByClass("bajada").remove();
		Elements volanta = encabezado.getElementsByAttributeValue("class", "volanta");
		Elements titulo = encabezado.getAllElements().select("h1");
		Elements descripcion = encabezado.getAllElements().select("p");
		descripcion.removeAll(volanta);
		Element cuerpo = doc.getElementById("cuerpo");
		Elements archRel = cuerpo.getElementsByAttributeValue("class", "archivos-relacionados");
		Elements fin = cuerpo.getElementsByAttributeValue("class", "fin");

		return new Note(volanta.text(), titulo.text(), descripcion.text(), cuerpo.text().replace(archRel.text(), "")
				.replace(fin.text(), ""), "", null);
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
