package model.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Recensione;
import util.Database;
import java.sql.Date;

public class RecensioneDAO implements RecensioneDAO_interface {
	public static void insertRev(int idUtente, int idPubblicazione, Date data, String testo, int convalida) { //inserisce recensione ad una pubblicazione
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("idUtente", idUtente);
		map.put("int idPubblicazione", idPubblicazione);
		map.put("data", data);
		map.put("testo", testo);
		map.put("convalida", convalida);
		try {
			Database.connect();
			Database.insertRecord("recensione", map);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void updateRev(int id, int idUtente, int idPubblicazione, Date data, String testo, int convalida) {//modifica una recensione
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("idUtente", idUtente);
		map.put("int idPubblicazione", idPubblicazione);
		map.put("data", data);
		map.put("testo", testo);
		map.put("convalida", convalida);
		try {
			Database.connect();
			Database.updateRecord("recensione", map, "recensione.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static List<Recensione> approvedRev(){//recensioni approvate
		return null;
	}
	public static List<Recensione> waitingRev(){//recensioni in attesa di giudizio
		return null;
	}
	public static void deleteRev(int id) {//cancella recensione
		try {
			Database.connect();
			Database.deleteRecord("recensione", "recensione.id="+id);
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
