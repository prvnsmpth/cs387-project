/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

/**
 *
 * @author praveen
 */
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.*;

public class Database {
    
    private static final String DB_NAME = "cs387-project-db";
    private static final String DB_HOST = "localhost";
    private static final String DB_USER = "root";
    private static final String DB_PASSWD = "123";
    Connection con = null;
       
    public void connect()
    {        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + DB_HOST + "/" + DB_NAME 
                    + "?user=" + DB_USER + "&password=" + DB_PASSWD);            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
               
    }  
    
    public Connection getConnection()
    {
        return con;
    }
    
    public int insert(String table, Map attr)
    {
        String query = "INSERT INTO " + table + " ";
        int ret = -1;
        Iterator it = attr.keySet().iterator();
        String cols = "", ques = "";
        String[] vals = new String[attr.size()];
        int i = 0;
        while (it.hasNext()) {
            String key = (String) it.next();
            Object _val = attr.get(key);
            if (_val != null) {
                cols += "`" + key + "`" + ",";
                ques += "?,";
                vals[i] = (String) _val;
                System.out.println("VAL: " + vals[i]);
                i++;
            }
        }
        
        if (cols.length() > 0)
            cols = cols.substring(0, cols.length() - 1);
        if (ques.length() > 0)
            ques = ques.substring(0, ques.length() - 1);
        
        
        query += "(" + cols + ") VALUES (" + ques + ")";
        System.out.println(query);
        
        try {
            PreparedStatement s = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int j = 0; j < i; j++) {
                s.setString(j + 1, vals[j]);
            }
            s.executeUpdate(); 
            ResultSet r = s.getGeneratedKeys();
            if (r.next())
                ret = r.getInt(1);
            r.close();
            s.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ret;
    }
    
    public ResultSet execute(String query)
    {
        ResultSet rs = null;
        try {
            Statement s = con.createStatement();
            rs = s.executeQuery(query);  
            System.out.println(query);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }        
        return rs;
    }
    
    public void closeConnection()
    {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
