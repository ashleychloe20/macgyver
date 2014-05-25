package io.macgyver.core.auth;

import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class InternalUser {

	String username;
	String scryptHash;
	List<String> roles = Lists.newArrayList();
	
	public String getUsername() {
		return username;
	}

	public String getScryptHash() {
		return scryptHash;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String toString() {
		
		return Objects.toStringHelper(this).add("username", username).add("roles", roles).toString();
	}
}
