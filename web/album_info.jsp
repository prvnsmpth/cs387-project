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
    List<Map <String, String>> reviews = 
                    (List<Map <String, String>>) request.getAttribute("reviews");         
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
        
        <!-- Bootstrap -->
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=albumInfo.get("title")%> - <%=albumInfo.get("artist_name")%></title>    
        <style type="text/css">
            
            a {
                text-decoration: none;
                color: #000;
            }
            
            a:hover {
                text-decoration: underline;
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
                                    setNewRating(responseText, __this);
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
                                    setNewRating(responseText, __this);
                                }
                            })
                        }
                    })
                    
                })
                
                rateBox.mouseleave(function () {
                    $(this).find('.starred').removeClass('starred');
                })
                
                function setNewRating(rating, _this) {
                    var row = _this.parent().parent().parent();
                    var _rating = new Number(rating);
                    rating = _rating.toPrecision(3);
                    if (row.children('td').size() == 2) {
                        // album
                        var nRow = row.next('tr');
                        var ovRate = nRow.find('td.overall_rating');
                        ovRate.html(rating);
                    } else {
                        // song listing
                        var ovRate = row.find('td.overall_rating');
                        ovRate.html(rating);
                    }
                }                                
                
                var reviewForm = $('#reviewForm');
                reviewForm.submit(function () {
                    var _this = $(this);
                    var review = _this.find('textarea[name=review]').val();
                    if (review != "") {
                        
                        $.ajax({
                            url : 'RateAlbum',
                            data : {
                                'album_id' : _this.find('input[name=album_id]').val(),
                                'review' : review
                            },
                            type : 'POST',
                            success : function() {
                                alert('POSTED!');
                            }
                        })
                        
                    } else {
                        $('.error').show();
                    }
                    
                    return false;
                })
                               
            })
        </script>
    </head>
    <body>        
        <%@include file="navbar.html" %>
        <div id="content-wrap">
        <div class="section" style="display: inline-block">
            <div class="sec_header">Album Info</div>                       
            <img src="img/albumcover.jpg" width="300" height="300" />
            <div style="font-weight: bold; background: #333; color: #ddd; padding: 5px 20px"><span style="font-size: 20px; color: #f07b0b"><%= albumInfo.get("song_count") %></span> SONGS</div>
            <div class="sec_content">                
                <table class="table table-striped" style="margin: 0px">                
                    <tr>
                        <td align="right"><b>Title<b></td>
                        <td><%= albumInfo.get("title") %></td>
                    </tr>
                    <tr>
                        <td align="right"><b>Artist<b></td>
                        <td><%= albumInfo.get("artist_name") %></td>
                    </tr>
                    <tr>
                        <td align="right"><b>Year<b></td>
                        <td><%= albumInfo.get("year") %></td>
                    </tr>
                    <tr>
                        <td align="right"><b>Your Rating<b></td>
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
                        <td align="right"><b>Overall Rating<b></td>
                        <td class="overall_rating"><%= albumInfo.get("rating") %></td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="section" style="display: inline-block; vertical-align: top">
            <div class="sec_header">Reviews</div>
            <div class="sec_content" style="padding: 20px">                
                <c:forEach items="${reviews}" var="_review">
                    <div class="review well">
                        <div class="review-by">
                            <a style="font-size: 14px; color: #777; font-weight: bold" href="UserInfo?username=${_review.username}">${_review.username}</a>
                        </div>
                        <div class="review-text">${_review.review}</div>
                        <div class="review-time"><b>${_review.time}</b></div>
                    </div>
                </c:forEach> 
                <div id="write-review">
                    <div style="font-weight: bold; font-size: 16px; margin-bottom: 10px">Write a review</div>
                    <form id="reviewForm" method="POST">
                        <input type="hidden" name="album_id" value="<%= albumInfo.get("id") %>" />
                        <textarea name="review" style="resize: none; width: 500px; height: 100px"></textarea><br />
                        <input type="submit" value="Post" /><span class="error" style="display:none;position:relative">Write a review first</span>
                    </form>
                </div>
            </div>
        </div>
        <div class="section">
            <div class="sec_header">Songs on this album</div>
            <div class="sec_content">
                <table width="100%" class="table table-striped">
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
                            <td align="center" class="overall_rating">${song.rating}</td>
                            <td>${song.genre}</td>
                            <td><a href="UserInfo?username=${song.uploaded_by}">${song.uploaded_by}</a></td>
                        </tr>
                </c:forEach>                
                </table>                
            </div>
        </div>
        </div>
    </body>
</html>
