package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Pubblicazione;
import model.Capitolo;
import model.Utente;
import model.dao.CapitoloDAO;
import util.FreeMarker;
import util.SecurityLayer;

/**
 * Servlet implementation class DettagliPubblicazione
 */
@WebServlet("/DettagliPubblicazione")
public class DettagliPubblicazione extends HttpServlet {
	Map<String, Object> data = new HashMap<String,Object>();
	public int id=0; //id dell'utente -> default
    public int ruolo=0; //ruolo dell'utente {1=normale,2=moderatore,3=admin} -> di default
    public Utente utente; //dati dell'utente -> servono quando è connesso
    public Pubblicazione pubblicazione;
    /**
     * 
     */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.print("process request di DettagliPubblicazione");
		
		data.put("id", id);    
    	data.put("utente", utente);
		// -> Dettagli della pubblicazione visualizzata
		// -> Dettagli del tag
		// -> Dettagli dell'autore
		// -> Dettagli dell'editore
		// -> Dettagli del sorgente
		// -> Dettagli delle modifiche ---- SOLO PER UTENTI CON POTERI
    	ArrayList<Capitolo> mostraCapitoli = (ArrayList<Capitolo>) CapitoloDAO.listCapPub(1);
    	System.out.println(mostraCapitoli.size() + " SIZE DI MOSTRACAPITOLI");
    	data.put("listaCapitoli", mostraCapitoli);
    	
		FreeMarker.process("dettagliPubblicazione.html", data, response, getServletContext()); // data ??
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("DettagliPubblicazione DOGET");
		HttpSession s = SecurityLayer.checkSession(request);
		if(s != null){//condizione per vedere se la sessione esiste.   		
            if(s.getAttribute("id") != null && s.getAttribute("utente") != null){// Esistono id e utente nella sessione
                id = (int) s.getAttribute("id");
                utente = (Utente) s.getAttribute("utente");
                //s.removeAttribute("ricerca");      //--> per cancellare la ricerca
            }else{ // Non esistono id e utente nella sessione
                id=0;
                utente = null;
                //utente non c'è.
            }
            System.out.println("Process Request DettagliPubblicazione ->  ID =" + id );           
        }else{//Non esiste per niente la sessione, l'utente non è connesso
            id = 0;
            utente = null;
            //utente non c'è quindi non mostri niente?
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
		System.out.println("DettagliPubblicazione DOPOST");
        String action = request.getParameter("value");
        System.out.println(" >> " +action+ " << ");
        if("invioCapitolo".equals(action)) {
        	String titolo = request.getParameter("titolo");
        	int numero = Integer.parseInt(request.getParameter("numero"));
        	int pagInizio = Integer.parseInt(request.getParameter("pagInizio"));
        	
        	System.out.println(titolo + " " + numero + " " + pagInizio);
        	CapitoloDAO.insertCapitolo(1, numero, pagInizio, titolo);
        	response.sendRedirect("dettagliPubblicazione");
        }
	}

}
