<%@page import="beans.*"%>
<%@page import="authentication.*"%>
<%@page import="dbConnection.PSQLAccess"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%! Comment comm =  null; %>

<%

comm = PSQLAccess.retrieveComment(request.getParameter("commId"));

if(comm == null){
	request.setAttribute("errorMessage", PSQLAccess.ERROR_IN_READING);
}


%>
    	
    	
<p>Remember: no HTML code is allowed</p>
    	
    	<p class="error" >
			<%
			
			if(request.getAttribute("errorMessage") != null){
				out.println(request.getAttribute("errorMessage"));
				request.setAttribute("errorMessage", null);
			}
			
			%>
		</p>
		
		<noscript>
	        <p>
	            Javascript is not currently enabled on your browser. If you can enable it, your input will be checked as you enter it (on most browsers, at least). You may find this helpful. 
	        </p>
	    </noscript>
	    <script type="text/javascript">
	        function validateOnSubmit(){
	            var elem;
			    var errs=0;
			    // execute all element validations in reverse order, so focus gets
			    // set to the first one in error.
			    if (!validatePresent(document.forms.commentForm.text,  'pText')){
		          errs += 1;
		        } 
			    if (errs>1){
			      alert('There are fields which need correction before sending');
			    }
			    if (errs==1){ 
			      alert('There is a field which needs correction before sending');
			    }
			
			    return (errs==0);
	                    
	        };
	
	    </script>
    	
    	
    	<c:if test="<%= comm != null %>">
    	
    	<form action="EditComment" method="post" name="commentForm" onsubmit=" return validateOnSubmit()" >
    	
    	<table>
    		<tr>
    			<td>Insert the comment here</td>
    		</tr>
    		<tr>
    			<td><textarea name="text" rows="10" cols="60" onchange="validatePresent(this, 'pText')" ><%= comm.getText() %></textarea></td>
    			<td id="pText">Required</td>
    		</tr>
    		<tr>
    			<td>
    				<input type="hidden" name="commId" value="<%= comm.getId()%>"/>
    				<input type="hidden" name="artId" value="<%= comm.getArtId()%>"/>
    				<input type="submit" value="edit comment"/>
    			</td>
    		</tr>
    	
    	</table>
    
    	</form>
    	
    	</c:if>
