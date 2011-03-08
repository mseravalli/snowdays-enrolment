<%@page import="beans.*"%>
<%@page import="authentication.Login"%>

<%!String manageUni = "";%>
<%!String manageUsers = "";%>
<%!String addUni = "";%>
<%!String addUsers = "";%>
<%!String addArticle = "";%>
<%!String query = "";%>
<%!User user = null;%>

<%! String currentPage = null; %>
<%!String selectedClass = "class=\"selected\""; %>
<%

	currentPage = request.getRequestURI();

	//get the page name
	for(int i = currentPage.length()-1; i >= 0; i--){
		
		if(currentPage.substring(i, i+1).equals("/")){
			currentPage = currentPage.substring(i+1, currentPage.length());
			break;
		}
	}

%>

<%
	if(request.getSession().getAttribute("user") != null){
    user = (User)request.getSession().getAttribute("user");
    
    //for managers and administrators
    if (user.getRights() == Login.ADMINISTRATOR_RIGHTS ||
    		user.getRights() == Login.MANAGER_RIGHTS){
    	
    	if(currentPage.equals("universityManagement.jsp")){
    		manageUni = "<li><a " + selectedClass +" href=\"universityManagement.jsp\">Manage Uni</a></li>";
    	}
    	else {
    		manageUni = "<li><a href=\"universityManagement.jsp\">Manage Uni</a></li>";
    	}
    	
    	if(currentPage.equals("userManagement.jsp")){
    		manageUsers = "<li><a " + selectedClass +" href=\"userManagement.jsp\">Manage Users</a></li>";
    	}
    	else {
    		manageUsers = "<li><a href=\"userManagement.jsp\">Manage Users</a></li>";
    	}
    	
    	if(currentPage.equals("universityAdd.jsp")){
    		addUni = "<li><a " + selectedClass +" href=\"universityAdd.jsp\">Add Uni</a></li>";
    	}
    	else {
    		addUni = "<li><a href=\"universityAdd.jsp\">Add Uni</a></li>";
    	}
    	
    	if(currentPage.equals("userAdd.jsp")){
    		addUsers = "<li><a " + selectedClass +" href=\"userAdd.jsp\">Add User</a></li>";
    	}
    	else {
    		addUsers = "<li><a href=\"userAdd.jsp\">Add User</a></li>";
    	}
    	
    }
    else {
    	manageUni = "";
    	manageUsers = "";
    	addUni = "";
    	addUsers = "";
    }
    
    // only for administrators
    if (user.getRights() == Login.ADMINISTRATOR_RIGHTS){  
    	
    	if(currentPage.equals("addArticle.jsp")){
    		addArticle = "<li><a " + selectedClass +" href=\"articleAdd.jsp\">Add article</a></li>";
    	}
    	else {
    		addArticle = "<li><a href=\"articleAdd.jsp\">Add article</a></li>";
    	}
    	
    	if(currentPage.equals("query.jsp")){
    		query = "<li><a " + selectedClass +" href=\"query.jsp\">Query the DB</a></li>";
    	}
    	else {
    		query = "<li><a href=\"query.jsp\">Query the DB</a></li>";
    	}
    	
    }
    else {
    	addArticle = "";
    	query = "";
    }
    
} else {
	manageUni = "";
	manageUsers = "";
	addUni = "";
	addUsers = "";
	addArticle = "";
	query = "";
}
%>



<div class="menu"> 
    <div class="m_blue"> 
        <h3>Administration</h3>
            <ul> 
                <li><a <%= (currentPage.equals("participantsView.jsp"))?"class=\"selected\"":"" %> href="participantsView.jsp">Participants</a></li>
                <li><a <%= (currentPage.equals("participantsAdd.jsp"))?"class=\"selected\"":"" %> href="participantsAdd.jsp">Add participant</a></li> 
                <%= manageUni %>
                <%= addUni %>
                <%= manageUsers %>
            	<%= addUsers %>
            	<%= addArticle %>
            	<%= query %>
                
            </ul> 
                    
    </div> 
    
        
</div> 