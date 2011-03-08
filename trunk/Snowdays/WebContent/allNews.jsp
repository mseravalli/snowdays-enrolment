<%@ include file="WEB-INF/header.jsp" %>
<%@page import="dbConnection.PSQLAccess"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%! ArrayList<Article> articleList = null; %>
<%! int numOfPages = -1; %>
<%! int requestedPage = -1; %>
<%! int articlesPerPage = 1;%>
<%! int initialPos = 0; %>
<%! int finalPos = 0; %>

<%

articlesPerPage = 5;

try{
	int tmpApp = 0;
	
	tmpApp = Integer.parseInt(request.getParameter("app"));
	
	//this is done in order to prevent misuses
	switch(tmpApp){
	case 5:
		articlesPerPage = 5;
		break;
	case 10:
		articlesPerPage = 10;
		break;
	case 15:
		articlesPerPage = 15;
		break;
	default:
		articlesPerPage = 5;
	}
	
	
	
	
} catch (NumberFormatException e) {
	e.printStackTrace();
}



//the articles are retrieves from the database and displayed
articleList = PSQLAccess.retrieveArticles();


numOfPages = (int)(articleList.size()/articlesPerPage);
if(articleList.size() % articlesPerPage > 0){
	numOfPages+=1;
}

//check the correctness of the requested page
requestedPage = 1;

try{
	requestedPage = Integer.parseInt(request.getParameter("page"));
} catch (NumberFormatException e) {
	e.printStackTrace();
}

if(requestedPage > numOfPages){
	requestedPage = 1;
}

initialPos = (requestedPage-1)*articlesPerPage;
finalPos = (requestedPage)*articlesPerPage;

request.setAttribute("initialPos", initialPos);
request.setAttribute("finalPos", finalPos);

%>

 
	
<div id="mcol"> 

	<div class="art">
		<h3>Here you can find all articles</h3>
		<p>
			To read an article just click on the title
		</p>
		<p>
			Select how many articles you want to see in each page: 
			<a href="?app=5&amp;page=1">5</a>
			<a href="?app=10&amp;page=1">10</a>
			<a href="?app=15&amp;page=1">15</a>
		</p>
		<p class="error">
		<%
		
		if(session.getAttribute("errorMessage") != null){
			out.println(session.getAttribute("errorMessage"));
			session.setAttribute("errorMessage", null);
		}
		
		%>
		</p>
	</div>
	<div class="grey_spacer"></div>
    
    	<c:forEach var="article" items="<%= articleList %>">
    		<c:choose>
    			<c:when test="${initialPos <= article.pos &&  article.pos < finalPos}">
    				<div class="art">
						<h3><a href="ViewArticle?artId=${article.id }">${article.title }</a></h3>
						<div class="dxinfo">
							<c:if test="<%= (user != null && (user.getRights()==Login.ADMINISTRATOR_RIGHTS ))  %>">
				    	 		<a href="EditArticle?artId=${article.id }">Edit</a> | 
								<a href="DeleteArticle?artId=${article.id }" onclick="return confirm_deletion()">Delete</a>
				    	 	</c:if>
						</div>
						
					</div>
					<div class="grey_spacer"></div>
    			</c:when>
    			<c:otherwise>
    			</c:otherwise>
    		
    		</c:choose>
    		
    	</c:forEach>
    	

		<div class="numeration">
			<%
			
			for(int i = 1; i <= numOfPages; i++){
				if(i ==  requestedPage){
					out.println("<a id=\"selected\" href=\"?app="+ articlesPerPage + "&amp;page=" + i +"\">" + i + "</a>");
				}
				else {
					out.println("<a href=\"?app="+ articlesPerPage + "&amp;page=" + i +"\">" + i + "</a>");
				}
			}
			
			%>
		</div>
	        
        
	</div> 
    
<%
request.setAttribute("initialPos", 0);
request.setAttribute("finalPos", 0);
%>

<%@ include file="WEB-INF/footer.jsp" %>