package io.macgyver.core.web.shiro;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.mapdb.DB;
import org.mapdb.TxBlock;
import org.mapdb.TxMaker;
import org.mapdb.TxRollbackException;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;
import com.lambdaworks.crypto.SCryptUtil;

public class InternalAuthorizingRealm extends AuthorizingRealm {

	public static final String SHADOW_MAP_NAME="internal_shadow";
	public static final String SCRYPT_FIELD_NAME="scryptHash";
	public static final String USERNAME_FIELD="username";
	public static final String ADMIN_USERNAME="admin";
	public static final String ADMIN_DEFAULT_PASSWORD="admin";
	
	@Autowired
	TxMaker txMaker;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {

		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {


		if (token instanceof UsernamePasswordToken) {

			UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

			final String username = usernamePasswordToken.getUsername();
			char[] password = usernamePasswordToken.getPassword();
			final AtomicReference<String> scryptHash = new AtomicReference<>();
			TxBlock b = new TxBlock() {

				@Override
				public void tx(DB db) throws TxRollbackException {
					Map<String, Map<String, String>> m = db
							.get(SHADOW_MAP_NAME);
					Map<String, String> shadowData = m.get(username);
					String hashVal = shadowData.get(SCRYPT_FIELD_NAME);

				
					scryptHash.set(hashVal);

				}
			};

			txMaker.execute(b);

			boolean success = SCryptUtil.check(new String(password),
					scryptHash.get());
			if (success) {
				SimpleAuthenticationInfo ai = new SimpleAuthenticationInfo(
						usernamePasswordToken.getPrincipal(),
						usernamePasswordToken.getCredentials(), getName());
				return ai;
			} else {
				throw new IncorrectCredentialsException();
			}

		}

		return null;
	}

	public void seedData() {
TxBlock b = new TxBlock() {
			
			@Override
			public void tx(DB db) throws TxRollbackException {
				Map<String,Map<String,String>> m = db.getTreeMap(SHADOW_MAP_NAME);
				
				if (!m.containsKey(ADMIN_USERNAME)) {
					System.out.println("Addming admin");
					Map<String,String> adminEntry = Maps.newHashMap();
					adminEntry.put(USERNAME_FIELD, ADMIN_USERNAME);
					int N = 16384;
			        int r = 8;
			        int p = 1;
					String hashed = SCryptUtil.scrypt(ADMIN_DEFAULT_PASSWORD, N,r,p);
					adminEntry.put(SCRYPT_FIELD_NAME, hashed);
				
					m.put(ADMIN_USERNAME, adminEntry);
				}
				
				
			}
		};
		txMaker.execute(b);
	}
}
