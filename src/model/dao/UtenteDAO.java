package model.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;
import javax.naming.NamingException;

import static util.Utile.crypt;
import model.Utente;
import util.Database;

public class UtenteDAO implements UtenteDAO_interface {
	public static void updateUser(int id, String email, Date data, String nome, String cognome, String password, int ruolo, 
			String citta, Date dataNascita) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("email", email);
		map.put("dataIscr", data);
		map.put("nome", nome);
		map.put("cognome", cognome);
		map.put("password", crypt(password));
		map.put("ruolo", ruolo);
		map.put("citta", citta);
		map.put("dataNascita", dataNascita);
		
		try {
			Database.connect();
			Database.updateRecord("utente", map, "utente.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void deleteUser(int id) {
		try {
			Database.connect();
			Database.deleteRecord("utente", "utente.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println("namingException " +e);
        }catch (SQLException e) {
        	System.out.println("sqlException " +e);
        }catch (Exception e) {
        	System.out.println("Exception " + e);
        }
	}
	public static void insertUser(String email, String nome, String cognome, String password, int ruolo, 
			String citta, Date dataNascita) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("email", email);
		//map.put("dataIscr", dataAttuale);
		map.put("nome", nome);
		map.put("cognome", cognome);
		map.put("password", crypt(password));
		map.put("ruolo", ruolo);
		map.put("citta", citta);
		map.put("dataNascita", dataNascita);
		
		try {
			Database.connect();
			Database.insertRecord("utente", map);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println("SQL exception nel DAO -> " + e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	//query difficile, critica. bisogna trovare il modo di farla!!!! subito!!!! non ho TEMPO per pensare!!! 
	public static List<Utente> mostActiveUsers(){ //che hanno inserito più pubblicazioni
		return null;
	}
	public static List<Utente> showAllUser(){
		return null;
	}
	public static Utente showDetailUser(int id) { //mostra dettagli dell'utente
		return null;
	}
	
	// conteggio like? conteggio recensioni?
}
