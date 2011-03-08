<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%beans.Participant part = (beans.Participant)session.getAttribute("participant");%>


<c:if test="<%=(part!=null) %>">

	<div>
		<img style="width: 60px; height: 60px;" alt="photo" src="imgs/${participant.photo}" />
	</div>
	
	<div>
		${participant.name } ${participant.surname } 
	</div>
	
	<div>
		${participant.id }
	</div>
	
	<div>
		<a href="PrintBadge?partId=${participant.id}&prev=true">prev</a>
		<a href="PrintBadge?partId=${participant.id}&next=true">next</a>
	</div>

</c:if>

