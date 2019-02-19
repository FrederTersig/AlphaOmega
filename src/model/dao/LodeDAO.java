package model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Capitolo;
import model.Lode;
import util.Database;
import java.sql.Date;
import java.sql.ResultSet;

public class LodeDAO implements LodeDAO_interface {
	public static void insertLike(int idPubblicazione, int idUtente) { //inserisce un like ad una pubblicazione
		Map<String, Object> map = new HashMap<String, Object>();
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
	public static List<Integer> listaIdLike(int idPubblicazione){
		ArrayList<Integer> lista = null;
		
		try {
			lista = new ArrayList<Integer>();
			Database.connect();
			ResultSet rs =Database.selectRecord("lode.id", "lode","lode.idPubblicazione=" + idPubblicazione ,"");
			while(rs.next()) {
				int id = rs.getInt("id");
				lista.add(id);
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
	public static void updateLike(int id, Date data) { //int idPubblicazione, int idUtente) { //Si può decidere se rimuovere un like dato
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
		int number=0;
		String table="lode";
		String condition="lode.idPubblicazione=" + idPubblicazione;
		
		try {
			Database.connect();
			number = Database.countRecord(table, condition);
			Database.close();
		}catch(NamingException e) {
			System.out.println("namingException " +e);
		}catch (SQLException e) {
        	System.out.println("sqlException " +e);
        }catch (Exception e) {
        	System.out.println("Exception " + e);
        }

		return number;
	}
}
