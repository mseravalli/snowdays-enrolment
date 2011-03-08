package dbConnection;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
 * Servlet implementation class DeleteParticipant
 */
public class DeleteParticipant extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteParticipant() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("participantsView.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		
		String partId = PSQLAccess.strictTrim(request.getParameter("partId"));
		
		if(user != null && user.getRights() == Login.ADMINISTRATOR_RIGHTS || 
				user.getRights() == Login.MANAGER_RIGHTS ||
				user.getRights() == Login.COLLABORATOR_RIGHTS){
			String query = String.format(	"DELETE FROM participants " +
											"WHERE id = %s", partId);
			
			
			if(query != null){
				
				try{
					Class.forName(PSQLAccess.driverName);
					
					Connection con = DriverManager.getConnection(PSQLAccess.dburl, PSQLAccess.dbuser, PSQLAccess.dbpasswd);
					Statement stmt = con.createStatement();
					
					
					stmt.execute("START TRANSACTION");

					// delete the image				
					ResultSet rs = stmt.executeQuery("SELECT photo FROM participants WHERE id = " + partId);
					rs.next();
					String path = getServletContext().getRealPath("") + "/imgs/";
					File file = new File(path + rs.getString(1));
					file.delete();
					
					stmt.executeUpdate(query);
					stmt.execute("COMMIT");					
					
//					stmt.executeUpdate(PSQLAccess.UPDATE_META_UNI);
					
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
		
		response.sendRedirect("participantsView.jsp");
	}

}
