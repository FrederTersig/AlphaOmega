package model.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Pubblicazione;
import model.Tag;
import util.Database;

public class TagDAO implements TagDAO_interface {

	public static List<Tag> showPubTags(int idPubblicazione){
		ArrayList<Tag> lista=null;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("listatag", "listatag.idTag = tag.id");
		data.put("metadati", "metadati.id = listatag.idMetadati");
		try {
			lista = new ArrayList<Tag>();
			Database.connect();
			// JOIN per ricavare i tag della pubblicazione
			ResultSet rs = Database.join("*", "tag", data, "metadati.idPubblicazione = " + idPubblicazione, "tag.nome");
			while(rs.next()) {
				String nome = rs.getString("nome");
				int id = rs.getInt("id");
				Tag nuovoTag = new Tag(id,nome);
				lista.add(nuovoTag);
			}
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		
		return lista;
	}
	public static  List<Tag> showAllTags(){
		ArrayList<Tag> lista=null;
		try {
			lista = new ArrayList<Tag>();
			Database.connect();
			ResultSet rs = Database.selectRecord("*", "tag", "", "tag.nome");
			
			while(rs.next()) {
				String nome = rs.getString("nome");
				int id = rs.getInt("id");
				Tag nuovoTag = new Tag(id,nome);
				lista.add(nuovoTag);
			}
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		
		return lista;
	}
	
	public static void insertPubTag(int idPubblicazione, String[] listaIdTag) {
		Map<String, Object> map = new HashMap<String, Object>();
	
		int a=0;
		int n=listaIdTag.length;
		while (a<n) {
			map.put("idTag", Integer.parseInt(listaIdTag[a]));
			map.put("idPubblicazione", idPubblicazione);
			try {
				Database.connect();
				Database.insertRecord("listatag", map);
				Database.close();
			}catch(NamingException e) {
	    		System.out.println(e);
	        }catch (SQLException e) {
	        	System.out.println(e);
	        }catch (Exception e) {
	        	System.out.println(e);                           
	        }
			a++;
		}
		
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
