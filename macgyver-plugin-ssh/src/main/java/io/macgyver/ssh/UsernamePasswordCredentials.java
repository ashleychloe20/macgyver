package io.macgyver.ssh;

import com.google.common.base.Preconditions;

public class UsernamePasswordCredentials implements Credentials {

	private String username;
	private char [] password;
	
	public UsernamePasswordCredentials(String username, char [] password) {
		Preconditions.checkNotNull(username);
		Preconditions.checkNotNull(password);
		this.username = username;
		this.password = password;
	}
	@Override
	public String getUsername() {
		return username;
	}
	
	public char [] getPassword() {
		return password;
	}
}
