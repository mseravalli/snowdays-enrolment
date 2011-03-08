package dbConnection;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import authentication.Login;
import beans.Participant;
import beans.User;

/**
 * Servlet implementation class PrintBadge
 */
public class PrintBadge extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PrintBadge() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.sendRedirect("index.jsp");
		//check if there is a user
		if(request.getSession().getAttribute("user") != null){
			User user = (User)request.getSession().getAttribute("user");
			
			//check if the user is an administrator
			if(user.getRights() == Login.ADMINISTRATOR_RIGHTS){
				
				int id = Integer.parseInt(PSQLAccess.noHTMLTrim(request.getParameter("partId")));
				boolean next = Boolean.parseBoolean(PSQLAccess.noHTMLTrim(request.getParameter("next")));
				boolean prev = Boolean.parseBoolean(PSQLAccess.noHTMLTrim(request.getParameter("prev")));
				
				Participant part = PSQLAccess.retrieveParticipant(id);
				
				//if the user is looking for the next participant
				//100 tries are attempted, if nothing is found a null pointer is returned
				if(next){
					int end = id + 100;
					while(id < end){
						id++;
						part = PSQLAccess.retrieveParticipant(id);
						if(part != null)
							break;
					}
					
					if(part == null)
						part = PSQLAccess.retrieveParticipant(end - 100);
				}
				
				
				//if the user is looking for the previous participant
				//100 tries are attempted, if nothing is found a null pointer is returned
				if(prev){
					int end = id - 100;
					while(id > end){
						id--;
						part = PSQLAccess.retrieveParticipant(id);
						if(part != null)
							break;
					}
					
					if(part == null)
						part = PSQLAccess.retrieveParticipant(end + 100);
				}
				
//				request.setAttribute("participant", part);
				HttpSession session = request.getSession();
				session.setAttribute("participant", part);
				
				RequestDispatcher dispatcher = request.getRequestDispatcher("badgePrinting.jsp");
				dispatcher.forward(request, response);

//				response.sendRedirect("badgePrinting.jsp");
				
			}
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("index.jsp");
	}

}
