package dbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;


import authentication.Login;

/**
 * Servlet implementation class AddUser
 */
public class AddUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("userManagement.jsp");
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
				if(EditUser.isCorrect(request)){
					
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
					
					String addUniversity = String.format(	"INSERT INTO users (username, password, name, surname, email, university, role)  " +
															"VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s')", username,  
																												password, 
																												name,  
																												surname, 
																												email,
																												university,
																												rights);
					
					Statement stmt = con.createStatement();
					stmt.executeUpdate(addUniversity);
					
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
						
						response.sendRedirect("userAdd.jsp");
						return;
					}
					
				}
				
				HttpSession session = request.getSession();
				session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_FIELDS);
				
				response.sendRedirect("userAdd.jsp");
				return;
			}			
		}
		
		
		response.sendRedirect("userManagement.jsp");
	}
	
	
}
