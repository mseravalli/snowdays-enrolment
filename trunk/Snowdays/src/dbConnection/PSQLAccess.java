package dbConnection;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.*;


import beans.*;


public class PSQLAccess{
    
//    public static final String driverName = "org.postgresql.Driver";
//    public static final String dburl = "jdbc:postgresql://localhost/it";
//    public static final String dbuser = "marco";
//    public static final String dbpasswd = "marco";
//    public static final String driverName = "org.postgresql.Driver";
//    public static final String dburl = "jdbc:postgresql://localhost/db36";
//    public static final String dbuser = "user36";
//    public static final String dbpasswd = "mckksi32";
	
    public static final String driverName = "com.mysql.jdbc.Driver";
    public static final String dburl = "jdbc:mysql://localhost:3306/snowdays_enrolment";
    public static final String dbuser = "root";
    public static final String dbpasswd = "spichiravo88";
    
    public static final String ERROR_IN_LOGIN = "ERROR: The username or the password is not correct";
    public static final String ERROR_IN_DB = "ERROR: The query cannot be performed";
    public static final String ERROR_IN_FIELDS = "ERROR: Please check again the paramenters in the fields before sending";
    public static final String ERROR_IN_READING = "ERROR: The requested resouce is not present in the database";
    public static final String ERROR_IN_DELETION = "ERROR: The selected element cannot be deleted";
    public static final String META_UNI = "allUni";
    public static final String PDATE_META_UNI = String.format("UPDATE uni SET max_participants = (SELECT sum(max_participants) from uni where university != '%s'), max_teams = (SELECT sum(max_teams) from uni where university != '%s') WHERE university = '%s'", META_UNI, META_UNI, META_UNI);
    
    public static void connectToDB(){
		try {
			
			Class.forName(driverName);
			
			Connection con = DriverManager.getConnection(dburl, dbuser, dbpasswd);
			
			
			String selectQuery =
				"SELECT * FROM user;";
			
			Statement stmt = con.createStatement();
			stmt.execute(selectQuery);
			
			System.out.printf("query executed");
			
			stmt.close();
			
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
      

    public static User checkUsername(String username, String password){
    
    	username = strictTrim(username);
    	password = strictTrim(password);
    	
    	User user = null;
    	
    	ArrayList<User> userList = retrieveUsers();
    	
    	//done in order not to have to use the passed parameters directly in the query
    	for(int i = 0; i < userList.size(); i++){
    		
    		if(userList.get(i).getUsername().equals(username) && userList.get(i).getPassword().equals(password)){
    			user = userList.get(i);
    			break;
    		}
    		
    	}
    	
		return user;
		
    	
    }

    
    public static ArrayList<Participant> retrieveParticipants(String university, String order){
    	
    	//university = strictTrim(university);
    	order = strictTrim(order);
    	
    	ArrayList<Participant> participantList = null;
    	
    	//new string is created in order not to used the passed one and execute some
    	//malicious query while the uni should be passed by the user
    	String resultOrder = "";
    	
    	if(order.equals("university")){
    		resultOrder = "ORDER BY university";
    	}
    	if(order.equals("id")){
    		resultOrder = "ORDER BY id";
    	}
    	if(order.equals("friday")){
    		resultOrder = "ORDER BY friday_activity";
    	}
    	if(order.equals("sex")){
    		resultOrder = "ORDER BY sex";
    	}
    	
    	try {
    		
    		participantList = new ArrayList<Participant>();
    		
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dburl, dbuser, dbpasswd);
			
			String query = "";
			
			if(university.equals(META_UNI)){			
				query = String.format(	"SELECT * " +
				"FROM participants %s;", resultOrder);
			} 
			else {
				
				query = String.format(	"SELECT * " +
						"FROM participants " +
						"WHERE university like '%s' %s;", university, resultOrder);
			}
			
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				Participant tmpPart = new Participant();
				
				tmpPart.setId(rs.getInt("id"));
				tmpPart.setUniversity(rs.getString("university"));
				tmpPart.setName(rs.getString("name"));
				tmpPart.setSurname(rs.getString("surname"));
				tmpPart.setSex(rs.getString("sex").charAt(0));
				tmpPart.setEmail(rs.getString("email"));
				tmpPart.setPhone(rs.getString("phone"));
				tmpPart.setBirthdate(rs.getString("birthdate"));
				tmpPart.setAlimentaryIntolerance(rs.getString("alimentary_intolerance"));
				tmpPart.setFridayActivity(rs.getString("friday_activity"));
				tmpPart.setRental(rs.getString("rental"));
				tmpPart.setShoeSize(rs.getString("shoe_size"));
				tmpPart.settShirtSize(rs.getString("tshirt_size"));
				tmpPart.setLastEditor(rs.getString("last_editor"));
				tmpPart.setPhoto(rs.getString("photo"));
				
				participantList.add(tmpPart);
				
			}
			
			stmt.close();
			con.close();
			
			return participantList;
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		} 
		
		return participantList;
    	
    	
    }
    
