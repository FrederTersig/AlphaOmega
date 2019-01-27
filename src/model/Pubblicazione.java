package model;

import java.sql.Date;

public class Pubblicazione{
	private int id;
	private int idInseritore;
	private int idEditore;
	private String titolo;
	private String descrizione;
	private Date dataInserimento;
	
	private int idMetadati;
	private String codiceISBN;
	private int numPagine;
	private String lingua;
	private Date dataCreazione; 
	
	public Pubblicazione(int id, int idInseritore, int idEditore, String titolo, String descrizione, Date dataInserimento) {
		this.id = id;
		this.idInseritore = idInseritore;
		this.titolo = titolo;
		this.descrizione = descrizione;
		this.dataInserimento = dataInserimento;
		this.idEditore = idEditore;
	}
	

	public int getId(){
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdInseritore() {
		return this.idInseritore;
	}
	public void setIdInseritore(int idInseritore) {
		this.idInseritore=idInseritore;
	}
	public int getIdEditore() {
		return this.idEditore;
	}
	public void setIdEditore(int idEditore) {
		this.idEditore=idEditore;
	}
	public String getTitolo() {
		return this.titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getDescrizione() {
		return this.descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione=descrizione;
	}
	public Date getDataInserimento() {
		return this.dataInserimento;
	}
	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}
	
	public int getIdMetadati() {
		return this.idMetadati;
	}
	public void setIdMetadati(int idMetadati) {
		this.idMetadati=idMetadati;
	}
	public String getCodiceISBN() {
		return this.codiceISBN;
	}
	public void setCodiceISBN(String codiceISBN) {
		this.codiceISBN=codiceISBN;
	}
	public int getNumPagine() {
		return this.numPagine;
	}
	public void setNumPagine(int numPagine) {
		this.numPagine=numPagine;
	}
	public String getLingua() {
		return this.lingua;
	}
	public void setLingua(String lingua) {
		this.lingua=lingua;
	}
	public Date getDataCreazione() {
		return this.dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
}