<%@page import="authentication.*"%>
<%@page import="beans.*"%>
<%@page import="dbConnection.PSQLAccess"%>
<%@page import="java.util.ArrayList"%> 

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%!ArrayList<User> userList;%>
<%!User user;%>

<%
	userList = PSQLAccess.retrieveUsers();
user = (User)request.getSession().getAttribute("user");
%>

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
		<th>username</th>
		<th>password</th>
		<th>name</th>
	    <th>surname</th>
	    <th>email</th>
	    <th>university</th>
	    <th>rights</th>
	</tr>
  	
  	<c:forEach var="u" items="<%= userList%>">
  	<tr>
	    <td>${u.username }</td>
	    <c:if test="<%=user.getRights() == Login.ADMINISTRATOR_RIGHTS %>">
	    	<td>${u.password }</td>
	    </c:if>
	   	<c:if test="<%=user.getRights() == Login.MANAGER_RIGHTS %>">
	   		
	   		<c:choose>
		   		<c:when test="${u.rights == 2}">
		   			<td>${u.password }</td>
		   		</c:when>
		   		<c:otherwise>
					<td>&nbsp;</td>
		   		</c:otherwise>		   		
	   		</c:choose>
	   	
	   	
	   	
	   		<c:if test="${u.rights == 2}">
	    		
	    	</c:if>
	    </c:if>

    	
   	 	<td>${u.name }</td>
 	   	<td>${u.surname }</td>
	   	<td>${u.email }</td>
  	  	<td>${u.university }</td>
  	  	<td>${u.rightsString } </td>
  	  	
  	  	<!-- if the user is ad admin can edit everyone -->
		<c:if test="<%=user.getRights() == Login.ADMINISTRATOR_RIGHTS%>">
			<td>
				<form action="EditUser" method="get">
					<ins>
						<input type="hidden" name="username" value="${u.username }" />
						<input type="submit" value="edit"/>
					</ins>
				</form>
			</td>
			<td>
				<form action="DeleteUser" method="post" id="${u.username }" onsubmit="return confirm_deletion()" >
					<ins>
						<input type="hidden" name="username" value="${u.username }" />
						<input type="submit" value="delete" />
					</ins>
				</form>
			</td>	
		</c:if>
		
		
		<!-- if the user is a manager can edit only collaborators -->
		<c:if test="<%=user.getRights() == Login.MANAGER_RIGHTS %>">
			<c:if test="${u.rights == 2}">
				<td>
					<form action="EditUser" method="get">
						<ins>
							<input type="hidden" name="username" value="${u.username }" />
							<input type="submit" value="edit"/>
						</ins>
					</form>
				</td>
				<td>
					<form action="DeleteUser" method="post" id="${u.username }" onsubmit="return confirm_deletion()" >
						<ins>
							<input type="hidden" name="username" value="${u.username }" />
							<input type="submit" value="delete" />
						</ins>
					</form>
				</td>
			</c:if>
		</c:if>
		
		
  	</tr>
	</c:forEach>
  
</table>