    public static Participant retrieveParticipant(int id){
    	
    	Participant part = null;
    	
    	ArrayList<Participant> partList = retrieveParticipants(META_UNI, null);
    	
    	for( int i = 0; i < partList.size(); i++){
    		if(partList.get(i).getId() == id){
    			part = partList.get(i);
    			break;
    		}
    	}
    			
		return part;
    }
    
    public static Participant retrieveParticipant(String id){
    	
    	id = strictTrim(id);
    	
    	Participant part = null;
    	
    	int partId = -1;
    	
    	try{
    		partId = Integer.parseInt(id);
    	} catch(java.lang.NumberFormatException e){
    		return part;
    	}
    	
		return retrieveParticipant(partId);
    	
    	
    }
    
    
    public static ArrayList<University> retrieveUniversities(){
    	
    	ArrayList<University> uniList = null;
    	
    	try {
    		
    		uniList = new ArrayList<University>();
    		
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dburl, dbuser, dbpasswd);
			
			String query = "SELECT * FROM uni ORDER BY university";
			
			Statement retrieveUni = con.createStatement();
			
			ResultSet rs = retrieveUni.executeQuery(query);
			
			Statement retrievePart = con.createStatement();
			ResultSet additionalInfo;
			
			while(rs.next()){
				
				University tmpUni = new University();
				
				tmpUni.setName(rs.getString("university"));
				tmpUni.setMaxPart(rs.getInt("max_participants"));
				tmpUni.setMaxTeams(rs.getInt("max_teams"));
				
				
				// retrieve number of current participants
				String participantQuery = "";
				
				//retrieve all participants if the selected uni is the meta uni
				if(rs.getString("university").equals(PSQLAccess.META_UNI)){
					participantQuery = String.format("SELECT count(id) FROM participants");
				}
				else{
					participantQuery = String.format("SELECT count(id) FROM participants WHERE university = '%s'", rs.getString("university"));
				}
				
				additionalInfo = retrievePart.executeQuery(participantQuery);
				additionalInfo.next();
				tmpUni.setCurrentPart(additionalInfo.getInt(1));
				
				// retrieve number of users for this university
				String usersQuery = String.format("SELECT count(*) FROM users WHERE university = '%s'", rs.getString("university"));
				additionalInfo = retrievePart.executeQuery(usersQuery);
				additionalInfo.next();
				tmpUni.setNumOfUsers(additionalInfo.getInt(1));
				
				uniList.add(tmpUni);
			}
			
			retrievePart.close();
			retrieveUni.close();
			con.close();
			
			//put allUni at first place
			University tmp = null;
			
			for(University u : uniList){
				if(u.getName().equals("allUni")){
					tmp = u;
					break;
				}
			}

			uniList.remove(tmp);
			uniList.add(0, tmp);
			
			return uniList;
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		
		return uniList;
    	
    	
    }
    
    
    public static University retrieveUniversity(String uniName){
    	
    	University uni = null;
    	
    	String retrievePartNum = "";
    	
    	
    	ArrayList<University> uniList = retrieveUniversities();
    	
    	// this is done in order to avoid the using the string passed
    	for(int i = 0; i < uniList.size(); i++){
    		if(uniList.get(i).getName().equals(uniName)){    			
    			uni = uniList.get(i);
    			break;    			
    		}
    	}
    	
    	if(uni != null){
    		//retrieve the participants
	    	try {
	        		
	    		Class.forName(driverName);
	    		Connection con = DriverManager.getConnection(dburl, dbuser, dbpasswd);
	    		Statement stmt = con.createStatement();
	    		ResultSet rs;    		
    		
				//retrieve total number of participants if the selected uni is the meta uni
		    	if(uniName.equals(META_UNI)){
		    		retrievePartNum = "SELECT count(id) FROM participants";
		    	} else {
		    		retrievePartNum = String.format("SELECT count(id) FROM participants WHERE university = '%s'", uni.getName());
		    	}				
				
				rs = stmt.executeQuery(retrievePartNum);
				
				
				if(rs.next()){
					uni.setCurrentPart(rs.getInt(1));
				}
    			
    			stmt.close();
    			con.close();
    			
    			return uni;
    			
    			
	    	} catch (ClassNotFoundException e) {
	    		e.printStackTrace();
	    	} catch (SQLException e){
	    		e.printStackTrace();
	    	}
    	}
        	
        	
       	return uni;   		
    	
    }

    
    public static ArrayList<User> retrieveUsers(){
    	
    	ArrayList<User> userList = null;
    	
    	try {
    		
    		userList = new ArrayList<User>();
    		
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dburl, dbuser, dbpasswd);
			
			String query = "SELECT * FROM users NATURAL JOIN roles ORDER BY role_id DESC;";
			
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				
				User tmpUser = new User();
				
				tmpUser.setUsername(rs.getString("username"));
				tmpUser.setPassword(rs.getString("password"));
				tmpUser.setEmail(rs.getString("email"));
				tmpUser.setName(rs.getString("name"));
				tmpUser.setSurname(rs.getString("surname"));
				tmpUser.setUniversity(rs.getString("university"));
				tmpUser.setRights(rs.getInt("role_id"));
				tmpUser.setRightsString(rs.getString("role"));
				
				userList.add(tmpUser);
			}
			
