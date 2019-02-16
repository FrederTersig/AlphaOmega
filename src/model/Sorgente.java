package model;

public class Sorgente {
	
	int id;
	int idPubblicazione;
	String tipo;
	String uri;
	String formato;
	String descrizione;
	
	public Sorgente(int id, int idPubblicazione, String tipo, String uri, String formato, String descrizione) {
		this.id=id;
		this.idPubblicazione=idPubblicazione;
		this.tipo=tipo;
		this.uri=uri;
		this.formato=formato;
		this.descrizione=descrizione;
	}
	public Sorgente(int id, String tipo, String uri, String formato, String descrizione) {
		this.id=id;
		this.tipo=tipo;
		this.uri=uri;
		this.formato=formato;
		this.descrizione=descrizione;
	}
	public int getId(){
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getIdPubblicazione(){
		return this.idPubblicazione;
	}
	public void setIdPubblicazione(int idPubblicazione) {
		this.idPubblicazione = idPubblicazione;
	}
	
	public String getDescrizione() {
		return this.descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione=descrizione;
	}
	public String getFormato() {
		return this.formato;
	}
	public void setFormato(String formato) {
		this.formato=formato;
	}
	public String getUri() {
		return this.uri;
	}
	public void setUri(String uri) {
		this.uri=uri;
	}
	public String getTipo() {
		return this.tipo;
	}
	public void setTipo(String tipo) {
		this.tipo=tipo;
	}
}
