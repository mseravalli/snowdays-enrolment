package dbConnection;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadBean;
import javazoom.upload.UploadException;
import javazoom.upload.UploadFile;

import authentication.Login;
import beans.User;

/**
 * Servlet implementation class AddParticipant
 */
public class AddParticipant extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddParticipant() {
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
		//check if there is a user
		if(request.getSession().getAttribute("user") != null){
			User user = (User)request.getSession().getAttribute("user");
			
			//check if the user has the rights
			if(user.getRights() == Login.ADMINISTRATOR_RIGHTS || 
					user.getRights() == Login.MANAGER_RIGHTS ||
					user.getRights() == Login.COLLABORATOR_RIGHTS){
				
							
				try{
						
					MultipartFormDataRequest mrequest = new MultipartFormDataRequest(request);
					
					// check if all the parameters are set
					if(EditParticipant.isCorrect(mrequest, request, true)){
						
						
					Class.forName(PSQLAccess.driverName);
					
					Connection con = DriverManager.getConnection(PSQLAccess.dburl, PSQLAccess.dbuser, PSQLAccess.dbpasswd);
					Statement stmt = con.createStatement();
					
					if(areConstrSatisf(request, response, user, stmt)){
					
						//get the names of the different paramenters
						String name = PSQLAccess.strictTrim(mrequest.getParameter("name"));
						String surname = PSQLAccess.strictTrim(mrequest.getParameter("surname"));
						String sex = PSQLAccess.strictTrim(mrequest.getParameter("sex"));
						String email = PSQLAccess.strictTrim(mrequest.getParameter("email"));
						String phone = PSQLAccess.strictTrim(mrequest.getParameter("phone"));
						String birthdate = PSQLAccess.strictTrim(mrequest.getParameter("birthdate"));
						String intolerance = PSQLAccess.strictTrim(mrequest.getParameter("alimentaryInt"));
						String friday = PSQLAccess.strictTrim(mrequest.getParameter("friday"));
						String rental = PSQLAccess.strictTrim(mrequest.getParameter("rental"));
						String shoeSize= PSQLAccess.strictTrim(mrequest.getParameter("shoeSize"));
						String tshirtSize= PSQLAccess.strictTrim(mrequest.getParameter("tshirtSize"));
						
						
						
						
						//database update
						String addParticipant = String.format(	"INSERT INTO participants (university, name, surname, sex, email, phone, birthdate, friday_activity, rental, shoe_size, tshirt_size, alimentary_intolerance) " +
																"VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');", 
																user.getUniversity(), name, surname,sex, email, phone, birthdate, friday, rental, shoeSize, tshirtSize, intolerance);
						
						
						updateDB(request, response, mrequest, con, stmt, name,
								surname, addParticipant);
						
						//end database updates
					}
						if(request.getSession().getAttribute("errorMessage") == null){
							response.sendRedirect("participantsView.jsp");					
							return;
						} else {
							response.sendRedirect("participantsAdd.jsp");
							return;
						}
					
					}
					else {
						HttpSession session = request.getSession();
						session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_FIELDS);
						
						response.sendRedirect("participantsAdd.jsp");
						return;
					}
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e){
					e.printStackTrace();
					
					HttpSession session = request.getSession();
					session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_DB);
					
					response.sendRedirect("participantsAdd.jsp");
					return;
				} catch (UploadException e){
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
					
					HttpSession session = request.getSession();
					session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_FIELDS);
					
					response.sendRedirect("participantsAdd.jsp");
					return;
					
				}
				
			}			
		}
		
		
		response.sendRedirect("participantsView.jsp");
	}

	private void updateDB(HttpServletRequest request,
			HttpServletResponse response, MultipartFormDataRequest mrequest,
			Connection con, Statement stmt, String name, String surname,
			String addParticipant) throws SQLException, Exception, IOException {
		ResultSet rs;
		stmt.executeUpdate("START TRANSACTION");
		
		stmt.executeUpdate(addParticipant);
		
		String retrieveID = String.format("SELECT id FROM participants WHERE name = '%s' AND surname = '%s' ORDER BY id DESC", name, surname);
		
		rs = stmt.executeQuery(retrieveID);
		
		rs.next();
		
		int id = rs.getInt(1);	
		
		String path = getServletContext().getRealPath("") + "/imgs/";
		
		String photoName = canUploadImg(mrequest,path, String.valueOf(id), null);
		
		if(photoName == null) {
			stmt.executeUpdate("ROLLBACK");
			
			stmt.close();
			con.close();
			
			HttpSession session = request.getSession();
			session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_FIELDS);
			
		} else {
		
			String setPhoto = String.format("UPDATE participants SET photo = '%s' WHERE id = %d", photoName, id);
			
			stmt.executeUpdate(setPhoto);
			
			stmt.executeUpdate("COMMIT");
			
//			stmt.executeUpdate(PSQLAccess.UPDATE_META_UNI);
			
			stmt.close();
			con.close();
		}
	}

	private boolean areConstrSatisf(HttpServletRequest request,
			HttpServletResponse response, User user, Statement stmt)
			throws SQLException, IOException {
		ResultSet rs1;
		
		
		//check if the university can accept more participants
		
		//retrieve the max number accepted
		String maxPart = String.format("SELECT max_participants FROM uni WHERE university = '%s'", user.getUniversity());
		rs1 = stmt.executeQuery(maxPart);
		rs1.next();
		int max = rs1.getInt("max_participants");
		
		//retrieve the current number
		String currentPart = String.format("SELECT count(id) from participants where university like '%s'", user.getUniversity());
		rs1 = stmt.executeQuery(currentPart);
		rs1.next();
		int current = rs1.getInt(1);
		
		if(current >= max){
			HttpSession session = request.getSession();
			session.setAttribute("errorMessage", "Total number of participants already reached");
			return false;
			
		}
		
		
		//avoid the possibility of inserting a participant in META_UNI
		if(user.getUniversity().equals(PSQLAccess.META_UNI)){
			HttpSession session = request.getSession();
			session.setAttribute("errorMessage", PSQLAccess.ERROR_IN_FIELDS);
			return false;
		}
		
		return true;
	}
	
	public static String canUploadImg(MultipartFormDataRequest mrequest, String path, String id, String oldImgPath) throws Exception{

	         // Uses MultipartFormDataRequest to parse the HTTP request.
	         String todo = null;
	         if (mrequest != null) todo = mrequest.getParameter("todo");
		     if ( (todo != null) && (todo.equalsIgnoreCase("upload")) ){
	                Hashtable files = mrequest.getFiles();
	                if ( (files != null) && (!files.isEmpty()) ){
	                    UploadFile file = (UploadFile) files.get("uploadfile");
	                    if (file != null &&
	                    		file.getFileSize() <= (350*1024) &&
	                    		(file.getContentType().equals("image/gif") || file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg"))){
	                    
	                    int lastPoint = file.getFileName().lastIndexOf('.');
	                    
	                    String fileName =  id + file.getFileName().substring(lastPoint, file.getFileName().length());	                    
	                    file.setFileName(fileName);
	                    	
	                    UploadBean upBean = new UploadBean();

	                    
	                    //delete the old image if required
	                    if(oldImgPath != null){
		                    File oldImg = new File(path + oldImgPath);
		                    oldImg.delete();
	                    }
	                    
	                    upBean.setFolderstore(path);
	                    upBean.store(mrequest, "uploadfile");
	                    return fileName;
	                    }
	                }
	                else
	                {
	                	//no file or wrong file uploaded
	                }
		     }
		     return null;
	      
	}
	

}
