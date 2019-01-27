package model;

public class Capitolo {
	
	int id;
	int idPubblicazione;
	String titolo;
	int numero;
	int pagInizio;
	
	public Capitolo(int id, int idPubblicazione, String titolo, int numero, int capitolo) {
		this.id=id;
		this.idPubblicazione=idPubblicazione;
		this.titolo=titolo;
		this.numero=numero;
		this.pagInizio=pagInizio;
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
	public String getTitolo() {
		return this.titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo=titolo;
	}
	public int getPagInizio(){
		return this.pagInizio;
	}
	public void setPagInizio(int pagInizio) {
		this.pagInizio = pagInizio;
	}
}
