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
			while(rs.next()) {
				int id = rs.getInt("id");
				int idInseritore = rs.getInt("idUtente");
				String editore = rs.getString("editore");
				Date dataInvio = rs.getDate("dataInvio");
				String titolo = rs.getString("titolo");
				String descrizione = rs.getString("descrizione");
				Pubblicazione pub = new Pubblicazione();
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
				Pubblicazione pub = new Pubblicazione();
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
	
	public static List<Pubblicazione> downloadPub(){ // mostra pubblicazioni che hanno un "download"
		ArrayList<Pubblicazione> lista=null;
		Map<String, Object> map = new HashMap<String, Object>();/*
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
	    }*/
		return lista;
	}
	
	public static List<Pubblicazione> userPub(int idUtente){//pubblicazioni di un utente
		ArrayList<Pubblicazione> lista=null;
		try {
			lista = new ArrayList<Pubblicazione>();
			Database.connect();
			ResultSet rs = Database.selectRecord("pubblicazione", "", "pubblicazione.idUtente="+idUtente, "pubblicazione.dataInvio");
			while(rs.next()) {
				int id = rs.getInt("id");
				int idInseritore = rs.getInt("idUtente");
				String editore = rs.getString("editore");
				Date dataInvio = rs.getDate("dataInvio");
				String titolo = rs.getString("titolo");
				String descrizione = rs.getString("descrizione");
				Pubblicazione pub = new Pubblicazione();
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
		
		String column="*";		
		String condition="pubblicazione.id="+ idPubblicazione;
		
		Pubblicazione pub = null;
		try {
			Database.connect();
			ResultSet rs = Database.selectRecord(column, "pubblicazione", condition, "");

			while(rs.next()) {
				int idInseritore = rs.getInt("pubblicazione.idUtente");
				String editore = rs.getString("pubblicazione.editore");
				Date dataInvio = rs.getDate("pubblicazione.dataInvio");
				String titolo = rs.getString("pubblicazione.titolo");
				String descrizione = rs.getString("pubblicazione.descrizione");;
				String ISBN = rs.getString("pubblicazione.ISBN");
				int numPagine = rs.getInt("pubblicazione.numPagine");
				String lingua = rs.getString("pubblicazione.lingua");
				Date dataScrittura = rs.getDate("pubblicazione.dataScrittura");
	
				pub = new Pubblicazione(idPubblicazione,idInseritore, editore, titolo, descrizione, dataInvio,
						ISBN, numPagine, lingua, dataScrittura);
			}
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		
		Map<String, Object> data = new HashMap<String,Object>();
		try {
			Database.connect();
			ResultSet rs = Database.selectRecord("*", "ristampa", "ristampa.idPubblicazione=" + pub.getId(), "");
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
			lista = new ArrayList<Pubblicazione>();
			Database.connect();
			ResultSet rs = Database.selectRecord("*", "pubblicazione", "", "pubblicazione.titolo");
			System.out.println("dopo rs");
			while(rs.next()) {
				System.out.println("inizio rs.next showCat()");
				int id = rs.getInt("id");
				Date dataScrittura = rs.getDate("dataScrittura");
				//Abbiamo bisogno anche della lista autori.
				String titolo = rs.getString("titolo");
				String editore = rs.getString("editore");
				System.out.println("vedo se le variabili sono nulle - >" + id +"|"+ editore +"|"+ titolo + "|" + dataScrittura);
				Pubblicazione pub = new Pubblicazione(id, editore, titolo, dataScrittura);
				lista.add(pub);
				System.out.println("Riesco a creare la lista aggiungendo pub : " + lista);
			}
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException"+e);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    	 e.printStackTrace(System.out);
	    }
		System.out.println("riesco ad uscire da qui???");
		return lista;
	}
	
	public static List<Pubblicazione> researchPub(String titolo, String ISBN, String Autore, String tag){ // Ricerca per varie cose-> popolare argomenti
		ArrayList<Pubblicazione> lista=null;
		//Query fatta nella stored procedure. 
		ArrayList<String> argum = new ArrayList<String>();
		argum.add(0, titolo);
		argum.add(1, ISBN);
		argum.add(2, Autore);
		argum.add(3, tag);
		System.out.println("researchPub PUBBLICAZIONE DAO::: argum");
		System.out.println(argum);
		System.out.println(argum.get(0) + argum.get(1) + argum.get(2) + argum.get(3));
		
		try {
			lista = new ArrayList<Pubblicazione>();
			int check=0;
			Database.connect();
			ResultSet rs = Database.callProcedure("ricerca", argum);
			while(rs.next()) {
				System.out.println("-------------- comincia rs.next() di researchPub -----------------");
				int id = rs.getInt("id");	
				System.out.println( check + " ---- " + id);
				if(check != id) {
					System.out.println("check Diverso da id!");
					check=id;
					String ISBN_ = rs.getString("ISBN");
					String titolo_ = rs.getString("titolo");
					String autore_ = rs.getString("autore.nomeAutore");
					String tag_ = rs.getString("tag.nome");
					System.out.println(ISBN_ + " " + titolo_ + " " + autore_ + " " + tag_ );
					Pubblicazione pub = new Pubblicazione(id,titolo_ , ISBN_);
					pub.addAutore(autore_);
					pub.addTag(tag_);
					lista.add(pub);
				}else { // check è == a id, stessi dati, devo prendere solo autore e tag SE sono diversi
					System.out.println("check uguale a id!");
					String autore_ = rs.getString("autore.nomeAutore");
					String tag_ = rs.getString("tag.nome");
					Pubblicazione pub = lista.get(lista.size()-1);
					if(!pub.containsAutore(autore_)) pub.addAutore(autore_);
					if(!pub.containsTag(tag_)) pub.addTag(tag_);
					lista.add(pub);
				}
			}
			Database.close();
		}catch(NamingException e) {
    		System.out.println("research pub pubblicazione DAO " +e);
        }catch (SQLException e) {
        	System.out.println("research pub pubblicazione DAO" +e);
        }catch (Exception e) {
        	System.out.println("research pub pubblicazione DAO" +e);    
        	 e.printStackTrace(System.out);
        }
		
		
		
		return lista;
	}
	public static List<Pubblicazione> showCatRist(){ //Mostra il catalogo ma con data ultima ristampa
		return null;
	}
	public static List<Pubblicazione> authOtherPub(){//data pubblicazione mostra altre pubb degli autori
		return null;
	}
	public static void insertPub(String descrizione, String titolo, String editore, 
			int idUtente, String ISBN, int numPagine, String lingua, Date dataCreazione) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("descrizione", descrizione);
		map.put("titolo",titolo);
		map.put("editore",editore);
		map.put("idUtente",idUtente);
		map.put("ISBN", ISBN);
		map.put("numPagine", numPagine);
		map.put("lingua",lingua);
		map.put("dataScrittura", dataCreazione);
		
		try {
			Database.connect();
			Database.insertRecord("pubblicazione", map);
			Database.close();
		}catch(NamingException e) {
    		System.out.println("prima parte " +e);
        }catch (SQLException e) {
        	System.out.println("prima parte " +e);
        }catch (Exception e) {
        	System.out.println("prima parte " +e);                           
        }

	}
	public static void updatePub(int id,String descrizione, String titolo, String editore, 
			int idUtente, String ISBN, int numPagine, String lingua, Date dataCreazione) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("descrizione", descrizione);
		map.put("titolo",titolo);
		map.put("editore",editore);
		map.put("idUtente",idUtente);
		map.put("ISBN", ISBN);
		map.put("numPagine", numPagine);
		map.put("lingua",lingua);
		map.put("dataScrittura", dataCreazione);
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