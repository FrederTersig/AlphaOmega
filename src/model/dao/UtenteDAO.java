package model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;
import java.sql.ResultSet;

import javax.naming.NamingException;

import static util.Utile.crypt;
import static util.Utile.decrypt;

import model.Entry;
import model.Pubblicazione;
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
			ArrayList<Utente> lista= null;
			try {
				lista = new ArrayList<Utente>();
				Database.connect();
				ResultSet rs = Database.selectRecord("pubblicazione.idUtente, count(*) AS conto, utente.email, utente.ruolo ", "pubblicazione, utente", "pubblicazione.idUtente = utente.id GROUP BY pubblicazione.idUtente", "conto DESC");
				while(rs.next()) {	
					int id = rs.getInt("pubblicazione.idUtente");
					int ruolo = rs.getInt("utente.ruolo");
					String email = rs.getString("utente.email");
					int conto = rs.getInt("conto");
					Utente us = new Utente(id,email,ruolo,conto);
					lista.add(us);
				}
				Database.close();
			}catch(NamingException e) {
	    		System.out.println(e);
	        }catch (SQLException e) {
	        	System.out.println(e);
	        	e.printStackTrace();
	        }catch (Exception e) {
	        	System.out.println(e);                           
	        }
			return lista;
	}
	


	public static List<Utente> showAllUser(){
		ArrayList<Utente> lista=null;
		try {
			lista = new ArrayList<Utente>();
			Database.connect();
			ResultSet rs =Database.selectRecord("*", "utente", "", "utente.nome DESC");
			while(rs.next()) {
				int id = rs.getInt("id");
				int ruolo = rs.getInt("ruolo");
				Date dataIscr = rs.getDate("dataIscr");
				Date dataNascita = rs.getDate("dataNascita");
				String email = rs.getString("email");
				String nome = rs.getString("nome");
				String cognome = rs.getString("cognome");
				String citta = rs.getString("citta");
				Utente ut = new Utente(id, ruolo, dataIscr, nome, cognome, email, citta, dataNascita);
				lista.add(ut);
			}
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println("SQL exception nel DAO -> " + e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
		return lista;
	}
	
	public static void updateRichiesta(int ruolo,int richiesta, int id) {
		Map<String, Object> map = new HashMap<String, Object>();	
		map.put("ruolo", ruolo);
		map.put("richiesta", richiesta);
		
		try {
			Database.connect();
			Database.updateRecord("utente", map, "utente.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println("SQL exception nel DAO -> " + e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	
	public static List<Utente> showRichiestaUt(){
		ArrayList<Utente> lista=null;
		try {
			lista = new ArrayList<Utente>();
			Database.connect();
			ResultSet rs =Database.selectRecord("*", "utente", "utente.richiesta=1 AND utente.ruolo=0", "");
			while(rs.next()) {
				int id = rs.getInt("id");
				int ruolo = rs.getInt("ruolo");
				Date dataIscr = rs.getDate("dataIscr");
				Date dataNascita = rs.getDate("dataNascita");
				String email = rs.getString("email");
				String nome = rs.getString("nome");
				String cognome = rs.getString("cognome");
				String citta = rs.getString("citta");
				Utente ut = new Utente(id, ruolo, dataIscr, nome, cognome, email, citta, dataNascita);
				lista.add(ut);
			}
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println("SQL exception nel DAO -> " + e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
		return lista;
	}
	
	public static Utente showDetailUser(int id) { //mostra dettagli dell'utente
		Utente ut=null;
		try {
			Database.connect();
			ResultSet rs =Database.selectRecord("*", "utente", "utente.id="+id, "");
			while(rs.next()) {
				int ruolo = rs.getInt("ruolo");
				Date dataIscr = rs.getDate("dataIscr");
				Date dataNascita = rs.getDate("dataNascita");
				String email = rs.getString("email");
				String nome = rs.getString("nome");
				String cognome = rs.getString("cognome");
				String citta = rs.getString("citta");
				int richiesta = rs.getInt("richiesta");
				ut = new Utente(id, ruolo, dataIscr, nome, cognome, email, citta, dataNascita, richiesta);
			}
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println("SQL exception nel DAO -> " + e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
		
		return ut;
	}
	
}
