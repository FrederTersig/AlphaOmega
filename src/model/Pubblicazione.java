package model;

import java.sql.Date;
import java.util.ArrayList;
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
	
	private Date ultimaModifica;
	private String modificataDa;
	private int idModificatore;
	
	private ArrayList<String> listaAutori;
	private ArrayList<String> listaTag;
	
	private ArrayList<Capitolo> listaCapitoli;
	private ArrayList<Sorgente> listaSorgente;
	private ArrayList<Ristampa> listaRistampe;
	
	private Date dataUltimaRistampa;
	private String nomeUltimaRistampa;
	
	
	public Pubblicazione(int id, String titolo, String ISBN) {
		this.id = id;
		this.titolo = titolo;
		this.codiceISBN = ISBN;
		initList();
	}
	



	
	public Pubblicazione(int id, String titolo, Date dataInvio, String ISBN) {
		this.id = id;
		this.titolo = titolo;
		this.codiceISBN = ISBN;
		this.dataInserimento = dataInvio;
		initList();
	}
	
	
	public Pubblicazione(int id, int idModificatore, String titolo, String ISBN, String modificataDa, Date ultimaModifica ) {
		this.id=id;
		this.idModificatore=idModificatore;
		this.titolo=titolo;
		this.codiceISBN=ISBN;
		this.modificataDa=modificataDa;
		this.ultimaModifica=ultimaModifica;
		initList();
	}
	
	public Pubblicazione(int id, String titolo, String ISBN, String editore) {
		this.id=id;
		this.titolo=titolo;
		this.codiceISBN=ISBN;
		this.editore=editore;
		initList();
	}
	public Pubblicazione(int id, String editore, String titolo, Date dataScrittura) {
		this.id = id;
		this.titolo = titolo;
		this.dataCreazione = dataScrittura;
		this.editore = editore;
		initList();
	}
	
	public Pubblicazione(int id, String titolo, String editore, String ISBN, Date dataInvio) {
		this.id = id;
		this.titolo = titolo;
		this.dataInserimento = dataInvio;
		this.editore = editore;
		this.codiceISBN = ISBN;
		initList();
		
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
		initList();
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
		initList();
	}

	public Pubblicazione(int id, String titolo, String ISBN, Date dataUltimaRistampa, String nomeUltimaRistampa) {
		this.id = id;
		this.titolo = titolo;
		this.codiceISBN = ISBN;
		this.dataUltimaRistampa = dataUltimaRistampa;
		this.nomeUltimaRistampa = nomeUltimaRistampa;
		initList();
	}	
	
	private void initList() {
		listaAutori = new ArrayList<String>();
		listaTag= new ArrayList<String>();
		
		listaCapitoli = new ArrayList<Capitolo>();
		listaSorgente = new ArrayList<Sorgente>();
		listaRistampe = new ArrayList<Ristampa>();
	}
	
	public ArrayList<Capitolo> getCapitoli(){
		return this.listaCapitoli;
	}
	public ArrayList<Sorgente> getSorgenti(){
		return this.listaSorgente;
	}
	public ArrayList<Ristampa> getRistampe(){
		return this.listaRistampe;
	}
	
	public Date getDataUltimaRistampa() {
		return this.dataUltimaRistampa;
	}
	public String getNomeUltimaRistampa() {
		return this.nomeUltimaRistampa;
	}
	public void addRistampa(Ristampa ristampa){
		this.listaRistampe.add(ristampa);
	}
	public void addSorgente(Sorgente sorgente){
		this.listaSorgente.add(sorgente);
	}
	public void addCapitolo(Capitolo capitolo){
		this.listaCapitoli.add(capitolo);
	}
	
	public ArrayList<String> getListaAutori(){
		return this.listaAutori;
	}
	
	public void addAutore(String autore) {
		this.listaAutori.add(autore);
	}
	public Boolean containsAutore(String autore) {
		if(this.listaAutori.contains(autore)) return true;
		return false;
		
	}
	public String showAutori() {
		String elenco="";
		int length = this.listaAutori.size();
		for(int i=0; i<length; i++) elenco += this.listaAutori.get(i) + ", ";
		elenco = elenco.substring(0, elenco.length() -2);
		return elenco;
	}
	public ArrayList<String> getListaTag(){
		return this.listaTag;
	}
	
	public void addTag(String tag) {
		this.listaTag.add(tag);
	}
	public Boolean containsTag(String tag) {
		if(this.listaTag.contains(tag)) return true;
		return false;
	}
	public String showTags() {
		String elenco="";
		int length = this.listaTag.size();
		for(int i=0; i<length; i++) elenco += this.listaTag.get(i) + ", ";
		elenco = elenco.substring(0, elenco.length() -2);
		return elenco;
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
	public String getEditore() {
		return this.editore;
	}
	public void setEditore(String editore) {
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

	
	public Date getUltimaMod() {
		return this.ultimaModifica;
	}
	public void setUltimaMod(Date ultimaModifica) {
		this.ultimaModifica = ultimaModifica;
	}
	
	public String getModificataDa() {
		return this.modificataDa;
	}
	public void setModificataDa(String modificataDa) {
		this.modificataDa=modificataDa;
	}
	
	
}