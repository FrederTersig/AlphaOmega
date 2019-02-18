package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Utente;
import model.dao.UtenteDAO;
import util.FreeMarker;
import util.SecurityLayer;



public class DettagliProfilo extends HttpServlet {
	Map<String, Object> data = new HashMap<String,Object>(); // la tree map è da togliere
	public int id=0; //id dell'utente -> default
	public int idPubblicazione=0;
    public int ruolo=0; //ruolo dell'utente {1=normale,2=moderatore,3=admin} -> di default
    public Utente utente; //dati dell'utente -> servono quando è connesso
    public Utente utenteVisitato;
    public int idProfiloVisitato=0;
    
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("process request di DettagliProfilo");
		
		
		
		FreeMarker.process("dettagliProfilo.html", data, response, getServletContext()); // data ??
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		data.put("id", id);    
    	data.put("utente", utente);
		
		
		idProfiloVisitato = Integer.parseInt(request.getParameter("codice"));
		utenteVisitato = (Utente) UtenteDAO.showDetailUser(idProfiloVisitato);
		data.put("utenteVisitato",utenteVisitato);
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
