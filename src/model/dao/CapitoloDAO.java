package model.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import util.Database;

public class CapitoloDAO implements CapitoloDAO_interface {
	public static void insertCapitolo(int idPubblicazione, int numero, int pagInizio, String titolo) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("idPubblicazione", idPubblicazione);
		map.put("numero",numero);
		map.put("pagInizio",pagInizio);
		map.put("titolo",titolo);
		
		try {
			Database.connect();
			Database.insertRecord("capitolo", map);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void updateCapitolo(int id, int idPubblicazione, int numero, int pagInizio, String titolo) {
		Map<String, Object> map = new HashMap<String, Object>();
		//Posizionare controllo SE argomento null?
		map.put("idPubblicazione", idPubblicazione);
		map.put("numero",numero);
		map.put("pagInizio",pagInizio);
		map.put("titolo",titolo);
		
		try {
			Database.connect();
			Database.updateRecord("capitolo", map, "capitolo.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void deleteCapitolo(int id) {
		try {
			Database.connect();
			Database.deleteRecord("capitolo", "capitolo.id="+id);
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
