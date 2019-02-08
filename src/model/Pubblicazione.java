package model;

import java.sql.Date;
import java.util.Map;

public class Pubblicazione{
	private int id;
	private int idInseritore;
	private String editore;
	private String titolo;
	private String descrizione;
	private Date dataInserimento;
	
	private String codiceISBN;
	private int numPagine;
	private String lingua;
	private Date dataCreazione; 
	
	// lista o array per il numero di ristampe del libro, con tanto di data
	private Map<String,Object> elencoRistampe;
	
	public Pubblicazione(int id, int idInseritore, String editore, String titolo, String descrizione, Date dataInserimento) {
		this.id = id;
		this.idInseritore = idInseritore;
		this.titolo = titolo;
		this.descrizione = descrizione;
		this.dataInserimento = dataInserimento;
		this.editore = editore;
	}
	
	public Pubblicazione(int id, int idInseritore, String editore, String titolo, String descrizione, Date dataInserimento, 
			String ISBN, int numPagine, String lingua, Date dataCreazione) {
		
		this.id = id;
		this.idInseritore = idInseritore;
		this.titolo = titolo;
		this.descrizione = descrizione;
		this.dataInserimento = dataInserimento;
		this.editore = editore;
		//parte metadati
		this.codiceISBN = ISBN;
		this.numPagine = numPagine;
		this.lingua = lingua;
		this.dataCreazione = dataCreazione;
	}
	public Pubblicazione(int idInseritore, String editore, String titolo, String descrizione, Date dataInserimento, 
			String ISBN, int numPagine, String lingua, Date dataCreazione) {
		
		//this.id = id;
		this.idInseritore = idInseritore;
		this.titolo = titolo;
		this.descrizione = descrizione;
		this.dataInserimento = dataInserimento;
		this.editore = editore;
		//parte metadati
		this.codiceISBN = ISBN;
		this.numPagine = numPagine;
		this.lingua = lingua;
		this.dataCreazione = dataCreazione;
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
	public String getIdEditore() {
		return this.editore;
	}
	public void setIdEditore(String editore) {
		this.editore=editore;
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
	public Map<String,Object> getElencoRistampe(){
		return this.elencoRistampe;
	}
	public void setElencoRistampe(Map<String,Object> elencoRistampe) {
		this.elencoRistampe=elencoRistampe;
	}
}