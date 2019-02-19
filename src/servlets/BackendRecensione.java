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
import model.Recensione;
import model.Utente;
import model.dao.AutoreDAO;
import model.dao.EntryDAO;
import model.dao.RecensioneDAO;
import util.FreeMarker;
import util.SecurityLayer;

/**
 * Servlet implementation class BackendRecensione
 */
@WebServlet("/BackendRecensione")
public class BackendRecensione extends HttpServlet {
	Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; //id dell'utente -> default
    //public int ruolo=1; //ruolo dell'utente {1=normale,2=moderatore,3=admin} -> di default
    public Utente utente;
    public ArrayList<Recensione> listaRecensioni=null;
    public int ruolo=0;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		  
    	data.put("id", id);    
    	data.put("utente", utente); // Potrebbe essere NULL se non è presente, creare oggetto vuoto?
    	//Funzione da fare in Utile -> Check RUolo
    	
    	ruolo = checkRuolo(id);
    	data.put("ruolo", ruolo);
		if(ruolo == 0) { // NON HA I PERMESSI PER TROVARSI IN QUESTA PAGINA
			response.sendRedirect("home");
    	}else {
    		FreeMarker.process("backendRecensione.html", data, response, getServletContext());   
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
                response.sendRedirect("home");
            }      
        }else{//Non esiste per niente la sessione, l'utente non è connesso
            id = 0;
            //utente non c'è quindi non mostri niente?
            utente=null;
            response.sendRedirect("home");
        } 
    	
    	listaRecensioni = (ArrayList<Recensione>) RecensioneDAO.waitingRev();
    	data.put("listaRecensioni", listaRecensioni);
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
		System.out.println("doPost del backendRecensione");
		String action = request.getParameter("value");
		if("accettaRev".equals(action)) {
			System.out.println("Recensione Accettata");
			int idPubblicazione=0;
			String idRec = request.getParameter("idRec");
			Integer idRev = Integer.valueOf(idRec);
			RecensioneDAO.judgeRev(idRev, 1);
			for(int i=0;i<listaRecensioni.size();i++) {
				if(listaRecensioni.get(i).getId()==idRev) {
					idPubblicazione=listaRecensioni.get(i).getIdPubblicazione();
				}
			}
			
			
			EntryDAO.insertEntry("ha accettato una recensione nella pubblicazione", idPubblicazione, id);
			response.sendRedirect("backendRecensione");
		}else if("rifiutaRev".equals(action)) {
			System.out.println("Recensione Rifiutata");
			String idRec = request.getParameter("idRec");
			Integer idRev = Integer.valueOf(idRec);
			RecensioneDAO.deleteRev(idRev);
			response.sendRedirect("backendRecensione");
		}else if("logout".equals(action)){
            System.out.println("** CLICCATO LOGOUT POSIZIONATO IN backendRecensione **");
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
