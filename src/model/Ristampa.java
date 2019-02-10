package model;

import java.sql.Date;

public class Ristampa {
	
	int id;
	int numero;
	int idPubblicazione;
	Date data; 
	
	public Ristampa(int id, int numero, int idPubblicazione, Date data) { 
		this.id=id;
		this.numero=numero;
		this.idPubblicazione=idPubblicazione;
		this.data=data;
	}
	
	public int getId(){
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNumero(){
		return this.numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
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
		this.data=data;
	}
	
	
}
