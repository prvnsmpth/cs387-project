/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import org.tritonus.share.sampled.TAudioFormat;
import org.tritonus.share.sampled.file.TAudioFileFormat;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.*;

/**
 *
 * @author praveen
 */
@WebServlet(name = "UploadMusic", urlPatterns = {"/UploadMusic"})
public class UploadMusic extends HttpServlet {

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
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                try {                    
                    String savePath = saveFile(request, response);                    
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage()); e.printStackTrace();
                }
            }
            //String songName = request.getParameter("songName").toString();
            //String songArtist = request.getParameter("songArtist").toString();            
            
        } finally {            
            out.close();
        }
    }
    
    protected String saveFile(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        String username = null;
        if (user == null)
            response.sendRedirect("login.jsp");
        else 
            username = (String) user;
        
        String savePath = getServletContext().getRealPath("/");       
        PrintWriter out = response.getWriter();
                
        File uploadedFile = null;
        try {            
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                    
                } else {
                    // Process form file field (input type="file").
                    String fieldName = item.getFieldName();
                    String fileName = item.getName();
                    String contentType = item.getContentType();
                    boolean isInMemory = item.isInMemory();
                    long sizeInBytes = item.getSize();
                    
                    File path = new File(savePath + "/music");
                    if (!path.exists()) {
                        boolean status = path.mkdirs();                        
                    }                                        
                    uploadedFile = new File(path + "/" + fileName);
                    item.write(uploadedFile);
                    out.println("Uploaded Filename: " + uploadedFile.getAbsolutePath() + "<br>");    
                    saveMusic(uploadedFile.getAbsolutePath(), out, username);
                }
            }
        } catch (FileUploadException e) {
            System.out.println("Cannot parse multipart request." );
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error: ");
            e.printStackTrace();
        }

        return uploadedFile.getAbsolutePath();
    }
    
    private void saveMusic(String musicFilePath, PrintWriter out, String username) 
    {
        try {                
            File f = new File(musicFilePath);
            AudioFileFormat baseFileFormat = null;
            AudioFormat baseFormat = null;
            baseFileFormat = AudioSystem.getAudioFileFormat(f);
            baseFormat = baseFileFormat.getFormat();
            
            // TAudioFileFormat properties
            if (baseFileFormat instanceof TAudioFileFormat)
            {
                Map properties = ((TAudioFileFormat)baseFileFormat).properties();
                
                String _title = properties.get("title") == null ? "" 
                        : (String) properties.get("title");
                String _album = properties.get("album") == null ? "" 
                        : (String) properties.get("album");
                String _artist = properties.get("author") == null ? "" 
                        : (String) properties.get("author");
                //String _genre = properties.get("mp3.id3tag.genre") == null ? "" 
                  //      : (String) properties.get("mp3.id3tag.genre");
                String _genre = "";
                
                            
                String length = properties.get("duration").toString();
                Integer len = new Integer(length);
                len /= 1000000;
                Iterator iterator = properties.keySet().iterator();
 
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    String value = properties.get(key).toString();

                    out.println("<b>" + key + "</b>" + " " + value + "<br />");
                }
                out.print("<hr>");
                
                Map<String, String> fields = new HashMap();
                fields.put("title", _title);
                fields.put("downloads", "0");
                fields.put("rating", "0");
                fields.put("genre", _genre);
                String dur =  "00:" + Integer.toString(len / 60) + ":" + Integer.toString(len % 60);
                fields.put("length", dur);
                
                // get logged in user here                
                fields.put("uploaded_by", username);
                
                int artist_id = getArtistId(_artist);
                if (artist_id != -1) {
                    int album_id = getAlbumId(_album);                
                    if (album_id != -1) {
                        fields.put("album_id", Integer.toString(album_id));
                        putMusic(fields); // album exists
                    } else {
                        // new album
                        Map<String, String> fd = new HashMap();
                        fd.put("title", _album);
                        fd.put("year", (String) properties.get("date"));
                        fd.put("artist_id", Integer.toString(artist_id));
                        int nal_id = putAlbum(fd);
                        
                        // new song
                        fields.put("album_id", Integer.toString(nal_id));
                        putMusic(fields);
                    }    
                } else {
                    // insert artist, album and song
                    Map<String, String> nart = new HashMap();
                    nart.put("name", _artist);
                    int na_id = putArtist(nart);
                    
                    nart.clear();
                    nart.put("title", _album);
                    nart.put("year", (String) properties.get("date"));
                    nart.put("artist_id", Integer.toString(na_id));
                    int nal_id = putAlbum(nart);
                    
                    fields.put("album_id", Integer.toString(nal_id));
                    putMusic(fields);                    
                }                               
                                
            }
            
            // TAudioFormat properties
            if (baseFormat instanceof TAudioFormat) {
                Map properties = ((TAudioFormat)baseFormat).properties();
                String key = "bitrate";
                Integer val = (Integer) properties.get(key);                                
                                
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage()); e.printStackTrace();
        }
    }        
    
    private int getAlbumId(String album_title)
    {
        Database d = new Database();
        d.connect();
        ResultSet rs = d.execute("SELECT album_id FROM album WHERE title = '" + album_title +"'");
        int ret = -1;
        try {
            if (rs.next()) 
                ret = rs.getInt("album_id");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage()); e.printStackTrace();
        }
        
        return ret;
    }
    
    private int getArtistId(String artist)
    {
        Database d = new Database();
        d.connect();
        ResultSet rs = d.execute("SELECT artist_id FROM artist WHERE name = '" + artist +"'");
        int ret = -1;
        try {
            if (rs.next()) 
                ret = rs.getInt("artist_id");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage()); e.printStackTrace();
        }
        
        return ret;
    }
    
    private int putAlbum(Map fields)
    {
        Database d = new Database();
        d.connect();   
        int t = d.insert("album", fields);
        d.closeConnection();
        return t;
    }
    
    private int putArtist(Map fields)
    {
        Database d = new Database();
        d.connect();   
        int t = d.insert("artist", fields);
        d.closeConnection();
        return t;
    }
    
    private int putMusic(Map fields) 
    {
        Database d = new Database();
        d.connect();   
        int t = d.insert("song", fields);
        d.closeConnection();
        return t;
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
