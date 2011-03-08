<%@ include file="WEB-INF/header.jsp" %>
 

   
  
	<div id="mcol"> 
    
    	<div id="main"> 
        	<h2>h2 main</h2> 
		text for main block
	</div> 
        
        <div class="grey_spacer"></div> 
    	
        <div class="art">
		<h3>title</h3>
		<p>paragraph</p>
		<div class="dxinfo">
			<a href="#">read everything</a> | 14/06/2009 | <a href="#">email</a>
		</div>
	</div>
	<div class="grey_spacer"></div> 
        
        <div class="art">
		<h3>title</h3>
		<p>paragraph <a href="#">anchor</a> ohter text</p>
		<div class="dxinfo">
			<a href="#">read everything</a> | 14/06/2009 | <a href="#">email</a>
		</div>
	</div>
	<div class="grey_spacer"></div>

	<div class="art">
		<h3>title</h3>
		<p>paragraph</p>
		<div class="dxinfo">
			<a href="#">read everything</a> | 14/06/2009 | <a href="#">email</a>
		</div>
	</div>
	<div class="grey_spacer"></div>

	

	<div class="numeration">
		<%
		int numOfPages = 5;
		
		if(request.getParameter("page") != null){
		
			for(int i = 1; i < numOfPages; i++){
				if (i == Integer.parseInt(request.getParameter("page"))){
					out.println("<a href=\"?page=" + i +  "\" id=\"selected\">" + i + "</a>");
				} else {
					out.println("<a href=\"?page=" + i +  "\">" + i + "</a>");
				}
			}
			
		}
		
		%>
<!--		<a href="#">1</a><a id="selected" href="#">2</a><a href="#">3</a>-->
	</div>
        
        
  </div> 
    
<%@ include file="WEB-INF/footer.jsp" %>
    
  	
    

