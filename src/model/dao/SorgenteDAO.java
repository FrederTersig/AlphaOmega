package model.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import util.Database;

public class SorgenteDAO implements SorgenteDAO_interface {
	public static void insertSorgente(String descrizione, String formato, int idPubblicazione, String tipo, String URI) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("descrizione", descrizione);
		map.put("formato", formato);
		map.put("idPubblicazione",idPubblicazione);
		map.put("tipo", tipo);
		map.put("URI", URI);
		try {
			Database.connect();
			Database.insertRecord("sorgente", map);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void updateSorgente(int id, String descrizione, String formato, int idPubblicazione, String tipo, String URI) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("descrizione", descrizione);
		map.put("formato", formato);
		map.put("idPubblicazione",idPubblicazione);
		map.put("tipo", tipo);
		map.put("URI", URI);
		try {
			Database.connect();
			Database.updateRecord("sorgente", map, "sorgente.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void deleteSorgente(int id) {
		try {
			Database.connect();
			Database.deleteRecord("sorgente", "sorgente.id="+id);
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
