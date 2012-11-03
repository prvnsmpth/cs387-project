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
        <script type="text/javascript" src="js/jquery.form.js"></script> 
        
        <!-- Bootstrap -->
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        
        <script type="text/javascript">
            $(function () {
                
                var fuform = $("#fileupload");
                var prog = $('.progress');
                var bar = prog.find('.bar');
                var status = prog.siblings('.status');               
                
                fuform.ajaxForm({
                    beforeSend : function() {
                        status.html('Uploading...');
                        prog.show();
                        return false;
                    },
                    uploadProgress: function(event, position, total, percentComplete) {
                        var percentVal = percentComplete + '%';
                        bar.width(percentVal);
                    },
                    complete: function() {
                        status.html('Upload complete.');
                        prog.removeClass('active');
                    } 
                })
                
                var browse = $('.fileinput-button').click(function () {
                    $(this).siblings('input[type=file]').click();
                });
            })
        </script>
        <style type="text/css">
            
            .bar {
                background: #333;
                width: 0%;
                height: 20px; 
                position: relative;
                border-radius: 3px 
            }
            .progress {
                width: 400px;
                position: relative;                                  
            }
            #content-wrap {
                margin: 100px auto; 
                width: 400px;
                padding: 40px;
                border: 1px solid #bbb;
                border-radius: 5px;
                background: #eee
            }
        </style>
        <title>Upload</title>
    </head>
    <body>
        <%@include file="navbar.html" %>
        <div id="content-wrap">
            <div style="font-weight: bold; font-size: 16px; margin: 20px 0px">Upload music files</div>
            <form id="fileupload" action="UploadMusic" method="POST" enctype="multipart/form-data">                                
                <span class="btn btn-success fileinput-button">
                    <i class="icon-plus icon-white"></i>
                    <span>Add files...</span>                    
                </span>
                <input type="file" name="files[]" multiple="" style="display: none">
                <button type="submit" class="btn btn-primary start">
                    <i class="icon-upload icon-white"></i>
                    <span>Start upload</span>
                </button>
            </form>  
            <div class="status"></div>
            <div class="progress progress-striped active" style="display: none">            
                <div class="bar"></div>
            </div>            
        </div>
    </body>
</html>
