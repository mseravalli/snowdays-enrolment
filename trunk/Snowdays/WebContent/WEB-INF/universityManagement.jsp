<%@page import="authentication.*"%>
<%@page import="beans.*"%>
<%@page import="dbConnection.PSQLAccess"%>
<%@page import="java.util.ArrayList"%> 

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%!ArrayList<University> uniList; %>
<%!User user;%>

<%
	uniList = PSQLAccess.retrieveUniversities();
user = (User)request.getSession().getAttribute("user");
%>

<p>
Remember that you cannot delete a university if there are some users or participants enrolled.<br />
Moreover you cannot either edit or delete allUni.
</p>
<p class="error" >
<%

if(session.getAttribute("errorMessage") != null){
	out.println(session.getAttribute("errorMessage"));
	session.setAttribute("errorMessage", null);
}

%>
</p>

<table class="full_width">
	<tr>
		<th>name</th>
		<th>total participants</th>
		<th>total teams</th>
		<th>users</th>			
	</tr>
	
	<c:forEach var="uni" items="<%= uniList%>">
	<tr>
		<td>${uni.name }</td>	
		<td>${uni.currentPart } / ${uni.maxPart }</td>
		<td>${uni.maxTeams }</td>
		<td>${uni.numOfUsers }</td>		
		<c:if test="${uni.name != 'allUni' }">
			<td>
				<form action="EditUniversity" method="get">
					<ins>
						<input type="hidden" name="uniName" value="${uni.name }" />
						<input type="submit" value="edit"/>
					</ins>
				</form>
			</td>
			<td>
				<form action="DeleteUni" method="post" id="${uni.name }" onsubmit="return confirm_deletion()" >
					<ins>
						<input type="hidden" name="uniName" value="${uni.name }" />
						<input type="submit" value="delete" />
					</ins>
				</form>
			</td>	
		</c:if>
	</tr>
	</c:forEach>


</table>