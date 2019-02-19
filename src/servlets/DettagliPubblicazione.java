package servlets;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
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

import model.Pubblicazione;
import model.Recensione;
import model.Ristampa;
import model.Sorgente;
import model.Capitolo;
import model.Entry;
import model.Utente;
import model.dao.CapitoloDAO;
import model.dao.EntryDAO;
import model.dao.LodeDAO;
import model.dao.PubblicazioneDAO;
import model.dao.RecensioneDAO;
import model.dao.RistampaDAO;
import model.dao.SorgenteDAO;
import util.Database;
import util.FreeMarker;
import util.SecurityLayer;


import util.Utile;

/**
 * Servlet implementation class DettagliPubblicazione
 */
@WebServlet("/DettagliPubblicazione")
public class DettagliPubblicazione extends HttpServlet {
	Map<String, Object> data = new HashMap<String,Object>();
	public int id=0; //id dell'utente -> default
	public int idPubblicazione=0;
    public int ruolo=0; //ruolo dell'utente {1=normale,2=moderatore,3=admin} -> di default
    public Utente utente; //dati dell'utente -> servono quando è connesso
    public Pubblicazione pubblicazione;
    /**
     * 
     */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.print("process request di DettagliPubblicazione");
			
    	
		FreeMarker.process("dettagliPubblicazione.html", data, response, getServletContext()); // data ??
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("DettagliPubblicazione DOGET");
		HttpSession s = SecurityLayer.checkSession(request);
		if(s != null){//condizione per vedere se la sessione esiste.   		
            if(s.getAttribute("id") != null && s.getAttribute("utente") != null){// Esistono id e utente nella sessione
                id = (int) s.getAttribute("id");
                utente = (Utente) s.getAttribute("utente");
                //s.removeAttribute("ricerca");      //--> per cancellare la ricerca
            }else{ // Non esistono id e utente nella sessione
                id=0;
                utente = null;
                //utente non c'è.
            }
            System.out.println("Process Request DettagliPubblicazione ->  ID =" + id );           
        }else{//Non esiste per niente la sessione, l'utente non è connesso
            id = 0;
            utente = null;
            //utente non c'è quindi non mostri niente?
        }  	
		data.put("id", id);    
    	data.put("utente", utente);
		idPubblicazione = Integer.parseInt(request.getParameter("codice"));
		
		pubblicazione = (Pubblicazione) PubblicazioneDAO.detailPub(idPubblicazione);
		data.put("Pubblicazione", pubblicazione);
		System.out.println("Ho finito la query del dettaglio, procedo avanti > " + pubblicazione);
		ArrayList<Capitolo> mostraCapitoli = new ArrayList<Capitolo>();
		ArrayList<Sorgente> mostraSorgenti = new ArrayList<Sorgente>();
		ArrayList<Ristampa> mostraRistampe = new ArrayList<Ristampa>();
		
		if(pubblicazione.getCapitoli() != null) mostraCapitoli = pubblicazione.getCapitoli();
		if(pubblicazione.getSorgenti() != null) mostraSorgenti = pubblicazione.getSorgenti();
		if(pubblicazione.getRistampe() != null) mostraRistampe = pubblicazione.getRistampe();
    	System.out.println("Dopo tutta la roba dei get MOSTRO ARRAY");
    	System.out.println(mostraCapitoli + " " + mostraSorgenti + " " + mostraRistampe);
    	
    	data.put("listaCapitoli", mostraCapitoli);
    	data.put("listaSorgenti", mostraSorgenti);
    	data.put("listaRistampe", mostraRistampe);
    	
    	ArrayList<Entry> listaEntry = new ArrayList<Entry>();
    	listaEntry = (ArrayList<Entry>) EntryDAO.showPubEntries(idPubblicazione);
    	data.put("listaEntry", listaEntry);
    	
    	ArrayList<Recensione> listaRecensioni = new ArrayList<Recensione>();
    	listaRecensioni = (ArrayList<Recensione>) RecensioneDAO.revList(idPubblicazione);
    	data.put("listaRecensioni", listaRecensioni);
    	
