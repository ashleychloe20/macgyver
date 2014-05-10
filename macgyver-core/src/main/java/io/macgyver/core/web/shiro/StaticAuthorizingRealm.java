package io.macgyver.core.web.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.base.Strings;

public class StaticAuthorizingRealm extends AuthorizingRealm {

	@Value("${macgyver.adminUsername:admin}")
	String adminUsername;
	
	@Value("${macgyver.adminPassword:admin}")
	String adminPassword;
	
	
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {

		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
	
		if (Strings.isNullOrEmpty(adminUsername) ||  Strings.isNullOrEmpty(adminPassword)) {
			return null;
		}
		
		if (token instanceof UsernamePasswordToken) {
			UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
			
			String username = usernamePasswordToken.getUsername();
			char[] password = usernamePasswordToken.getPassword();

			if (username!=null && username.equals(adminUsername)) {
				
				if (password!=null && adminPassword.equals(new String(password))) {
					SimpleAuthenticationInfo ai = new SimpleAuthenticationInfo(usernamePasswordToken.getPrincipal(),usernamePasswordToken.getCredentials(),getName());
					return ai;	
				}
				else {
					throw new IncorrectCredentialsException();
				}
				
			}
			
		}
		return null;
	}

}
