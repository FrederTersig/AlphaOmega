package model;

import java.sql.Date;

public class Entry {
	int id;
	int idUtente;
	int idPubblicazione;
	Date data;
	String descrizione;
	
	public Entry(int id, int idUtente, int idPubblicazione, Date data, String descrizione) {
		this.id=id;
		this.idUtente=idUtente;
		this.idPubblicazione=idPubblicazione;
		this.data=data;
		this.descrizione=descrizione;
	}
	
	public int getId(){
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdUtente(){
		return this.idUtente;
	}
	public void setIdUtente(int idUtente) {
		this.idUtente = idUtente;
	}
	public int getIdPubblicazione(){
		return this.idPubblicazione;
	}
	public void setIdPubblicazione(int idPubblicazione) {
		this.idPubblicazione = idPubblicazione;
	}
	public Date getData() {
		return this.data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getDescrizione() {
		return this.descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione=descrizione;
	}
}
