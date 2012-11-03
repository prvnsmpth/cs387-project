<%-- 
    Document   : user_info
    Created on : 20 Oct, 2012, 11:00:53 PM
    Author     : praveen
--%>

<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="css/global.css" type="text/css" />    
        <link rel="stylesheet" href="css/layout.css" type="text/css" />    
        <link rel="stylesheet" href="css/layout.css" type="text/css" /> 
        
        <!-- jquery -->
        <script type="text/javascript" src="js/jquery.js"></script>
        
        <!-- Bootstrap -->
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Info</title>
        <style type="text/css">
            a {
                text-decoration: none;
                color: #000;
            }
            
            a:hover {
                text-decoration: underline;
            }
            
        </style>
    </head>
    <body>
        <%@include file="navbar.html" %>
        <div id="content-wrap">
        <%
            String userFullName = request.getAttribute("userFullName").toString();
            List<Map <String, String>> uploadedSongData = 
                    (List<Map <String, String>>) request.getAttribute("uploadedSongData");            
        %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <div class="section">
            <div class="sec_header"><%=userFullName%></div>
            <div class="sec_content">
                Uploaded <%=uploadedSongData.size()%> songs.
            </div>
        </div>
        <div class="section">
            <div class="sec_header">Uploaded Songs</div>
            <div class="sec_content">
                <% 
                        if (uploadedSongData.size() > 0) {                        
                %>
                <table width="100%" class="table table-striped"> 
                    <thead>
                        <tr>
                            <td>Title</td><td>Album</td><td>Artist</td>
                        </tr>
                    </thead>            
                    <tbody>
                    <c:forEach items="${uploadedSongData}" var="song">
                        <tr>
                            <td>${song.song_title}</td>
                            <td><a href="AlbumInfo?album_id=${song.album_id}">${song.album_title}</a></td>
                            <td>${song.artist_name}</td>
                        </tr>
                    </c:forEach>
                            
                    <%
                        } else {
                    %>
                            <div style="margin: 0 auto; width: 100%; text-align: center; padding: 40px; font-size: 20px; font-weight: bold">
                                No songs found.
                            </div>
                    <%
                        }
                    %>
                    </tbody>
                </table>
             </div>
        </div>
        </div>
    </body>
</html>
