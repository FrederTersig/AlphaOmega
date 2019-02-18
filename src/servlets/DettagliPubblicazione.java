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

import model.Pubblicazione;
import model.Ristampa;
import model.Sorgente;
import model.Capitolo;
import model.Utente;
import model.dao.CapitoloDAO;
import model.dao.EntryDAO;
import model.dao.PubblicazioneDAO;
import model.dao.RistampaDAO;
import model.dao.SorgenteDAO;
import util.FreeMarker;
import util.SecurityLayer;

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
		
		
		// -> Dettagli della pubblicazione visualizzata
		// -> Dettagli del tag
		// -> Dettagli dell'autore
		// -> Dettagli dell'editore
		// -> Dettagli del sorgente
		// -> Dettagli delle modifiche ---- SOLO PER UTENTI CON POTERI
    	
    	
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
        // ----> 1 nelle funzioni significa ID PUBBLICAZIONE!!!!
        if("invioCapitolo".equals(action)) {
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
        	String descrizione = request.getParameter("descrizione");
        	String formato =request.getParameter("formato");
        	String tipo =request.getParameter("tipo");
        	String URI = request.getParameter("URI");
        	// --> BISOGNA AGGIUNGERE L'AZIONE NELLA ENTRY!!
        	if(idPubblicazione==0)idPubblicazione = Integer.parseInt(request.getParameter("codice"));
        	SorgenteDAO.insertSorgente(descrizione, formato, idPubblicazione, tipo, URI);
        	
        	EntryDAO.insertEntry("ha inserito una nuova sorgente nella pubblicazione", idPubblicazione, id);
        	response.sendRedirect("dettagliPubblicazione");
        }else if("invioRistampe".equals(action)) {
        	String nome = request.getParameter("nome");
        	if(idPubblicazione==0)idPubblicazione = Integer.parseInt(request.getParameter("codice"));
        	String dataR = request.getParameter("dataRistampa");
        	try {
        		//conversione data da String a Date
            	SimpleDateFormat data_a = new SimpleDateFormat("yyyy-MM-dd");
        		java.util.Date data_b = data_a.parse(dataR);
        		java.sql.Date dataRistampa = new java.sql.Date(data_b.getTime());        	
            	RistampaDAO.insertRistampa(nome, idPubblicazione, dataRistampa);
            	EntryDAO.insertEntry("ha inserito una nuova ristampa nella pubblicazione", idPubblicazione, id);
        	
            } catch (Exception e) {
            	System.out.println(e);
            }
        	
        	response.sendRedirect("dettagliPubblicazione");
        }
	}

}
