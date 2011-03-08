package dbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import authentication.Login;
import beans.User;

/**
 * Servlet implementation class EditUser
 */
public class EditUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = PSQLAccess.retrieveUser(request.getParameter("username"));
		
		if(user == null){
			RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp?errorType="+PSQLAccess.ERROR_IN_READING);
			dispatcher.forward(request, response);
		}
		else {
			RequestDispatcher dispatcher = request.getRequestDispatcher("userEditing.jsp");
			dispatcher.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//check if there is a user
		if(request.getSession().getAttribute("user") != null){
			User user = (User)request.getSession().getAttribute("user");
			
			//check if the user is an administrator
			if(user.getRights() == Login.ADMINISTRATOR_RIGHTS || user.getRights() == Login.MANAGER_RIGHTS){
				
				// check if all the parameters are set
				if(isCorrect(request)){
					
					//update the database
					try{
					Class.forName(PSQLAccess.driverName);
					
					Connection con = DriverManager.getConnection(PSQLAccess.dburl, PSQLAccess.dbuser, PSQLAccess.dbpasswd);
					
					String username = PSQLAccess.strictTrim(request.getParameter("username"));
					String password = PSQLAccess.strictTrim(request.getParameter("password"));
					String name = PSQLAccess.strictTrim(request.getParameter("name"));
					String surname = PSQLAccess.strictTrim(request.getParameter("surname"));
					String email = PSQLAccess.strictTrim(request.getParameter("email"));
					String university = PSQLAccess.strictTrim(request.getParameter("university"));
					String rights = PSQLAccess.strictTrim(request.getParameter("rights"));
					String oldUsername = PSQLAccess.strictTrim(request.getParameter("oldUsername"));
					
					String editQuery = String.format(	"UPDATE users SET username = '%s', password = '%s', name = '%s', surname = '%s', email = '%s', university = '%s', role = '%s' " +
															"WHERE username = '%s'", username, password, name, surname, email, university, rights, oldUsername);
					
					Statement stmt = con.createStatement();
					stmt.executeUpdate(editQuery);
					
					stmt.close();
					con.close();
					
					response.sendRedirect("userManagement.jsp");					
					return;					
					
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e){
						e.printStackTrace();
						
						HttpSession session = request.getSession();
						session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_DB);
						
						response.sendRedirect("EditUser?username=" + request.getParameter("oldUsername"));
						return;
					}
					
				}
				
				HttpSession session = request.getSession();
				session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_FIELDS);
				
				response.sendRedirect("EditUser?username=" + request.getParameter("oldUsername"));
				return;
			}			
		}
		
		
		response.sendRedirect("userManagement.jsp");
		
	}
	
	
	public static boolean isCorrect(HttpServletRequest request){
		
		if(request.getParameter("username") != null &&
				request.getParameter("password") != null &&
				request.getParameter("rePassword") != null &&
				request.getParameter("name") != null &&
				request.getParameter("surname") != null &&
				request.getParameter("email") != null &&
				request.getParameter("university") != null &&
				request.getParameter("rights") != null){
			
			if(!request.getParameter("username").equals("") &&
					!request.getParameter("password").equals("") &&
					!request.getParameter("rePassword").equals("") &&
					!request.getParameter("name").equals("") &&
					!request.getParameter("surname").equals("") &&
					!request.getParameter("email").equals("") &&
					!request.getParameter("university").equals("") &&
					!request.getParameter("rights").equals("") &&
					request.getParameter("password").equals(request.getParameter("rePassword"))){
				
				if(PSQLAccess.valideEmail(request.getParameter("email")) != null){
					return true;
				}
				
			}
		}

		return false;
		
	}
	

}
