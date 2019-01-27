package model.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Pubblicazione;
import model.Utente;
import util.Database;

public class PubblicazioneDAO implements PubblicazioneDAO_interface{
	public static List<Pubblicazione> lastTenPub(){ //Ultimi 10 giorni
		ArrayList<Pubblicazione> lista=null;
		try {
			Database.connect();
			ResultSet rs = Database.selectRecord("*", "pubblicazione", "", "pubblicazione.data DESC LIMIT 10");
			//ResultSet rs = Database.selectRecord("pubblicazione","pubblicazione.data DESC LIMIT 10");
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		return lista;
	}
	public static List<Pubblicazione> recentUpdates(){//Ultimi 30 giorni --> Meglio la Procedura??
		ArrayList<Pubblicazione> lista=null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Database.connect();
			map.put("pubblicazione.id", "entry.idPubblicazione");
			ResultSet rs = Database.join("*", "pubblicazione", map, "", "entry.data BETWEEN NOW() - INTERVAL 30 DAY AND NOW");
			//ResultSet rs = Database.selectJoin("*", "pubblicazione", "entry", "pubblicazione.id = entry.idPubblicazione", 
			//		"entry.data BETWEEN NOW() - INTERVAL 30 DAY AND NOW");
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		return lista;
	}
	public static List<Pubblicazione> userPub(int idUtente){//pubblicazioni di un utente
		ArrayList<Pubblicazione> lista=null;
		try {
			Database.connect();
			ResultSet rs = Database.selectRecord("pubblicazione", "", "pubblicazione.idUtente="+idUtente, "pubblicazione.dataInvio");
			//ResultSet rs = Database.selectRecord("pubblicazione", "pubblicazione.idUtente="+idUtente, "pubblicazione.dataInvio");
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		return lista;
		
	}
	public static Pubblicazione detailPub(int idPubblicazione) {//Dettagli di una specifica pubblicazione
		return null;
	}
	public static List<Pubblicazione> showCat(){//Mostra l'intero catalogo
		ArrayList<Pubblicazione> lista=null;
		try {
			Database.connect();
			ResultSet rs = Database.selectRecord("*", "pubblicazione", "", "pubblicazione.titolo");
			//ResultSet rs = Database.selectRecord("pubblicazione", "pubblicazione.titolo");
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		return lista;
	}
	public static List<Pubblicazione> researchPub(){ // Ricerca per varie cose-> popolare argomenti
		//Sfrutta la stored procedure
		return null;
	}
	public static List<Pubblicazione> showCatRist(){ //Mostra il catalogo ma con data ultima ristampa
		return null;
	}
	public static List<Pubblicazione> authOtherPub(){//data pubblicazione mostra altre pubb degli autori
		return null;
	}
	public static void insertPub(int id, String descrizione, Date dataInvio, String titolo, int idEditore, int idUtente) {//Inserisci pubblicazione -> popolare argomenti
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("id",id);
		map.put("descrizione", descrizione);
		map.put("dataInvio", dataInvio);
		map.put("titolo",titolo);
		map.put("idEditore",idEditore);
		map.put("idUtente",idUtente);
		try {
			Database.connect();
			Database.insertRecord("pubblicazione", map);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void updatePub(int id, String descrizione, Date dataInvio, String titolo, int idEditore, int idUtente) {//update pubblicazione -> simile alla insert
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("descrizione", descrizione);
		map.put("dataInvio", dataInvio);
		map.put("titolo",titolo);
		map.put("idEditore",idEditore);
		map.put("idUtente",idUtente);
		try {
			Database.connect();
			Database.updateRecord("pubblicazione", map, "pubblicazione.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void deletePub(int id) {//Si cancellano pure le modifiche di una pubblicazione?
		try {
			Database.connect();
			Database.deleteRecord("pubblicazione", "pubblicazione.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println("namingException " +e);
        }catch (SQLException e) {
        	System.out.println("sqlException " +e);
        }catch (Exception e) {
        	System.out.println("Exception " + e);
        }
	}
}