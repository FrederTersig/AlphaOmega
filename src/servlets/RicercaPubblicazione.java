package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Pubblicazione;
import model.Utente;
import model.dao.PubblicazioneDAO;
import util.Database;
import util.FreeMarker;
import util.SecurityLayer;
import util.Utile;

/**
 * Servlet implementation class RicercaPubblicazione
 */
@WebServlet("/RicercaPubblicazione")
public class RicercaPubblicazione extends HttpServlet {

    Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; //id dell'utente -> default
    //public int ruolo=1; //ruolo dell'utente {1=normale,2=moderatore,3=admin} -> di default
    public Utente utente;
    
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("process request della RicercaPubblicazione");
		
	
		FreeMarker.process("ricercaPubblicazione.html", data, response, getServletContext()); // data ??
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(" **Get di Ricerca Pubblicazione! **");
		int tipoRicerca;
		
		if(request.getParameter("tipoRicerca") != null) tipoRicerca = Integer.parseInt(request.getParameter("tipoRicerca"));
		else {
			HttpSession t= request.getSession(true);
			tipoRicerca = (int) t.getAttribute("tipoRicerca");
		}
		data.put("tipoRicerca", tipoRicerca);
    	if(tipoRicerca != 0) {
    		System.out.println("tipo ricerca diverso da 0");
    		//PRIMO PASSO : controllo sessione
    		HttpSession s = SecurityLayer.checkSession(request);
    		if(s != null){//condizione per vedere se la sessione esiste.   		
	    		System.out.println("LA SESSIONE ESISTE");
	            if(s.getAttribute("id") != null && s.getAttribute("utente") != null){// Esistono id e utente nella sessione
	                id = (int) s.getAttribute("id");
	                utente = (Utente) s.getAttribute("utente");
	                //s.removeAttribute("ricerca");      //--> per cancellare la ricerca
	            }else{ // Non esistono id e utente nella sessione
	                id=0;

	                utente=null;
	            }     
	        }else{//Non esiste per niente la sessione, l'utente non � connesso
	            id = 0;
	            utente=null;
	        } 
    		data.put("id", id);
	    	data.put("utente", utente);
    		
    		if(tipoRicerca==1) {
    			// Mostra catalogo con le ultime ristampe
    			System.out.println("tipoRicerca==1 --> mostra catalogo con le ultime ristampe");
    			//ArrayList<Pubblicazione> lista = (ArrayList<Pubblicazione>) PubblicazioneDAO.showCatRist();
    			
    			ArrayList<Pubblicazione> lista = (ArrayList<Pubblicazione>) PubblicazioneDAO.showCatRist();
    			int totElementi = lista.size();
    			data.put("pubblicazioni", lista);
    			data.put("totElementi", totElementi);
    			
    		}else if(tipoRicerca==2) {
    			// Mostra ultime 10 pubblicazioni inserite
    			System.out.println("tipoRicerca==2 --> mostra ultime 10 pubblicazioni inserite");
    			ArrayList<Pubblicazione> lista = (ArrayList<Pubblicazione>) PubblicazioneDAO.lastTenPub();
    			int totElementi = lista.size();
    			
    			data.put("pubblicazioni", lista);
    			data.put("totElementi", totElementi);
    		}else if(tipoRicerca==3){
    			// Mostra ultime pubblicazioni aggiornate in 30 giorni (entry)
    			System.out.println("tipoRicerca==3 --> mostra pubblicazioni aggiornate di recente");
    			ArrayList<Pubblicazione> lista = (ArrayList<Pubblicazione>) PubblicazioneDAO.recentUpdated();
    			int totElementi = lista.size();
    			
    			data.put("pubblicazioni", lista);
    			data.put("totElementi", totElementi);
    		}else if(tipoRicerca==4) {//se tipo Ricerca==4, mi mostra il DOWNLOAD
    			System.out.println("tipoRicerca==4 --> mostra pubblicazioni con download");
    			ArrayList<Pubblicazione> lista = (ArrayList<Pubblicazione>) PubblicazioneDAO.downloadPub();
    			int totElementi = lista.size();
    			
    			data.put("pubblicazioni", lista);
    			data.put("totElementi", totElementi);
    		}else { // tutto il catalogo
    			System.out.println("tipoRicerca==5 --> tutto il catalogo");
    			
    			ArrayList<Pubblicazione> lista = (ArrayList<Pubblicazione>) PubblicazioneDAO.showCat();
    			int totElementi = lista.size();
    			data.put("pubblicazioni", lista);
    			data.put("totElementi", totElementi);
    		}
    	}else{ // tipoRicerca == 0 -> Viene dalla ricerca manuale
    		
    		System.out.println("tipo ricerca � uguale a 0");
    		HttpSession t= request.getSession(true);
			String titoloRic = (String) t.getAttribute("titolo");
			String isbnRic = (String) t.getAttribute("ISBN");
			String autoreRic = (String) t.getAttribute("autore");
			String tagRic = (String) t.getAttribute("tag");
			
			System.out.println("PASSO ATTRIBUTI CON SESSIONE");
			System.out.println(titoloRic + " " + isbnRic + " " + autoreRic + " " + tagRic + " " + tipoRicerca);
			
			HttpSession s = SecurityLayer.checkSession(request);

	    	if(s != null){//condizione per vedere se la sessione esiste.   		
	    		System.out.println("LA SESSIONE ESISTE");
	            if(s.getAttribute("id") != null && s.getAttribute("utente") != null){// Esistono id e utente nella sessione
	                id = (int) s.getAttribute("id");
	                utente = (Utente) s.getAttribute("utente");
	                //s.removeAttribute("ricerca");      //--> per cancellare la ricerca
	            }else{ // Non esistono id e utente nella sessione
	            	System.out.println("LA SESSIONE ESISTE MA NON HA ID E UTENTE!!");
	                id=0;
	                //utente non c'�.
	                utente=null;
	            }     
	            //SESSIONE ESISTE, fare qualcosa
	            
	            
	        }else{//Non esiste per niente la sessione, l'utente non � connesso
	        	System.out.println("LA SESSIONE NON ESISTE !!!!!!");
	            id = 0;
	            //utente non c'� quindi non mostri niente?
	            utente=null;
	            //SESSIONE NON ESISTE, fare qualcosa
	        } 
	    	data.put("id", id);
	    	data.put("utente", utente);
	    	
	    	/* --- TIPO DI RICERCHE
	    	 * 0- RICERCA NORMALE & MOSTRA CATALOGO --> (Stringa piena O stringa vuota);
	    	 * 1- catalogo che mostra le ultime ristampe delle pubblicazioni;
	    	 * 2- ultime 10 pubblicazioni inserite che mostra la data dell'inserimento;
	    	 * 3- ultime pubblicazioni aggiornate in 30 giorni che mostra la data dell'ultimo aggiornamento;
	    	 * 
	    	 * 
	    	 * ---IMPORTANTE--- 
	    	 * SE il tipo � uno tra questi, non serve la stringa.
	    	 * SE il tipo non � tra questi, SERVE la stringa (ricerca normale) --> in questo caso il tipo � 0
	    	 */
    		
    		//Condizionale per vedere se almeno una delle stringhe � stata popolata
    		if(titoloRic != null && !titoloRic.trim().isEmpty() || isbnRic != null && !isbnRic.trim().isEmpty()
    		|| autoreRic != null && !autoreRic.trim().isEmpty() || tagRic != null && !tagRic.trim().isEmpty()) { 
    			System.out.println("c'� almeno UNA cosa scritta nel form di ricerca");
    			//Mostrer� le cose scritte nell'html
    			data.put("titoloR", titoloRic);
    			data.put("isbnR", isbnRic);
    			data.put("autoreR", autoreRic);
    			data.put("tagR", tagRic);

    			ArrayList<Pubblicazione> lista = (ArrayList<Pubblicazione>) PubblicazioneDAO.researchPub(titoloRic,isbnRic,autoreRic,tagRic);
    			int totElementi = lista.size();
    			
    			data.put("pubblicazioni", lista);
    			data.put("totElementi", totElementi);
    			
    			System.out.println("mostro la lista");
    			System.out.println(lista);
    			
    		}else{ // SE tutte le form sono vuote, allora mi mostri il catalogo completo
    			System.out.println("NESSUNA cosa scritta nel form : mostro tutto");
    			
    			ArrayList<Pubblicazione> lista = (ArrayList<Pubblicazione>) PubblicazioneDAO.showCat();
    			int totElementi = lista.size();
    			data.put("pubblicazioni", lista);
    			data.put("totElementi", totElementi);
    			
    		}
    	}
    	
    	
		
		
        try {        	
            processRequest(request, response);
        } catch (Exception e) {
           e.printStackTrace();
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// logout, login, dettagliPubblicazione, dettagliProfilo(proprio)
	}

}
