package model;

import java.sql.Date;

public class Recensione {
	private int id;
	private int idUtente;
	private String nomeUtente;
	private String titolo;
	private int idPubblicazione;
	private Date data;
	private String testo;
	private int convalida;
	
	public Recensione(int id, int idUtente, int idPubblicazione, Date data, String testo, int convalida, String email, String titolo) {
		this.id=id;
		this.idUtente=idUtente;
		this.idPubblicazione=idPubblicazione;
		this.data=data;
		this.testo=testo;
		this.convalida=convalida;
		this.nomeUtente=email;
		this.titolo=titolo;
	}
	public Recensione(int id, int idUtente, Date data, String testo, String email) {
		this.id=id;
		this.idUtente=idUtente;
		this.data=data;
		this.testo=testo;
		this.nomeUtente=email;
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
	public String getNomeUtente() {
		return this.nomeUtente;
	}
	public void setNomeUtente(String nome) {
		this.nomeUtente=nome;
	}
	public String getTitolo() {
		return this.titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo=titolo;
	}
	
	public int getConvalida() {
		return this.convalida;
	}
	public void setConvalida(int convalida) {
		this.convalida=convalida;
	}
	
}
