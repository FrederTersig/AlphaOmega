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

import model.Tag;
import model.Utente;
import model.dao.TagDAO;
import util.FreeMarker;
import util.SecurityLayer;

/**
 * Servlet implementation class BackendTag
 */
@WebServlet("/BackendTag")
public class BackendTag extends HttpServlet {
	Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; //id dell'utente -> default
    //public int ruolo=1; //ruolo dell'utente {1=normale,2=moderatore,3=admin} -> di default
    public Utente utente;
    public int ruolo=0;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	System.out.println("process Request del Pannello Gestione");
    	 
    	
    	data.put("id", id);    
    	data.put("utente", utente); // Potrebbe essere NULL se non è presente, creare oggetto vuoto?
    	//Funzione da fare in Utile -> Check RUolo
    	
    	ruolo = checkRuolo(id);
    	data.put("ruolo", ruolo);
    	if(ruolo == 0) { // NON HA I PERMESSI PER TROVARSI IN QUESTA PAGINA
    		response.sendRedirect("home");
    	}else {
    		FreeMarker.process("backendTag.html", data, response, getServletContext());   
    	}
           
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(" **Get di Pannello Gestione! **");
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
            System.out.println("Process Request Home ->  ID =" + id );           
        }else{//Non esiste per niente la sessione, l'utente non è connesso
            id = 0;
            //utente non c'è quindi non mostri niente?
            utente=null;
            response.sendRedirect("home");
        } 
        
    	ArrayList<Tag> listaTag = (ArrayList<Tag>) TagDAO.showAllTags();
    	data.put("listaTag", listaTag);		
		
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
		
		System.out.println("POST backendTag! Di seguito id --> " +id );
        String action = request.getParameter("value");
        System.out.println(" >> " +action+ " << ");
		if("invioTag".equals(action)) {
			System.out.println("invioTag ---->");
			String inputTag = request.getParameter("inputTag");
			TagDAO.insertTag(inputTag);
			System.out.println("Inviato nuovo tag!!");
			response.sendRedirect("backendTag");
        }else if("eliminaTag".equals(action)) {
        	System.out.println("eliminaTag --->>");
        	String idTagSel = request.getParameter("idTagSel");
        	Integer idTagSele = Integer.valueOf(idTagSel);
        	TagDAO.deleteTag(idTagSele);
        	response.sendRedirect("backendTag");
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
