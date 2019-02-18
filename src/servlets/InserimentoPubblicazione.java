package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Utente;
import model.Tag;
import model.Autore;
import model.dao.PubblicazioneDAO;
import model.dao.TagDAO;
import model.dao.AutoreDAO;
import model.dao.EntryDAO;
import model.dao.UtenteDAO;
import util.FreeMarker;
import util.SecurityLayer;
import util.Utile;

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
		data.put("id", id);    
    	data.put("utente", utente);
		
		
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
        
    	// Prendo la lista dei tag presente nel sistema.
    	ArrayList<Tag> listaTags = (ArrayList<Tag>) TagDAO.showAllTags();
    	data.put("listaTag", listaTags);
		// Prendo la lista degli autori presente nel sistema.
    	
    	ArrayList<Autore> listaAutore = (ArrayList<Autore>) AutoreDAO.showAllAuth();
    	data.put("listaAutore",listaAutore);
    	 
		
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
			int idUtente = id; //L'id dell'utente che sta creando la pubblicazione
			String titolo = request.getParameter("titolo");
			String editore = request.getParameter("editore");
			String ISBN = request.getParameter("ISBN");
			String descrizione = request.getParameter("descrizione");
			String dataScrittura = request.getParameter("dataScrittura");
			int numPagine = Integer.parseInt(request.getParameter("numPagine"));
			String lingua = request.getParameter("lingua");
			try {
				int idNuovaPub = Utile.existPub(ISBN, idUtente);
				String[] tagIdSelezionati = request.getParameterValues("tagSpunta");
				String[] autIdSelezionati = request.getParameterValues("autoreSpunta");
				
            	if(idNuovaPub == 0) { // non esiste la pubblicazione : posso crearla!
            		//conversione DATACREAZIONE
            		SimpleDateFormat data_b = new SimpleDateFormat("yyyy-MM-dd");
            		java.util.Date dateb = data_b.parse(dataScrittura);
            		java.sql.Date d_creazione = new java.sql.Date(dateb.getTime());
            		//Insert della pubblicazione
            		PubblicazioneDAO.insertPub(descrizione, titolo, editore, idUtente, ISBN, numPagine, lingua, d_creazione);
            		
            		int idPubblicazione = Utile.existPub(ISBN, idUtente);
            		EntryDAO.insertEntry("ha inserito la pubblicazione", idPubblicazione, idUtente);
            	}else{ // La pubblicazione esiste!!
            		System.out.println("Esiste già questa pubblicazione nel DB");
            		//Resetta e ritorna in inserimento Pubblicazione
            		response.sendRedirect("inserimentoPubblicazione");
            	}
            	//Ricavo l'id della pubblicazione
            	idNuovaPub = Utile.existPub(ISBN, idUtente);
            	//Inserisco i tag & autori selezionati per la pubblicazione
            	if(tagIdSelezionati.length != 0) TagDAO.insertPubTag(idNuovaPub, tagIdSelezionati);
            	if(autIdSelezionati.length != 0) AutoreDAO.insertPubAuth(idNuovaPub, autIdSelezionati);
            	//finisco
            	
            	
            	
            	response.sendRedirect("pannelloGestione");
            } catch (Exception e) {
            	System.out.println("Eccezione invio pubblicazione nuova" + e);
            }
		}else if("logout".equals(action)){
            System.out.println("** CLICCATO LOGOUT POSIZIONATO IN HOME **");
            try{
                SecurityLayer.disposeSession(request); 
                id=0; 
                data.put("id",id);
                utente = null;
                data.put("utente", utente);
                response.sendRedirect("home");
            }catch(Exception e3){
                e3.printStackTrace();
            }
        }else if("profilo".equals(action)) {
        	System.out.println("Sto cercando di entrare nel mio profilo");
        	//Mi devo ricavare il mio ID
        	//Devo andare nella pagina "dettagliProfilo" utilizzando il mio ID come "destinazione"
        }
		
	}

}
