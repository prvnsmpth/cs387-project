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
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "UserInfo", urlPatterns = {"/UserInfo"})
public class UserInfo extends HttpServlet {
    
    private static final String USER_QUERY = "select name from user where username = ?;";
    private static final String UPLOAD_QUERY = "select song.song_id as song_id, song.title as song_title, song.album_id as album_id, song.upload_time as upload_time, album.title as album_title, album.artist_id as artist_id, artist.name as artist_name from song left join album on song.album_id = album.album_id left join artist on album.artist_id = artist.artist_id where song.uploaded_by = ? order by upload_time desc;";
    private static final String DOWNLOAD_QUERY = "select song.song_id as song_id, song.title as song_title, song.album_id as album_id, album.title as album_title, album.artist_id as artist_id, artist.name as artist_name, download.time as download_time from download left join song on download.song_id = song.song_id left join album on song.album_id = album.album_id left join artist on album.artist_id = artist.artist_id where download.username = ? order by download_time desc;";    
    private static final String SRATING_QUERY = "select song.song_id as song_id, song.title as song_title, song.album_id as album_id, album.title as album_title, album.artist_id as artist_id, artist.name as artist_name, song_rating.rating as rating from song_rating left join song on song_rating.song_id = song.song_id left join album on song.album_id = album.album_id left join artist on album.artist_id = artist.artist_id where song_rating.username = ? order by song.downloads desc;";
    private static final String SREVIEW_QUERY = "select song.song_id as song_id, song.title as song_title, song.album_id as album_id, album.title as album_title, album.artist_id as artist_id, artist.name as artist_name, song_review.review from song_review left join song on song_review.song_id = song.song_id left join album on song.album_id = album.album_id left join artist on album.artist_id = artist.artist_id where song_review.username = ? order by song_review.time desc;";
    private static final String ARATING_QUERY = "select album.album_id as album_id, album.title as album_title, album.artist_id as artist_id, artist.name as artist_name, album_rating.rating as rating from album_rating left join album on album_rating.album_id = album.album_id left join artist on album.artist_id = artist.artist_id where album_rating.username = ?;";
    private static final String AREVIEW_QUERY = "select album.album_id as album_id, album.title as album_title, album.artist_id as artist_id, artist.name as artist_name, album_review.review as review from album_review left join album on album_review.album_id = album.album_id left join artist on album.artist_id = artist.artist_id where album_review.username = ? order by album_review.time desc;";

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
        try {
            /*
             * TODO output your page here. You may use following sample code.
             */
            Database d = new Database();
            d.connect();
            Connection con = null;
            String strUsername = request.getParameter("username").toString();
            String userFullName = null;  
            
            List<Map <String, String>> uploadedSongData;
            List<Map <String, String>> downloadedSongData;
            List<Map <String, String>> songRatingData;
            List<Map <String, String>> songReviewData;
            List<Map <String, String>> albumRatingData;
            List<Map <String, String>> albumReviewData;
            
            try {
                con = d.getConnection();
                System.out.println("DB connected");
                PreparedStatement prepStmt1 = con.prepareStatement(USER_QUERY);
                prepStmt1.setString(1, strUsername);
                ResultSet rs1 = prepStmt1.executeQuery();
                System.out.println("Query executed!");
                if(rs1.next()) {
                    userFullName = rs1.getString("name");
                }
                PreparedStatement prepStmt2 = con.prepareStatement(UPLOAD_QUERY);
                prepStmt2.setString(1, strUsername);
                ResultSet rs2 = prepStmt2.executeQuery();
                System.out.println("Query executed!");
                uploadedSongData = new ArrayList<Map <String, String>>();
                while(rs2.next()) {    
                    Map<String, String> info = new HashMap<String, String>();                    
                    info.put("song_id", rs2.getString("song_id"));
                    info.put("song_title", rs2.getString("song_title"));
                    info.put("album_id", rs2.getString("album_id"));
                    info.put("upload_time", rs2.getString("upload_time"));
                    info.put("album_title", rs2.getString("album_title"));
                    info.put("artist_id", rs2.getString("artist_id"));
                    info.put("artist_name", rs2.getString("artist_name"));
                    uploadedSongData.add(info);                    
                }
                PreparedStatement prepStmt3 = con.prepareStatement(DOWNLOAD_QUERY);
                prepStmt3.setString(1, strUsername);
                ResultSet rs3 = prepStmt3.executeQuery();
                System.out.println("Query executed!");
                downloadedSongData = new ArrayList<Map <String, String>>();
                while(rs3.next()) {
                    Map<String, String> info = new HashMap<String, String>();                    
                    info.put("song_id", rs3.getString("song_id"));
                    info.put("song_title", rs3.getString("song_title"));
                    info.put("album_id", rs3.getString("album_id"));
                    info.put("upload_time", rs3.getString("download_time"));
                    info.put("album_title", rs3.getString("album_title"));
                    info.put("artist_id", rs3.getString("artist_id"));
                    info.put("artist_name", rs3.getString("artist_name"));
                    
                    downloadedSongData.add(info);
                }
                PreparedStatement prepStmt4 = con.prepareStatement(SRATING_QUERY);
                prepStmt4.setString(1, strUsername);
                ResultSet rs4 = prepStmt4.executeQuery();
                System.out.println("Query executed!");
                songRatingData = new ArrayList<Map <String, String>>();
                while(rs4.next()) {
                    Map<String, String> info = new HashMap<String, String>();
                    info.put("song_id", rs4.getString("song_id"));
                    info.put("song_title", rs4.getString("song_title"));
                    info.put("album_id", rs4.getString("album_id"));
                    info.put("album_title", rs4.getString("album_title"));
                    info.put("artist_id", rs4.getString("artist_id"));
                    info.put("artist_name", rs4.getString("artist_name"));
                    info.put("song_rating", rs4.getString("rating"));
                    
                    songRatingData.add(info);
                }
                PreparedStatement prepStmt5 = con.prepareStatement(SREVIEW_QUERY);
                prepStmt5.setString(1, strUsername);
                ResultSet rs5 = prepStmt5.executeQuery();
                System.out.println("Query executed!");
                songReviewData = new ArrayList<Map <String, String>>();
                while(rs5.next()) {
                    Map<String, String> info = new HashMap<String, String>();
                    info.put("song_id", rs5.getString("song_id"));
                    info.put("song_title", rs5.getString("song_title"));
                    info.put("album_id", rs5.getString("album_id"));
                    info.put("album_title", rs5.getString("album_title"));
                    info.put("artist_id", rs5.getString("artist_id"));
                    info.put("artist_name", rs5.getString("artist_name"));
                    info.put("song_review", rs5.getString("review"));
                    
                    songReviewData.add(info);
                }
                PreparedStatement prepStmt6 = con.prepareStatement(ARATING_QUERY);
                prepStmt6.setString(1, strUsername);
                ResultSet rs6 = prepStmt6.executeQuery();
                System.out.println("Query executed!");
                albumRatingData = new ArrayList<Map <String, String>>();
                while(rs6.next()) {
                    Map<String, String> info = new HashMap<String, String>();                   
                    info.put("album_id", rs6.getString("album_id"));
                    info.put("album_title", rs6.getString("album_title"));
                    info.put("artist_id", rs6.getString("artist_id"));
                    info.put("artist_name", rs6.getString("artist_name"));
                    info.put("album_rating", rs6.getString("rating"));
                    
                    albumRatingData.add(info);
                }
                PreparedStatement prepStmt7 = con.prepareStatement(AREVIEW_QUERY);
                prepStmt7.setString(1, strUsername);
                ResultSet rs7 = prepStmt7.executeQuery();
                System.out.println("Query executed!");
                albumReviewData = new ArrayList<Map <String, String>>();
                while(rs7.next()) {
                    Map<String, String> info = new HashMap<String, String>();                   
                    info.put("album_id", rs7.getString("album_id"));
                    info.put("album_title", rs7.getString("album_title"));
                    info.put("artist_id", rs7.getString("artist_id"));
                    info.put("artist_name", rs7.getString("artist_name"));
                    info.put("album_rating", rs7.getString("rating"));
                    
                    albumReviewData.add(info);
                }
                
                // prepare attributes to be passed
                request.setAttribute("userFullName", userFullName);
                request.setAttribute("uploadedSongData", uploadedSongData);
                request.setAttribute("downloadedSongData", downloadedSongData);
                request.setAttribute("songRatingData", songRatingData);
                request.setAttribute("albumRatingData", albumRatingData);
                request.setAttribute("songReviewData", songReviewData);
                request.setAttribute("albumReviewData", albumReviewData);
                
                RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/user_info.jsp");
                dispatch.forward(request, response);
                
            } 
            catch(Exception e) {
                System.out.println("UserInfo Error: "+ e.getMessage());
                e.printStackTrace();
            }
            finally {                 
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UserInfo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
