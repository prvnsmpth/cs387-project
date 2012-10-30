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
        <style type="text/css">
            .star {
                background: url("img/star.png") repeat-x;
                background-position: 0 -27px;
                height: 13px; width: 70px
            } 
            .starred {
                background: url("img/star.png") repeat-x;
                background-position: 0 -68px;
                height: 13px; width: 70px;
                position: absolute
            }
            .permstar {
                background: url("img/star.png") repeat-x;
                background-position: 0 -40px;
                height: 13px; width: 70px;
                position: absolute
            }
        </style>
        <script type="text/javascript" src="js/jquery.js"></script> 
        <script type="text/javascript">
            $(function () {
                var rateBox = $('div.star');
                
                // set user rating
                rateBox.each(function () {                    
                    var pos = $(this).position();
                    var user_rating = $(this).find('input[name="user_rating"]').val();
                    if (user_rating != null && user_rating != undefined) {
                        var div = $(this).find('div:first').addClass('permstar');
                        if (user_rating == 'null')
                            user_rating = 0;
                        var divWid = user_rating * 14;
                        div.width(divWid);
                        div.position(pos);
                    }
                })
                
                rateBox.mousemove(function (e) {
                    var _this = $(this);
                    var pos = _this.position();
                    var xpos = e.pageX - pos.left;                    
                    var boxWid = _this.width();
                    var starWid = boxWid / 5;
                    var rating = Math.floor(xpos / starWid + 1);
                    var starredWid = rating * starWid;
                    
                    _this.find('div:eq(1)').addClass('starred');
                    var starred = _this.find('.starred');
                    starred.width(starredWid);
                    starred.position(pos);
                             
                    starred.unbind('click');
                    starred.click(function (e) {             
                        
                        // send rating
                        var song_id = $(this).siblings('input[name="song_id"]').val();                        
                        var __this = $(this);
                        var newWid = rating * 14;
                        //alert(song_id);
                        if (song_id != null && song_id != "" && song_id != undefined) {
                            $.ajax({
                                url : 'RateSong',
                                data : {
                                    'song_id' : song_id,
                                    'rate' : rating
                                },
                                type : 'POST',
                                success : function(responseText) {
                                    __this.siblings('div.permstar').width(newWid);
                                }
                            })
                        }
                        
                        // check if album rating; if so, send rating
                        var album_id = $(this).siblings('input[name="album_id"]').val();                        
                        //alert(album_id);
                        if (album_id != null && album_id != "" && album_id != undefined) {
                            $.ajax({
                                url : 'RateAlbum',
                                data : {
                                    'album_id' : album_id,
                                    'rate' : rating
                                },
                                type : 'POST',
                                success : function(responseText) {
                                    __this.siblings('div.permstar').width(newWid);
                                }
                            })
                        }
                    })
                    
                })
                
                rateBox.mouseleave(function () {
                    $(this).find('.starred').removeClass('starred');
                })
                               
            })
        </script>
    </head>
    <body>
        <div class="section">
            <div class="sec_header">Album Info</div>
            <img src="img/albumcover.jpg" width="300" height="300" />
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
                        <td align="right"><b>Your Rating:<b></td>
                        <td>
                            
                            <div class="star">
                                <input type="hidden" name="album_id" value="<%= albumInfo.get("id") %>">
                                <input type="hidden" name="user_rating" value="<%= albumInfo.get("user_rating") %>">
                                <div></div>
                                <div></div>
                            </div>
                            
                        </td>
                    </tr>
                    <tr>
                        <td align="right"><b>Overall Rating:<b></td>
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
                        <td align="center">Length</td>
                        <td align="center">Downloads</td>
                        <td align="center">Your rating</td>
                        <td align="center">Overall rating</td>
                        <td>Genre</td>
                        <td>Uploaded by</td>
                    </thead>
                <c:forEach items="${songListing}" var="song">
                        <tr>
                            <td>${song.title}</td>
                            <td align="center">${song.length}</td>
                            <td align="center">${song.downloads}</td>
                            <td align="center">
                                <div class="star">
                                    <input type="hidden" name="song_id" value="${song.song_id}">
                                    <input type="hidden" name="user_rating" value="${song.user_rating}">
                                    <div></div>
                                    <div></div>
                                </div>
                            </td>
                            <td align="center">${song.rating}</td>
                            <td>${song.genre}</td>
                            <td>${song.uploaded_by}</td>
                        </tr>
                </c:forEach>
                </table>
            </div>
        </div>
    </body>
</html>
