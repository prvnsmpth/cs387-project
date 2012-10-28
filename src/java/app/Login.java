/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author praveen
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {
    
    private static final String LOGIN_QUERY = "select username from user where username=? and password=?";
    
    private boolean authenticateLogin(String strUserName, String strPassword) throws Exception {
        boolean isValid = false;
        Database conhelper = new Database();
        conhelper.connect();
        Connection con = null;
        try {
            con = conhelper.getConnection();
            PreparedStatement prepStmt = con.prepareStatement(LOGIN_QUERY);
            prepStmt.setString(1, strUserName);
            prepStmt.setString(2, strPassword);
            ResultSet rs = prepStmt.executeQuery();
            System.out.println("TEST user: " + strUserName);
            System.out.println("TEST pass:" + strPassword);
            if(rs.next()) {
                System.out.println("User login is valid in DB");
                isValid = true;
            }
        } catch(Exception e) {
            System.out.println("validateLogon: Error while validating password: "+e.getMessage());
            throw e;
        } finally {
            con.close();
            conhelper.closeConnection();
        }
        return isValid;
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
            String strPassword = request.getParameter("password").toString();
            //byte[] bytesOfMessage = strPassword.getBytes("UTF-8");
            try {
//                MessageDigest md = MessageDigest.getInstance("MD5");
//                byte[] thedigest = md.digest(bytesOfMessage);
//                String check_password=new String(thedigest);
                boolean valid = authenticateLogin(strUserName, strPassword);
                System.out.println("Sparrtaaa: " + valid);
                if (valid) {
                    HttpSession session = request.getSession();
                    session.setAttribute("username", strUserName);
                    response.sendRedirect("home.jsp");                    
                } else {
                    response.sendRedirect("login.jsp");                    
                }
            }
            catch(Exception e){
                System.out.println("error in encryption");
            }
            
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
