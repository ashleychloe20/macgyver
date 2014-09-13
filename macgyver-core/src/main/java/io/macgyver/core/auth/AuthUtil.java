package io.macgyver.core.auth;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

	static Logger logger = LoggerFactory.getLogger(AuthUtil.class);

	@SuppressWarnings("unchecked")
	public static boolean currentUserHasRole(String role) {
		if (role==null) {
			return false;
		}
		try {
			Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder
					.getContext().getAuthentication().getAuthorities();

			for (SimpleGrantedAuthority a : authorities) {
				if (role.equalsIgnoreCase(a.getAuthority())) {
					return true;
				}
			}
		} catch (RuntimeException e) {
			logger.warn("could not determine user role",e);
		}
		return false;
	}
	
	public static boolean currentUserHasRole(MacGyverRole role) {
		if (role==null) {
			return false;
		}
		
		return currentUserHasRole(role.toString());
	}
}
