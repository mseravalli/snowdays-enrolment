<%@ include file="WEB-INF/header.jsp" %>

<%@page import="dbConnection.PSQLAccess"%>

<%! ArrayList<User> userList = null; %>

<%
userList = null;

userList = PSQLAccess.retrieveUsers();

if(userList == null){
	userList = new ArrayList<User>();
}

%>


<div id="mcol"> 

	<div class="art">
        <h3>Contact Us</h3>
        <p> 
        	For any info, feel free to contact us at: <a href="mailto:snowdays@unibz.it">snowdays@unibz.it</a> 
		</p>
		
			Also, you can call at:
			<ul>
				<li>Mobile Alberto (main coordinator): +39 335 8068201</li>
				<li>Mobile Marc (contacts): +39 333 2465204</li>
			</ul>
		
		<p>
			S.C.U.B. - Sports Club University Bolzano<br /> 
			Via Sernesi, 1<br /> 
			39100 Bolzano<br /> 
			Steuer Nr \ Codice fiscale: 94075450213<br /> 
			<br /> 
			Email: <a href="mailto:scub@unibz.it">scub@unibz.it</a><br /> 
			Tel. (office): +39 0471 012 183<br /> 
			Fax.: +39 0471 012 109<br /> 
        </p> 
        
        <p>
        If you have some technical troubles please contact one of the administrators
        
        </p>
        <ul>
        
	        <%
	        for(int i = 0; i < userList.size(); i++){
	        	
	        	if(userList.get(i).getRights() == Login.ADMINISTRATOR_RIGHTS){
	        		
	        		String contact = String.format("<li><a href=\"mailto:%s\">%s</a></li>", userList.get(i).getEmail(), userList.get(i).getUsername());
	        		
	        		out.println(contact);
	        	}
	        	
	        }
	        
	        
	        %>
        
        </ul>
        
	</div>
    <div class="grey_spacer"></div>


</div>


<%@ include file="WEB-INF/footer.jsp" %>