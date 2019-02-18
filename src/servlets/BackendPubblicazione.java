package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Utente;
import util.FreeMarker;

/**
 * Servlet implementation class BackendPubblicazione
 */
@WebServlet("/BackendPubblicazione")
public class BackendPubblicazione extends HttpServlet {
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
