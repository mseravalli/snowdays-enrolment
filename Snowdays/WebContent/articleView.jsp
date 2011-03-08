<%@ include file="WEB-INF/header.jsp" %>

<%@page import="dbConnection.PSQLAccess"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%! Article art = null; %>
<%! ArrayList<Comment> commentList = null; %>

<%

commentList = null;

art = PSQLAccess.retrieveArticle(request.getParameter("artId"));
if (art == null){
	request.setAttribute("errorMessage", PSQLAccess.ERROR_IN_READING);
} 

else {
	commentList = PSQLAccess.retrieveComments(art.getId());
}


%>


<div id="mcol">

    <div class="art">
    
	    <p class="error" >
		<%
		
		if(request.getAttribute("errorMessage") != null){
			out.println(request.getAttribute("errorMessage"));
			request.setAttribute("errorMessage", null);
		}
		
		%>
		</p>
		
		<c:if test="<%= art != null%>">
    
    	<h3><%= art.getTitle() %></h3>
    
    	<p>
    		<%= art.getText() %>
    	</p>
    	
		<div class="dxinfo">
			<!-- <a href="AddComment?artId=<%= art.getId() %>">comment</a> | --> 
			<%= art.getLastEdit() %> | <a href="mailto:<%= art.getEmail() %>"><%= art.getAuthor() %></a>
			<c:if test="<%= (user != null && (user.getRights()==Login.ADMINISTRATOR_RIGHTS))  %>">
	   	 		| <a href="EditArticle?artId=<%= art.getId() %>">Edit</a> | 
				<a href="DeleteArticle?artId=<%= art.getId() %>" onclick="return confirm_deletion()">Delete</a>
			</c:if>
		</div>
		
		</c:if>
    
    </div>
    <div class="grey_spacer"></div>
    
    <c:forEach var="comm" items="<%= commentList%>">
    	 <div class="art">
    	 	<p>${comm.text }</p>
    	 	<div class="dxinfo">
    	 	
    	 	<c:if test="<%= (user != null && (user.getRights()==Login.ADMINISTRATOR_RIGHTS ))  %>">
    	 		<a href="EditComment?commId=${comm.id }">Edit</a> | 
				<a href="DeleteComment?commId=${comm.id }&artId=<%= art.getId() %>" onclick="return confirm_deletion()">Delete</a>
    	 	</c:if>
    	 	    	 	
    	 	</div>
    	 </div>
    	 <div class="grey_spacer"></div>
    
    </c:forEach>


</div>


<%@ include file="WEB-INF/footer.jsp" %>