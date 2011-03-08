<%@ page import="beans.*"%>
<%@page import="dbConnection.PSQLAccess"%>
<%@page import="java.util.ArrayList"%> 
<%@page import="authentication.*"%> 

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%!User user = null;%>
<%!String order = "";%>
<%!University uni = null; %>
<%! ArrayList<University> uniList = null; %>

    <%
    	uniList = PSQLAccess.retrieveUniversities();
    	if(request.getSession().getAttribute("user") != null){
                    user = (User)request.getSession().getAttribute("user");
                }
                
                if(request.getParameter("order") != null){
                	order = request.getParameter("order");
                }
                else {
                	order = "";
                } 
                
                ArrayList<Participant> participantList = PSQLAccess.retrieveParticipants(user.getUniversity(), order);
                
				uni = PSQLAccess.retrieveUniversity(user.getUniversity());
                
    %>
    
	<!-- displayed only for the managers and the administrators   -->
    <c:if test="<%= (user != null && (user.getRights()==Login.ADMINISTRATOR_RIGHTS || user.getRights()==Login.MANAGER_RIGHTS))  %>">
	<form action="ChangeUni" method="post">
    
	    <div class="dxinfo">
	    	<input type="submit" value="change uni to"/>
		    <select name="university">
		    	<c:forEach var="uni" items="<%= uniList %>">
		    		<option>${uni.name}</option>
	    		</c:forEach>
		    </select>
		    
	    </div>
    
    </form>
    </c:if>
    
    <p class="error" >
	<%
	
	if(session.getAttribute("errorMessage") != null){
		out.println(session.getAttribute("errorMessage"));
		session.setAttribute("errorMessage", null);
	}
	
	%>
	</p>
    
    <p>
		There are currently registered <%=uni.getCurrentPart() %> out of <%=uni.getMaxPart() %> participants
    </p>
    
    	<table class="full_width">
			  <tr>
			  		<th><a href="?order=id">photo</a></th>
			  		<th><a href="?order=sex">sex</a></th>
			        <th>name</th>
			        <th><a href="?order=university">uni</a></th>
			        <th>birthdate</th>
			        <th>contact</th>
			        <th>int.</th>
			        <th><a href="?order=friday">friday</a></th>
			        <th>rental</th>
			        <th>t-shirt</th>

			</tr>
			
			<c:forEach var="p" items="<%=participantList %>" >
				<tr>
					<td>  <img style="width: 60px; height: 60px;" alt="photo" src="imgs/${ p.photo }" /> </td>
					<td>${p.sex}</td>
					<td>${p.name} ${p.surname}</td>
					<td>${p.university}</td>
					<td>${p.birthdate}</td>
					<td>${p.email} <br /> ${p.phone}</td>
					<td>${p.alimentaryIntolerance}</td>
					<td>${p.fridayActivity}</td>
					<td>${p.rental}&nbsp;${p.shoeSize}</td>
					<td>${p.tShirtSize}</td>
					<td>
						<form action="EditParticipant" method="get">
						<ins>
							<input type="hidden" name="id" value="${p.id}" />
							<input type="submit" value="edit"/>
						</ins>
						</form>
					</td>
					<td>
						<form action="DeleteParticipant" method="post" onsubmit="return confirm_deletion()">
						<ins>
							<input type="hidden" name="partId" value="${p.id}" />
							<input type="submit" value="delete"/>
						</ins>
						</form>
					</td>
					<c:if test="<%= (user != null && (user.getRights()==Login.ADMINISTRATOR_RIGHTS))  %>">
						<td>
							<form action="PrintBadge" method="get">
							<ins>
								<input type="hidden" name="partId" value="${p.id}" />
								<input type="submit" value="print"/>
							</ins>
							</form>
						<td>
					</c:if>
				
				</tr>
		    </c:forEach>
			
		</table>
    
