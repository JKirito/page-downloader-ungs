import java.util.Date;

public class Note {
	private String volante;
	private String titulo;
	private String descripcion;
	private String cuerpo;
	private String autor;
	private Date fechaPublicacion;

	public Note(String volante, String titulo, String descripcion, String cuerpo, String autor, Date fechaPublicacion) {
		super();
		this.volante = volante;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.cuerpo = cuerpo;
		this.autor = autor;
		this.fechaPublicacion = fechaPublicacion;
	}

	public String getVolante() {
		return volante;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public String getAutor() {
		return autor;
	}

	public Date getFechaPublicacion() {
		return fechaPublicacion;
	}

	public String toString(){
		String nota = "";
		if(!this.volante.trim().isEmpty()){
			nota+=this.volante + "\n";
		}
		if(!this.titulo.trim().isEmpty()){
			nota+= this.titulo +"\n";
		}
		if(!this.descripcion.trim().isEmpty()){
			nota+= this.descripcion + "\n";
		}
		if(!this.cuerpo.trim().isEmpty()){
			nota+= this.cuerpo + "\n";
		}
		if(!this.autor.trim().isEmpty()){
			nota+= this.autor;
		}
		return nota;
	}
}
