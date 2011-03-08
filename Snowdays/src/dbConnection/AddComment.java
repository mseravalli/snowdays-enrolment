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

import beans.Article;

/**
 * Servlet implementation class AddComment
 */
public class AddComment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddComment() {
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
			RequestDispatcher dispatcher = request.getRequestDispatcher("articleComment.jsp");
			dispatcher.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// check if all the parameters are set
		if(EditComment.isCorrect(request)){
			
			//update the database
			try{
			Class.forName(PSQLAccess.driverName);
			
			Connection con = DriverManager.getConnection(PSQLAccess.dburl, PSQLAccess.dbuser, PSQLAccess.dbpasswd);
			
			String text = PSQLAccess.noHTMLTrim(request.getParameter("text"));
			String artId = request.getParameter("artId");
			
			String addComment = String.format(	"INSERT INTO comments (text, article_id) " +
													"VALUES('%s', %s)", text, artId);
			
			Statement stmt = con.createStatement();
			stmt.executeUpdate(addComment);
			
			stmt.close();
			con.close();
			
			response.sendRedirect("articleView.jsp?artId=" + request.getParameter("artId"));			
			return;
			
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e){
				e.printStackTrace();
				
				HttpSession session = request.getSession();
				session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_DB);
				
				response.sendRedirect("articleComment.jsp?artId=" + request.getParameter("artId"));
				return;
			} 
			
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_FIELDS);
		
		response.sendRedirect("articleComment.jsp?artId=" + request.getParameter("artId"));
	}

}
