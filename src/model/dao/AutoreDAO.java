package model.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import model.Autore;
import util.Database;

public class AutoreDAO implements AutoreDAO_interface  {

	public static void insertAutore() { //Mancano gli attributi per la insert
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			Database.connect();
			Database.insertRecord("autore", map);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void updateAutore(int id) { // Mancano gli attributi per la update
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			Database.connect();
			Database.updateRecord("autore", map, "autore.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void deleteAutore(int id) { //completo
		try {
			Database.connect();
			Database.deleteRecord("autore", "autore.id="+id);
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
