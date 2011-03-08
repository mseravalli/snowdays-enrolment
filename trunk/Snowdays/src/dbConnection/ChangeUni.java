package dbConnection;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import authentication.Login;
import beans.User;


/**
 * Servlet implementation class ChangeUni
 */
public class ChangeUni extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeUni() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.sendRedirect("index.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getSession().getAttribute("user") != null && request.getParameter("university") != null){
			User user = (User)request.getSession().getAttribute("user");
			
			//check if the user is an administrator
			if(user.getRights() == Login.ADMINISTRATOR_RIGHTS || user.getRights() == Login.MANAGER_RIGHTS){
				user.setUniversity(PSQLAccess.noHTMLTrim(request.getParameter("university")));
			}
			
			
			if(request.getParameter("redirection") != null && request.getParameter("redirection").equals("add")){
				response.sendRedirect("participantsAdd.jsp");
				return;
			}
			response.sendRedirect("participantsView.jsp");
		}
		else {
			response.sendRedirect("index.jsp");
		}
	}

}
