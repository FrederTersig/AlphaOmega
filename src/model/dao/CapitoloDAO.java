package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import model.Capitolo;
import java.util.ArrayList;
import java.util.List;
import util.Database;

public class CapitoloDAO implements CapitoloDAO_interface {
	public static List<Capitolo> listCapPub(int idPubblicazione){
		ArrayList<Capitolo> lista=null;
		try {
			lista = new ArrayList<Capitolo>();
			Database.connect();
			ResultSet rs =Database.selectRecord("*", "capitolo","capitolo.idPubblicazione=" + idPubblicazione ,"capitolo.numero");
			while(rs.next()) {
				int id = rs.getInt("id");
				int idPub = rs.getInt("idPubblicazione");
				int numero = rs.getInt("numero");
				String titolo = rs.getString("titolo");
				int pagInizio = rs.getInt("pagInizio");
				Capitolo cap = new Capitolo(id,idPub,titolo,numero,pagInizio);
				lista.add(cap);
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
