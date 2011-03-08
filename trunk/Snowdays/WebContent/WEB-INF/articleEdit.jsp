<%@page import="beans.*"%>
<%@page import="dbConnection.*"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%! Article art = null; %>

<%

art = PSQLAccess.retrieveArticle(request.getParameter("artId"));
if (art == null){
	request.setAttribute("errorMessage", PSQLAccess.ERROR_IN_READING);
} 

%>

<p class="error">
<%

if(request.getAttribute("errorMessage") != null){
	out.println(request.getAttribute("errorMessage"));
	request.setAttribute("errorMessage", null);
}

%>
</p>

	<noscript>
        <p>
            Javascript is not currently enabled on your browser. 
            If you can enable it, your input will be checked as you enter it (on most browsers, at least). 
            You may find this helpful. 
        </p>
    </noscript>
    <script type="text/javascript">
        function validateOnSubmit(){
            var elem;
		    var errs=0;
		    // execute all element validations in reverse order, so focus gets
		    // set to the first one in error.
		    if (!validatePresent(document.forms.articleForm.text,  'pText')){
	          errs += 1;
	        } 
            if (!validatePresent(document.forms.articleForm.title,  'pTitle')){
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
<c:if test="<%= art != null %>">
<form id="articleForm" action="EditArticle" method="post" onsubmit=" return validateOnSubmit()">
	<table>
		<tr>
			<td>title</td>
			<td><input type="text" name="title" onchange="validatePresent(this, 'pTitle')" value="<%=art.getTitle() %>"/></td>
			<td id="pTitle">Required</td>
		</tr>
		<tr>
			<td>text</td>
			<td>
				<textarea rows="10" cols="50" name="text" onchange="validatePresent(this, 'pText')" ><%= art.getText() %></textarea>
			</td>		
			<td id="pText">Required</td>
		</tr>
		<tr>
			<td>
				<input type="hidden" name="artId" value="<%= art.getId() %>" />
				<input type="submit" value="Edit article"/>
			</td>
		</tr>
	
	</table>

	

</form>
</c:if>