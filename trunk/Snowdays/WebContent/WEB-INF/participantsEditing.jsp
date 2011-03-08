<%@page import="authentication.*"%>
<%@page import="dbConnection.PSQLAccess"%>
<%@page import="java.util.ArrayList"%>
<%@page import="beans.*"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%! ArrayList<String> fridayList = null; %>
<%! ArrayList<String> rentList = null; %>
<%! ArrayList<Participant> participantList = null; %>
<%! String order = ""; %>
<%! Participant part = null; %>

<%
fridayList = PSQLAccess.retrieveFriday();
rentList = PSQLAccess.retrieveRental();
participantList = PSQLAccess.retrieveParticipants("allUni", order);

		part = PSQLAccess.retrieveParticipant(request.getParameter("id"));
		
		if(part == null) {
			session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_READING);
		}
		
%>




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
		    toggleShoeSize();

		    //check the condition for the shoe size input 
		    if(document.forms.participantForm.rental.value != 'no'){
		    	if (!validatePresent(document.forms.participantForm.shoeSize,  'pShoeSize')){
		          errs += 1;
			    }
			}
		    
		    
		    if (!validatePresent(document.forms.participantForm.birthdate,  'pBirthdate')){
              errs += 1;
	        } 
		    if (!validatePresent(document.forms.participantForm.phone,  'pPhone')){
	          errs += 1;
	        } 
		    if (!validateEmail(document.forms.participantForm.email,  'pEmail', true)){
	          errs += 1;
	        } 
            if (!validatePresent(document.forms.participantForm.surname,  'pSurname')){
              errs += 1;
            } 
		    if (!validatePresent(document.forms.participantForm.name,  'pName')){
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
    
    <c:if test="<%= part != null%>">
    
    <%
		
		//part is added to the request in order to be able to use the expression language
		request.setAttribute("part", part);
		
		//swap the activity of the current participant to the first position
		for(int j = 0; j < fridayList.size(); j++){
			if(fridayList.get(j).equals(part.getFridayActivity())){
				
				String tmp = fridayList.get(0);
				fridayList.set(0, fridayList.get(j));
				fridayList.set(j, tmp);
				break;
			}
		}
	%>

    <form id="participantForm" onsubmit="return validateOnSubmit()" action="EditParticipant" method="post" enctype="multipart/form-data" >
        <table>
        	<tr>
        		<td>University</td>
        		<td>${part.university }</td>
        	</tr>
        
        
	        <tr>
	            <td>
                    Name
	            </td>
	            <td>
	               <input type="text" name="name" onchange="validatePresent(this, 'pName')" value="${part.name }" />
	            </td>
	            <td id="pName">
	               Required
	            </td>
	        </tr>
	        
            <tr>
                <td>
                    Surname
                </td>
                <td>
                   <input type="text" name="surname" onchange="validatePresent(this, 'pSurname')" value="${part.surname }" />
                </td>
                <td id="pSurname">
                   Required
                </td>
            </tr>
            
            <tr>
                <td>
                    Photo
                </td>
                <td>
                <img style="width: 100px; height: 100px;" alt="photo" src="imgs/${part.photo }">
                  <input style="background-color: #ffffff;" type="file" name="uploadfile" />
                  <input type="hidden" name="photo" value="${part.photo }" />
                </td>
                <td id="pPhoto">
                   gif, jpg, png &lt;= 350KB
                </td>
                 <td style="width: 20px;"></td>
                <td>
                	<a href="http://www.shrinkpictures.com/" target="_blank">need to shrink images?</a>
                </td>
            </tr>
            
            <tr>
            	<td>
            		Sex
            	</td>
            	<td>
            		<input type="radio" name="sex" value="m" <%= (part.getSex() == 'm')?"checked=\"checked\"":"" %> /> Male
					<input type="radio" name="sex" value="f" <%= (part.getSex() == 'f')?"checked=\"checked\"":"" %>/> Female
            	</td>
            	<td>
            		Required
            	</td>
            </tr>
            
			<tr>
                <td>
                    Email
                </td>
                <td>
                   <input type="text" name="email" onchange="validateEmail(this, 'pEmail', true)" value="${part.email }" />
                </td>
                <td id="pEmail">
                   Required
                </td>
            </tr>
            
            <tr>
                <td>
                    Phone
                </td>
                <td>
                   <input type="text" name="phone" onchange="validatePresent(this, 'pPhone')" value="${part.phone }" />
                </td>
                <td id="pPhone">
                   Required
                </td>
            </tr>
            
            <tr>
                <td>
                    Birth date
                </td>
                <td>
                   <input type="text" maxlength="10" name="birthdate" onchange="validatePresent(this, 'pBirthdate')" value="${part.birthdate }" />
                </td>
                <td id="pBirthdate">
                   Required
                </td>
                <td style="width: 20px;"></td>
                <td>
                	[yyyy-mm-dd]
                </td>
            </tr>
            
            <tr>
                <td>
                    Alimentary intolerance
                </td>
                <td>
                   <input type="text" name="alimentaryInt" value="${part.alimentaryIntolerance }" />
                </td>
                <td>
                   &nbsp;
                </td>
            </tr>
            
            <tr>
                <td>
                    Friday activity
                </td>
                <td>
                   	<select name="friday">
                 		<c:forEach var="activity" items="<%=fridayList %>">
                 			<c:choose>
                 				<c:when test="${part.fridayActivity == activity}">
                 					<option selected="selected">${activity}</option>
                 				</c:when>
                 				<c:otherwise>
                 					<option>${activity}</option>
                 				</c:otherwise>
                 			
                 			</c:choose>
                 		</c:forEach>
                   	</select>
                </td>
                <td>
                   Required
                </td>
            </tr>
            
            <tr>
                <td>
                    Rental
                </td>
                <td>
<!--                	<input type="text" name="rental" onchange="toggleShoeSize()" value="${part.rental }" />-->
					<select name="rental" onchange="toggleShoeSize()">
						<c:forEach var="element" items="<%= rentList %>">
                 			<c:choose>
                 				<c:when test="${part.rental == element}">
                 					<option selected="selected">${element}</option>
                 				</c:when>
                 				<c:otherwise>
                 					<option>${element}</option>
                 				</c:otherwise>
                 			
                 			</c:choose>
                 		</c:forEach>
					</select>
                </td>
                <td >
                   Required
                </td>
            </tr>
            
            <tr>
                <td>
                    Shoe size
                </td>
                <td>
                	<input type="text" name="shoeSize" readonly="readonly" onchange="validatePresent(this, 'pShoeSize')" value="${part.shoeSize }"  />
                </td>
                <td id="pShoeSize">
                   &nbsp;
                </td>
            </tr>
            
            <tr>
                <td>
                    T-shirt size
                </td>
                <td>
                   <select name="tshirtSize">
	                   <option <%= (part.gettShirtSize().equals("xs"))?"selected=\"selected\"":"" %> >xs</option>
	                   <option <%= (part.gettShirtSize().equals("s"))?"selected=\"selected\"":"" %> >s</option>
	                   <option <%= (part.gettShirtSize().equals("m"))?"selected=\"selected\"":"" %> >m</option>
	                   <option <%= (part.gettShirtSize().equals("l"))?"selected=\"selected\"":"" %> >l</option>
	                   <option <%= (part.gettShirtSize().equals("xl"))?"selected=\"selected\"":"" %> >xl</option>
                   </select>
                </td>
                <td>
                   Required
                </td>
            </tr>
            
            <tr>
            	<td>
            		<input type="hidden" name="id" value="${part.id }"/>
            		<input type="hidden" name="todo" value="upload">
            		<input type="submit" id="submit" value="edit participant" />
            	</td>            
            </tr>
        
        
        </table>
    </form>
    
    </c:if>

    <script type="text/javascript">
    	toggleShoeSize();
	</script>
    <% request.setAttribute("part", null);%>