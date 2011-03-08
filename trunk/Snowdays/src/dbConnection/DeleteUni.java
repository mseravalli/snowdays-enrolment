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
 * Servlet implementation class DeleteUni
 */
public class DeleteUni extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteUni() {
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
		User user = (User) request.getSession().getAttribute("user");
		
		String uniName = PSQLAccess.strictTrim(request.getParameter("uniName"));
		
		if(user != null && user.getRights() == Login.ADMINISTRATOR_RIGHTS || user.getRights() == Login.MANAGER_RIGHTS){
			String query = String.format(	"DELETE FROM uni " +
											"WHERE university LIKE '%s';", uniName);
			
			if(!request.getParameter("uniName").equals(PSQLAccess.META_UNI)){
				
				try{
					Class.forName(PSQLAccess.driverName);
					
					Connection con = DriverManager.getConnection(PSQLAccess.dburl, PSQLAccess.dbuser, PSQLAccess.dbpasswd);
					
					
					Statement stmt = con.createStatement();
					stmt.executeUpdate(query);
					
//					stmt.executeUpdate(PSQLAccess.UPDATE_META_UNI);
					PSQLAccess.updateMetaUni(con, stmt);
					
					//change the user uni if it was the deleted one
					if(user.getUniversity().equals(request.getParameter("uniName"))){
						user.setUniversity(PSQLAccess.META_UNI);
					}
					
					
					
					stmt.close();
					con.close();
					

					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e){
					e.printStackTrace();
					
					HttpSession session = request.getSession();
					session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_DELETION);
				} 
				
				
			}
			else {
				HttpSession session = request.getSession();
				session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_DELETION);
			}
			
			
		}
		
		response.sendRedirect("universityManagement.jsp");
		
	}

}
