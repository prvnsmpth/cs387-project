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
import java.sql.SQLException;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author praveen
 */
@WebServlet(name = "AlbumInfo", urlPatterns = {"/AlbumInfo"})
public class AlbumInfo extends HttpServlet {
    
    private static final String ALBUM_QUERY = "select * from album where album_id = ?;";
    private static final String SONG_QUERY = "select * from song where album_id = ?;";
    private static final String ARTIST_QUERY = "select name from artist where artist_id = ?;";
    private static final String REVIEWS_QUERY = "select * from album_review where album_id = ?;";

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
            /*
             * TODO output your page here. You may use following sample code.
             */
            
            String strAlbum_id = request.getParameter("album_id").toString();
            Database d = new Database();
            d.connect();
            Connection con = d.getConnection();
            int artist_id = 0;
            
            Map <String, String> albumInfo;
            List<Map<String, String>> songListing;
            List<Map<String, String>> reviews;

            try {
                PreparedStatement prepStmt1 = con.prepareStatement(ALBUM_QUERY);
                prepStmt1.setString(1, strAlbum_id);
                ResultSet rs1 = prepStmt1.executeQuery();
                albumInfo = new HashMap<String, String>();
                if(rs1.next()) {
                    albumInfo.put("id", rs1.getString("album_id"));
                    albumInfo.put("title", rs1.getString("title"));
                    albumInfo.put("artist_id", rs1.getString("artist_id"));
                    albumInfo.put("rating_count", rs1.getString("rating_count"));
                    albumInfo.put("rating", rs1.getString("rating"));
                    
                    // put artist_id in int to get artist name
                    artist_id = rs1.getInt("artist_id");
                }
                
                PreparedStatement prepStmt2 = con.prepareStatement(SONG_QUERY);
                prepStmt2.setString(1, strAlbum_id);
                ResultSet rs2 = prepStmt2.executeQuery();
                System.out.println("Query executed!");
                songListing = new ArrayList<Map<String, String>>();
                while(rs2.next()) {
                    Map<String, String> songInfo = new HashMap<String, String>();
                    songInfo.put("song_id", rs2.getString("song_id"));
                    songInfo.put("title", rs2.getString("title"));
                    String length = rs2.getString("length");
                    songInfo.put("length", rs2.getString("length"));
                    songInfo.put("downloads", rs2.getString("downloads"));
                    songInfo.put("rating", rs2.getString("rating"));
                    songInfo.put("genre", rs2.getString("genre"));
                    songInfo.put("uploaded_by", rs2.getString("uploaded_by"));
                    songInfo.put("upload_time", rs2.getString("upload_time"));
                    songInfo.put("rating_count", rs2.getString("rating_count"));
                    
                    songListing.add(songInfo);
                }
                PreparedStatement prepStmt3 = con.prepareStatement(ARTIST_QUERY);
                prepStmt3.setString(1, Integer.toString(artist_id));
                ResultSet rs3 = prepStmt3.executeQuery();
                if(rs3.next()) {
                    albumInfo.put("artist_name", rs3.getString("name"));
                }
                PreparedStatement prepStmt4 = con.prepareStatement(REVIEWS_QUERY);
                prepStmt4.setString(1, strAlbum_id);
                ResultSet rs4 = prepStmt4.executeQuery();
                reviews = new ArrayList<Map<String, String>>();
                while(rs4.next()) {   
                    Map<String, String> reviewInfo = new HashMap<String, String>();
                    reviewInfo.put("username", rs4.getString("username"));
                    reviewInfo.put("review", rs4.getString("review"));
                    reviewInfo.put("time", rs4.getString("time"));
                    reviews.add(reviewInfo);
                }
                
                // prepare attributes
                request.setAttribute("albumInfo", albumInfo);
                request.setAttribute("songListing", songListing);
                request.setAttribute("reviews", reviews);
                
                RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/album_info.jsp");
                dispatch.forward(request, response);
                

            } 
            catch(Exception e) {
                System.out.println("search_users: Error while searching users: "+e.getMessage());
            }
            finally {
                try {
                    con.close();
                } catch (SQLException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
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
