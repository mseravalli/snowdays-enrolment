<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@page import="beans.*"%>

<%!User user = null;%>
<%!String styleSheet = "style.css"; %>

<% 
user = (User)request.getSession().getAttribute("user");

if(user != null){
	if((user.getRights() == Login.ADMINISTRATOR_RIGHTS ||
			user.getRights() == Login.MANAGER_RIGHTS)){
		styleSheet = "style_admin.css";
	}
	else {
		styleSheet = "style.css";
	}
	
}
else {
	styleSheet = "style.css";
}

%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
 
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" /> 
    <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=iso-8859-1" /> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    
    <script type="text/javascript" src="participantValidator.js"></script>
    <script type="text/javascript" src="confirmDeletion.js"></script>
        
    <link rel="stylesheet" type="text/css" href="style/<%= styleSheet %>" /> 
    
    <link rel="icon" href="style/res/favicon.png" /> 
    <link rel="shortcut icon" href="style/res/favicon.png" />   
        
    <title>Snowdays</title> 
</head> 
 
<body> 
<div id="header"> 
    <div id="header_inner"> 
        
        <h1>
        <a href="index.jsp"><img src="style/res/logo.png" alt="logo" /> Snowdays 2011</a>
        </h1> 
  </div> 
  
  <div id="header_info"> 
    
        <div class="left">
            <%
            
            	GregorianCalendar today = new GregorianCalendar();
                        GregorianCalendar eventDate = new GregorianCalendar(2011, 3, 24);
                        int days = (int)((eventDate.getTimeInMillis() - today.getTimeInMillis())/(1000*3600*24));
                        out.print(days);
            %>
            days before the event
        </div> 
        
        
        
    <div class="right">    

    
        <form action="Login" method="post">
            <div>
                
                <%
					if(user != null){
                       	String output = String.format("Welcome %s, you are working on %s ", 
						user.getUsername(), user.getUniversity());
                        out.print(output + " - <a href=\"Login?logout=true\">Logout</a>");
					}
					else {
						out.print("username: <input type=\"text\" name=\"usr\" size=\"10\"/> ");
						out.print("password: <input type=\"password\" name=\"psw\" size=\"10\"/> ");
						out.print("<input type=\"submit\" value=\"login\"/>");
					}
				%>
                
        
            </div>
        </form>
    </div> 
    
  </div> 
    
</div>

<div id="container"> 


<%@ include file="leftCol.jsp" %>
<%@ include file="rightCol.jsp" %>
