<%-- 
    Document   : upload
    Created on : 18 Oct, 2012, 2:11:18 PM
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
        <link rel="stylesheet" href="css/global.css" type="text/css" />    
        <script type="text/javascript" src="js/jquery.js"></script>  
        <script type="text/javascript" src="js/upload.js"></script>   
        
        <title>Upload</title>
    </head>
    <body>
        <form id="fileupload" action="UploadMusic" method="POST" enctype="multipart/form-data">
            <input type="file" name="files[]" multiple />
            <input type="submit" value="Upload" />
        </form>                
    </body>
</html>
