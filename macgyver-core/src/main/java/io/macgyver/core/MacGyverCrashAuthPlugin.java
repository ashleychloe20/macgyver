package io.macgyver.core;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.crsh.auth.AuthenticationPlugin;
import org.crsh.plugin.CRaSHPlugin;
import org.springframework.beans.factory.annotation.Autowired;

public class MacGyverCrashAuthPlugin extends CRaSHPlugin<AuthenticationPlugin>
		implements AuthenticationPlugin {

	@Autowired
	SecurityManager securityManager;

	@Override
	public AuthenticationPlugin getImplementation() {
		return this;
	}

	@Override
	public boolean authenticate(String username, Object password)
			throws Exception {
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(username,
					password.toString().toCharArray());
			ThreadContext.bind(securityManager);
		
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.login(token);
			
			return true;
		} catch (IllegalArgumentException e) {
			// This is hack
			if (e.getMessage().contains(
					
				
					"SessionContext must be an HTTP compatible implementation")) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String getName() {
		return "macgyvercrash";
	}

	@Override
	public Class getCredentialType() {
		// TODO Auto-generated method stub
		return String.class;
	}

}