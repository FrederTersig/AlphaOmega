package model.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import model.Capitolo;
import model.Pubblicazione;
import model.Ristampa;
import model.Sorgente;
import model.Utente;
import util.Database;

public class PubblicazioneDAO implements PubblicazioneDAO_interface{
	// QUERY FATTA
	public static List<Pubblicazione> lastTenPub(){ //Ultimi 10 giorni
		ArrayList<Pubblicazione> lista=null;
		String column = "pubblicazione.id, pubblicazione.dataInvio, pubblicazione.titolo, pubblicazione.ISBN, autore.nomeAutore";
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("listaautori", "listaautori.idPubblicazione = Pubblicazione.id");
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
					Pubblicazione pub = new Pubblicazione(id,titolo,dataInvio,ISBN);
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
	// finita
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
				String modificataDa = rs.getString("utente.email");
				int utenteId = rs.getInt("utente.id");
				Pubblicazione pub = new Pubblicazione(id, utenteId, titolo, ISBN, modificataDa, ultimaModifica);
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
	// fatta
	public static List<Pubblicazione> userPub(int idUtente){//pubblicazioni di un utente
		ArrayList<Pubblicazione> lista=null;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("listaautori", "listaautori.idPubblicazione = pubblicazione.id");
		map.put("autore", "listaautori.idAutore = autore.id");
		String column="pubblicazione.id, pubblicazione.editore, pubblicazione.dataInvio, pubblicazione.titolo" + 
		", autore.nomeAutore";
		int check=0;
		try {
			lista = new ArrayList<Pubblicazione>();
			Database.connect();
			ResultSet rs = Database.join(column, "pubblicazione", map, "pubblicazione.idUtente="+idUtente, "pubblicazione.titolo");
			while(rs.next()) {
				int id = rs.getInt("id");
				if(check != id) {
					System.out.println("check Diverso da id!");
					check=id;
					String editore = rs.getString("editore");
					Date dataInvio = rs.getDate("dataInvio");
					String titolo = rs.getString("titolo");
					String ISBN = rs.getString("ISBN");
					String autore_ = rs.getString("autore.nomeAutore");
					Pubblicazione pub = new Pubblicazione(id,titolo,editore,ISBN,dataInvio);
					pub.addAutore(autore_);
					lista.add(pub);
				}else { // check è == a id, stessi dati, devo aggiornare pubblicazione precedente
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
	    }
		return lista;		
	}
	// fatta
	public static Pubblicazione detailPub(int idPubblicazione) {//Dettagli di una specifica pubblicazione
		
		Pubblicazione pub = null;
		
		Boolean check=false; // deve essere eseguito solo una volta
		Sorgente sor = null;
		Capitolo cap = null;
		Ristampa ris = null;

		try {
			Database.connect();
			//ResultSet rs = Database.join(column, "pubblicazione", map, condition, "");
			ResultSet rs = Database.callProcedure("dettagliPubblicazione", null, idPubblicazione);
			//viene fatta la query
			while(rs.next()) { // ciclo pesante -> da testare
				//succede qualcosa di strano qui dentro
				
				System.out.println("DENTRO RS.NEXT - INIZIO ITERAZIONE");
				if(!check) {
					check=true;
					System.out.println("DENTRO IL PRIMO CHECK!");
					int idInseritore = rs.getInt("pubblicazione.idUtente");
					Date dataInvio = rs.getDate("pubblicazione.dataInvio");
					String titolo = rs.getString("pubblicazione.titolo");
					String descrizione = rs.getString("pubblicazione.descrizione");			
					String ISBN = rs.getString("pubblicazione.ISBN");
					int numPagine = rs.getInt("pubblicazione.numPagine");
					String lingua = rs.getString("pubblicazione.lingua");
					String editore = rs.getString("pubblicazione.editore");
					Date dataScrittura = rs.getDate("pubblicazione.dataScrittura");
					pub = new Pubblicazione(idPubblicazione, idInseritore, editore, titolo, descrizione, dataInvio, ISBN, 
							numPagine, lingua, dataScrittura);
				}
				System.out.println("PRIMA DI AUTORE");
				if(rs.getObject(1)!= null) {
					String autore_ = rs.getString("autore.nomeAutore");
					if(!pub.containsAutore(autore_))  pub.addAutore(autore_);
					System.out.println(autore_ + " <--- AUTORE");
					
				}
				System.out.println("PRIMA DI TAG");
				if(rs.getObject(2)!= null) {
					String tag_ = rs.getString("tag.nome");
					if(!pub.containsTag(tag_)) pub.addTag(tag_);
					System.out.println(tag_ + " <--- TAG");
				}
				// 1a= Autore, 2b= tag, 3c = sorgente, 4d= ristampa, 5e=capitolo
				System.out.println("PRIMA DI SORGENTE");

				if(rs.getObject(3) != null) {					
					int idSorgente = rs.getInt("sorgente.id");
					String tipo = rs.getString("sorgente.tipo");
					String URI = rs.getString("sorgente.URI");
					String formato = rs.getString("sorgente.formato");
					String descrizione = rs.getString("sorgente.descrizione");
					sor = new Sorgente(idSorgente, tipo, URI, formato, descrizione);
					if(!pub.equalSor(idSorgente)) pub.addSorgente(sor);
				}
				
				System.out.println("PRIMA DI ristampa");
				if(rs.getObject(4) != null) {
					int idRistampa = rs.getInt("ristampa.id");
					String nome = rs.getString("ristampa.nome");
					Date data = rs.getDate("ristampa.data");
					ris = new Ristampa(idRistampa, nome, data);
					if(!pub.equalRis(idRistampa)) pub.addRistampa(ris);
				
				}
				
				System.out.println("PRIMA DI CAPITOLO");
				if(rs.getObject(5) != null) {
					int idCapitolo = rs.getInt("capitolo.id");
					String titolo = rs.getString("capitolo.titolo");
					int numero = rs.getInt("capitolo.numero");
					int pagInizio = rs.getInt("capitolo.pagInizio");
					cap = new Capitolo(idCapitolo, titolo, numero, pagInizio);
					if(!pub.equalCap(idCapitolo)) pub.addCapitolo(cap);

				}
				
			}
			
			Database.close();
		}catch(NamingException e) {
			System.out.println("NamingException"+e);
	    }catch (SQLException e) {
	    	System.out.println("SQLException");
	    	e.printStackTrace(System.out);
	    }catch (Exception e) {
	    	System.out.println("Exception"+e);    
	    }
		System.out.println("PUBBLICAZIONE POPOLATA?" + pub + " ||||| " + pub.showAutori());
		return pub;
	}
	//fatta
	public static List<Pubblicazione> showCat(){//Mostra l'intero catalogo
		ArrayList<Pubblicazione> lista=null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("listaautori", "listaautori.idPubblicazione = pubblicazione.id");
		map.put("autore", "listaautori.idAutore = autore.id");

		try {
			lista = new ArrayList<Pubblicazione>();
			int check=0;
			Database.connect();
			ResultSet rs = Database.join("*", "pubblicazione", map, "", "pubblicazione.titolo");

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
					Pubblicazione pub = new Pubblicazione(id, editore, titolo, dataScrittura);
					lista.add(pub);
					String autore_ = rs.getString("autore.nomeAutore");
					pub.addAutore(autore_);
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
			ResultSet rs = Database.callProcedure("ricerca", argum, 0);
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
	//fatta ma da controllare -> forse SOLO un risultato
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
				Date dataRis = rs.getDate("ristampa.data");
				String nomeRis = rs.getString("ristampa.nome");
				// da completare
				Pubblicazione pub = new Pubblicazione(id,titolo,ISBN,dataRis,nomeRis);
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
		int check=0;
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
				if(check != id) {
					System.out.println("check Diverso da id!");
					check=id;
					String titolo = rs.getString("titolo");
					String ISBN = rs.getString("ISBN");
					//nome autore da mettere
	
					Pubblicazione pub = new Pubblicazione(id,titolo,ISBN);
					lista.add(pub);
					String autore_ = rs.getString("autore.nomeAutore");
					pub.addAutore(autore_);
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
	    }
		return lista;
		
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