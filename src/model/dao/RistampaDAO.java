package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Ristampa;
import util.Database;

public class RistampaDAO implements RistampaDAO_interface {
	public static List<Ristampa> listRisPub(int idPubblicazione){
		ArrayList<Ristampa> lista=null;
		try {
			lista = new ArrayList<Ristampa>();
			Database.connect();
			ResultSet rs =Database.selectRecord("*", "ristampa","ristampa.idPubblicazione=" + idPubblicazione ,"ristampa.numero");
			while(rs.next()) {
				int id = rs.getInt("id");
				int idPub = rs.getInt("idPubblicazione");
				int numero = rs.getInt("numero");
				Date data = rs.getDate("data");
				Ristampa ris = new Ristampa(id,numero,idPub,data);
				lista.add(ris);
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
	public static void insertRistampa(int numero, int idPub, Date dataRistampa) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("idPubblicazione", idPub);
		map.put("numero", numero);
		map.put("data", dataRistampa);
		
		try {
			Database.connect();
			Database.insertRecord("ristampa", map);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
		
	}
	public static void updateRistampa(int id, int numero, int idPub, Date data) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("idPubblicazione", idPub);
		map.put("numero", numero);
		map.put("data", data);
		
		try {
			Database.connect();
			Database.updateRecord("ristampa", map, "ristampa.id =" + id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void deleteRistampa(int id) {
		try {
			Database.connect();
			Database.deleteRecord("ristampa", "ristampa.id="+id);
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
