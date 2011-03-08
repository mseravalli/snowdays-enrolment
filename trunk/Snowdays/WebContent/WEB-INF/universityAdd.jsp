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
		    if (!validatePresent(document.forms.uniForm.max_teams,  'pMax_teams', true)){
	          errs += 1;
	        } 
            if (!validatePresent(document.forms.uniForm.max_part,  'pMax_part')){
              errs += 1;
            } 
		    if (!validatePresent(document.forms.uniForm.name,  'pName')){
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

<form id="uniForm" action="AddUniversity" method="post" onsubmit=" return validateOnSubmit()">
	<table>
		<tr>
			<td>name</td>
			<td><input type="text" name="name" onchange="validatePresent(this, 'pName')"/></td>
			<td id="pName">Required</td>
		</tr>
		<tr>
			<td>max participants</td>
			<td><input type="text" name="max_part" onchange="validatePresent(this, 'pMax_part')"/></td>
			<td id="pMax_part">Required</td>
		</tr>
		<tr>
			<td>max teams</td>
			<td><input type="text" name="max_teams" onchange="validatePresent(this, 'pMax_teams')"/></td>
			<td id="pMax_teams">Required</td>
		</tr>
		<tr>
			<td>
				<input type="submit" value="Add uni"/>
			</td>
		</tr>
	
	</table>

	

</form>