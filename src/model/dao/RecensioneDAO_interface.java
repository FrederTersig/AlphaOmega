package model.dao;

import java.util.List;
import model.Recensione;

public interface RecensioneDAO_interface {
	public static void insertRev(int idPubblicazione) { //inserisce recensione ad una pubblicazione
		
	}
	public static void judgeRev(int id, int convalida) {//modifica una recensione
		
	}
	public static void updateRev(int id) {//modifica una recensione
		
	}
	public static List<Recensione> revList(){//recensioni approvate
		return null;
	}
	public static List<Recensione> waitingRev(){//recensioni in attesa di giudizio
		return null;
	}
	public static void deleteRev(int id) {//cancella recensione
		
	}
}
