/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.sql.*;

/**
 *
 * @author praveen
 */
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class Database {
    
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
                    + "?user=" + DB_USER + "&password=" + DB_PASSWD);            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return con;
    }  
        
}
