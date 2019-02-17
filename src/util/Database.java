package util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 
 * @author Federico Tersigni
 *
 */
public class Database {
	protected static String DRIVER ="com.mysql.jdbc.Driver";
	protected static String url="jdbc:mysql://localhost/biblioteca";
	protected static String user ="root";
	protected static String psw ="";	
	private static Connection db; 
	
	
	
	/**
	 * Metodo per connettersi al database
	 * @throws Exception (SQLException && ClassNotFoundException)
	 * 
	 */
	public static void connect() throws Exception{    
    	try{                    
    		Class.forName(DRIVER);             
    		db = DriverManager.getConnection(url,user,psw);
    	}catch(SQLException e){
    		System.out.println("Eccezione SQL");
    		System.out.println(e.getMessage());
    	}catch (ClassNotFoundException e) {
    		System.out.println("Eccezione NO CLASSE");
    		e.printStackTrace();
    	}
	}
	
    /**
     * Metodo per fare una select immettendo tabella e condizione E ricevendo i dati ordinati
     * @param column		colonna su cui si fa la query
     * @param table         tabella da cui si richiamano i dati
     * @param condition     condizione per filtrare i dati
     * @param order         ordine da dare ai dati
     * @return              restituisce il ResultSet con i dati
     * @throws java.sql.SQLException
     */
    public static ResultSet selectRecord(String column,String table, String condition, String order) throws SQLException{
        String query = "SELECT "+ column +" FROM " + table;
        if(condition != null && !condition.trim().isEmpty()) query += " WHERE " + condition;
        if(order != null && !order.trim().isEmpty()) query += " ORDER BY " + order;
        System.out.println(query);
        return Database.executeQuery(query);   
    }
    /**
     * Metodo per fare una select immettendo tabella e condizione E ricevendo l'elemento massimo
     * @param columnMaxMin      colonna da cui selezionare il massimo
     * @param table         	tabella da cui si richiamano i dati
     * @param condition     	condizione per filtrare i dati
     * @return              	restituisce il ResultSet con i dati
     * @throws java.sql.SQLException
     */
    public static ResultSet selectMax(String columnMaxMin, String table, String condition) throws SQLException{
    	String query="SELECT MAX("+ columnMaxMin +") AS attr FROM "+ table +" WHERE " + condition;
    	return Database.executeQuery(query);
    }
 

    /**
     * Metodo per inserire il record nel DB
     * @param table     tabella in cui fare la insert
     * @param data      un oggetto Map<String,Object> che avrà i dati da inserire
     * @return 			restituisce boolean per comunicare la riuscita della insert : True se riesce, False altrimenti
     * @throws java.sql.SQLException
     */
    public static boolean insertRecord(String table, Map<String, Object> map) throws SQLException{
        String query = "INSERT INTO " + table + " SET ";
        Object value;
        String attr;    
        for(Map.Entry<String,Object> e:map.entrySet()){
            attr = e.getKey();
            value = e.getValue();
            if(value instanceof Integer){
                query = query + attr + " = " + value + ", ";
            }else{
                value = value.toString().replace("\'", "\\'");
                query = query + attr + " = '" + value + "', ";
            }
        }
        query = query.substring(0, query.length() - 2); // Serve per togliere la virgola
        System.out.println("Query della insert-> " + query);
        return Database.updateQuery(query);
    }
    
   
    
    //Query join che si applica ad un qualsiasi numero di attributi sul JOIN
    public static ResultSet join(String column, String table, Map<String, Object> data,String condition, String order)throws SQLException{
    	String query = "SELECT "+ column +" FROM "+ table;
    	Object value;
    	String attr;
    	for(Map.Entry<String, Object> e:data.entrySet()) {
    		attr = e.getKey();
    		value = e.getValue();
    		query += " JOIN " + attr +" ON "+ value ; // value -> prod.id = brad.id ; 
    	}
    	if(condition != null && !condition.trim().isEmpty()) query += " WHERE " + condition;
        if(order != null && !order.trim().isEmpty()) query += " ORDER BY " + order;
        System.out.println("QUERY EFFETTUATA:");
        System.out.println(query);
    	return Database.executeQuery(query);
    }
    

    public static ResultSet callProcedure(String proc, ArrayList<String> data, int num)throws SQLException{
    	System.out.println("COMINCIO CALL PROCEDURE");
    	
    	String query = "CALL "+ proc +"(";
    	if(num==0) {
	    	String arg = "";
	    	int len = data.size();
	    	for(int i=0; i<len; i++) {
	    		System.out.println(data.get(i));
	    		arg = arg + "'" + data.get(i) + "'" + ",";
	    		System.out.println(arg);
	    	}
	    	arg = arg.substring(0, arg.length() - 1);
	    	query = query + arg +");";
    	}else {
    		query = query + num + ");";
    	}
    	System.out.println("CALL PROCEDURE!! ECCO LA QUERY CHE MOSTRA ::");
    	System.out.println(query);
    	return Database.executeQuery(query);
    }
    	
