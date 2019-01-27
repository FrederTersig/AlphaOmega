package model;

import java.sql.Date;

public class Lode {
	
	int id;
	int idUtente;
	int idPubblicazione;
	Date data; // ?? -> Forse devo farlo nella chiamata sql
	
	public Lode(int id, int idUtente, int idPubblicazione) { //, Date data) {
		this.id=id;
		this.idUtente=idUtente;
		this.idPubblicazione=idPubblicazione;
		//this.data=data;
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
	
}
