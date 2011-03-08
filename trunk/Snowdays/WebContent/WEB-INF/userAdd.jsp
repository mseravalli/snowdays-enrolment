<%@page import="authentication.*"%>
<%@page import="dbConnection.PSQLAccess"%>
<%@page import="java.util.ArrayList"%>
<%@page import="beans.*"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%! ArrayList<University> uniList = null; %>
<%! ArrayList<Role> roleList = null; %>

<%
uniList = PSQLAccess.retrieveUniversities();
roleList = PSQLAccess.retrieveRoles();
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

<form id="userForm" action="AddUser" method="post" onsubmit="return validateOnSubmit()">

	<table>
		<tr>
			<td>Username</td>
			<td><input type="text" name="username" onchange="validatePresent(this, 'pUsername')" /></td>
			<td id="pUsername">Required</td>
		</tr>	
		<tr>
			<td>Password</td>
			<td><input type="password" name="password" id="pass" onchange="validatePresent(this, 'pPassword')"/></td>
			<td id="pPassword">Required</td>
		</tr>
		<tr>
			<td>Repeat password</td>
			<td><input type="password" name="rePassword" onchange="validateRePass(this, 'pRePassword', true, 'pass')" /></td>
			<td id="pRePassword">Required</td>
		</tr>	
		<tr>
			<td>Name</td>
			<td><input type="text" name="name" onchange="validatePresent(this, 'pName')"/></td>
			<td id="pName">Required</td>
		</tr>
		<tr>
			<td>Surname</td>
			<td><input type="text" name="surname" onchange="validatePresent(this, 'pSurname')"/></td>
			<td id="pSurname">Required</td>
		</tr>
		<tr>
			<td>Email</td>
			<td><input type="text" name="email" onchange="validateEmail(this, 'pEmail', true)"/></td>
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
				<input type="submit" value="Add user"/>
			</td>
		</tr>
	
	</table>

	

</form>