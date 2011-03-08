<%@page import="authentication.*"%>
<%@page import="dbConnection.PSQLAccess"%>
<%@page import="java.util.ArrayList"%>
<%@page import="beans.*"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%! ArrayList<String> fridayList = null; %>
<%! ArrayList<String> rentList = null; %>

<%
fridayList = PSQLAccess.retrieveFriday();
rentList = PSQLAccess.retrieveRental();
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
		    
		    
		    if (!validateDate(document.forms.participantForm.birthdate,  'pBirthdate', true)){
              errs += 1;
	        } 
		    if (!validatePresent(document.forms.participantForm.phone,  'pPhone')){
	          errs += 1;
	        } 
		    if (!validateEmail(document.forms.participantForm.email,  'pEmail', true)){
	          errs += 1;
	        } 
		    if (!validatePresent(document.forms.participantForm.uploadfile,  'pPhoto')){
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
    

    <form id="participantForm" onsubmit="return validateOnSubmit()" action="AddParticipant" method="post" enctype="multipart/form-data">
        <table>
	        <tr>
	            <td>
                    Name
	            </td>
	            <td>
	               <input type="text" name="name" onchange="validatePresent(this, 'pName')" />
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
                   <input type="text" name="surname" onchange="validatePresent(this, 'pSurname')"/>
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
                  <input style="background-color: #ffffff;" type="file" name="uploadfile" onchange="validatePresent(this, 'pPhoto')" />
                </td>
                <td id="pPhoto">
                   Required
                 </td>
                 <td style="width: 20px;"></td>
                 <td> 
                 	gif
                 	<%
                 		//identify internet explorer
						String ua = request.getHeader( "User-Agent" );
						boolean isMSIE = ( ua != null && ua.indexOf( "MSIE" ) != -1 );
						if(!isMSIE){
							out.println(", jpg, png ");
						}
					%>
					&lt;= 350KB
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
            		<input type="radio" name="sex" value="m" checked="checked" /> Male
					<input type="radio" name="sex" value="f" /> Female
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
                   <input type="text" name="email" onchange="validateEmail(this, 'pEmail', true)"/>
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
                   <input type="text" name="phone" onchange="validatePresent(this, 'pPhone')"/>
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
                   <input type="text" maxlength="10" name="birthdate" onchange="validateDate(this, 'pBirthdate', true)" value="yyyy-mm-dd"/>
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
                   <input type="text" name="alimentaryInt"/>
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
                 			<option>${activity}</option>
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
<!--                	<input type="text" name="rental" onchange="toggleShoeSize()"/>-->
					<select name="rental" onchange="toggleShoeSize()">
						<c:forEach var="element" items="<%= rentList %>">
                 			<option>${element}</option>
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
                   <input type="text" name="shoeSize" readonly="readonly" onchange="validatePresent(this, 'pShoeSize')"/>
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
	                   <option>xs</option>
	                   <option>s</option>
	                   <option selected="selected">m</option>
	                   <option>l</option>
	                   <option>xl</option>
                   </select>
                </td>
                <td>
                   Required
                </td>
            </tr>
            
            <tr>
            	<td>
            		<input type="hidden" name="todo" value="upload">
            		<input type="submit" id="submit" value="add new participant" />
            	</td>            
            </tr>
        
        
        </table>
    </form>
