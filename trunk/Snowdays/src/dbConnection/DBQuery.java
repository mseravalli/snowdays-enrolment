package dbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import authentication.Login;
import beans.User;

/**
 * Servlet implementation class DBQuery
 */
public class DBQuery extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DBQuery() {
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
				
				
				try{
					Class.forName(PSQLAccess.driverName);
					
					Connection con = DriverManager.getConnection(PSQLAccess.dburl, PSQLAccess.dbuser, PSQLAccess.dbpasswd);
				
				
					Statement stmt = con.createStatement();
					ResultSet rs;
					
					String query = request.getParameter("query");
					query = query.trim();
					
					//checks whether the first word is not equal to select
					if(query.length() > 6 && !query.substring(0, 6).toLowerCase().equals("select")){
						stmt.executeUpdate(query);
					} 
					else {
						rs = stmt.executeQuery(query);
					
						// after the query is executed the result is stored in
						// ArrayList<String[]> resultSet
						
						ResultSetMetaData rsmd = rs.getMetaData();
						
						int numberOfColumns = rsmd.getColumnCount();
						
						String[] colNames = new String[numberOfColumns];
						
						ArrayList<String[]> resultSet = new ArrayList<String[]>(); 
						
						for(int i = 1; i <= numberOfColumns; i++){
							colNames[i-1] = rsmd.getColumnName(i);
						}
						resultSet.add(colNames);
						
						while(rs.next()){
							
							String[] row = new String[numberOfColumns];
							
							for(int i = 1; i <= numberOfColumns; i++){
								row[i-1] = rs.getString(i);
							}
							
							resultSet.add(row);
							
						}
						
						HttpSession session = request.getSession();
						session.setAttribute("queryResult", resultSet);
						
						stmt.close();
						con.close();
					}
				
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e){
					HttpSession session = request.getSession();
					session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_DB);
					e.printStackTrace();
				}
				
				
//				HttpSession session = request.getSession();
//				session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_FIELDS);
				
				response.sendRedirect("query.jsp");
				return;
			}			
		}
		
		
		response.sendRedirect("index.jsp");
	}

}
