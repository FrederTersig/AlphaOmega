package model;

import java.sql.Date;

public class Recensione {
	private int id;
	private int idUtente;
	private int idPubblicazione;
	private Date data;
	private String testo;
	private boolean convalida;
	
	public Recensione(int id, int idUtente, int idPubblicazione, Date data, String testo, boolean convalida) {
		this.id=id;
		this.idUtente=idUtente;
		this.idPubblicazione=idPubblicazione;
		this.data=data;
		this.testo=testo;
		this.convalida=convalida;
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
	public Date getData(){
		return this.data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getTesto() {
		return this.testo;
	}
	public void setTesto(String testo) {
		this.testo=testo;
	}
	public boolean getConvalida() {
		return this.convalida;
	}
	public void setConvalida(boolean convalida) {
		this.convalida=convalida;
	}
	
}
