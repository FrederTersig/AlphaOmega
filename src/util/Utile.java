package util;


import util.Database;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static java.util.Objects.isNull;
import javax.naming.NamingException;
import java.io.*;
/**
 *
 * @author Federico
 */


public class Utile {
	
	
	
	public static int existPub(String ISBN, int idUtente) throws Exception {
		int w=0;
		String table ="pubblicazione";
		String condition ="pubblicazione.ISBN = '" + ISBN + "' AND pubblicazione.idUtente = " + idUtente;
		try {
			Database.connect();
			ResultSet rs=Database.selectRecord("*", table, condition, "");
			while (rs.next()) { 
				int id = rs.getInt("id");
				w = id;
			}
			Database.close();
		}catch (NamingException e) {
        	System.out.println("existPub NamingException: " + e.getMessage());
        } catch (SQLException e) {
        	System.out.println("existPub SQLException: " + e.getMessage());
        }
		return w;
	}
	
	
	public static boolean checkLike(int idUtente, int idPubblicazione) throws Exception{
		boolean x = false;
		try {
			Database.connect();
			ResultSet rs = Database.selectRecord("*", "lode", "lode.idUtente="+idUtente+" AND lode.idPubblicazione="+idPubblicazione, "");
			while(rs.next()) x=true;
			Database.close();
		}catch(NamingException e) {
			System.out.println("CheckRuolo NamingException: " + e.getMessage());
	    }catch(SQLException e) {
	    	System.out.println("CheckRuolo SQLException: " + e.getMessage());
	    }
		return x;
	}
	
	public static boolean checkRecensione(int idUtente, int idPubblicazione) throws Exception{
		boolean x = false;
		try {
			Database.connect();
			ResultSet rs = Database.selectRecord("*", "recensione", "recensione.idUtente="+idUtente+" AND recensione.idPubblicazione="+idPubblicazione, "");
			while(rs.next()) x=true;
			Database.close();
		}catch(NamingException e) {
			System.out.println("CheckRuolo NamingException: " + e.getMessage());
	    }catch(SQLException e) {
	    	System.out.println("CheckRuolo SQLException: " + e.getMessage());
	    }
		return x;
	}

	
	/**
     * Restituisce l'id dell'utente che possiede l'email e la password passati per argomenti
     * @param String				email dell'utente
     * @param String				password dell'utente non criptata
     * @return                  	l'id dell'utente
     */ 
	
    public static int checkUser(String email, String pass) throws Exception {
        int w = 0;
        try {
            Database.connect();
            if (!isNull(pass)) {
            	System.out.println("CRIPTO");
                pass = crypt(pass);
            }

            String condition = "email = '" + email + "' AND password = '" + pass + "'"; 
            System.out.println(condition);
            ResultSet r = Database.selectRecord("utente.id","utente",condition,"");        
            while (r.next()) {
                w = r.getInt("id");
            }           
            Database.close();
        } catch (NamingException e) {
        	System.out.println("CheckUser NamingException: " + e.getMessage());
        } catch (SQLException e) {
        	System.out.println("CheckUser SQLException: " + e.getMessage());
        }
        System.out.println("FINE CHECKUSER, risultato >  " + w );
        return w;
    }
    /**
     * Restituisce una booleana che spiega se la password passata in argomento � quella dell'utente che ha
     * come id quello passato in argomento.
     * 
     * @param password			password dell'utente da controllare
     * @param idTrofeo			l'id dell'utente
     * @return                  valore booleano: Se true la password appartiene all'utente, se false no.
     */ 
    public static Boolean checkPsw(String password, int id) throws Exception {
    	boolean isEqual=false;
    	/*try {
    		Database.connect();
    		if(!isNull(password)) {
    			password = crypt(password);
    			System.out.println(password);
    		}
    		String condition = "utente.id="+id+" AND utente.password='"+password+"'";
    		ResultSet rs=Database.select("utente", condition);
    		while(rs.next()) isEqual=true;
    		Database.close();
    	}catch (NamingException e) {
        	System.out.println("CheckUser NamingException: " + e.getMessage());
        } catch (SQLException e) {
        	System.out.println("CheckUser SQLException: " + e.getMessage());
        }*/
    	
    	return isEqual;
    }
    
    /**
     * Restituisce il ruolo di un determinato ID utente
     * @param id    			id dell'utente
     * @return                  ruolo dell'utente nella piattaforma
     */ 
    

    public static int checkRuolo(int id) throws Exception{
    		System.out.println("Inizio CheckRuolo");
    		int z=0;
    		try {
    			Database.connect();
    			String condition ="id = '" + id + "'";
    			ResultSet r = Database.selectRecord("*","utente", condition,"");
    			while (r.next()) {
    				z = r.getInt("ruolo");
    			}
    		}catch(NamingException e) {
    			System.out.println("CheckRuolo NamingException: " + e.getMessage());
            }catch(SQLException e) {
            	System.out.println("CheckRuolo SQLException: " + e.getMessage());
            }
            return z;
    }
    
    

    /**
     * Restituisce la versione criptata della stringa passata nell'argomento
     * @param string    		stringa da criptare
     * @return                  la stringa criptata, null se avviene l'eccezione
     */ 
    
    public static String crypt(String string) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = string.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digested.length; i++) {
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }

    }
    
    /**
     * Verifica se una stringa criptata � stata generata da un'altra stringa
     * @param string_crypted    stringa criptata
     * @param to_check          stringa da verificare
     * @return                  true se la password � stata verificata, false altrimenti
     */
    public static boolean decrypt(String string_crypted, String to_check){
        if(to_check == null || string_crypted == null) return false;
        return string_crypted.equals(crypt(to_check));
    }
    
    
    
    /**
     * Crea un url di un gioco e verifica che non sia gi� stato associato ad un altro gioco.
     * @return                  Url da assegnare al gioco (stringa)
     */
    
}
