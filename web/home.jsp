<%-- 
    Document   : home
    Created on : 20 Oct, 2012, 5:02:56 PM
    Author     : praveen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%   
    Object user = session.getAttribute("username");
    if (user == null)
        response.sendRedirect("login.jsp");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
    </head>
    <body>
        <h1>Welcome, <% out.print((String) user); %></h1>        
    </body>
</html>
