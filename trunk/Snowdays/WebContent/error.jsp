<%@ include file="WEB-INF/header.jsp" %>

<%@page import="dbConnection.PSQLAccess"%>

<%! int error = -1; %>
<%! String errorMessage = ""; %>

<%

	errorMessage = "";
	
	if(request.getParameter("errorType") == null){
		response.sendRedirect("index.jsp");
	} else {
		
		if(request.getParameter("errorType").equals(PSQLAccess.ERROR_IN_DB)){
			errorMessage = PSQLAccess.ERROR_IN_DB;
		}
		if(request.getParameter("errorType").equals(PSQLAccess.ERROR_IN_FIELDS)){
			errorMessage = PSQLAccess.ERROR_IN_FIELDS;
		}
		if(request.getParameter("errorType").equals(PSQLAccess.ERROR_IN_LOGIN)){
			errorMessage = PSQLAccess.ERROR_IN_LOGIN;
		}
		if(request.getParameter("errorType").equals(PSQLAccess.ERROR_IN_READING)){
			errorMessage = PSQLAccess.ERROR_IN_READING;
		}
		if(request.getParameter("errorType").equals(Login.RIGHTS_ERROR)){
			errorMessage = Login.RIGHTS_ERROR;
		}
		
	}


%>


<div id="mcol"> 
    
    	<div id="main"> 
        	<h2>Error</h2>
			<p><%= errorMessage %></p>
		</div>
		 <div class="grey_spacer"></div> 
		 
		 
		
</div>


<%@ include file="WEB-INF/footer.jsp" %>