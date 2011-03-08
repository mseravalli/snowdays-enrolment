package dbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import authentication.Login;
import beans.User;

/**
 * Servlet implementation class AddArticle
 */
public class AddArticle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddArticle() {
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
		//check if there is a user
		if(request.getSession().getAttribute("user") != null){
			User user = (User)request.getSession().getAttribute("user");
			
			//check if the user is an administrator
			if(user.getRights() == Login.ADMINISTRATOR_RIGHTS){
				
				// check if all the parameters are set
				if(EditArticle.isCorrect(request)){
					
					//update the database
					try{
					Class.forName(PSQLAccess.driverName);
					
					Connection con = DriverManager.getConnection(PSQLAccess.dburl, PSQLAccess.dbuser, PSQLAccess.dbpasswd);
					
					String title = PSQLAccess.HTMLAllowdedTrim(request.getParameter("title"));
					String text = PSQLAccess.HTMLAllowdedTrim(request.getParameter("text"));					
					String author = user.getUsername();
					
					//get today's dates
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String lastEdit = sdf.format(cal.getTime());

					String addArticle = String.format(	"INSERT INTO articles (author, last_edit, title, text) " +
															"VALUES('%s', '%s', '%s', '%s')", author, lastEdit, title, text);
					
					Statement stmt = con.createStatement();
					stmt.executeUpdate(addArticle);
					
					stmt.close();
					con.close();
					
					response.sendRedirect("index.jsp");					
					return;
					
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e){
						e.printStackTrace();
						
						HttpSession session = request.getSession();
						session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_DB);
						
						response.sendRedirect("articleAdd.jsp");
						return;
					} 
					
				}
				
				HttpSession session = request.getSession();
				session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_FIELDS);
				
				response.sendRedirect("articleAdd.jsp");
				return;
				
			}			
		}
		
		
		response.sendRedirect("index.jsp");
	}

}
