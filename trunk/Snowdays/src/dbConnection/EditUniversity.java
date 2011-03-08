package dbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import authentication.Login;
import beans.*;

/**
 * Servlet implementation class EditUniversity
 */
public class EditUniversity extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditUniversity() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		University uni = PSQLAccess.retrieveUniversity(PSQLAccess.noHTMLTrim(request.getParameter("uniName")));
		
		if(uni == null){
			RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp?errorType="+PSQLAccess.ERROR_IN_READING);
			dispatcher.forward(request, response);
		}
		else {
			RequestDispatcher dispatcher = request.getRequestDispatcher("universityEditing.jsp");
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
			if(user.getRights() == Login.ADMINISTRATOR_RIGHTS ||
					user.getRights() == Login.MANAGER_RIGHTS){
				
				// check if all the parameters are set
				if(isCorrect(request)){
					
					//update the database
					try{
					Class.forName(PSQLAccess.driverName);
					
					Connection con = DriverManager.getConnection(PSQLAccess.dburl, PSQLAccess.dbuser, PSQLAccess.dbpasswd);
					
					String name = PSQLAccess.strictTrim(request.getParameter("name"));
					String maxPart = PSQLAccess.strictTrim(request.getParameter("max_part"));
					String maxTeams = PSQLAccess.strictTrim(request.getParameter("max_teams"));
					String oldName = PSQLAccess.strictTrim(request.getParameter("oldName"));
					
					String editQuery = String.format(	"UPDATE uni SET university = '%s', max_participants = '%s', max_teams = '%s' WHERE university like '%s'", name, maxPart, maxTeams, oldName);
					
					Statement stmt = con.createStatement();
					stmt.executeUpdate(editQuery);
					
//					stmt.executeUpdate(PSQLAccess.UPDATE_META_UNI);
					PSQLAccess.updateMetaUni(con, stmt);
					
					stmt.close();
					con.close();
					
					
					//change the user university if s/he was working on the selected one
					if(user.getUniversity().equals(oldName)){
						user.setUniversity(name);
					}
					
					response.sendRedirect("universityManagement.jsp");			
					return;					
					
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e){
						e.printStackTrace();
						
						HttpSession session = request.getSession();
						session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_DB);
						
						response.sendRedirect("EditUniversity?uniName=" + request.getParameter("oldName"));
						return;
					}
					
				}
				
				HttpSession session = request.getSession();
				session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_FIELDS);
				
				response.sendRedirect("EditUniversity?uniName=" + request.getParameter("oldName"));
				return;
			}			
		}
		
		
		response.sendRedirect("universityManagement.jsp");
	}
	
	
	
	public static boolean isCorrect(HttpServletRequest request){
		
		if(request.getParameter("name") != null &&
				request.getParameter("max_part") != null &&
				request.getParameter("max_teams") != null){
			
			if(!request.getParameter("name").equals("") &&
					!request.getParameter("max_part").equals("") &&
					!request.getParameter("max_teams").equals("")){
				
				
				//be careful with meta uni
				if(request.getParameter("name").equals(PSQLAccess.META_UNI) || 
						(request.getParameter("oldName") != null &&
						request.getParameter("oldName").equals(PSQLAccess.META_UNI))){
					return false;
				}
				
		
				//check whether the new participant member number is less than the
				//enrolled participants
				try {
					Class.forName(PSQLAccess.driverName);
					Connection con = DriverManager.getConnection(PSQLAccess.dburl, PSQLAccess.dbuser, PSQLAccess.dbpasswd);
					
					String query = String.format("SELECT count(id) from participants where university like '%s'", request.getParameter("name"));
					
					Statement stmt = con.createStatement();
					
					ResultSet rs = stmt.executeQuery(query);
					
					
					
					rs.next();
					
					int maxPart = 0;
					
					try{
						maxPart = Integer.parseInt(request.getParameter("max_part"));
					} catch (NumberFormatException e){
						return false;
					}
					
					if(rs.getInt("count(id)") <= maxPart){
						stmt.close();
						con.close();
						return true;
					}
					else{
						stmt.close();
						con.close();
						return false;
					}
										
					
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}

		HttpSession session = request.getSession();
		session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_FIELDS);
		
		return false;
		
	}
	

}
