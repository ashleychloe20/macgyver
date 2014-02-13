package io.macgyver.core;

import java.util.Properties;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public abstract class CollaboratorRegistrationCallback {

	public abstract void registerCollaborators(RegistrationDetail detail);
	
	public static class RegistrationDetail {

		BeanDefinitionRegistry registry;
		Properties properties;
		BeanDefinition primaryBeanDefinition;
		String primaryBeanName;
		public BeanDefinitionRegistry getRegistry() {
			return registry;
		}
		public void setRegistry(BeanDefinitionRegistry registry) {
			this.registry = registry;
		}
		public Properties getProperties() {
			return properties;
		}
		public void setProperties(Properties properties) {
			this.properties = properties;
		}
		public BeanDefinition getPrimaryBeanDefinition() {
			return primaryBeanDefinition;
		}
		public void setPrimaryBeanDefinition(BeanDefinition primaryBeanDefinition) {
			this.primaryBeanDefinition = primaryBeanDefinition;
		}
		public String getPrimaryBeanName() {
			return primaryBeanName;
		}
		public void setPrimaryBeanName(String primaryBeanName) {
			this.primaryBeanName = primaryBeanName;
		}

	}
}
