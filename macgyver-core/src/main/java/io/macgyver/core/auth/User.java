package io.macgyver.core.auth;

import java.util.List;

public class User {

	String username;
	List<String> roles;
	String scryptHash;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public String getScryptHash() {
		return scryptHash;
	}
	public void setScryptHash(String scryptHash) {
		this.scryptHash = scryptHash;
	}
	
}
