/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
@WebServlet(name = "RateAlbum", urlPatterns = {"/RateAlbum"})
public class RateAlbum extends HttpServlet {
    
   private static final String RATE_QUERY = "insert into album_rating values(?,?,?)";
   private static final String REVIEW_QUERY = "insert into album_review (`album_id`, `username`, `review`) values(?,?,?)";
   private static final String UPDATE_RATE_QUERY = "update album_rating set rating =? WHERE username=? and album_id=?";
   private static final String UPDATE_REVIEW_QUERY = "update album_review set review =? WHERE username=? and album_id=?";
   private static final String CHECKIFREVIEWED_QUERY = "select album_id from album_review where username=? and album_id=?";
   private static final String CHECKIFRATED_QUERY = "select album_id, rating from album_rating where username=? and album_id=?";

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
            String rate = request.getParameter("rate");
            String review = request.getParameter("review");
            String album_id = request.getParameter("album_id");
            
            HttpSession session = request.getSession();
            String userName = session.getAttribute("username").toString();            
            
            //// the parameters have been fetched
            boolean rated=false, reviewed=false;
            int oldRating = 0;
            Database d = new Database();
            d.connect();            
            Connection con = d.getConnection();
            try {
                PreparedStatement prepStmt = con.prepareStatement(CHECKIFRATED_QUERY);
                prepStmt.setString(1, userName);
                prepStmt.setString(2, album_id);
                ResultSet rs = prepStmt.executeQuery();    //// check if the user has rated the song before
                if(rs.next()){
                    rated=true;
                    oldRating = rs.getInt("rating");
                }
                
                prepStmt = con.prepareStatement(CHECKIFREVIEWED_QUERY);
                prepStmt.setString(1, userName);
                prepStmt.setString(2, album_id);
                ResultSet rs2 = prepStmt.executeQuery();   //////// check if the user has reviewed the song before
                if(rs2.next()){
                    reviewed=true;
                }
                
            } catch(Exception e) {
                System.out.println("Exception in finding user's rating/reviews");
                e.printStackTrace();
            }
            
            try {
                if(!rated){   ///just insert the new rating value
                PreparedStatement prepStmt = con.prepareStatement(RATE_QUERY);
                prepStmt.setString(1, album_id);
                prepStmt.setString(2, userName);
                prepStmt.setString(3, rate);
                int t = prepStmt.executeUpdate(); 
                
                // update overall rating
                ResultSet r = d.execute("SELECT rating, rating_count FROM album WHERE album_id = " + album_id);
                float rating = 0;
                int numRatings = 0;
                if (r.next()) {
                    rating = r.getFloat("rating");
                    numRatings = r.getInt("rating_count");
                }
                rating = (rating * numRatings + Integer.parseInt(rate)) / (numRatings + 1);
                int res = d.update("UPDATE album SET rating = " + rating + " WHERE album_id = " + album_id);
                res = d.update("UPDATE album SET rating_count = rating_count + 1 WHERE album_id = " + album_id);
                
                out.println(rating);
                
                }
                else{
                    ////overwrite the rating
                    PreparedStatement prepStmt = con.prepareStatement(UPDATE_RATE_QUERY);
                    prepStmt.setString(1, rate);
                    prepStmt.setString(2, userName);
                    prepStmt.setString(3, album_id);
                    int t = prepStmt.executeUpdate();
                    
                    // update overall rating
                    ResultSet r = d.execute("SELECT rating, rating_count FROM album WHERE album_id = " + album_id);
                    float rating = 0;
                    int numRatings = 0;
                    if (r.next()) {
                        rating = r.getFloat("rating");
                        numRatings = r.getInt("rating_count");
                    }
                    rating = (rating * numRatings - oldRating + Integer.parseInt(rate)) / (numRatings);
                    int res = d.update("UPDATE album SET rating = " + rating + " WHERE album_id = " + album_id);
                    
                    out.println(rating);
                }                            
                
            
            }catch(Exception e){
                System.out.println("Exception in rating songs");
                e.printStackTrace();
            }
            finally {            
            out.close();
            }
            
            try {
                if(reviewed==false){    ////just insert the new review
                PreparedStatement prepStmt = con.prepareStatement(REVIEW_QUERY);
                prepStmt.setString(1, album_id);
                prepStmt.setString(2, userName);
                prepStmt.setString(3, review);
                int t = prepStmt.executeUpdate();
                }
                else{
                    /////overwrite the review
                    PreparedStatement prepStmt = con.prepareStatement(UPDATE_REVIEW_QUERY);
                    prepStmt.setString(1, review);
                    prepStmt.setString(2, userName);
                    prepStmt.setString(3, album_id);
                    int t = prepStmt.executeUpdate();
                    //UPDATE_REVIEW_QUERY = "insert into song_review review =? time=? WHERE username=? amd song_id=?";
                }
            
            
            }catch(Exception e){
                System.out.println("Exception in reviewing songs");
                e.printStackTrace();
            }
            finally {            
            out.close();
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
