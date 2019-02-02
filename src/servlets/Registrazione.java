package servlets;


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

import util.Database;
import util.FreeMarker;
import util.SecurityLayer;
import util.Utile;


/**
 * Servlet implementation class Registrazione
 */
@WebServlet("/Registrazione")
public class Registrazione extends HttpServlet {
	Map<String, Object> data = new HashMap<String,Object>(); // la tree map è da togliere
       
    
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("process request della Registrazione");
		//Non c'è motivo per avere una sessione in questa pagina.
		System.out.println("Vado direttamente alla pagina");
		FreeMarker.process("registrazione.html", data, response, getServletContext()); // data ??
	}
    /**
     * Nessuna query in particolare se non le insert derivate dall'inserimento dei dati
     */
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*L'unica chiamata POST sarebbe la insert dei dati inseriti dall'utente al database.
		 *(Tramite pressione del tasto.) -> Ricordati di usare il crypt
		 * */
	}

}
