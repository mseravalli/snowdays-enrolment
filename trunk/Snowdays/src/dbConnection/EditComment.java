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
import beans.*;

/**
 * Servlet implementation class AddComment
 */
public class EditComment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditComment() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Comment comm = PSQLAccess.retrieveComment(request.getParameter("commId"));
		
		if(comm == null){
			RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp?errorType="+PSQLAccess.ERROR_IN_READING);
			dispatcher.forward(request, response);
		}
		else {
			RequestDispatcher dispatcher = request.getRequestDispatcher("commentEdit.jsp");
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
				if(EditComment.isCorrect(request)){
					
					//update the database
					try{
					Class.forName(PSQLAccess.driverName);
					
					Connection con = DriverManager.getConnection(PSQLAccess.dburl, PSQLAccess.dbuser, PSQLAccess.dbpasswd);
					
					String text = PSQLAccess.noHTMLTrim(request.getParameter("text"));
					String commId = request.getParameter("commId");
					
					String addComment = String.format(	"UPDATE comments SET text = '%s' WHERE id = '%s' ", text, commId);
					
					Statement stmt = con.createStatement();
					stmt.executeUpdate(addComment);
					
					stmt.close();
					con.close();
					
					response.sendRedirect("articleView.jsp?artId=" + PSQLAccess.retrieveComment(commId).getArtId());			
					return;
					
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e){
						e.printStackTrace();
						
						HttpSession session = request.getSession();
						session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_DB);
						
						response.sendRedirect("EditComment?commId=" + request.getParameter("commId"));
						return;
					} 
					
				}
			}
			
			HttpSession session = request.getSession();
			session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_FIELDS);
			
			response.sendRedirect("EditComment?commId=" + request.getParameter("artId"));
		
		}
		
		response.sendRedirect("index.jsp");
	}
	
	public static boolean isCorrect(HttpServletRequest request){
		if(request.getParameter("text") != null &&
				request.getParameter("artId") != null &&
				request.getParameter("commId") != null){
			
			if(!request.getParameter("text").equals("") &&
					!request.getParameter("artId").equals("") &&
					!request.getParameter("commId").equals("")){
				
				return true;
				
			}
		}

		return false;
	}

}
