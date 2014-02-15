package io.macgyver.config;

import io.macgyver.core.CollaboratorRegistrationCallback;
import io.macgyver.core.ServiceFactoryBean;

import java.util.Properties;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import com.google.common.base.Optional;

public class LdapFactoryBean extends ServiceFactoryBean<LdapContextSource> {

	public LdapFactoryBean() {
		super(LdapContextSource.class);
	}

	@Override
	public LdapContextSource createObject() throws Exception {
		LdapContextSource cs = new LdapContextSource();
		
		org.springframework.beans.BeanWrapper gw = new BeanWrapperImpl(cs);
		
		assignProperties(cs, getProperties(), true);
		/*System.out.println(getProperties());
		cs.setUrl(getProperties().getProperty("url"));
		cs.setUserDn(getProperties().getProperty("userDn"));
		cs.setPassword(getProperties().getProperty("password"));
		cs.setReferral(getProperties().getProperty("referral"));
		cs.setBase(getProperties().getProperty("base"));
		*/
		cs.afterPropertiesSet();
		return cs;
	}

	
	
	@Override
	public CollaboratorRegistrationCallback getCollaboratorRegistrationCallback() {
		
	
		CollaboratorRegistrationCallback reg = new CollaboratorRegistrationCallback() {
			
			@Override
			public void registerCollaborators(CollaboratorRegistrationCallback.RegistrationDetail detail) {
				String name = detail.getPrimaryBeanName()+"Template";
				BeanDefinition bd = BeanDefinitionBuilder.rootBeanDefinition(LdapTemplate.class).addConstructorArgReference(detail.getPrimaryBeanName()).getBeanDefinition();
				detail.getRegistry().registerBeanDefinition(name, bd);
				
			}
		};
		return reg;

	}
}
