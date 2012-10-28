<%-- 
    Document   : new_user
    Created on : 20 Oct, 2012, 2:11:26 PM
    Author     : praveen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="app.FormHelper"  %>
<%    
    FormHelper fh = new FormHelper();  
    
    Object user = session.getAttribute("username");
    if (user != null)
        response.sendRedirect("home.jsp");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/global.css" type="text/css" />  
        <script type="text/javascript" src="js/jquery.js"></script> 
        <title>Login</title>
        <script type="text/javascript">
            $(function () {
                var form = $("#new_user_form");
                var nm_error = "<span class='error'>Passwords don't match!</span>";
                var match_ok = "<span class='allclear'>Passwords match!</span>";
                var empty_error = "<span class='error'>Required field</span>";
                var passwd = form.find("input[name='password']");
                var repasswd = form.find("input[name='re-passwd']");  
                var username = form.find("input[name='username']");  
                var name = form.find("input[name='name']");  
                
                repasswd.keyup(function () {                    
                    if (passwd.val() == repasswd.val()) {
                        $('span.allclear').remove();
                        $('span.error').remove();
                        repasswd.parent().append(match_ok);
                        var pos = repasswd.position();
                        var h = repasswd.height();
                        var w = repasswd.outerWidth();
                        $('span.allclear').offset({top: pos.top + (h / 2), left: pos.left + w + 10});
                    } else {
                        $('span.allclear').remove();                        
                    }
                })
                
                passwd.keyup(function () {                                               
                    if (passwd.val() == repasswd.val() 
                        && passwd.val() != "") {
                        $('span.allclear').remove();
                        $('span.error').remove();
                        repasswd.parent().append(match_ok);
                        var pos = repasswd.position();
                        var h = repasswd.height();
                        var w = repasswd.outerWidth();
                        $('span.allclear').position({top: pos.top, left: pos.left});
                    } else {
                        $('span.allclear').remove();                        
                    }
                })
                
                form.submit(function () {
                    if (passwd.val() != repasswd.val()) {
                        repasswd.parent().append(nm_error);   
                        var pos = repasswd.position();
                        var h = repasswd.height();
                        var w = repasswd.outerWidth();
                        $('span.error').offset({top: pos.top + (h / 2), left: pos.left + w + 10});
                        return false;
                    } 
                    
                    if (username.val() == "") {
                        username.parent().append(empty_error);   
                        var pos = username.position();
                        var h = username.height();
                        var w = username.outerWidth();
                        $('span.error').offset({top: pos.top + (h / 2), left: pos.left + w + 10});
                        return false;
                    }
                    
                    if (name.val() == "") {
                        name.parent().append(empty_error);   
                        var pos = name.position();
                        var h = name.height();
                        var w = name.outerWidth();
                        $('span.error').offset({top: pos.top + (h / 2), left: pos.left + w + 10});
                        return false;
                    }
                })
                
            })
        </script>
    </head>
    <body> 
         <div id="login_wrap"  align="center">
            <h3>Login</h3>
            <form action="Login" method="POST">
                <table>
                <%
                    out.print(fh.makeRow("Username: ", "text", "username", ""));
                    out.print(fh.makeRow("Password: ", "password", "password", ""));
                    out.print(fh.makeRow("", "submit", "submitb", "Login"));
                    
                %>
                </table>
            </form>
        </div>
        <div id="new_user_wrap" align="center">
            <h3>New Account</h3>
            <form id="new_user_form" action="NewAccount" method="POST">
                <table>
                <%
                    out.print(fh.makeRow("Full Name: ", "text", "name", ""));
                    out.print(fh.makeRow("Username: ", "text", "username", ""));
                    out.print(fh.makeRow("Password: ", "password", "password", ""));
                    out.print(fh.makeRow("Re-enter password: ", "password", "re-passwd", ""));
                    out.print(fh.makeRow("", "submit", "submitb", "Sign Up"));
                %>
                </table>
            </form>
        </div>                      
    </body>
</html>
