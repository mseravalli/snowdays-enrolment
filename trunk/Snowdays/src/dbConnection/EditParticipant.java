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

import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadException;

import authentication.Login;
import beans.*;

/**
 * Servlet implementation class EditParticipant
 */
public class EditParticipant extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditParticipant() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Participant part = PSQLAccess.retrieveParticipant(request.getParameter("id"));
		
		if(part == null){
			RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp?errorType="+PSQLAccess.ERROR_IN_READING);
			dispatcher.forward(request, response);
		}
		else {
			RequestDispatcher dispatcher = request.getRequestDispatcher("participantsEditing.jsp");
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
			
			//check if the user has the rights
			if(user.getRights() == Login.ADMINISTRATOR_RIGHTS || 
					user.getRights() == Login.MANAGER_RIGHTS ||
					user.getRights() == Login.COLLABORATOR_RIGHTS){
				
				
					
					//update the database
				try{
						
					MultipartFormDataRequest mrequest = new MultipartFormDataRequest(request);
					
					// check if all the parameters are set
					if(isCorrect(mrequest, request, false)){
						
					Class.forName(PSQLAccess.driverName);
					
					Connection con = DriverManager.getConnection(PSQLAccess.dburl, PSQLAccess.dbuser, PSQLAccess.dbpasswd);
					
					String name = PSQLAccess.strictTrim(mrequest.getParameter("name"));
					String surname = PSQLAccess.strictTrim(mrequest.getParameter("surname"));
					String sex = PSQLAccess.strictTrim(mrequest.getParameter("sex"));
					String email = PSQLAccess.strictTrim(mrequest.getParameter("email"));
					String phone = PSQLAccess.strictTrim(mrequest.getParameter("phone"));
					String birthdate = PSQLAccess.strictTrim(mrequest.getParameter("birthdate"));
					String intolerance = PSQLAccess.strictTrim(mrequest.getParameter("alimentaryInt"));
					String friday = PSQLAccess.strictTrim(mrequest.getParameter("friday"));
					String rental = PSQLAccess.strictTrim(mrequest.getParameter("rental"));
					String shoeSize = PSQLAccess.strictTrim(mrequest.getParameter("shoeSize"));
					String tshirtSize = PSQLAccess.strictTrim(mrequest.getParameter("tshirtSize"));
					String id = PSQLAccess.strictTrim(mrequest.getParameter("id"));
					String oldPhoto = PSQLAccess.strictTrim(mrequest.getParameter("photo"));
					
					String editQuery = String.format(	"UPDATE participants SET name = '%s', surname = '%s', sex = '%s', email = '%s', phone = '%s', birthdate = '%s', alimentary_intolerance = '%s', friday_activity = '%s', rental = '%s', shoe_size = '%s', tshirt_size = '%s' " +
															"WHERE id = '%s'", name, surname, sex, email, phone, birthdate, intolerance, friday, rental, shoeSize, tshirtSize, id);
					
					Statement stmt = con.createStatement();
					
					
					stmt.execute("START TRANSACTION");
					
					stmt.executeUpdate(editQuery);
					
					String photoName = null;
					try {
						String path = getServletContext().getRealPath("") + "/imgs/";
						photoName = AddParticipant.canUploadImg(mrequest, path, String.valueOf(id), oldPhoto);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(photoName != null) {
						
						String setPhoto = String.format("UPDATE participants SET photo = '%s' WHERE id = %s", photoName, id);
						
						stmt.executeUpdate(setPhoto);
						
					}
					
					stmt.execute("COMMIT");
					
					stmt.close();
					con.close();
					
					response.sendRedirect("participantsView.jsp");					
					return;	
					}
					else {
						HttpSession session = request.getSession();
						session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_FIELDS);
						
						response.sendRedirect("EditParticipant?id=" + request.getParameter("id"));
						return;
					}
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e){
					e.printStackTrace();
					
					HttpSession session = request.getSession();
					session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_DB);
					
					response.sendRedirect("EditParticipant?id=" + request.getParameter("id"));
					return;
				} catch (UploadException e){
					e.printStackTrace();
				}
				
				
			}			
		}
		
		
		response.sendRedirect("participantsView.jsp");
	}
	
	public static boolean isCorrect(MultipartFormDataRequest request,HttpServletRequest req, boolean isUniNeeded){
		
		if(request.getParameter("name") != null &&
				request.getParameter("surname") != null &&
				request.getParameter("email") != null &&
				request.getParameter("phone") != null &&
				request.getParameter("birthdate") != null &&
				request.getParameter("friday") != null &&
				request.getParameter("tshirtSize") != null){
			
			
			if(!request.getParameter("name").equals("") &&
					!request.getParameter("surname").equals("") &&
					!request.getParameter("email").equals("") &&
					!request.getParameter("phone").equals("") &&
					!request.getParameter("birthdate").equals("") &&
					!request.getParameter("friday").equals("") &&
					!request.getParameter("tshirtSize").equals("")){
				
				//constraint on the meta university
				User user = (User)req.getSession().getAttribute("user");
				if(isUniNeeded && user.getUniversity().equals(PSQLAccess.META_UNI)){
					return false;
				}
				
				//check the email
				if(PSQLAccess.valideEmail(request.getParameter("email")) == null){
					return false;
				}
				
				//check the date
				if(PSQLAccess.validateDate(request.getParameter("birthdate")) == null){
					return false;
				}
				
				/*
				 * check a further DB constraint: the shoe size can be added only
				 * something will be rent
				 */
				if(request.getParameter("rental") != null && !request.getParameter("rental").equals("no")){
					if(request.getParameter("shoeSize") != null && !request.getParameter("shoeSize").equals("")){
						return true;
					} else {
						return false;
					}
				}
				
				if(request.getParameter("rental") == null || request.getParameter("rental").equals("no")){
					if(request.getParameter("shoeSize") == null || request.getParameter("shoeSize").equals("")){
						return true;
					} else {
						return false;
					}
				}
				
			}
		}
		
		return false;
	}

}
