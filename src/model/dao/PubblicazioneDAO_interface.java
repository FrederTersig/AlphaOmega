package model.dao;

import java.util.List;

import model.Pubblicazione;

public interface PubblicazioneDAO_interface {
	public static List<Pubblicazione> lastTenPub(){ //Ultimi 10 giorni
		return null;
	}
	public static List<Pubblicazione> recentUpdates(){//Ultimi 30 giorni
		return null;
	}
	public static List<Pubblicazione> downloadPub(){
		return null;
	}
	public static List<Pubblicazione> userPub(int idUtente){//pubblicazioni di un utente
		return null;
	}
	public static Pubblicazione detailPub(int idPubblicazione) {//Dettagli di una specifica pubblicazione
		return null;
	}
	public static List<Pubblicazione> showCat(){//Mostra l'intero catalogo
		return null;
	}
	public static List<Pubblicazione> researchPub(){ // Ricerca per varie cose-> popolare argomenti
		return null;
	}
	public static List<Pubblicazione> showCatRist(){ //Mostra il catalogo ma con data ultima ristampa
		return null;
	}
	public static List<Pubblicazione> authOtherPub(){//data pubblicazione mostra altre pubb degli autori
		return null;
	}
	public static void insertPub() {//Inserisci pubblicazione -> popolare argomenti
		
	}
	public static boolean updatePub(int id) {//update pubblicazione -> simile alla insert
		return false;
	}
	public static boolean deletePub(int id) {//Cancellazione pubblicazione (bisogna specificare l'id)
		return false;
	}
}
