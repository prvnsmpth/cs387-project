/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author praveen
 */
@WebServlet(name = "NewAccount", urlPatterns = {"/NewAccount"})
public class NewAccount extends HttpServlet {
    
    private static final String CHECK_QUERY = "select * from user where username=?";
    
    private boolean check_if_present(String strUserName) throws SQLException {
        boolean present = false;
        Database conhelper=new Database();
        Connection con = null;
        try {
            conhelper.connect();
            con = conhelper.getConnection();
            PreparedStatement prepStmt = con.prepareStatement(CHECK_QUERY);
            prepStmt.setString(1, strUserName);
            ResultSet rs = prepStmt.executeQuery();
            if(rs.next()) {
                System.out.println("User login is valid in DB");
                present = true;
            }
            con.close();
        } catch(Exception e) {
            System.out.println("validateLogon: Error while validating password: "+e.getMessage());
        } finally {
            con.close();
            conhelper.closeConnection();
        }
        return present;
    }
    
    private void insert(String strUserName, String strName, String strPassword){
        try { 
//            byte[] bytesOfMessage = strPassword.getBytes("UTF-8");
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] thedigest = md.digest(bytesOfMessage);
//            String entered_password = new String(thedigest);    
            
            String entered_password = strPassword;
                        
            Database d = new Database();
            d.connect();
            Map<String, String> m = new HashMap();
            m.put("name", strName);
            m.put("username", strUserName);
            m.put("password", entered_password);

            int id = d.insert("user", m);
            System.out.println("Insert id: " + id);
            
        }
        catch(Exception e){
            System.out.println("Exception in encryption in creating a new user");
        }
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String strUserName = request.getParameter("username").toString();
            String strname = request.getParameter("name").toString();
            String strPassword = request.getParameter("password").toString();
            boolean present = check_if_present(strUserName);
            try {
                if(present){
                    // display the fact that the username is takenS
                }
                else{
                    insert(strUserName, strname, strPassword);
                }
            }
            catch(Exception e){
                System.out.println("error in encryption");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
