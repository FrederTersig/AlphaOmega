package model;

import java.sql.Date;

public class Ristampa {
	
	int id;
	String nome;
	int idPubblicazione;
	Date data; 
	
	public Ristampa(int id, String nome, int idPubblicazione, Date data) { 
		this.id=id;
		this.nome=nome;
		this.idPubblicazione=idPubblicazione;
		this.data=data;
	}
	
	public Ristampa(int id, String nome, Date data) { 
		this.id=id;
		this.nome=nome;
		this.data=data;
	}
	
	public int getId(){
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome(){
		return this.nome;
	}
	public void setNome(String nome) {
		this.nome=nome;
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
