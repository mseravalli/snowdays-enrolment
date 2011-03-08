package beans;

public class University {
	private String name;
	private int maxPart;
	private int maxTeams;
	private int currentPart;
	private int numOfUsers;
	
	public int getCurrentPart() {
		return currentPart;
	}
	public void setCurrentPart(int actualPart) {
		this.currentPart = actualPart;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMaxPart() {
		return maxPart;
	}
	public void setMaxPart(int maxPart) {
		this.maxPart = maxPart;
	}
	public int getMaxTeams() {
		return maxTeams;
	}
	public void setMaxTeams(int maxTeams) {
		this.maxTeams = maxTeams;
	}
	public int getNumOfUsers() {
		return numOfUsers;
	}
	public void setNumOfUsers(int numOfUsers) {
		this.numOfUsers = numOfUsers;
	}
	
	
}
