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
import model.Utente;
import util.Database;

public class PubblicazioneDAO implements PubblicazioneDAO_interface{
	public static List<Pubblicazione> lastTenPub(){ //Ultimi 10 giorni
		ArrayList<Pubblicazione> lista=null;
		try {
			lista = new ArrayList<Pubblicazione>();
			Database.connect();
			ResultSet rs = Database.selectRecord("*", "pubblicazione", "", "pubblicazione.dataInvio DESC LIMIT 10");
			//ResultSet rs = Database.selectRecord("pubblicazione","pubblicazione.data DESC LIMIT 10");
			while(rs.next()) {
				int id = rs.getInt("id");
				int idInseritore = rs.getInt("idUtente");
				String editore = rs.getString("editore");
				Date dataInvio = rs.getDate("dataInvio");
				String titolo = rs.getString("titolo");
				String descrizione = rs.getString("descrizione");
				Pubblicazione pub = new Pubblicazione(id,idInseritore, editore, titolo, descrizione, dataInvio);
				lista.add(pub);
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
	public static List<Pubblicazione> recentUpdated(){//Ultimi 30 giorni --> Meglio la Procedura??
		ArrayList<Pubblicazione> lista=null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("entry", "pubblicazione.id = entry.idPubblicazione");
		
		try {
			lista = new ArrayList<Pubblicazione>();
			Database.connect();
			// **** DA CONTROLLARE IL RISULTATO -> al posto di * mettere "entry.data, pubblicazione.titolo ecc"
			ResultSet rs = Database.join("*", "pubblicazione", map, "", "entry.data BETWEEN NOW() - INTERVAL 30 DAY AND NOW");
			//ResultSet rs = Database.selectJoin("*", "pubblicazione", "entry", "pubblicazione.id = entry.idPubblicazione", 
			//		"entry.data BETWEEN NOW() - INTERVAL 30 DAY AND NOW");
			
			while(rs.next()) {
				int id = rs.getInt("id");
				int idInseritore = rs.getInt("idUtente");
				String editore = rs.getString("editore");
				Date dataInvio = rs.getDate("dataInvio");
				String titolo = rs.getString("titolo");
				String descrizione = rs.getString("descrizione");
				Pubblicazione pub = new Pubblicazione(id,idInseritore, editore, titolo, descrizione, dataInvio);
				lista.add(pub);
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
	public static List<Pubblicazione> userPub(int idUtente){//pubblicazioni di un utente
		ArrayList<Pubblicazione> lista=null;
		try {
			lista = new ArrayList<Pubblicazione>();
			Database.connect();
			ResultSet rs = Database.selectRecord("pubblicazione", "", "pubblicazione.idUtente="+idUtente, "pubblicazione.dataInvio");
			//ResultSet rs = Database.selectRecord("pubblicazione", "pubblicazione.idUtente="+idUtente, "pubblicazione.dataInvio");
			while(rs.next()) {
				int id = rs.getInt("id");
				int idInseritore = rs.getInt("idUtente");
				String editore = rs.getString("editore");
				Date dataInvio = rs.getDate("dataInvio");
				String titolo = rs.getString("titolo");
				String descrizione = rs.getString("descrizione");
				Pubblicazione pub = new Pubblicazione(id,idInseritore, editore, titolo, descrizione, dataInvio);
				lista.add(pub);
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
	public static Pubblicazione detailPub(int idPubblicazione) {//Dettagli di una specifica pubblicazione
		//DA CONTROLLARE E CORREGGERE
		
		String columns="pubblicazione.idUtente, pubblicazione.editore, pubblicazione.titolo, pubblicazione.descrizione,"
				+ " pubblicazione.dataInvio, metadati.id, metadati.ISBN, metadati.numPagine, metadati.lingua, metadati.data ";
		
		String onMet="pubblicazione.id=metadati.idPubblicazione";
		//String onRis="";
		String condition="pubblicazione.id="+ idPubblicazione;
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("metadati", onMet);
		
		Pubblicazione pub = null;
		try {
			Database.connect();
			ResultSet rs = Database.join(columns, "pubblicazione", map, condition, "");	
			while(rs.next()) {
				int idInseritore = rs.getInt("pubblicazione.idUtente");
				String editore = rs.getString("pubblicazione.editore");
				Date dataInvio = rs.getDate("pubblicazione.dataInvio");
				String titolo = rs.getString("pubblicazione.titolo");
				String descrizione = rs.getString("pubblicazione.descrizione");
				//parte metadati
				int idMetadati = rs.getInt("metadati.id");
				String ISBN = rs.getString("metadati.ISBN");
				int numPagine = rs.getInt("metadati.numPagine");
				String lingua = rs.getString("metadati.lingua");
				Date dataCreazione = rs.getDate("metadati.data");
	
				pub = new Pubblicazione(idPubblicazione,idInseritore, editore, titolo, descrizione, dataInvio,
						idMetadati, ISBN, numPagine, lingua, dataCreazione);
			}
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		
		//Prova a vedere se il riutilizzo di "map" può aiutare
		Map<String, Object> data = new HashMap<String,Object>();
		try {
			Database.connect();
			ResultSet rs = Database.selectRecord("*", "ristampa", "ristampa.idMetadati=" + pub.getIdMetadati(), "");
			while(rs.next()){
				data.put("numero", rs.getInt("numero"));
				data.put("data", rs.getDate("data"));
			}
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		
		pub.setElencoRistampe(data);
		
		return pub;
	}
	public static List<Pubblicazione> showCat(){//Mostra l'intero catalogo
		ArrayList<Pubblicazione> lista=null;
		try {
			Database.connect();
			ResultSet rs = Database.selectRecord("*", "pubblicazione", "", "pubblicazione.titolo");
			//ResultSet rs = Database.selectRecord("pubblicazione", "pubblicazione.titolo");
			while(rs.next()) {
				int id = rs.getInt("id");
				int idInseritore = rs.getInt("idUtente");
				String editore = rs.getString("editore");
				Date dataInvio = rs.getDate("dataInvio");
				String titolo = rs.getString("titolo");
				String descrizione = rs.getString("descrizione");
				Pubblicazione pub = new Pubblicazione(id,idInseritore, editore, titolo, descrizione, dataInvio);
				lista.add(pub);
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
	/*ELEMENTI IMPORTANTI PER LA CHIAMATA
	 * Titolo, ISBN, Autore, Editore, Tag
	 * Download disponibile
	 * Data Rilascio (metadati.data)
	 * Data Ultima Ristampa (!!!!)
	 * Chiamata Critica
	 * */
	public static List<Pubblicazione> researchPub(){ // Ricerca per varie cose-> popolare argomenti
		ArrayList<Pubblicazione> lista=null;
		//Query fatta nella stored procedure. 
		return lista;
	}
	public static List<Pubblicazione> showCatRist(){ //Mostra il catalogo ma con data ultima ristampa
		return null;
	}
	public static List<Pubblicazione> authOtherPub(){//data pubblicazione mostra altre pubb degli autori
		return null;
	}
	public static void insertPub(int id, String descrizione, Date dataInvio, String titolo, String editore, int idUtente) {//Inserisci pubblicazione -> popolare argomenti
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("id",id);
		map.put("descrizione", descrizione);
		map.put("dataInvio", dataInvio);
		map.put("titolo",titolo);
		map.put("editore",editore);
		map.put("idUtente",idUtente);
		try {
			Database.connect();
			Database.insertRecord("pubblicazione", map);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void updatePub(int id, String descrizione, Date dataInvio, String titolo, String editore, int idUtente) {//update pubblicazione -> simile alla insert
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("descrizione", descrizione);
		map.put("dataInvio", dataInvio);
		map.put("titolo",titolo);
		map.put("editore",editore);
		map.put("idUtente",idUtente);
		try {
			Database.connect();
			Database.updateRecord("pubblicazione", map, "pubblicazione.id="+id);
			Database.close();
		}catch(NamingException e) {
    		System.out.println(e);
        }catch (SQLException e) {
        	System.out.println(e);
        }catch (Exception e) {
        	System.out.println(e);                           
        }
	}
	public static void deletePub(int id) {//Si cancellano pure le modifiche di una pubblicazione?
		try {
			Database.connect();
			Database.deleteRecord("pubblicazione", "pubblicazione.id="+id);
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