<%-- 
    Document   : upload
    Created on : 18 Oct, 2012, 2:11:18 PM
    Author     : praveen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    public String makeRow(String label, String type, String name, String value)
    {
        return "<tr>"
                + "<td align='right'>"
                + "<label for='" + name + "'>" + label + "</label>"
                + "</td>"
                + "<td>"
                + "<input type='" + type + "' name='" + name + "' value='" + value + "'/>"
                + "</td>"
                + "</tr>";
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/global.css" type="text/css" />
        <!-- <link rel="stylesheet" href="css/jquery.fileupload-ui.css" type="text/css" /> -->
        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="js/jquery.ui.widget.js"></script>
        <script type="text/javascript" src="js/jquery.iframe-transport.js"></script>
        <script type="text/javascript" src="js/jquery.fileupload.js"></script>
        <script type="text/javascript" src="js/upload.js"></script>
        <title>Upload</title>
    </head>
    <body>
        <div id="fileupload" align="center">
        <form method="post" enctype="multipart/form-data" action="UploadMusic">
            <div class="fileupload-buttonbar">
            <label class="fileinput-button">
            <span>Upload</span>
            <input type="file" name="files[]" multiple>
            <input type="submit" value="Upload" />
            </label>
            </div>
        </form>
        <div class="fileupload-content">
            <table class="files"></table>
        </div>
        </div>
    </body>
</html>
