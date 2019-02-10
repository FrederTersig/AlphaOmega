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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(" **Get di Ricerca Pubblicazione! **");
		HttpSession s = SecurityLayer.checkSession(request);
    	
    	if(s != null){//condizione per vedere se la sessione esiste.   		
            if(s.getAttribute("id") != null && s.getAttribute("utente") != null){// Esistono id e utente nella sessione
                id = (int) s.getAttribute("id");
                utente = (Utente) s.getAttribute("utente");
                //s.removeAttribute("ricerca");      //--> per cancellare la ricerca
            }else{ // Non esistono id e utente nella sessione
                id=0;
                //utente non c'è.
                utente=null;
            }     
        }else{//Non esiste per niente la sessione, l'utente non è connesso
            id = 0;
            //utente non c'è quindi non mostri niente?
            utente=null;
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
    	 * SE il tipo è uno tra questi, non serve la stringa.
    	 * SE il tipo non è tra questi, SERVE la stringa (ricerca normale) --> in questo caso il tipo è 0
    	 */
    	
    	int tipoRicerca = (int) request.getSession(true).getAttribute("tipoRicerca");
    	if(tipoRicerca != 0) {
    		if(tipoRicerca==1) {
    			// Mostra catalogo con le ultime ristampe
    		}else if(tipoRicerca==2) {
    			// Mostra ultime 10 pubblicazioni inserite
    		}else {
    			// Mostra ultime pubblicazioni aggiornate in 30 giorni (entry)
    		}
    	}else{ // ha una stringa per ricercare
    		String ricercaStr = (String) request.getSession(true).getAttribute("ricerca");
    		if(ricercaStr != null && ricercaStr.trim().isEmpty()) { // stringa piena
    			data.put("ricerca", ricercaStr);
    			ArrayList<Pubblicazione> lista = (ArrayList<Pubblicazione>) PubblicazioneDAO.researchPub();
    			int totElementi = lista.size();
    			for(int i=0; i<totElementi; i++) {
    				
    			}
    			data.put("pubblicazioni", lista);
    			data.put("totElementi", totElementi);
    			
    			
    		}else{ // Se la stringa è vuota, mi mostri il catalogo completo (assieme al download della source)
    			//viene mostrato TUTTO il catalogo ordinato per titolo
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
		
	}

}