    	int cont = LodeDAO.countLike(idPubblicazione);
    	data.put("cont", cont);
    	
    	
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
		System.out.println("DettagliPubblicazione DOPOST");
        String action = request.getParameter("value");
        System.out.println(" >> " +action+ " << ");
        
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
        }else {//Se si è connessi
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
	        }else if("invioCapitolo".equals(action)) {
	        	String titolo = request.getParameter("titolo");
	        	int numero = Integer.parseInt(request.getParameter("numero"));
	        	int pagInizio = Integer.parseInt(request.getParameter("pagInizio"));
	        	// --> BISOGNA AGGIUNGERE L'AZIONE NELLA ENTRY!!
	        	System.out.println(titolo + " " + numero + " " + pagInizio);
	        	if(idPubblicazione==0)idPubblicazione = Integer.parseInt(request.getParameter("codice"));
	        	CapitoloDAO.insertCapitolo(idPubblicazione, numero, pagInizio, titolo);
	        	
	        	EntryDAO.insertEntry("ha inserito un nuovo capitolo nella pubblicazione", idPubblicazione, id);
	        	response.sendRedirect("dettagliPubblicazione?codice="+idPubblicazione);
	        }else if("invioSorgenti".equals(action)) {
	        	String descrizione = request.getParameter("descrizioneS");
	        	String formato =request.getParameter("formatoS");
	        	String tipo =request.getParameter("tipoS");
	        	String URI = request.getParameter("uriS");
	        	// --> BISOGNA AGGIUNGERE L'AZIONE NELLA ENTRY!!
	        	if(idPubblicazione==0)idPubblicazione = Integer.parseInt(request.getParameter("codice"));
	        	SorgenteDAO.insertSorgente(descrizione, formato, idPubblicazione, tipo, URI);
	        	
	        	EntryDAO.insertEntry("ha inserito una nuova sorgente nella pubblicazione", idPubblicazione, id);
	        	response.sendRedirect("dettagliPubblicazione?codice="+idPubblicazione);
	        }else if("invioRistampe".equals(action)) {
	        	String nome = request.getParameter("nomeR");
	        	if(idPubblicazione==0)idPubblicazione = Integer.parseInt(request.getParameter("codice"));
	        	String dataR = request.getParameter("dataR");
	        	try {
	        		//conversione data da String a Date
	            	SimpleDateFormat data_a = new SimpleDateFormat("yyyy-MM-dd");
	        		java.util.Date data_b = data_a.parse(dataR);
	        		java.sql.Date dataRistampa = new java.sql.Date(data_b.getTime());    
	        		
	        		
	            	RistampaDAO.insertRistampa(nome, idPubblicazione, dataRistampa);
	            	EntryDAO.insertEntry("ha inserito una nuova ristampa nella pubblicazione", idPubblicazione, id);
	        	
	            } catch (Exception e) {
	            	System.out.println(e);
	            	e.printStackTrace();
	            }
	        	
	        	response.sendRedirect("dettagliPubblicazione?codice="+idPubblicazione);
	        }else if("inviaRecensione".equals(action)) {
	        	String testo = request.getParameter("testoRec");
	        	if(idPubblicazione==0)idPubblicazione = Integer.parseInt(request.getParameter("codice"));
	        	
	        	boolean checkR=false;
	        	try {
	        		checkR = Utile.checkRecensione(id, idPubblicazione);
	        	} catch (Exception e) {
	            	System.out.println(e);
	            	e.printStackTrace();
	            }
	        	
	        	if(!checkR) RecensioneDAO.insertRev(id, idPubblicazione, testo, 0);
	        	response.sendRedirect("dettagliPubblicazione?codice="+idPubblicazione);
	        	
	        }else if("inviaLike".equals(action)) {
	        	if(idPubblicazione==0)idPubblicazione = Integer.parseInt(request.getParameter("codice"));
	        	boolean checkL=false;
	        	try {
	        		checkL = Utile.checkLike(id, idPubblicazione);
	        	} catch (Exception e) {
	            	System.out.println(e);
	            	e.printStackTrace();
	            }
	        	
	        	if(!checkL) LodeDAO.insertLike(idPubblicazione, id);
	        	
	        	response.sendRedirect("dettagliPubblicazione?codice="+idPubblicazione);
	        }
        }
	}

}
