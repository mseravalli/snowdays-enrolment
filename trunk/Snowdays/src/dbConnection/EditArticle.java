package dbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import authentication.Login;
import beans.*;

/**
 * Servlet implementation class EditArticle
 */
public class EditArticle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditArticle() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Article art = PSQLAccess.retrieveArticle(request.getParameter("artId"));
		
		if(art == null){
			RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp?errorType="+PSQLAccess.ERROR_IN_READING);
			dispatcher.forward(request, response);
		}
		else {
			RequestDispatcher dispatcher = request.getRequestDispatcher("articleEdit.jsp");
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
			if(user.getRights() == Login.ADMINISTRATOR_RIGHTS){
				
				// check if all the parameters are set
				if(EditArticle.isCorrect(request)){
					
					//update the database
					try{
					Class.forName(PSQLAccess.driverName);
					
					Connection con = DriverManager.getConnection(PSQLAccess.dburl, PSQLAccess.dbuser, PSQLAccess.dbpasswd);
					
					String title = PSQLAccess.HTMLAllowdedTrim(request.getParameter("title"));
					String text = PSQLAccess.HTMLAllowdedTrim(request.getParameter("text"));
					String artId =  PSQLAccess.strictTrim(request.getParameter("artId"));
					String author = user.getUsername();
					
					String addArticle = String.format(	"UPDATE articles SET author = '%s', title = '%s', text = '%s' WHERE id = %s ", author, title, text, artId);
					
					Statement stmt = con.createStatement();
					stmt.executeUpdate(addArticle);
					
					stmt.close();
					con.close();
					
					response.sendRedirect("articleView.jsp?artId=" + artId);					
					return;
					
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e){
						e.printStackTrace();
						
						HttpSession session = request.getSession();
						session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_DB);
						
						response.sendRedirect("EditArticle?artId=" + request.getParameter("artId"));
						return;
					} 
					
				}
				
				HttpSession session = request.getSession();
				session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_FIELDS);
				
				response.sendRedirect("EditArticle?artId=" + request.getParameter("artId"));
				return;
				
			}			
		}
		
		
		response.sendRedirect("index.jsp");
	}
	
	public static boolean isCorrect(HttpServletRequest request){
		if(request.getParameter("text") != null &&
				request.getParameter("title") != null &&
				request.getParameter("artId") != null){
			
			if(!request.getParameter("text").equals("") &&
					!request.getParameter("title").equals("") &&
					!request.getParameter("artId").equals("")){
				
				return true;
				
			}
		}

		return false;
	}

}
