
var nbsp = 160;		
var node_text = 3;	
var emptyString = /^\s*$/ ;
var global_valfield;	

function trim(str)
{
	return str.replace(/^\s+|\s+$/g, '');
}

function setFocusDelayed() {
	global_valfield.focus();
}

function setfocus(valfield) {
	global_valfield = valfield;
 	 setTimeout( 'setFocusDelayed()', 100 );
}

function msg(fld, msgtype, message){
 	 var dispmessage;
 	 if (emptyString.test(message)) 
 		 dispmessage = String.fromCharCode(nbsp);    
 	 else  
 		 dispmessage = message;

 	 var elem = document.getElementById(fld);
  	elem.firstChild.nodeValue = dispmessage;  
  
  	elem.className = msgtype;
}

var proceed = 2;  

function commonCheck    (valfield, infofield, required){
	if (!document.getElementById) 
		return true; 
	var elem = document.getElementById(infofield);
	if (!elem.firstChild) return true; 
	if (elem.firstChild.nodeType != node_text) 
		return true;  

	if (emptyString.test(valfield.value)) {
    if (required) {
    	msg (infofield, "error", "ERROR: required");  
    	setfocus(valfield);
    	return false;
    }
    else {
    	msg (infofield, "warn", "");   // OK
    	return true;  
    }
	}
	return proceed;
}

function validatePresent(valfield, infofield ){
	var stat = commonCheck (valfield, infofield, true);
	if (stat != proceed) 
		return stat;

	msg (infofield, "warn", "");  
	return true;
}


function validateEmail  (valfield, infofield, required){
	var stat = commonCheck (valfield, infofield, required);
	if (stat != proceed) 
		return stat;

	var tfld = trim(valfield.value);  // value of field with whitespace trimmed off
	var email = /^[^@]+@[^@.]+\.[^@]*\w\w$/  ;
	if (!email.test(tfld)) {
		msg (infofield, "error", "ERROR: not a valid e-mail address");
    	setfocus(valfield);
    	return false;
	}

	var email2 = /^[A-Za-z][\w.-]+@\w[\w.-]+\.[\w.-]*[A-Za-z][A-Za-z]$/  ;
	if (!email2.test(tfld)) 
		msg (infofield, "warn", "Unusual e-mail address - check if correct");
	else
		msg (infofield, "warn", "");
	return true;
}

function validateDate(valfield, infofield, required){
	var stat = commonCheck (valfield, infofield, required);
	if (stat != proceed) 
		return stat;
	
	var tfld = trim(valfield.value);  // value of field with whitespace trimmed off
	var date = /\d\d\d\d-\d\d-\d\d$/  ;
	if (!date.test(tfld)) {
		msg (infofield, "error", "ERROR: not a valid date");
    	setfocus(valfield);
    	return false;
	}
	
	msg (infofield, "warn", "");
	return true;
	
}



function validateRePass(valfield, infofield, required, compid){
	var stat = commonCheck (valfield, infofield, required);
	if (stat != proceed) return stat;
	
	var compfield = document.getElementById("pass");
	
	if(valfield.value === compfield.value){
		msg (infofield, "", "");
		return true;
	}
	else {
		msg (infofield, "error", "ERROR: the passwords do not match");
	    setfocus(valfield);
		return false;
	}
	
}


function toggleShoeSize(){
	
	var r = document.forms.participantForm.rental;
	var size = document.forms.participantForm.shoeSize;
	var infofield = document.getElementById('pShoeSize');			
	if (r.options[r.selectedIndex].text != 'no'){
		// 0 required by IE
		size.removeAttribute("readonly", 0);
		infofield.innerHTML= "Required";
	} else {
		size.setAttribute("readonly", "readonly");
		size.value = '';
		infofield.innerHTML= "leave this blank";
	}
}

 