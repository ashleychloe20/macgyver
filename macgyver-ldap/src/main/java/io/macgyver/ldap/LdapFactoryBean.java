package io.macgyver.ldap;

import io.macgyver.core.factory.ServiceFactory;

import java.util.Properties;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

public class LdapFactoryBean extends ServiceFactory<LdapContextSource> {

	public LdapFactoryBean(String name) {
		super(name);
	}

	@Override
	public LdapContextSource createObject(Properties props) {
		LdapContextSource cs = new LdapContextSource();

		org.springframework.beans.BeanWrapper gw = new BeanWrapperImpl(cs);

		assignProperties(cs, props, true);

		cs.afterPropertiesSet();
		return cs;
	}

	@Override
	public void configure(String name, Properties props) {
		super.configure(name, props);
		addCollaboratorRelationship(name + "Template", name);
	}

	@Override
	protected void registerCollaborators(String name, Object primary) {
		super.registerCollaborators(name, primary);
		LdapTemplate template = new LdapTemplate((LdapContextSource) primary);
		super.registry.registerCollaborator(name+"Template", template);
	}


}
