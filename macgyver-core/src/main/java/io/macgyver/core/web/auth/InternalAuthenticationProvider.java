package io.macgyver.core.web.auth;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.mapdb.DB;
import org.mapdb.TxBlock;
import org.mapdb.TxMaker;
import org.mapdb.TxRollbackException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lambdaworks.crypto.SCryptUtil;

public class InternalAuthenticationProvider implements AuthenticationProvider {

	org.slf4j.Logger logger = LoggerFactory.getLogger(InternalAuthenticationProvider.class);
	
	@Autowired
	TxMaker txMaker;

	@Override
	public Authentication authenticate(final Authentication authentication)
			throws AuthenticationException {
		final AtomicReference<UsernamePasswordAuthenticationToken> authToken = new AtomicReference<>();
		
		final String username = Objects.toString(authentication
				.getPrincipal());
		final String password = Objects.toString(authentication
				.getCredentials());
		logger.info("authenticating: {}", username);
		TxBlock b = new TxBlock() {

			

			@Override
			public void tx(DB db) throws TxRollbackException {
				Map<String, Map<String, String>> x = db.get("internalUsers");
				if (x == null) {
					authToken.set(null);
				} else {
					

					Map<String, String> userData = x.get(username);
					if (userData == null) {
						authToken.set(null);
					} else {
						String hash = userData.get("scryptHash");

						
						boolean b = SCryptUtil.check(password, hash);
						
						if (b) {
							GrantedAuthority ga = new GrantedAuthority() {
								
								@Override
								public String getAuthority() {
									return "XXX";
								}
							};
							
						
							UsernamePasswordAuthenticationToken upt = new UsernamePasswordAuthenticationToken(username,password,Lists.newArrayList(ga));
							authToken.set(upt);
						}
					}
				}
			}
		};
		try {
		txMaker.execute(b);
		}
		catch (RuntimeException e) {
			logger.warn(e.toString());
		}
		return authToken.get();
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class
				.isAssignableFrom(authentication);

	}

	public void seedData() {
		TxBlock b = new TxBlock() {

			@Override
			public void tx(DB db) throws TxRollbackException {
				Map<String, Map<String, String>> x = db
						.getTreeMap("internalUsers");
				Map<String, String> adminUser = x.get("admin");
				if (adminUser == null) {
					String defaultAdminHash = "$s0$c0801$DGeuLMLEl1cPAMy3uNp9cQ==$4JTLftVGoUc/3fKrlflSfG904uVx+TdWPJTp6GT+5XA=";
					defaultAdminHash = SCryptUtil.scrypt("password", 2048,8,1);
					adminUser = Maps.newHashMap();
					adminUser.put("username", "admin");
					adminUser.put("scryptHash", defaultAdminHash);
					x.put("admin", adminUser);
				}

			}
		};
		txMaker.execute(b);
	}
}
