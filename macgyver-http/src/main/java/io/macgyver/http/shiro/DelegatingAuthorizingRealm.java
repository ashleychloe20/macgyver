package io.macgyver.http.shiro;

import io.macgyver.core.Kernel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.Lists;

public class DelegatingAuthorizingRealm extends AuthorizingRealm {

	Logger logger = LoggerFactory.getLogger(DelegatingAuthorizingRealm.class);
	@Autowired
	ApplicationContext applicationContext;

	List<AuthorizingRealm> realmList = null;

	synchronized List<AuthorizingRealm> findRealms() {
		if (realmList == null) {
			Map<String, AuthorizingRealm> r = applicationContext
					.getBeansOfType(AuthorizingRealm.class);
			List<AuthorizingRealm> list = Lists.newArrayList();
			list.addAll(r.values());
			Iterator<AuthorizingRealm> t = list.iterator();
			while (t.hasNext()) {
				AuthorizingRealm realm = t.next();
				if (realm instanceof DelegatingAuthorizingRealm) {
					t.remove();
				}
			}
			realmList = list;
		}
		
		return realmList;
	}

	public static class MyCredentiaslMatcher extends SimpleCredentialsMatcher {

		@Override
		public boolean doCredentialsMatch(AuthenticationToken token,
				AuthenticationInfo info) {

			return true;
		}

	}

	public DelegatingAuthorizingRealm() {
		setCredentialsMatcher(new MyCredentiaslMatcher());
		setCacheManager(new MemoryConstrainedCacheManager());
		setCachingEnabled(true);
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {

		Object principal = token.getPrincipal();
		
		for (AuthorizingRealm realm : findRealms()) {
			logger.debug("authenticating using realm: {}",realm);
			AuthenticationInfo info = realm.getAuthenticationInfo(token);
			if (info != null) {
				return info;
			}
		}
		throw new UnknownAccountException(principal!=null ?  principal.toString(): null);

	}

}
