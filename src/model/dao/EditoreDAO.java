package model.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import model.Editore;
import util.Database;

public class EditoreDAO implements EditoreDAO_interface {

	public static void addEditore(String nome) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("nome", nome);
		try {
			Database.connect();
			Database.insertRecord("editore", map);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void updateEditore(int id, String nome) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("nome", nome);
		try {
			Database.connect();
			Database.updateRecord("editore", map, "editore.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void deleteEditore(int id) {
		try {
			Database.connect();
			Database.deleteRecord("editore", "editore.id="+id);
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
