<%@page import="java.util.ArrayList"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form action="DBQuery" method="post">
<p>
<textarea cols="60" rows="10" name="query"></textarea><br />
<input type="submit" />
</p>
</form>

<%! ArrayList<String[]> resultSet = null; %>

<h3>Results</h3>

<p class="error" >
<%

if(request.getSession().getAttribute("errorMessage") != null){
	out.println(request.getSession().getAttribute("errorMessage"));
	request.getSession().setAttribute("errorMessage", null);
}

resultSet = (ArrayList<String[]>)request.getSession().getAttribute("queryResult");
%>

</p>

<table class="full_width">
<c:forEach var="r" items="<%= resultSet %>">
	<tr>
	<c:forEach var="element" items="${r}">
		<td>
		${element }
		</td>
	</c:forEach>
	</tr>
</c:forEach>

</table>



<%
request.getSession().setAttribute("queryResult", null);
%>
