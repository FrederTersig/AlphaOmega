package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Capitolo;
import model.Sorgente;
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
	public static List<Sorgente> listSorPub(int idPubblicazione){
		ArrayList<Sorgente> lista=null;
		try {
			lista = new ArrayList<Sorgente>();
			Database.connect();
			ResultSet rs =Database.selectRecord("*", "sorgente","sorgente.idPubblicazione=" + idPubblicazione ,"");
			while(rs.next()) {
				int id = rs.getInt("id");
				String tipo = rs.getString("tipo");
				String URI = rs.getString("URI");
				String formato = rs.getString("formato");
				String descrizione = rs.getString("descrizione");
				Sorgente sor = new Sorgente(id,tipo,URI,formato,descrizione);
				lista.add(sor);
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
