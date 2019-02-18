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

import model.Autore;
import model.Utente;
import model.dao.AutoreDAO;
import util.FreeMarker;
import util.SecurityLayer;

/**
 * Servlet implementation class BackendAutore
 */
@WebServlet("/BackendAutore")
public class BackendAutore extends HttpServlet {
	Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; //id dell'utente -> default
    //public int ruolo=1; //ruolo dell'utente {1=normale,2=moderatore,3=admin} -> di default
    public Utente utente;
    public int ruolo=0;
    
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		  
		
		if(ruolo == 0) { // NON HA I PERMESSI PER TROVARSI IN QUESTA PAGINA
    		FreeMarker.process("home.html", data, response, getServletContext());   
    	}else {
    		FreeMarker.process("pannelloGestione.html", data, response, getServletContext());   
    	}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(" **Get di Backend Autore **");
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
    	
    	ArrayList<Autore> listaAutori = (ArrayList<Autore>) AutoreDAO.showAllAuth();
    	data.put("listaAutori", listaAutori);
    	
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
