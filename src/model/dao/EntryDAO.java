package model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Entry;
import model.Pubblicazione;
import util.Database;
import java.sql.Date;
import java.sql.ResultSet;

public class EntryDAO implements EntryDAO_interface {
	
	public static List<Entry> showUtEntries(int idUt){
		ArrayList<Entry> lista=null;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("pubblicazione", "entry.idPubblicazione = pubblicazione.id");
		try {
			lista = new ArrayList<Entry>();
			Database.connect();
			ResultSet rs = Database.join("entry.id, entry.idPubblicazione, entry.data, entry.descrizione, pubblicazione.titolo", "entry", map, "entry.idUtente="+idUt, "entry.data DESC");
			while(rs.next()) {
				int id = rs.getInt("entry.id");
				int idPubblicazione = rs.getInt("entry.idPubblicazione");
				Date data = rs.getDate("entry.data");
				String descrizione = rs.getString("entry.descrizione");
				String titolo = rs.getString("pubblicazione.titolo");
				Entry modifica = new Entry(id,idPubblicazione,data,descrizione, titolo);
				lista.add(modifica);
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
	
	
	public static List<Entry> showPubEntries(int idPub){ //Data pubblicazione, mostra tutte le modifiche
		ArrayList<Entry> lista=null;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("utente", "entry.idUtente = utente.id");
		try {
			lista=new ArrayList<Entry>();
			Database.connect();
			ResultSet rs = Database.join("entry.id, entry.idUtente, entry.idPubblicazione, entry.data, entry.descrizione, utente.email", "entry", map,"entry.idPubblicazione="+idPub, "entry.data DESC");
			while(rs.next()) {
				int id = rs.getInt("id");
				int idUtente = rs.getInt("idUtente");
				int idPubblicazione = rs.getInt("idPubblicazione");
				Date data = rs.getDate("data");
				String descrizione = rs.getString("descrizione");
				String email = rs.getString("email");
				Entry modifica = new Entry(id,idUtente,idPubblicazione,data,descrizione, email);
				lista.add(modifica);
			}
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
		
		return lista;
	}
	public static void insertEntry(String descrizione, int idPubblicazione, int idUtente) { //Inserisce una modifica ad una pubblicazione -> argomenti da popolare
		Map<String, Object> map = new HashMap<String, Object>();
		
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
