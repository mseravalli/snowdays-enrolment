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

import authentication.Login;
import beans.User;

/**
 * Servlet implementation class DeleteComment
 */
public class DeleteComment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteComment() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		
		String commId = PSQLAccess.strictTrim(request.getParameter("commId"));
		
		if(user != null && user.getRights() == Login.ADMINISTRATOR_RIGHTS){
			String query = String.format(	"DELETE FROM comments " +
											"WHERE id = %s", commId);
			
			if(query != null){
				
				try{
					Class.forName(PSQLAccess.driverName);
					
					Connection con = DriverManager.getConnection(PSQLAccess.dburl, PSQLAccess.dbuser, PSQLAccess.dbpasswd);
					
					
					Statement stmt = con.createStatement();
					stmt.executeUpdate(query);
					
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
			
			
		}
		
		response.sendRedirect("articleView.jsp?artId=" + request.getParameter("artId"));
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("index.jsp");
	}

}
