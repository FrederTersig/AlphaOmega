package model.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Tag;
import util.Database;

public class TagDAO implements TagDAO_interface {

	public static List<Tag> showPubTags(int idPubblicazione){
		return null;
	}
	public static  List<Tag> showAllTags(){
		return null;
	}
	public static void insertTag(String nome) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("nome", nome);
		try {
			Database.connect();
			Database.insertRecord("tag", map);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void updateTag(int id, String nome) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("nome", nome);
		try {
			Database.connect();
			Database.updateRecord("tag", map, "tag.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void deleteTag(int id) {
		try {
			Database.connect();
			Database.deleteRecord("tag", "tag.id="+id);
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
