<%! String currentPage = null; %>

<%

	currentPage = request.getRequestURI();

	//get the page name
	for(int i = currentPage.length()-1; i >= 0; i--){
		
		if(currentPage.substring(i, i+1).equals("/")){
			currentPage = currentPage.substring(i+1, currentPage.length());
			break;
		}
	}

%>

<div id="lcol"> 
		<div class="menu"> 
			<div class="m_green"> 
				<h3>Main</h3> 
				<ul> 
					<li><a <%= (currentPage.equals("index.jsp"))?"class=\"selected\"":"" %> href="index.jsp">Home</a></li>
					<li><a <%= (currentPage.equals("allNews.jsp"))?"class=\"selected\"":"" %> href="allNews.jsp">All News</a></li> 
					<!-- not needed
                    <li><a <%= (currentPage.equals("info.jsp"))?"class=\"selected\"":"" %> href="info.jsp">Info</a></li> 
					<li><a <%= (currentPage.equals("universities.jsp"))?"class=\"selected\"":"" %> href="universities.jsp">Universities</a></li> 
					<li><a <%= (currentPage.equals("program.jsp"))?"class=\"selected\"":"" %> href="program.jsp">Program</a></li> 
					 -->
				</ul> 
			</div> 
		</div> 
		<!-- not needed
		<div class="menu"> 
			<div class="m_orange"> 
				<h3>Media</h3> 
				<ul> 
					<li><a <%= (currentPage.equals("photo.jsp"))?"class=\"selected\"":"" %> href="photo.jsp">Photo</a></li> 
					<li><a <%= (currentPage.equals("video.jsp"))?"class=\"selected\"":"" %> href="video.jsp">Video</a></li> 
				</ul> 
			</div> 
		</div> 
		-->
		
		<!-- not needed
		<div class="menu"> 
			<div class="m_blue"> 
				<h3>About</h3> 
				<ul> 
					<li><a <%= (currentPage.equals("team.jsp"))?"class=\"selected\"":"" %> href="team.jsp">Team</a></li> 
					<li><a <%= (currentPage.equals("contact.jsp"))?"class=\"selected\"":"" %> href="contact.jsp">Contact us</a></li> 
					<li><a href="report.pdf" rel="external" >Report</a></li>
				</ul> 
			</div> 
		</div> 
		-->
		
  	</div> 