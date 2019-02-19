package model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Recensione;
import util.Database;
import java.sql.Date;
import java.sql.ResultSet;

public class RecensioneDAO implements RecensioneDAO_interface {
	public static void insertRev(int idUtente, int idPubblicazione, String testo, int convalida) { //inserisce recensione ad una pubblicazione
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("idUtente", idUtente);
		map.put("idPubblicazione", idPubblicazione);
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
	public static void judgeRev(int id, int convalida) {//modifica una recensione
		Map<String, Object> map = new HashMap<String, Object>();
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
	public static void updateRev(int id, int idUtente, int idPubblicazione, Date data, String testo, int convalida) {//modifica una recensione
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("idUtente", idUtente);
		map.put("idPubblicazione", idPubblicazione);
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
	public static List<Recensione> revList(int idPubblicazione){//recensioni approvate
		ArrayList<Recensione> lista=null;
		String condition ="recensione.idPubblicazione="+idPubblicazione+" AND recensione.convalida=1";
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("utente", "recensione.idUtente = utente.id");
		
		
		try {
			lista = new ArrayList<Recensione>();
			Database.connect();
			ResultSet rs = Database.join("recensione.id, recensione.idUtente, recensione.data, recensione.testo, utente.email", "recensione", map,condition, "recensione.data DESC");
			//id, idUtente, idPubblicazione, data, testo, convalida
			while(rs.next()) {
				int id = rs.getInt("id");
				int idUtente = rs.getInt("idUtente");
				Date data = rs.getDate("data");
				String testo = rs.getString("testo");
				String email = rs.getString("email");
				//int convalida = rs.getInt("convalida");
				Recensione rec = new Recensione(id,idUtente,data,testo,email);
				lista.add(rec);
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
	public static List<Recensione> waitingRev(){//recensioni in attesa di giudizio
		ArrayList<Recensione> lista=null;
		String condition ="recensione.convalida=0";
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("utente", "recensione.idUtente = utente.id");
		map.put("pubblicazione", "recensione.idPubblicazione = pubblicazione.id");
		try {
			lista = new ArrayList<Recensione>();
			Database.connect();
			ResultSet rs = Database.join("recensione.id,recensione.idUtente, recensione.idPubblicazione, recensione.data, recensione.convalida, recensione.testo, utente.email, pubblicazione.titolo", "recensione", map, condition, "recensione.data DESC");
			//id, idUtente, idPubblicazione, data, testo, convalida
			while(rs.next()) {
				int id = rs.getInt("id");
				int idUtente = rs.getInt("idUtente");
				int idPubblicazione = rs.getInt("idPubblicazione");
				Date data = rs.getDate("data");
				String testo = rs.getString("testo");
				int convalida = rs.getInt("convalida");
				String email = rs.getString("email");
				String titolo = rs.getString("titolo");
				Recensione rec = new Recensione(id,idUtente,idPubblicazione,data,testo,convalida, email, titolo);
				lista.add(rec);
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
