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

import authentication.*;

/**
 * Servlet implementation class AddUniversity
 */
public class AddUniversity extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddUniversity() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("universityManagement.jsp");
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
				if(EditUniversity.isCorrect(request)){
					
					//update the database
					try{
					Class.forName(PSQLAccess.driverName);
					
					Connection con = DriverManager.getConnection(PSQLAccess.dburl, PSQLAccess.dbuser, PSQLAccess.dbpasswd);
					
					String name = PSQLAccess.strictTrim(request.getParameter("name"));
					String maxPart = PSQLAccess.strictTrim(request.getParameter("max_part"));
					String maxTeams = PSQLAccess.strictTrim(request.getParameter("max_teams"));
					
					String addUniversity = String.format(	"INSERT INTO uni (university, max_participants, max_teams) " +
															"VALUES('%s', %s, %s)", name, maxPart, maxTeams);
					
					Statement stmt = con.createStatement();
					stmt.executeUpdate(addUniversity);
					
//					stmt.executeUpdate(PSQLAccess.UPDATE_META_UNI);
					PSQLAccess.updateMetaUni(con, stmt);
					
					stmt.close();
					con.close();
					
					response.sendRedirect("universityManagement.jsp");					
					return;
					
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e){
						e.printStackTrace();
						
						HttpSession session = request.getSession();
						session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_DB);
						
						response.sendRedirect("universityAdd.jsp");
						return;
					} 
					
				}
				
				HttpSession session = request.getSession();
				session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_FIELDS);
				
				response.sendRedirect("universityAdd.jsp");
				return;
				
			}			
		}
		
		
		response.sendRedirect("universityManagement.jsp");
		
		
	}
	
}
