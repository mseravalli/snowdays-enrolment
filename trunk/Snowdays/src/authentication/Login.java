package authentication;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.*;

import dbConnection.PSQLAccess;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public final static int ADMINISTRATOR_RIGHTS = 0;
	public final static int MANAGER_RIGHTS = 1;
	public final static int COLLABORATOR_RIGHTS = 2;
	
	public final static String RIGHTS_ERROR = "You do not have the rights to access this page!!";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(null !=request.getParameter("logout") && request.getParameter("logout").equals("true")){
			invalidateSession(request.getSession());			
		}
		response.sendRedirect("index.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//if two users connect from the same browser only the last connection is kept
		invalidateSession(request.getSession());
		
		String username = PSQLAccess.strictTrim(request.getParameter("usr"));
		String password = PSQLAccess.strictTrim(request.getParameter("psw"));
		
		User returnedUser = PSQLAccess.checkUsername(username, password);
		
		if(returnedUser != null){
			returnedUser.setPassword("");
			HttpSession session = request.getSession();
			session.setAttribute("user", returnedUser);			
			response.sendRedirect("index.jsp");
		}
		else {
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp?errorType="+PSQLAccess.ERROR_IN_LOGIN);
			dispatcher.forward(request, response);
		}
		
	}
	
	
	private void invalidateSession(HttpSession session){
		session.setAttribute("user", null);		
		session.invalidate();
	}

}