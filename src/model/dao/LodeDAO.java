package model.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Lode;
import util.Database;
import java.sql.Date;

public class LodeDAO implements LodeDAO_interface {
	public static void insertLike(int idPubblicazione, int idUtente, Date data) { //inserisce un like ad una pubblicazione
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", data);
		map.put("idPubblicazione", idPubblicazione);
		map.put("idUtente", idUtente);
		try {
			Database.connect();
			Database.insertRecord("lode", map);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
		
	}
	public static void updateLike(int id, Date data) { //int idPubblicazione, int idUtente) { //Si pu� decidere se rimuovere un like dato
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("data", data);
		//map.put("idPubblicazione", idPubblicazione);
		//map.put("idUtente", idUtente);
		try {
			Database.connect();
			Database.updateRecord("lode", map, "lode.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void deleteLike(int id) {
		try {
			Database.connect();
			Database.deleteRecord("lode", "lode.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println("namingException " +e);
        }catch (SQLException e) {
        	System.out.println("sqlException " +e);
        }catch (Exception e) {
        	System.out.println("Exception " + e);
        }
	}
	public static int countLike(int idPubblicazione) {//conta il totale dei like dati ad una pubblicazione
		return 0;
	}
}
