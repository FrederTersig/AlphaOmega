package model.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Entry;
import util.Database;
import java.sql.Date;

public class EntryDAO implements EntryDAO_interface {
	public static List<Entry> showPubEntries(int idPubblicazione){ //Data pubblicazione, mostra tutte le modifiche
		return null;
	}
	public static void insertEntry(Date data, String descrizione, int idPubblicazione, int idUtente) { //Inserisce una modifica ad una pubblicazione -> argomenti da popolare
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("data", data);
		map.put("descrizione", descrizione);
		map.put("idPubblicazione", idPubblicazione);
		map.put("idUtente", idUtente);
		
		try {
			Database.connect();
			Database.insertRecord("entry", map);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void updateEntry(int id, Date data, String descrizione, int idPubblicazione, int idUtente) { //Aggiorna entry. #> Cancella utente > modifica entry per farle rimanere
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("data", data);
		map.put("descrizione", descrizione);
		map.put("idPubblicazione", idPubblicazione);
		map.put("idUtente", idUtente);
		
		try {
			Database.connect();
			Database.updateRecord("entry", map, "entry.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void deleteEntry(int id) { 
		try {
			Database.connect();
			Database.deleteRecord("entry", "entry.id="+id);
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
