<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@page import="beans.*"%>
<%@page import="authentication.Login"%>


<%!User user = null;%>
<%!String styleSheet = "style.css"; %>

<% 
user = (User)request.getSession().getAttribute("user");
%>

<%! boolean canAccess = false; %>
<%! String pageTitle = Login.RIGHTS_ERROR; %>
<%! String pageToImport = "WEB-INF/blank.jsp"; %>

<%
if(request.getSession().getAttribute("user") != null){
    user = (User)request.getSession().getAttribute("user");
    if (user.getRights() == Login.ADMINISTRATOR_RIGHTS){
        canAccess = true;
        pageTitle = "Query the Database";
        pageToImport = "WEB-INF/badgePrinting.jsp";
    } else {
    	canAccess = false;
    	pageTitle = Login.RIGHTS_ERROR;
    	pageToImport = "WEB-INF/blank.jsp";
    	RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp?errorType="+Login.RIGHTS_ERROR);
    	dispatcher.forward(request, response);
    }
    
} else {
	canAccess = false;
	pageTitle = Login.RIGHTS_ERROR;
	pageToImport = "WEB-INF/blank.jsp";
	RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp?errorType="+Login.RIGHTS_ERROR);
	dispatcher.forward(request, response);
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
        
    <link rel="icon" href="style/res/favicon.png" /> 
    <link rel="shortcut icon" href="style/res/favicon.png" />   
        
    <title>Snowdays</title> 
</head> 


<body>

	<jsp:include page = "<%= pageToImport %>" />

</body>
</html>