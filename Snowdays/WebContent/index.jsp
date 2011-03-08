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

articlesPerPage = 3;

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
    
    	<div id="main"> 
        	<h2>Snowdays Enrolment Service</h2> 
			
		</div> 
        
        <div class="grey_spacer"></div> 
    	
    	
    	<c:forEach var="article" items="<%= articleList %>">
    		<c:choose>
    			<c:when test="${initialPos <= article.pos &&  article.pos < finalPos}">
    				<div class="art">
						<h3><a href="ViewArticle?artId=${article.id }">${article.title }</a></h3>
						<p>${article.text }</p>
						<div class="dxinfo">
							<!-- <a href="AddComment?artId=${article.id }">comment</a> |  --> 
							${article.lastEdit } | <a href="mailto:${article.email }">${article.author }</a>
							<c:if test="<%= (user != null && (user.getRights()==Login.ADMINISTRATOR_RIGHTS ))  %>">
				    	 		| <a href="EditArticle?artId=${article.id }">Edit</a> | 
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
					out.println("<a id=\"selected\" href=\"?page=" + i +"\">" + i + "</a>");
				}
				else {
					out.println("<a href=\"?page=" + i +"\">" + i + "</a>");
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