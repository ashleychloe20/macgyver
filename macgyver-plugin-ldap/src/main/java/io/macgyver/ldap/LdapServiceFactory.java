package io.macgyver.ldap;

import io.macgyver.core.MacGyverException;
import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.core.service.ServiceFactory;
import io.macgyver.core.service.ServiceRegistry;

import java.util.Properties;
import java.util.Set;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import com.google.common.base.Throwables;

public class LdapServiceFactory extends ServiceFactory<LdapContextSource> {

	public LdapServiceFactory(String name) {
		super(name);
	}

	@Override
	public LdapContextSource doCreateInstance(ServiceDefinition def) {
		try {
			Properties props = def.getProperties();
			LdapContextSource cs = new LdapContextSource();

			org.springframework.beans.BeanWrapper gw = new BeanWrapperImpl(cs);

			assignProperties(cs, props, true);

			cs.afterPropertiesSet();
			return cs;
		} catch (Exception e) {
			Throwables.propagateIfPossible(e);
			throw new MacGyverException(e);
		}
	}

	@Override
	protected void doCreateCollaboratorInstances(ServiceRegistry registry,
			ServiceDefinition primaryDefinition, LdapContextSource primaryBean) {
		LdapTemplate template = new LdapTemplate(
				(LdapContextSource) primaryBean);
		registry.registerCollaborator(primaryDefinition.getName() + "Template",
				template);

	}

	@Override
	public void doCreateCollaboratorDefinitions(Set<ServiceDefinition> defSet,
			ServiceDefinition def) {
		ServiceDefinition templateDef = new ServiceDefinition(def.getName()
				+ "Template", def.getName(), def.getProperties(), this);
		defSet.add(templateDef);

	}

}
