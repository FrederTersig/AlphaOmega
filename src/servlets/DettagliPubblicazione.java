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

import model.Pubblicazione;
import model.Utente;
import util.FreeMarker;
import util.SecurityLayer;

/**
 * Servlet implementation class DettagliPubblicazione
 */
@WebServlet("/DettagliPubblicazione")
public class DettagliPubblicazione extends HttpServlet {
	Map<String, Object> data = new HashMap<String,Object>();
	public int id=0; //id dell'utente -> default
    public int ruolo=1; //ruolo dell'utente {1=normale,2=moderatore,3=admin} -> di default
    public Utente utente; //dati dell'utente -> servono quando è connesso
    public Pubblicazione pubblicazione;
    /**
     * 
     */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.print("process request di DettagliPubblicazione");
		HttpSession s = SecurityLayer.checkSession(request);
		if(s != null){//condizione per vedere se la sessione esiste.   		
            if(s.getAttribute("id") != null && s.getAttribute("utente") != null){// Esistono id e utente nella sessione
                id = (int) s.getAttribute("id");
                utente = (Utente) s.getAttribute("utente");
                //s.removeAttribute("ricerca");      //--> per cancellare la ricerca
            }else{ // Non esistono id e utente nella sessione
                id=0;
               
                //utente non c'è.
            }
            System.out.println("Process Request Home ->  ID =" + id );           
        }else{//Non esiste per niente la sessione, l'utente non è connesso
            id = 0;
            //utente non c'è quindi non mostri niente?
        }  	
		
		// -> Dettagli della pubblicazione visualizzata
		// -> Dettagli del tag
		// -> Dettagli dell'autore
		// -> Dettagli dell'editore
		// -> Dettagli del sorgente
		// -> Dettagli delle modifiche ---- SOLO PER UTENTI CON POTERI
		FreeMarker.process("dettagliPubblicazione.html", data, response, getServletContext()); // data ??
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
