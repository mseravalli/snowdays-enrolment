<%@page import="authentication.*"%>
<%@page import="dbConnection.PSQLAccess"%>
<%@page import="java.util.ArrayList"%>
<%@page import="beans.*"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%! ArrayList<University> uniList = null; %>
<%! ArrayList<Role> roleList = null; %>
<%! User userToEdit = null; %>

<%
uniList = PSQLAccess.retrieveUniversities();
roleList = PSQLAccess.retrieveRoles();

userToEdit = PSQLAccess.retrieveUser(request.getParameter("username"));

if(userToEdit == null){
	session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_READING);
}


%>




<p>
Remember that the following characters are not allowed: <br />
!&quot;#$%&amp;'()*,/:;&lt;=&gt;?\^`{|}~[]
</p>

<p class="error" >
<%

if(session.getAttribute("errorMessage") != null){
	out.println(session.getAttribute("errorMessage"));
	session.setAttribute("errorMessage", null);
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
		    if (!validateEmail(document.forms.userForm.email,  'pEmail', true)){
	          errs += 1;
	        } 
            if (!validatePresent(document.forms.userForm.surname,  'pSurname')){
              errs += 1;
            } 
		    if (!validatePresent(document.forms.userForm.name,  'pName')){
		      errs += 1;
		    }
		    if (!validatePresent(document.forms.userForm.rePassword,  'pRePassword')){
			  errs += 1;
			}
		    if (!validatePresent(document.forms.userForm.password,  'pPassword')){
			  errs += 1;
			}
		    if (!validatePresent(document.forms.userForm.username,  'pUsername')){
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



<c:if test="<%= userToEdit != null%>">

<%		
		//userToEdit is added to the request in order to be able to use the expression language
		request.setAttribute("userToEdit", userToEdit);
		
		//shift at the first place the university
		for(int j = 0; j < uniList.size(); j++){
			if(uniList.get(j).getName().equals(userToEdit.getUniversity())){				
				University tmp = uniList.get(0);
				uniList.set(0, uniList.get(j));
				uniList.set(j, tmp);				
			}
		}
		
		//shift at the first place the role
		for(int j = 0; j < roleList.size(); j++){
			if(roleList.get(j).getRoleName().equals(userToEdit.getRightsString())){				
				Role tmp = roleList.get(0);
				roleList.set(0, roleList.get(j));
				roleList.set(j, tmp);				
			}
		}
		
%>


<form id="userForm" action="EditUser" method="post" onsubmit="return validateOnSubmit()">

	<table>
		<tr>
			<td>Username</td>
			<td><input type="text" name="username" value="${userToEdit.username }" onchange="validatePresent(this, 'pUsername')" /></td>
			<td id="pUsername">Required</td>
		</tr>	
		<tr>
			<td>Password</td>
			<td><input type="password" name="password" id="pass" value="${userToEdit.password }" onchange="validatePresent(this, 'pPassword')"/></td>
			<td id="pPassword">Required</td>
		</tr>
		<tr>
			<td>Repeat password</td>
			<td><input type="password" name="rePassword" value="${userToEdit.password }" onchange="validateRePass(this, 'pRePassword', true, 'pass')" /></td>
			<td id="pRePassword">Required</td>
		</tr>	
		<tr>
			<td>Name</td>
			<td><input type="text" name="name" value="${userToEdit.name }" onchange="validatePresent(this, 'pName')"/></td>
			<td id="pName">Required</td>
		</tr>
		<tr>
			<td>Surname</td>
			<td><input type="text" name="surname" value="${userToEdit.surname }" onchange="validatePresent(this, 'pSurname')"/></td>
			<td id="pSurname">Required</td>
		</tr>
		<tr>
			<td>Email</td>
			<td><input type="text" name="email" value="${userToEdit.email }" onchange="validateEmail(this, 'pEmail', true)"/></td>
			<td id="pEmail">Required</td>
		</tr>
		<tr>
			<td>University</td>
			<td>
				<select name="university">
			    	<c:forEach var="uni" items="<%= uniList %>">
			    	
			    		<!-- if the user is the admin display all university  -->
			    		<c:if test="${user.rights == 0}">    	
			    			<option>${uni.name}</option>
			    		</c:if>
			    		
			    		<!-- if user is a manager do not display 'allUni' -->
			    		<c:if test="${uni.name != 'allUni' && user.rights == 1}">    	
			    			<option>${uni.name}</option>
			    		</c:if>
			    		
		    		</c:forEach>
			    </select>
			</td>
			<td>Required</td>
		</tr>
		<tr>
			<td>Rights</td>
			<td>
				<select name="rights">
					<c:forEach var="r" items="<%= roleList %>">
					
						<!-- if the user is the admin display all rights  -->
			    		<c:if test="${user.rights == 0}">    	
			    			<option>${r.roleName }</option>
			    		</c:if>
			    		
			    		<!-- if user is a manager do not display admin -->
			    		<c:if test="${r.id == 2 && user.rights == 1}">    	
			    			<option>${r.roleName }</option>
			    		</c:if>
			    		
					</c:forEach>
				</select>
			</td>
			<td>Required</td>
		</tr>
		<tr>
			<td>
				<input type="hidden" name="oldUsername" value="${userToEdit.username }"/>
				<input type="submit" value="Edit user"/>
			</td>
		</tr>
	
	</table>

	

</form>

</c:if>

<% 
uniList = null; 
roleList = null; 
userToEdit = null; 
request.setAttribute("userToEdit", null);
%>