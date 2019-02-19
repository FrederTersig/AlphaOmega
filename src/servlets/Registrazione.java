package servlets;


import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import util.Database;
import util.FreeMarker;
import util.SecurityLayer;
import util.Utile;

import model.Utente;
import model.dao.UtenteDAO;

/**
 * Servlet implementation class Registrazione
 */

public class Registrazione extends HttpServlet {
	Map<String, Object> data = new HashMap<String,Object>(); // la tree map è da togliere
	public int id=0; //id dell'utente -> default
	public Utente utente;
    
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("process request della Registrazione");
		//Non c'è motivo per avere una sessione in questa pagina.
		//CANCELLO la sessione entrando in questa pagina
		//ricordando che il bottone per questa pagina appare SOLO se si è GUEST.
		data.put("id", id);    
    	data.put("utente", utente);
		FreeMarker.process("registrazione.html", data, response, getServletContext()); // data ??
	}
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet della registrazione");
		HttpSession session = request.getSession();
        session.invalidate();
        utente=null;
        id=0;
        try {
            processRequest(request, response);
        } catch (Exception e) {
           e.printStackTrace();
        }
        //Cancello la sessione appena entro : sicuro di essere un Guest
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*L'unica chiamata POST sarebbe la insert dei dati inseriti dall'utente al database.
		 *(Tramite pressione del tasto.) -> Ricordati di usare il crypt
		 * */
		System.out.println("doPost di Registrazione");
		String action = request.getParameter("value");
		if("sign".equals(action)) {
			//Parametri immessi dall'utente
			String nome = request.getParameter("nome");
            String cognome = request.getParameter("cognome");
            String dataNascita = request.getParameter("dataNascita");
            String email = request.getParameter("email");
            String citta = request.getParameter("citta");      
            int ruolo = 0; //Il ruolo iniziale dell'utente è 0, quello BASE
            String password = request.getParameter("password");
            //Data di iscrizione-
			//Calendar calendar = Calendar.getInstance();
            //Date dataIscr = new Date(calendar.getTime().getTime());
           // System.out.println("CHECK");
            //System.out.println(nome + " " + cognome + " " + dataNascita + " " + citta + " " + dataIscr);
            
            //fine data iscrizione-
           
            try {
            	int check = Utile.checkUser(email, password);
            	if(check==0) {
            		//conversione di dataNascita da String a Date
            		SimpleDateFormat data_a = new SimpleDateFormat("yyyy-MM-dd");
            		
            		java.util.Date date = data_a.parse(dataNascita);
            		java.sql.Date nascita = new java.sql.Date(date.getTime());
            		//fine conversione
            		
            		UtenteDAO.insertUser(email, nome, cognome, password, ruolo, citta, nascita);
            	}else{
            		System.out.println("Esiste già un utente con queste credenziali nel DB");
            		//Resetta e ritorna in registrazione.html
            		response.sendRedirect("registrazione");
            	}
            	response.sendRedirect("home");
            }catch (NamingException e) {
            	System.out.println(e);
            } catch (SQLException e) {
            	System.out.println("SQL exception nella Registrazione -> " + e);
            } catch (Exception e) {
            	System.out.println(e);
            }
            
		}else if("login".equals(action)){ // SE il metodo post è il login....
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
		
	}

}
