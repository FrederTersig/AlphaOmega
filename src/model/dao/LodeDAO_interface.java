package model.dao;

import java.util.List;
import model.Lode;

public interface LodeDAO_interface {
	public static void insertLike(int idPubblicazione) { //inserisce un like ad una pubblicazione
	
	}
	public static void updateLike() { //Si può decidere se rimuovere un like dato
		
	}
	public static void deleteLike() {
		
	}
	public static int countLike(int idPubblicazione) {//conta il totale dei like dati ad una pubblicazione
		return 0;
	}
	public static List<Integer> listaIdLike(int idPubblicazione){
		return null;
	}
}
