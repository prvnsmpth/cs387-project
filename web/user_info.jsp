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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
    </head>
    <body>
        <%
            out.print(request.getAttribute("userFullName"));
            List<Map <String, String>> uploadedSongData = 
                    (List<Map <String, String>>) request.getAttribute("uploadedSongData");
        %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <table> 
            <thead>
                <tr>
                    <td>Title</td><td>Album</td><td>Artist</td>
                </tr>
            </thead>
            <tbody>
            <c:forEach items="${uploadedSongData}" var="song">
                <tr>
                    
                    <td>${song.song_title}</td>
                    <td>${song.album_title}</td>
                    <td>${song.artist_name}</td>
                    
                    <!--
                    /*<c:forEach items="${song}" var="attr">
                        <td></td><td>${attr.key}</td>
                        <td>${attr.value}</td>
                    </c:forEach>
                    -->
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </body>
</html>