    /**
     * Metodo per fare l'update di un record
     * @param table         tabella dove andiamo ad aggiornare i dati
     * @param data          un oggetto Map<String,Object> che avrà i dati da inserire
     * @param condition     condizione per filtrare i dati
     * @return              restituisce boolean per comunicare la riuscita della insert : True se riesce, False altrimenti
     * @throws java.sql.SQLException
     */
    public static boolean updateRecord(String table, Map<String,Object> data, String condition) throws SQLException{
        String query = "UPDATE " + table + " SET ";
        Object value;
        String attr;      
        for(Map.Entry<String,Object> e:data.entrySet()){
            attr = e.getKey();
            value = e.getValue();
            if(value instanceof String){
                value = value.toString().replace("\'", "\\'");
                /* Nella variabile qui sotto, dopo value inseriamo un apice seguito da una virgola e da uno spazio; 
                 * Il motivo per questo spazio è l'evitare conflitti con il linguaggio.*/
               
                query = query + attr + " = '" + value + "', ";
            }else{
                query = query + attr + " = " + value + ", ";
            }            
        }
        //query = query.substring(0, query.length()-2) + " WHERE " + condition;
        query = query.substring(0, query.length()-2); //toglie lo la virgola e lo spazio finale aggiunto prima
        if(condition != null && condition.trim().isEmpty()) query += " WHERE " + condition;
        //IF nuovo, da testare
        return Database.updateQuery(query);
    }
   
    
    /**
     * Metodo per fare l'update di un record senza l'uso della Map
     * @param table         tabella dove andiamo ad aggiornare i dati
     * @param data          stringa che avrà i dati da inserire
     * @param condition     condizione per filtrare i dati
     * @return              restituisce boolean per comunicare la riuscita della insert : True se riesce, False altrimenti
     * @throws java.sql.SQLException
     */
    //da rimuovere  se quella di sopra va bene
    public static boolean updateRecord(String table, String change, String condition) throws SQLException{
    	String query = "UPDATE " + table + " SET " + change + " WHERE " + condition;
    	return Database.updateQuery(query);   	
    }
    
    /**
     * Metodo per fare la delete di un record
     * @param table         tabella in cui eliminare i dati
     * @param condition     condizione per il filtro dei dati
     * @return              true se l'eliminazione è andata a buon fine, false altrimenti
     * @throws java.sql.SQLException
     */
    public static boolean deleteRecord(String table, String condition) throws SQLException{
        String query = "DELETE FROM " + table + " WHERE " + condition;
        return Database.updateQuery(query);
    }
    
    /**
     * Count record
     * @param table         tabella in cui contare i dati
     * @param condition     condizione per il filtro dei dati
     * @return              numero dei record se la query è stata eseguita on successo, -1 altrimenti
     * @throws java.sql.SQLException
     */
    public static int countRecord(String table, String condition) throws SQLException{
        String query = "SELECT COUNT(*) FROM " + table + " WHERE " + condition;
        ResultSet record = Database.executeQuery(query);
        record.next();
        return record.getInt(1);
    }   
    /**
     * Imposta a NULL un attributo di una tabella  
     * @param table         tabella in cui è presente l'attributo
     * @param attribute     attributo da impostare a NULL
     * @param condition     condizione
     * @return
     * @throws java.sql.SQLException
     */
    public static boolean resetAttribute(String table, String attribute, String condition) throws SQLException{
        String query = "UPDATE " + table + " SET " + attribute + " = NULL WHERE " + condition;
        return Database.updateQuery(query);
    }
    
    /**
     * executeQuery personalizzata
     * @param query query da eseguire
     */
    private static ResultSet executeQuery(String query) throws SQLException{      
        Statement s1 = Database.db.createStatement();
        ResultSet records = s1.executeQuery(query);
        return records;             
    }  
    /**
     * updateQuery personalizzata
     * @param query query da eseguire
     */
    private static boolean updateQuery(String query) throws SQLException{       
        Statement s1;        
        s1 = Database.db.createStatement();
        s1.executeUpdate(query); 
        s1.close();
        return true; 
    }
    
	/**
     * Metodo per chiudere al connessione al database
     * @throws java.sql.SQLException
     */
    public static void close() throws SQLException{
        Database.db.close();
    }
	
	
}