			stmt.close();
			con.close();
			
			return userList;
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		} 
		
		return userList;
    	
    	
    }
    
    public static User retrieveUser(String username){
    	
    	username = strictTrim(username);
    	
    	User user = null;
    	
    	ArrayList<User> userList = retrieveUsers();
    	
    	for(int i = 0; i < userList.size(); i++){
    		if(userList.get(i).getUsername().equals(username)){
    			user = userList.get(i);
    			break;
    		}
    	}
    	
    	return user;
    	
    }
    
    
    
    public static ArrayList<Role> retrieveRoles(){
    	
    	ArrayList<Role> roleList = null;
    	
    	try {
    		
    		roleList = new ArrayList<Role>();
    		
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dburl, dbuser, dbpasswd);
			
			String query = "SELECT * FROM roles ORDER BY role_id DESC";
			
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				
				Role tmpRole = new Role();
				
				tmpRole.setId(rs.getInt("role_id"));
				tmpRole.setRoleName(rs.getString("role"));
				
				roleList.add(tmpRole);
			}
			
			stmt.close();
			con.close();
			
			return roleList;
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		} 
		
		return roleList;
    	
    	
    }
    
    
    public static ArrayList<String> retrieveFriday(){
    	
    	ArrayList<String> fridayList = null;
    	
    	try {
    		
    		fridayList = new ArrayList<String>();
    		
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dburl, dbuser, dbpasswd);
			
			String query = "SELECT * FROM friday_program";
			
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				
				String tmpFriday = rs.getString("activity");
				
				if(tmpFriday.equals("relax")){
					fridayList.add(0, tmpFriday);
				} else {
					fridayList.add(fridayList.size(), tmpFriday);
				}
				
			}
			
			stmt.close();
			con.close();
			
			return fridayList;
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		} 
		
		return fridayList;
    	
    	
    }
    
    public static ArrayList<String> retrieveRental(){
    	
    	ArrayList<String> rentList = null;
    	
    	try {
    		
    		rentList = new ArrayList<String>();
    		
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dburl, dbuser, dbpasswd);
			
			String query = "SELECT * FROM rental";
			
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				
				String tmpFriday = rs.getString("element");
				
				rentList.add(tmpFriday);
			}
			
			stmt.close();
			con.close();
			
			return rentList;
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		} 
		
		return rentList;
    	
    	
    }
    
    
    public static ArrayList<Article> retrieveArticles(){
    	
    	ArrayList<Article> articleList = null;
    	
    	try {
    		
    		articleList = new ArrayList<Article>();
    		
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dburl, dbuser, dbpasswd);
			
			String query = 	"SELECT a.id, a.author, u.email, a.last_edit, a.title, text " +
							"FROM articles a JOIN users u ON a.author = u.username " +
							"ORDER BY a.last_edit DESC, a.id DESC";
			
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			int index = 0;
			while(rs.next()){
				
				Article tmpArt = new Article();
				
				tmpArt.setId(rs.getInt("id"));
				tmpArt.setAuthor(rs.getString("author"));
				tmpArt.setEmail(rs.getString("email"));
				tmpArt.setLastEdit(rs.getString("last_edit"));
				tmpArt.setTitle(rs.getString("title"));
				tmpArt.setText(rs.getString("text"));
				tmpArt.setPos(index);
				
				articleList.add(tmpArt);
				index++;
			}
			
			stmt.close();
			con.close();
			
			return articleList;
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		} 
		
		return articleList;
    	
    	
    }
    
    
    public static Article retrieveArticle(String id){
    	
    	id = strictTrim(id);
    	
    	Article art = null;
    	
    	int artId = -1;
    	
    	try{
    		artId = Integer.parseInt(id);
    	} catch(java.lang.NumberFormatException e){
    		return art;
    	}
    	
    	ArrayList<Article> artList = retrieveArticles();
    	
    	for(int i = 0; i < artList.size(); i++){
    		if(artList.get(i).getId() == artId){
    			art = artList.get(i);
    			break;
    		}
    	}
    	
    	return art;   	
    	
    }
    
    
    public static ArrayList<Comment> retrieveComments(){
    	
    	ArrayList<Comment> commentList = null;
    	
    	
    	try {
    		
    		commentList = new ArrayList<Comment>();
    		
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dburl, dbuser, dbpasswd);
			
			String query = "SELECT * FROM comments ORDER BY id DESC";
			
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				
				Comment tmpComment = new Comment();
				
				tmpComment.setId(rs.getInt("id"));
				tmpComment.setText(rs.getString("text"));
				tmpComment.setArtId(rs.getInt("article_id"));
				
				commentList.add(tmpComment);
			}
			
			stmt.close();
			con.close();
			
			return commentList;
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		} 
    	
    	
    	return commentList;
    	
    }
    
    
    
    //artId is safe because it is an integer
    public static ArrayList<Comment> retrieveComments(int artId){
    	
    	ArrayList<Comment> commentList = null;
    	
    	
    	try {
    		
    		commentList = new ArrayList<Comment>();
    		
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dburl, dbuser, dbpasswd);
			
			String query = "SELECT * FROM comments WHERE article_id = " + artId + " ORDER BY id DESC";
			
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				
				Comment tmpComment = new Comment();
				
				tmpComment.setId(rs.getInt("id"));
				tmpComment.setText(rs.getString("text"));
				
				commentList.add(tmpComment);
			}
			
			stmt.close();
			con.close();
			
			return commentList;
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		} 
    	
    	
    	return commentList;
    	
    }
    
    public static Comment retrieveComment(String id){
    	
    	id = strictTrim(id);
    	
    	Comment comm = null;
    	
    	int commId = -1;
    	
    	try{
    		commId = Integer.parseInt(id);
    	} catch(java.lang.NumberFormatException e){
    		return comm;
    	}
    	
    	ArrayList<Comment> commList = retrieveComments();
    	
    	for(int i = 0; i < commList.size(); i++){
    		if(commList.get(i).getId() == commId){
    			comm = commList.get(i);
    			break;
    		}
    	}
    	
    	
    	return comm;   	
    	
    }
    
    
    public static String strictTrim(String s){
    	
    	if(s != null){
	    	s = s.trim();
	    	//all the following characters are deleted
	    	s = s.replaceAll("[!\"#$%&'()*,/:;<=>?\\^`'{|}~\\[\\]]", "");
    	}
    	else {
    		s = "";
    	}
    	
    	s = noHTMLTrim(s); 
    	
    	return s;
    }
    
    public static String noHTMLTrim(String s){
    	
    	if(s != null){
	    	s = s.trim();
	    	
	    	// at first place because otherwise it substitutes the & inserted by
	    	// other replacements
	    	s = s.replaceAll("&", "&amp;");
	    	
	    	s = s.replaceAll("<", "&lt;");
	    	s = s.replaceAll(">", "&gt;");
	    	s = s.replaceAll("è", "&egrave;");
	    	s = s.replaceAll("Ã¨", "&egrave;");
	    	s = s.replaceAll("ì", "&igrave;");
	    	s = s.replaceAll("Ã¬", "&igrave;");
	    	s = s.replaceAll("ò", "&ograve;");
	    	s = s.replaceAll("Ã²", "&ograve;");
	    	s = s.replaceAll("ö", "&ouml;");
	    	s = s.replaceAll("Ã¶", "&ouml;");
	    	s = s.replaceAll("ù", "&ugrave;");
	    	s = s.replaceAll("Ã¹", "&ugrave;");
	    	s = s.replaceAll("ü", "&uuml;");
	    	s = s.replaceAll("Ã¼", "&uuml;");
	    	s = s.replaceAll("à¼", "&uuml;");
	    	s = s.replaceAll("ä", "&auml;");
	    	s = s.replaceAll("Ã¤", "&auml;");
	    	s = s.replaceAll("à", "&agrave;");
	    	s = s.replaceAll("Ã ", "&agrave;");
	    	
	    	s = s.replaceAll("'", "&#39;");
	    	s = s.replaceAll("\"", "&quot;");
    	}
    	else {
    		s = "";
    	}
    	
    	return s;
    }
    
    public static String HTMLAllowdedTrim(String s){
    	
    	if(s != null){
	    	s = s.trim();
	    	
	    	// at first place because otherwise it substitutes the & inserted by
	    	// other replacements

	    	s = s.replaceAll("è", "&egrave;");
	    	s = s.replaceAll("Ã¨", "&egrave;");
	    	s = s.replaceAll("ì", "&igrave;");
	    	s = s.replaceAll("Ã¬", "&igrave;");
	    	s = s.replaceAll("ò", "&ograve;");
	    	s = s.replaceAll("Ã²", "&ograve;");
	    	s = s.replaceAll("ö", "&ouml;");
	    	s = s.replaceAll("Ã¶", "&ouml;");
	    	s = s.replaceAll("ù", "&ugrave;");
	    	s = s.replaceAll("Ã¹", "&ugrave;");
	    	s = s.replaceAll("ü", "&uuml;");
	    	s = s.replaceAll("Ã¼", "&uuml;");
	    	s = s.replaceAll("à¼", "&uuml;");
	    	s = s.replaceAll("ä", "&auml;");
	    	s = s.replaceAll("Ã¤", "&auml;");
	    	s = s.replaceAll("à", "&agrave;");
	    	s = s.replaceAll("Ã ", "&agrave;");
	    	
	    	s = s.replaceAll("'", "&#39;");
	    	s = s.replaceAll("\"", "&quot;");
    	}
    	else {
    		s = "";
    	}
    	
    	return s;
    }
    
    public static String valideEmail(String email){
    	
    	Pattern p = Pattern.compile("^\\.|^\\@");
        Matcher m = p.matcher(email);
        if (m.find()){
        	return null;
        }

        //Checks for email addresses that start with www
        p = Pattern.compile("^www\\.");
        m = p.matcher(email);
        if (m.find()) {
        	return null;
        }
        
        p = Pattern.compile("[^A-Za-z0-9\\.\\@_\\-~#]+");
        m = p.matcher(email);
        StringBuffer sb = new StringBuffer();
        boolean result = m.find();
        boolean deletedIllegalChars = false;

        while(result) {
           deletedIllegalChars = true;
           m.appendReplacement(sb, "");
           result = m.find();
        }

        // Add the last segment of input to the new String
        m.appendTail(sb);

        email = sb.toString();

        if (deletedIllegalChars) {
           return null;
        }
    	
    	
    	return email;
    	
    }
    
    public static String validateDate(String date){
    	
    	date = strictTrim(date);
    	
    	Pattern p = Pattern.compile("\\d\\d\\d\\d\\-\\d\\d\\-\\d\\d");
        Matcher m = p.matcher(date);
        if (m.find()){
        	return date;
        }
        
        return null;
        
    }
    
    
    //the method updates the maximum number of participants in the allUni row
    //the whole method could be substituted by a single sql statement
    //public static final String PDATE_META_UNI = String.format("UPDATE uni SET max_participants = (SELECT sum(max_participants) from uni where university != '%s'), max_teams = (SELECT sum(max_teams) from uni where university != '%s') WHERE university = '%s'", META_UNI, META_UNI, META_UNI);
    //if the database supports it
    public static void updateMetaUni(Connection con, Statement stmt) throws ClassNotFoundException, SQLException{
    	
    	Class.forName(driverName);
		con = DriverManager.getConnection(dburl, dbuser, dbpasswd);
		
		String query = "SELECT sum(max_participants) from uni where university != 'allUni'";
		stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		int totalPart = rs.getInt("sum(max_participants)");
		
		
		query = "SELECT sum(max_teams) from uni where university != 'allUni'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		int totalTeams = rs.getInt("sum(max_teams)");
		
		query = String.format("UPDATE uni SET max_participants = %d, max_teams = %d" +
				" WHERE university = 'allUni'", totalPart, totalTeams);
    	
		stmt.executeUpdate(query);
		
    }
    
}


 