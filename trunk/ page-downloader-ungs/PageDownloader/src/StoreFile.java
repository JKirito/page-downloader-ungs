import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class StoreFile {
	private String path;
	private String extension;
	private String textoAGuardar;
	private String nombreArchivo;
	private String charset;


	/**
	 *
	 * @param path: ruta donde se va a guardar el archivo
	 * @param extension: puede ser vacío [""]. En caso de agregarlo, agregar el punto '.'
	 * @param textoAGuardar: Cadena de texto que se desea guardar en disco
	 * @param nombreArchivo: nombre con el cual se guardará el archivo (SIN extensión)
	 * @param charset: formato charset con el que se guardará el archivo. Ej: "utf-8"
	 */
	public StoreFile(String path, String extension, String textoAGuardar, String nombreArchivo, String charset) {
		super();
		this.path = path;
		this.extension = extension;
		this.textoAGuardar = textoAGuardar;
		this.nombreArchivo = nombreArchivo;
		this.charset = charset;
	}


	public void store() throws IOException{
		File page = new File(this.path + this.nombreArchivo + this.extension);
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(page), this.charset));
		try {
			out.write(this.textoAGuardar);
		} finally {
			out.close();
		}
	}
}
