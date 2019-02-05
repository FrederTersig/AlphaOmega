package servlets;



import util.Database;
import util.FreeMarker;
import util.SecurityLayer;
import util.Utile;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Utente;

/**
 * @author Federico Tersigni
 */

public class Home extends HttpServlet {
	
       
	/**
     * Processes requests di Home
     *
     * @param request servlet request
     * @param response servlet response
     * @throws Exception 
     */
	Map<String, Object> data = new HashMap<String,Object>();
    public int id=0; //id dell'utente -> default
    public int ruolo=1; //ruolo dell'utente {1=normale,2=moderatore,3=admin} -> di default
    public Utente utente;
    
    //Gli elementi della  home:
    /* Poche informazioni dell'utente sulla barra 
     * Lista pubblicazioni inserite
     * FORSE lista utenti più collaborativi?
     * 
     * 
     * */
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	System.out.println("process Request della Home");
    	 
    	
    	data.put("id", id);    
    	data.put("utente", utente); // Potrebbe essere NULL se non è presente, creare oggetto vuoto?
    	//System.out.println(response);
    	//System.out.println(data); 
    	//System.out.println(getServletContext());
    	System.out.println("-----------Prima del FreeMarker HOME------------");
    	//response.sendRedirect("home");
        FreeMarker.process("home.html", data, response, getServletContext());      
    }
    
    /**
     * doGet di Home
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(" **Get di Home! **");
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
     * doPost di Home
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs 
     */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("POST di Home! Di seguito id --> " +id );
        String action = request.getParameter("value");
        System.out.println(" >> " +action+ " << ");
        /*AZIONI:
         * LOGIN, LOGOUT // basta
         * */
        if(id==0) {//Se non si ha un id, quindi se non si è connessi
        	//System.out.println("id=0 -> provo doPost");
        	//System.out.println("Valore di action -->" + action);
	        
	        if("login".equals(action)){ // SE il metodo post è il login....
	            System.out.println("Verso il login ");
	            String email = request.getParameter("email");
	            String password = request.getParameter("password");    
	            //Check su email && password per vedere se l'utente esiste nel DB (1 sì,0 no)
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
	            	FreeMarker.process("home.html", data, response, getServletContext());
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
	                    FreeMarker.process("home.html", data, response, getServletContext());
	                }catch(Exception e){
	                    System.out.println("Errore creazione sessione HOME " + e);
	                }
	            }
	        }
        }else {//Se si è connessi
	        if("logout".equals(action)){
	            System.out.println("** CLICCATO LOGOUT POSIZIONATO IN HOME **");
	            try{
	                SecurityLayer.disposeSession(request); 
	                id=0; 
	                data.put("id",id);
	                utente = null;
	                data.put("utente", utente);
	                response.sendRedirect("");
	            }catch(Exception e3){
	                e3.printStackTrace();
	            }
	        }
        }
        
        if("search".equals(action)) {
        	System.out.println("Search");
        	try {
        		System.out.println("Cerchiamo::>>");
        		String searchStringa= request.getParameter("ricerca");
        		HttpSession s = SecurityLayer.checkSession(request);
        		s.setAttribute("ricerca", searchStringa);
        		data.put("ricerca", searchStringa);
        		response.sendRedirect("ricercaPubblicazione");
        	}catch(Exception e) {
        		System.out.println("Home exception per la search" + e);
        	}
        }
		/*Fine del doPost*/
	}

}
