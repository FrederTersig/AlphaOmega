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
import util.FreeMarker;
import util.SecurityLayer;

/**
 * Servlet implementation class InserimentoPubblicazione
 */
@WebServlet("/InserimentoPubblicazione")
public class InserimentoPubblicazione extends HttpServlet {
	Map<String, Object> data = new HashMap<String,Object>(); // la tree map è da togliere
    int id=0;
    Utente utente;
    
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("process request di InserimentoPubblicazione");
		
		
		
		FreeMarker.process("inserimentoPubblicazione.html", data, response, getServletContext()); // data ??
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Controllo ID, controllo permessi, poi inserimento campi e POST
		System.out.println(" **Get di InserimentoPubblicazione **");
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
            System.out.println("Process Request Home ->  ID =" + id );           
        }else{//Non esiste per niente la sessione, l'utente non è connesso
            id = 0;
            //utente non c'è quindi non mostri niente?
            utente=null;
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
		System.out.println("doPost dell'inserimentoPubblicazione");
		String action = request.getParameter("value");
		if("invia".equals(action)) {
			//Crei una nuova pubblicazione
			
			String titolo = request.getParameter("titolo");
			String Editore = request.getParameter("editore");
			String dataRilascio = request.getParameter("dataRilascio");
			String codiceISBN = request.getParameter("ISBN");
			String descrizione = request.getParameter("descrizione");
			// Prendi il tag tramite una finestra con le varie "opzioni"
			// Prendi l'autore tramite una finestra con le varie "opzioni" (Oppure piccolissima barra di ricerca?)
			String numPagine = request.getParameter("numPagine");
			String lingua = request.getParameter("lingua");
			String data = request.getParameter("data"); //-> Data di creazione della pubblicazione!!!! (presente metadati)
			//I capitoli, Le ristampe e i Sorgenti potranno essere inseriti dopo nel dettaglio Pubblicazione
			 
			
		}
		
	}

}
