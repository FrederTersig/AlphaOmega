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

import model.Capitolo;
import model.Entry;
import model.Lode;
import model.Pubblicazione;
import model.Recensione;
import model.Ristampa;
import model.Sorgente;
import model.Utente;
import model.dao.CapitoloDAO;
import model.dao.EntryDAO;
import model.dao.LodeDAO;
import model.dao.PubblicazioneDAO;
import model.dao.RecensioneDAO;
import model.dao.RistampaDAO;
import model.dao.SorgenteDAO;
import util.FreeMarker;
import util.SecurityLayer;

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
		  
    	data.put("id", id);    
    	data.put("utente", utente); // Potrebbe essere NULL se non è presente, creare oggetto vuoto?
    	//Funzione da fare in Utile -> Check RUolo
    	
    	ruolo = checkRuolo(id);
    	data.put("ruolo", ruolo);
		if(ruolo == 0) { // NON HA I PERMESSI PER TROVARSI IN QUESTA PAGINA
			response.sendRedirect("home");   
    	}else {
    		FreeMarker.process("backendPubblicazione.html", data, response, getServletContext());   
    	}
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(" **Get di Backend Pubblicazione**");
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
    	
    	ArrayList<Pubblicazione> listaPubblicazioni = (ArrayList<Pubblicazione>) PubblicazioneDAO.showCat();
    	data.put("listaPubblicazioni", listaPubblicazioni);
    	
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
		System.out.println("doPost del backend Pubblicazione");
		String action = request.getParameter("value");
		if("rimuoviPub".equals(action)) {
			System.out.println("Cancelliamo la pubblicazione selezionata");
			String idDelete = request.getParameter("idDelete");
			Integer idPubCanc = Integer.valueOf(idDelete);
			//Rimuovo ogni cosa che dipende dalla pubblicazione
			ArrayList<Recensione> lr = (ArrayList<Recensione>) RecensioneDAO.revList(idPubCanc);
			for(int i=0;i<lr.size();i++) RecensioneDAO.deleteRev(lr.get(i).getId()); 
			
			ArrayList<Integer> ld = (ArrayList<Integer>) LodeDAO.listaIdLike(idPubCanc);
			for(int i=0;i<ld.size();i++) LodeDAO.deleteLike(ld.get(i));
			
			ArrayList<Sorgente> ls = (ArrayList<Sorgente>) SorgenteDAO.listSorPub(idPubCanc);
			for(int i=0;i<ls.size();i++) SorgenteDAO.deleteSorgente(ls.get(i).getId());
			
			ArrayList<Capitolo> lc = (ArrayList<Capitolo>) CapitoloDAO.listCapPub(idPubCanc);
			for(int i=0;i<lc.size();i++) CapitoloDAO.deleteCapitolo(lc.get(i).getId());
			
			ArrayList<Entry> le = (ArrayList<Entry>) EntryDAO.showPubEntries(idPubCanc);
			for(int i=0;i<le.size();i++) EntryDAO.deleteEntry(le.get(i).getId());
			
			ArrayList<Ristampa> lris = (ArrayList<Ristampa>) RistampaDAO.listRisPub(idPubCanc);
			for(int i=0;i<lris.size();i++) RistampaDAO.deleteRistampa(lris.get(i).getId());
			
			System.out.println("cancello PER ULTIMA la pubblicazione (che cancellerà le liste)");	
			PubblicazioneDAO.deletePub(idPubCanc);
			response.sendRedirect("backendPubblicazione");
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
        	response.sendRedirect("dettagliProfilo?codice=" + id);
        }
	}

}
