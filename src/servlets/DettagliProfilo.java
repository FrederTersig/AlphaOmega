package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.FreeMarker;

/**
 * Servlet implementation class DettagliProfilo
 */
@WebServlet("/DettagliProfilo")
public class DettagliProfilo extends HttpServlet {
	Map<String, Object> data = new HashMap<String,Object>(); // la tree map è da togliere
       
    
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("process request di DettagliProfilo");
		//Controllo ID, controllo permessi, poi inserimento campi e POST
		
		
		FreeMarker.process("dettagliProfilo.html", data, response, getServletContext()); // data ??
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
