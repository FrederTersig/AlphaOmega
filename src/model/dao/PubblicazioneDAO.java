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
	// QUERY FATTA
	public static List<Pubblicazione> lastTenPub(){ //Ultimi 10 giorni
		ArrayList<Pubblicazione> lista=null;
		String column = "pubblicazione.id, pubblicazione.dataInvio, pubblicazione.titolo, pubblicazione.ISBN, autore.nomeAutore";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("listaautore", "listaautori.idPubblicazione = Pubblicazione.id");
		map.put("autore", "listaautori.idAutore = autore.id");
		int check=0;
		try {
			lista = new ArrayList<Pubblicazione>();
			Database.connect();
			
			//join per autore.
			ResultSet rs=  Database.join(column, "pubblicazione", map, "", "pubblicazione.dataInvio DESC LIMIT 10");
			while(rs.next()) {
				int id = rs.getInt("id");
				if(check != id) {
					System.out.println("check Diverso da id!");
					check=id;
					Date dataInvio = rs.getDate("dataInvio");
					String titolo = rs.getString("titolo");
					String ISBN = rs.getString("ISBN");
					String autore_ = rs.getString("autore.nomeAutore");
					Pubblicazione pub = new Pubblicazione();
					pub.addAutore(autore_);
					lista.add(pub);
				}else {
					System.out.println("check uguale a id!");
					String autore_ = rs.getString("autore.nomeAutore");
					if(!lista.get(lista.size()-1).containsAutore(autore_)) lista.get(lista.size()-1).addAutore(autore_);
				}
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
	// manca PUBBLICAZIONE classe da popolare, ma per il resto è finita
	public static List<Pubblicazione> recentUpdated(){//Ultimi 30 giorni --> Meglio la Procedura??
		ArrayList<Pubblicazione> lista=null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("entry", "pubblicazione.id = entry.idPubblicazione");
		map.put("utente", "utente.id = entry.idUtente");
		try {
			lista = new ArrayList<Pubblicazione>();
			Database.connect();
			// **** DA CONTROLLARE IL RISULTATO -> al posto di * mettere "entry.data, pubblicazione.titolo ecc"
			ResultSet rs = Database.join("*", "pubblicazione", map, "", "entry.data BETWEEN NOW() - INTERVAL 30 DAY AND NOW");
			//ResultSet rs = Database.selectJoin("*", "pubblicazione", "entry", "pubblicazione.id = entry.idPubblicazione", 
			//		"entry.data BETWEEN NOW() - INTERVAL 30 DAY AND NOW");
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String ISBN = rs.getString("ISBN");
				String titolo = rs.getString("titolo");
				Date ultimaModifica = rs.getDate("entry.data");
				String ModificataDa = rs.getString("utente.email");
				int utenteId = rs.getInt("utente.id");
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
	// fatta
	public static List<Pubblicazione> downloadPub(){ // mostra pubblicazioni che hanno un "download"
		ArrayList<Pubblicazione> lista=null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sorgente", "sorgente.idPubblicazione = pubblicazione.id");
		try {
			lista = new ArrayList<Pubblicazione>();
			Database.connect();
			ResultSet rs = Database.join("*", "pubblicazione", map, "sorgente.tipo='download'", "pubblicazione.titolo");
			while(rs.next()) {
				int id = rs.getInt("id");
				String titolo = rs.getString("titolo");
				String ISBN = rs.getString("ISBN");
				String editore = rs.getString("editore");
				//NON mi serve una variabile "download" => i selezionati da questa query POSSONO essere scaricati.
				Pubblicazione pub = new Pubblicazione(id,titolo,ISBN,editore);
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
	// autore??
	public static List<Pubblicazione> userPub(int idUtente){//pubblicazioni di un utente
		ArrayList<Pubblicazione> lista=null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("listaautore", "listaautori.idPubblicazione = pubblicazione.id");
		map.put("autore", "listaautori.idAutore = autore.id");
		map.put("listatag", "listatag.idPubblicazione = pubblicazione.id");
		map.put("tag", "listatag.idTag = tag.id");
		int check=0;
		try {
			lista = new ArrayList<Pubblicazione>();
			Database.connect();
			ResultSet rs = Database.selectRecord("pubblicazione", "", "pubblicazione.idUtente="+idUtente, "pubblicazione.titolo");
			while(rs.next()) {
				int id = rs.getInt("id");
				if(check != id) {
					System.out.println("check Diverso da id!");
					check=id;
					
					String editore = rs.getString("editore");
					Date dataInvio = rs.getDate("dataInvio");
					String titolo = rs.getString("titolo");
					String autore_ = rs.getString("autore.nomeAutore");
					String tag_ = rs.getString("tag.nome");
					Pubblicazione pub = new Pubblicazione();
					pub.addAutore(autore_);
					pub.addTag(tag_);
					lista.add(pub);
				}else { // check è == a id, stessi dati, devo aggiornare pubblicazione precedente
					System.out.println("check uguale a id!");
					String autore_ = rs.getString("autore.nomeAutore");
					String tag_ = rs.getString("tag.nome");
					//lista.get(lista.size()-1)  --> Non è altro che l'ultima pubblicazione inserita nella lista.
					if(!lista.get(lista.size()-1).containsAutore(autore_)) lista.get(lista.size()-1).addAutore(autore_);
					if(!lista.get(lista.size()-1).containsTag(tag_)) lista.get(lista.size()-1).addTag(tag_);
				}
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
	// da completare il while per l'autore, il tag e le altre cose
	public static Pubblicazione detailPub(int idPubblicazione) {//Dettagli di una specifica pubblicazione
		
		String column="*";		
		String condition="pubblicazione.id="+ idPubblicazione;
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("listaautore", "listaautori.idPubblicazione = pubblicazione.id");
		map.put("autore", "listaautori.idAutore = autore.id");
		map.put("listatag", "listatag.idPubblicazione = pubblicazione.id");
		map.put("tag", "listatag.idTag = tag.id");
		
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
	//da completare
	public static List<Pubblicazione> showCat(){//Mostra l'intero catalogo
		ArrayList<Pubblicazione> lista=null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("listaautore", "listaautori.idPubblicazione = pubblicazione.id");
		map.put("autore", "listaautori.idAutore = autore.id");

		try {
			lista = new ArrayList<Pubblicazione>();
			int check=0;
			Database.connect();
			ResultSet rs = Database.selectRecord("*", "pubblicazione", "", "pubblicazione.titolo");
			System.out.println("dopo rs");
			while(rs.next()) {
				System.out.println("inizio rs.next showCat()");
				int id = rs.getInt("id");
				if(check != id) {
					System.out.println("check Diverso da id!");
					check=id;
					Date dataScrittura = rs.getDate("dataScrittura");
					String titolo = rs.getString("titolo");
					String editore = rs.getString("editore");
					System.out.println("vedo se le variabili sono nulle - >" + id +"|"+ editore +"|"+ titolo + "|" + dataScrittura);
					Pubblicazione pub = new Pubblicazione(id, editore, titolo, dataScrittura);
					lista.add(pub);
					String autore_ = rs.getString("autore.nomeAutore");
					pub.addAutore(autore_);
					System.out.println("Riesco a creare la lista aggiungendo pub : " + lista);
				}else {
					System.out.println("check uguale a id!");
					String autore_ = rs.getString("autore.nomeAutore");
					//lista.get(lista.size()-1)  --> Non è altro che l'ultima pubblicazione inserita nella lista.
					if(!lista.get(lista.size()-1).containsAutore(autore_)) lista.get(lista.size()-1).addAutore(autore_);
				}
				
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
	
	
	//query fatta
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
				}else { // check è == a id, stessi dati, devo aggiornare pubblicazione precedente
					System.out.println("check uguale a id!");
					String autore_ = rs.getString("autore.nomeAutore");
					String tag_ = rs.getString("tag.nome");
					//lista.get(lista.size()-1)  --> Non è altro che l'ultima pubblicazione inserita nella lista.
					if(!lista.get(lista.size()-1).containsAutore(autore_)) lista.get(lista.size()-1).addAutore(autore_);
					if(!lista.get(lista.size()-1).containsTag(tag_)) lista.get(lista.size()-1).addTag(tag_);
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
	//mancano alcune cose ristampe
	public static List<Pubblicazione> showCatRist(){ //Mostra il catalogo ma con data ultima ristampa
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ristampa", "ristampa.idPubblicazione = pubblicazione.id");
		ArrayList<Pubblicazione> lista=null;
		try {
			lista = new ArrayList<Pubblicazione>();
			Database.connect();
			ResultSet rs = Database.join("pubblicazione.id, pubblicazione.titolo,pubblicazione.ISBN, ristampa.data, ristampa.nome", "pubblicazione", map, "", "ristampa.data DESC LIMIT 1");
			while(rs.next()) {
				int id = rs.getInt("id");
				String titolo = rs.getString("titolo");
				String ISBN = rs.getString("ISBN");
				String dataRis = rs.getString("ristampa.data");
				String nomeRis = rs.getString("ristampa.nome");
				// da completare
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
	public static List<Pubblicazione> authOtherPub(Map<String, Object> map){//tramite autori pubblicazione mostra altre pubblicazioni degli autori
		String condition = "listaautori.idPubblicazione=pubblicazione.id AND (";
		Object value;
		String attr;
		for(Map.Entry<String, Object> e:map.entrySet()) {
			attr = e.getKey();
			value = e.getValue();
			condition = condition + attr + " = " + value + " AND ";
		}
		condition = condition.substring(0, condition.length()-5) + ")"; //toglie l'AND finale e chiude la parentesi tonda
		
		Map<String, Object> data = new HashMap<String,Object>();
		data.put("listaautori", "listaautori.idPubblicazione = pubblicazione.id");
		data.put("autore", "autore.id = listaautori.idAutore");
		ArrayList<Pubblicazione> lista=null;
		try {
			lista = new ArrayList<Pubblicazione>();
			Database.connect();
			ResultSet rs = Database.join("pubblicazione.id,pubblicazione.titolo, pubblicazione.ISBN ,autore.nomeAutore", "pubblicazione", map, condition, "pubblicazione.titolo");
			while(rs.next()) {
				int id = rs.getInt("id");
				String titolo = rs.getString("titolo");
				String ISBN = rs.getString("ISBN");
				//nome autore da mettere

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
		
		/**
		 	SELECT * FROM pubblicazione 
			JOIN listaautori ON listaautori.idPubblicazione = pubblicazione.id
			JOIN autore ON autore.id = listaautori.idAutore
			WHERE listaautori.idPubblicazione=pubblicazione.id AND (listaautori.idAutore = 10 OR listaautori.idAutore = 9) 
		 **/
		
		
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