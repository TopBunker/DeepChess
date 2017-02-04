package ChessEngine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JBDC {
	 private static String url = "jdbc:mysql://localhost:3306/***";    
	    private static String driverName = "com.mysql.jdbc.Driver";   
	    private static String username = "root";   
	    private static String password = "triala";
	    private static Connection con;
	    private static String urlstring;

	    public static Connection getConnection() {
	        try {
	            Class.forName(driverName);
	            try {
	                con = DriverManager.getConnection(urlstring, username, password);
	            } catch (SQLException ex) {
	                // log an exception. fro example:
	                System.out.println("Failed to create the database connection."); 
	            }
	        } catch (ClassNotFoundException ex) {
	            // log an exception. for example:
	            System.out.println("Driver not found."); 
	        }
	        return con;
	    }

}
