<%@page import="authentication.*"%>
<%@page import="dbConnection.PSQLAccess"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="beans.*"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%! ArrayList<University> uniList = null; %>

<%
	uniList = PSQLAccess.retrieveUniversities();
%>
   
<p>If you have to add a participant you should select a university different from allUni</p>
    
<form action="ChangeUni" method="post">
    
	    <div>
		    <select name="university">
		    	<c:forEach var="uni" items="<%= uniList %>">
		    		<option>${uni.name}</option>
	    		</c:forEach>
		    </select>
		    <input type="hidden" name="redirection" value="add" />
		    <input type="submit" value="select university"/>
	    </div>
    
    </form>