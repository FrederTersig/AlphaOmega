package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
 * Servlet implementation class RicercaPubblicazione
 */
@WebServlet("/RicercaPubblicazione")
public class RicercaPubblicazione extends HttpServlet {

       
    Map<String, Object> data = new HashMap<String,Object>(); // la tree map è da togliere
       
    
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("process request della RicercaPubblicazione");
		//Mostra risultati cercati, permette di ricercare di nuovo una stringa, altro.
		
		
		FreeMarker.process("ricercaPubblicazione.html", data, response, getServletContext()); // data ??
	}

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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
