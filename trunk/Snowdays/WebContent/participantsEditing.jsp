<%@ include file="WEB-INF/header.jsp" %>

<%@ page import="beans.User"%>
<%@ page import="authentication.Login"%>

<%! boolean canAccess = false; %>
<%! String pageTitle = Login.RIGHTS_ERROR; %>
<%! String pageToImport = "WEB-INF/blank.jsp"; %>

<%
if(request.getSession().getAttribute("user") != null){
    user = (User)request.getSession().getAttribute("user");
    if (user.getRights() == Login.ADMINISTRATOR_RIGHTS ||
    		user.getRights() == Login.MANAGER_RIGHTS ||
    		user.getRights() == Login.COLLABORATOR_RIGHTS){
        canAccess = true;
        pageTitle = "Edit a participant";
        pageToImport = "WEB-INF/participantsEditing.jsp";
        
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



<div id="mcol"> 

    <div class="art">
    <h3><%= pageTitle %></h3>
    
	<jsp:include page = "<%= pageToImport %>" />

	</div>
    <div class="grey_spacer"></div>


</div>






<%@ include file="WEB-INF/footer.jsp" %>