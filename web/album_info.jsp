<%-- 
    Document   : album_info
    Created on : 30 Oct, 2012, 5:11:43 PM
    Author     : praveen
--%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>

<%
    Map <String, String> albumInfo = 
                    (Map <String, String>) request.getAttribute("albumInfo"); 
    List<Map <String, String>> review = 
                    (List<Map <String, String>>) request.getAttribute("review");
    List<Map <String, String>> songListing = 
                    (List<Map <String, String>>) request.getAttribute("songListing");        
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="css/global.css" type="text/css" />
        <link rel="stylesheet" href="css/layout.css" type="text/css" /> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=albumInfo.get("title")%> - <%=albumInfo.get("artist_name")%></title>        
    </head>
    <body>
        <div class="section">
            <div class="sec_header">Album Info</div>
            <div class="sec_content">
                <table>                
                    <tr>
                        <td align="right"><b>Title:<b></td>
                        <td><%= albumInfo.get("title") %></td>
                    </tr>
                    <tr>
                        <td align="right"><b>Artist:<b></td>
                        <td><%= albumInfo.get("artist_name") %></td>
                    </tr>
                    <tr>
                        <td align="right"><b>Year:<b></td>
                        <td><%= albumInfo.get("year") %></td>
                    </tr>
                    <tr>
                        <td align="right"><b>Rating:<b></td>
                        <td><%= albumInfo.get("rating") %></td>
                    </tr>
                     <tr>
                        <td colspan="2"><%= albumInfo.get("rating_count") %> people rated this album.</td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="section">
            <div class="sec_header">Songs on this album</div>
            <div class="sec_content">
                <table width="100%">
                    <thead>
                        <td>Title</td>
                        <td>Length</td>
                        <td>Downloads</td>
                        <td>Rating</td>
                        <td>Genre</td>
                        <td>Uploaded by</td>
                    </thead>
                <c:forEach items="${songListing}" var="song">
                        <tr>
                            <td>${song.title}</td>
                            <td>${song.length}</td>
                            <td>${song.downloads}</td>
                            <td>${song.rating}</td>
                            <td>${song.genre}</td>
                            <td>${song.uploaded_by}</td>
                        </tr>
                </c:forEach>
                </table>
            </div>
        </div>
    </body>
</html>
