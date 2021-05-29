package cz.railway_system.pojo;

public class AuthenticationRequest {

	private String username;
	private String password;
	
// Bezparametrový konstruktor /////////////////////////////////////////////////////////////	
	
	public AuthenticationRequest() {
		
	}

// Gettery + Settery //////////////////////////////////////////////////////////////////////	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
