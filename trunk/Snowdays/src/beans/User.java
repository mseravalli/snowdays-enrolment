package beans;

public class User {

	private String username = null;
	private int rights = -1;
	private String rightsString = null;
	private String university = null;
	private String name;
	private String surname;
	private String password;
	private String email;
	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public int getRights(){
		return this.rights;
	}
	
	public String getUniversity(){
		return this.university;
	}
	
	public void setUsername(String user){
		this.username = user;
	}
	
	public void setRights(int r){
		this.rights = r;
	}
	
	public void setUniversity(String uni){
		this.university = uni;
	}
	
	public String getRightsString() {
		return rightsString;
	}

	public void setRightsString(String rightsString) {
		this.rightsString = rightsString;
	}
	
}