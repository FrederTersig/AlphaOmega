package servlets;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Entry;
import model.Pubblicazione;
import model.Utente;
import model.dao.EntryDAO;
import model.dao.PubblicazioneDAO;
import model.dao.UtenteDAO;
import util.Database;
import util.FreeMarker;
import util.SecurityLayer;
import util.Utile;



public class DettagliProfilo extends HttpServlet {
	Map<String, Object> data = new HashMap<String,Object>(); // la tree map � da togliere
	public int id=0; //id dell'utente -> default
	public int idPubblicazione=0;
    public int ruolo=0; //ruolo dell'utente {1=normale,2=moderatore,3=admin} -> di default
    public Utente utente; //dati dell'utente -> servono quando � connesso
    public Utente utenteVisitato;
    public int idProfiloVisitato=0;
    
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("process request di DettagliProfilo");
		
		
		System.out.println("a");
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
                //utente non c'�.
                
            }
            System.out.println("Process Request DettagliPubblicazione ->  ID =" + id );           
        }else{//Non esiste per niente la sessione, l'utente non � connesso
            id = 0;
            utente = null;
            
        }  	
		data.put("id", id);    
    	data.put("utente", utente);
		
		
		idProfiloVisitato = Integer.parseInt(request.getParameter("codice"));
		
		System.out.println("a");
		utenteVisitato = (Utente) UtenteDAO.showDetailUser(idProfiloVisitato);
		data.put("utenteVisitato",utenteVisitato);
		if(id == idProfiloVisitato) {
			if(utenteVisitato.getRichiesta()==0 && utenteVisitato.getRuolo()==0) {
				data.put("canReq", 1);
			}
			
		}
		System.out.println("a");
		ArrayList<Pubblicazione> listaPubblicazioni = (ArrayList<Pubblicazione>) PubblicazioneDAO.userPub(idProfiloVisitato);
		data.put("listaPubblicazioni", listaPubblicazioni);
		System.out.println("a");
		ArrayList<Entry> listaEntry = (ArrayList<Entry>) EntryDAO.showUtEntries(idProfiloVisitato);
		data.put("listaEntry", listaEntry);
		
		System.out.println("a");
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

		System.out.println("POST di Home! Di seguito id --> " +id );
        String action = request.getParameter("value");
        System.out.println(" >> " +action+ " << ");
        /*AZIONI:
         * LOGIN, LOGOUT // basta
         * */
        if(id==0) {//Se non si ha un id, quindi se non si � connessi
        	//System.out.println("id=0 -> provo doPost");
        	//System.out.println("Valore di action -->" + action);
	        
	        if("login".equals(action)){ // SE il metodo post � il login....
	            System.out.println("Verso il login ");
	            String email = request.getParameter("email");
	            String password = request.getParameter("password");    
	            //Check su email && password per vedere se l'utente esiste nel DB (1 s�,0 no)
	            System.out.println("email :: " + email + " || "+ "password " + password); 
	            if(!(null == email) && !(null == password)) {
	            	try {
	            		id=Utile.checkUser(email, password); // prendo l'id dell'utente
	            	}catch(Exception e) {
	            		e.printStackTrace();
	            	}
	            }
	            System.out.println("Ecco l'id!" + id);
	            if(id==0) { //Non esiste l'utente nel db
	            	System.out.println("Utente non presente nel DB!");
	            	data.put("id", 0);
	            	utente=null;
	            	data.put("utente", utente);
	            	response.sendRedirect("home");
	            }else { //Esiste l'utente nel db
	            	System.out.println("Utente presente nel DB!  > procedo!!");
	            	try{ 
	                    HttpSession s = SecurityLayer.createSession(request, email, id);
	                    System.out.println("Sessione Creata, Connesso!");
	                    data.put("id",id);
	                    s.setAttribute("id", id);
	                    Utente profilo=null;
	                    try {
	                    	Database.connect();
	                        id = (int) s.getAttribute("id");
	                        ResultSet pr=Database.selectRecord("*", "utente", "utente.id="+id, "");
	                    	while(pr.next()) {
	                    		int ruolo = pr.getInt("ruolo");
	                			Date dataNascita = pr.getDate("dataNascita");
	                			Date dataIscr = pr.getDate("dataIscr");
	                			String nome = pr.getString("nome");
	                			String cognome = pr.getString("cognome");
	                			String emailX = pr.getString("email");
	                			String citta = pr.getString("citta");
	                    		profilo = new Utente(id,ruolo,dataIscr,nome,cognome,emailX,citta,dataNascita);
	                    	}
	                    	Database.close();
	                    }catch (SQLException e) {
	                    	System.out.println(e);
	                    }catch (Exception e) {
	                    	System.out.println(e);
	                    }
	                    
	                    data.put("utente", profilo);
	                    s.setAttribute("utente", profilo);
	                    response.sendRedirect("home");
	                }catch(Exception e){
	                    System.out.println("Errore creazione sessione HOME " + e);
	                }
	            }
	        }
        }else {//Se si � connessi
	        if("logout".equals(action)){
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
        if("reqAtt".equals(action)){
        	System.out.println("Richiedo Attivit�!!!");
        	if(id == idProfiloVisitato) {
    			if(utenteVisitato.getRichiesta()==0) {
    				System.out.println("Richiesta Avvenuta!!!");
    				UtenteDAO.updateRichiesta(0, 1, id);
    				
    			}
        	}
        	response.sendRedirect("dettagliProfilo?codice=" + idProfiloVisitato);
        }
	}

}
