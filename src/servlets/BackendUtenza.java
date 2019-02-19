package servlets;

import static util.Utile.checkRuolo;

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
import model.dao.TagDAO;
import model.dao.UtenteDAO;
import util.FreeMarker;
import util.SecurityLayer;

/**
 * Servlet implementation class BackendUtenza
 */
@WebServlet("/BackendUtenza")
public class BackendUtenza extends HttpServlet {
	Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; //id dell'utente -> default
    //public int ruolo=1; //ruolo dell'utente {1=normale,2=moderatore,3=admin} -> di default
    public Utente utente;
    public int ruolo=0;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		  
    	data.put("id", id);    
    	data.put("utente", utente); // Potrebbe essere NULL se non � presente, creare oggetto vuoto?
    	//Funzione da fare in Utile -> Check RUolo
    	
    	ruolo = checkRuolo(id);
    	data.put("ruolo", ruolo);
		if(ruolo == 0) { // NON HA I PERMESSI PER TROVARSI IN QUESTA PAGINA
			response.sendRedirect("home");
    	}else {
    		FreeMarker.process("backendUtenza.html", data, response, getServletContext());   
    	}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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
                //utente non c'�.
                utente=null;
                response.sendRedirect("home");
            }      
        }else{//Non esiste per niente la sessione, l'utente non � connesso
            id = 0;
            //utente non c'� quindi non mostri niente?
            utente=null;
            response.sendRedirect("home");
        } 
    	
    	ArrayList<Utente> listaUtenti = (ArrayList<Utente>) UtenteDAO.showAllUser();
    	
    	data.put("listaUtenti", listaUtenti);
    	
    	ArrayList<Utente> listaRichieste = (ArrayList<Utente>) UtenteDAO.showRichiestaUt();
    	data.put("listaRichieste",listaRichieste);
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
		System.out.println("POST backendUtenza! Di seguito id --> ************************** " +id );
        String action = request.getParameter("value");
        System.out.println(" >> " +action+ " << ");
		if("rimuoviUt".equals(action)) {
			System.out.println("rimuovi utente ---->");
			String idUtentePreso = request.getParameter("idUtDel");
			Integer idUt = Integer.valueOf(idUtentePreso);
			UtenteDAO.deleteUser(idUt);
			System.out.println("AGITO SU UTENTE:: " + idUt);
			response.sendRedirect("backendUtenza");
		}else if("adminUt".equals(action)) {
			System.out.println("ADMIN ---->");
			String idUtentePreso = request.getParameter("idUtDel");
			Integer idUt = Integer.valueOf(idUtentePreso);
			UtenteDAO.updateRichiesta(3, 0, idUt);
			System.out.println("AGITO SU UTENTE:: " + idUt);
			response.sendRedirect("backendUtenza");	
		}else if("moderatoreUt".equals(action)) {
			System.out.println("MODERATORE ---->");
			String idUtentePreso = request.getParameter("idUtDel");
			Integer idUt = Integer.valueOf(idUtentePreso);
			UtenteDAO.updateRichiesta(2, 0, idUt);
			System.out.println("AGITO SU UTENTE:: " + idUt);
			response.sendRedirect("backendUtenza");
		}else if("attivoUt".equals(action)) {
			System.out.println("ATTIVO ---->");
			String idUtentePreso = request.getParameter("idUtDel");
			Integer idUt = Integer.valueOf(idUtentePreso);
			UtenteDAO.updateRichiesta(1, 0, idUt);
			System.out.println("AGITO SU UTENTE:: " + idUt);
			response.sendRedirect("backendUtenza");
		}else if("passivoUt".equals(action)) {
			System.out.println("PASSIVO ---->");
			String idUtentePreso = request.getParameter("idUtDel");
			Integer idUt = Integer.valueOf(idUtentePreso);
			UtenteDAO.updateRichiesta(0, 0, idUt);
			System.out.println("AGITO SU UTENTE:: " + idUt);
			response.sendRedirect("backendUtenza");
        }else if("richiestaOK".equals(action)) {
        	System.out.println("avanza utente --->>");
        	String idUtenteRichiesta = request.getParameter("idUtenteRichiesta");
        	Integer idUtRic = Integer.valueOf(idUtenteRichiesta);
        	UtenteDAO.updateRichiesta(1, 0, idUtRic);
        	response.sendRedirect("backendUtenza");
        }else if("richiestaNO".equals(action)) {
        	System.out.println("rimuovi richiesta --->>");
        	String idUtenteRichiesta = request.getParameter("idUtenteRichiesta");
        	Integer idUtRic = Integer.valueOf(idUtenteRichiesta);
        	UtenteDAO.updateRichiesta(0, 0, idUtRic);
        	response.sendRedirect("backendUtenza");
        }else if("logout".equals(action)){
            System.out.println("** CLICCATO LOGOUT POSIZIONATO in BackendTag **");
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
        	response.sendRedirect("dettagliProfilo?codice=" + id);
        }
	}

}
