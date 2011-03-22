<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%beans.Participant part = (beans.Participant)session.getAttribute("participant");%>


<c:if test="<%=(part!=null) %>">

	<div style="margin-top: 38px; margin-left: 45px;">
		<img style="height: 160px; width: 124px;" alt="photo" src="imgs/${participant.photo}" />
	</div>
	
	<div style="text-align: center; font-family: verdana; font-size: 15px; width: 210px; margin-top: -2px;">
		${participant.name }
	</div>
	
	<div style="font-family: verdana; font-size: 15px; text-align: center; width: 210px; margin-top: 3px;">
		${participant.surname }
	</div>
	
	<div style="font-family: verdana; font-size: 15px; text-align: center; width: 210px; margin-top: 3px;">
		${participant.id } - ${participant.university }
	</div>
	
	
	
	<div style="margin-top: 300px;">
		<a href="PrintBadge?partId=${participant.id}&prev=true">prev</a>
		<a href="PrintBadge?partId=${participant.id}&next=true">next</a>
	</div>

</c:if>

