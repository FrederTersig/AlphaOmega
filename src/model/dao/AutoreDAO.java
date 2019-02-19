package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Autore;
import model.Tag;
import util.Database;

public class AutoreDAO implements AutoreDAO_interface  {
	
	
	public static List<Autore> showPubAuth(int idPubblicazione){
		ArrayList<Autore> lista=null;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("listaautori", "listaautori.idAutore = autore.id");
		try {
			lista = new ArrayList<Autore>();
			Database.connect();
			// JOIN per ricavare gli autori della pubblicazione
			ResultSet rs = Database.join("*", "autore", data, "listaautori.idPubblicazione = " + idPubblicazione, "autore.nomeAutore");
			while(rs.next()) {
				String nome = rs.getString("nomeAutore");
				int id = rs.getInt("id");
				Autore nuovoAut = new Autore(id,nome);
				lista.add(nuovoAut);
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
	public static  List<Autore> showAllAuth(){
		ArrayList<Autore> lista=null;
		try {
			lista = new ArrayList<Autore>();
			Database.connect();
			ResultSet rs = Database.selectRecord("*", "autore", "", "autore.nomeAutore");
			while(rs.next()) {
				String nome = rs.getString("nomeAutore");
				int id = rs.getInt("id");
				Autore nuovoAut = new Autore(id,nome);
				lista.add(nuovoAut);
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
	
	public static void insertPubAuth(int idPubblicazione, String[] listaIdAutore) {
		Map<String, Object> map = new HashMap<String, Object>();
	
		int a=0;
		int n=listaIdAutore.length;
		while (a<n) {
			map.put("idAutore", Integer.parseInt(listaIdAutore[a]));
			map.put("idPubblicazione", idPubblicazione);
			try {
				Database.connect();
				Database.insertRecord("listaautori", map);
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
	
	public static void insertAutore(String nomeAutore) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("nomeAutore", nomeAutore);
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
