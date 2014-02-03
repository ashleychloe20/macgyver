package io.macgyver.ldap;

import javax.json.JsonObject;

import io.macgyver.core.ServiceFactory;

import org.springframework.ldap.core.support.LdapContextSource;

public class LdapContextSourceFactory extends ServiceFactory<LdapContextSource> {

	public LdapContextSourceFactory() {
		super("ldap");
	}

	@Override
	public LdapContextSource create(String name, JsonObject cfg) {
		LdapContextSource cs = new LdapContextSource();

		String base = cfg.getString("base");
		if (base != null) {
			cs.setBase(base);
		}
		

		String url = cfg.getString("url");
		if (url != null) {
			cs.setUrl(url);
		}

		String userDn = cfg.getString("userDn");
		if (userDn != null) {
			cs.setUserDn(userDn);
		}
		
		String password = cfg.getString("password");
		if (password != null) {
			cs.setUserDn(password);
		}
	
		return cs;
	}

}
