package model;

import java.sql.Date;

public class Utente {
	
	int id;
	int ruolo;
	Date data; //Data Iscrizione al sito
	String nome;
	String cognome;
	String email;
	String citta;
	Date dataNascita;
	int attCont;
	int richiesta;
	
	public Utente(int id, int ruolo, Date data, String nome, String cognome, String email, String citta, 
			Date dataNascita,int richiesta) {
		this.id = id;
		this.ruolo = ruolo;
		this.data = data;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.citta = citta;
		this.dataNascita = dataNascita;
		this.richiesta=richiesta;
	}
	
	
	
	public Utente(int id, int ruolo, Date data, String nome, String cognome, String email, String citta, 
			Date dataNascita) {
		this.id = id;
		this.ruolo = ruolo;
		this.data = data;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.citta = citta;
		this.dataNascita = dataNascita;
	}
	
	
	public Utente(int id, String email, int ruolo, int attCont) {
		this.id=id;
		this.ruolo=ruolo;
		this.email=email;
		this.attCont=attCont;
	}
	
	public int getRichiesta() {
		return this.richiesta=richiesta;
	}
	
	public void setRichiesta(int n) {
		this.richiesta=n;
	}
	
	public int getAttCont(){
		return this.attCont;
	}
	public void setAttCont(int attCont) {
		this.attCont = attCont;
	}
	public int getId(){
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRuolo(){
		return this.ruolo;
	}
	public void setRuolo(int ruolo) {
		this.ruolo = ruolo;
	}
	public Date getData() {
		return this.data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getNome() {
		return this.nome;
	}
	public void setNome(String nome) {
		this.nome=nome;
	}
	public String getCognome() {
		return this.cognome;
	}
	public void setCognome(String cognome) {
		this.cognome=cognome;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email=email;
	}
	public String getCitta() {
		return this.citta;
	}
	public void setCitta(String citta) {
		this.citta=citta;
	}
	public Date getDataNascita() {
		return this.dataNascita;
	}
	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}
}
