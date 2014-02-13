package io.macgyver.config;

import org.springframework.ldap.core.support.LdapContextSource;

import io.macgyver.core.ServiceFactoryBean;

public class LdapFactoryBean extends ServiceFactoryBean<LdapContextSource> {

	public LdapFactoryBean() {
		super(LdapContextSource.class);
	}

	@Override
	public LdapContextSource createObject() throws Exception {
		LdapContextSource cs = new LdapContextSource();
		cs.setUrl(getProperties().getProperty("url"));
		cs.setUserDn(getProperties().getProperty("userDn"));
		cs.setPassword(getProperties().getProperty("password"));
		cs.setReferral(getProperties().getProperty("referral"));
		cs.setBase(getProperties().getProperty("base"));
		return cs;
	}

}
