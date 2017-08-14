package br.com.tiagoamp.aton.model;

public class User {
	
	public User() {
	}
	
	public User(String email, String name, Role role) {
		this.email = email;
		this.name = name;
		this.role = role;
	}
	
	
	private String email;
	private String name;	
	private Role role;
	
	
	@Override
    public String toString() {
    	StringBuilder r = new StringBuilder(name + " - " + email + " - " + role); 
    	return r.toString();
    }

	
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
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
				
}
