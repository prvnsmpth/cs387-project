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

import java.sql.*;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
                    System.out.println("Error: " + e.getMessage());
                }
            }
            //String songName = request.getParameter("songName").toString();
            //String songArtist = request.getParameter("songArtist").toString();
            System.out.println("Hello");
            
        } finally {            
            out.close();
        }
    }
    
    protected String saveFile(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
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
                    saveMusic(uploadedFile.getAbsolutePath(), out);
                }
            }
        } catch (FileUploadException e) {
            System.out.println("Cannot parse multipart request." + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return uploadedFile.getAbsolutePath();
    }
    
    private void saveMusic(String musicFilePath, PrintWriter out) 
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
                
                String title = (String) properties.get("title");
                String artist = (String) properties.get("author");
                String album = (String) properties.get("album");
                String genre = (String) properties.get("genre");
                int length = getDurationWithMp3Spi(f);
                
                /*String key = "author";
                String val = (String) properties.get(key);
                System.out.println(val);
                key = "mp3.id3tag.v2";
                InputStream tag= (InputStream) properties.get(key);*/
            }
            
            // TAudioFormat properties
            if (baseFormat instanceof TAudioFormat) {
                Map properties = ((TAudioFormat)baseFormat).properties();
                String key = "bitrate";
                Integer val = (Integer) properties.get(key);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static int getDurationWithMp3Spi(File file) {
        int mili = 0;
        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
            String key = "duration";
            Long microseconds = (Long) properties.get(key);
            mili = (int) (microseconds / 1000);
        } catch (Exception e) {
            System.out.println("Error in calculation length:" + e.getMessage());
        }
        return (mili / 1000) % 60;
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
