/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.sql.Connection;

/**
 *
 * @author praveen
 */
import java.sql.DriverManager;
public class DatabaseConnection {
    
    private static final String DB_NAME = "cs387-project-db";
    private static final String DB_HOST = "localhost";
    private static final String DB_USER = "root";
    private static final String DB_PASSWD = "123";
    Connection con = null;
    
    
    public Connection connect()
    {        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + DB_HOST + "/" + DB_NAME 
                    + "?username=" + DB_USER + "&password=" + DB_PASSWD);            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return con;
    }
}
