
<%@ page import="beans.*"%>
<%@page import="authentication.Login"%>

<div id="rcol"> 

<%!String topMenu = "WEB-INF/blank.jsp";%>
<%
	if(request.getSession().getAttribute("user") != null){
	beans.User o = (User)request.getSession().getAttribute("user");
	if (o.getRights() == Login.ADMINISTRATOR_RIGHTS ||
	o.getRights() == Login.MANAGER_RIGHTS ||
	o.getRights() == Login.COLLABORATOR_RIGHTS){
		topMenu = "WEB-INF/administration.jsp";
	} else {
		//topMenu = "WEB-INF/sponsors.jsp";
		topMenu = "WEB-INF/blank.jsp";
	}
	
} else {
	//topMenu = "WEB-INF/sponsors.jsp";
	topMenu = "WEB-INF/blank.jsp";
}
%>
       
       
        <jsp:include page= "<%= topMenu %>" />
        
        <!-- not needed
		<div class="menu">        
			<div class="m_orange"> 
				<h3>Search</h3> 
                	<div class="m_content"> 
 					<span>Google search</span>
					<form action="http://www.google.com/cse" id="cse-search-box">
						<div>
							<input type="hidden" name="cx" value="000409258633111218572:jys0sfhdlxs" />
							<input type="hidden" name="ie" value="UTF-8" />
							<input type="text" name="q" class="full_width" />
							<input type="submit" name="sa" value="Search" />
						</div>
					</form>
				</div> 
			</div> 
		</div> 
		
		-->
        
        <!-- not needed
        <div class="menu"> 
			<div class="m_green"> 
            	<h3>Our sponsors</h3> 
				<div class="m_content"> 
					Minor sponsors
			  </div> 
		  </div> 
		</div> 
		
		-->
  </div> 