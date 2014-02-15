package io.macgyver.core;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import io.macgyver.config.CoreConfig;
import io.macgyver.core.crypto.Crypto;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class MacGyverPropertySourcesPlaceholderConfigurer extends
		PropertySourcesPlaceholderConfigurer implements InitializingBean,
		BeanDefinitionRegistryPostProcessor, Ordered {
	Logger logger = LoggerFactory.getLogger(getClass());
	public static final String MACGYVER_PROPERTIES_CONFIG_SYSTEM_PROPERTY = "macgyver.propertiesConfigFile";
	public static final String MACGYVER_GROOVY_CONFIG_SYSTEM_PROPERTY = "macgyver.groovyConfigFile";
	Crypto crypto = null;

	Properties effectiveProperties = new Properties();
	ServiceFactoryClassFinder locator = null;

	public MacGyverPropertySourcesPlaceholderConfigurer() {

		crypto = new Crypto();
		Crypto.instance = crypto;

	}

	protected Properties processProperties() {

		try {
			File f = findGroovyConfigFile();

			if (f.exists()) {

				Optional<String> profile = Kernel.getExecutionProfile();

				ConfigSlurper cs = new ConfigSlurper();
				if (profile.isPresent()) {
					logger.info("sourcing {} with profile '{}'", f,
							profile.get());
					cs.setEnvironment(profile.get());
				} else {
					logger.info("sourcing {} with no profile", f);
				}
				ConfigObject co = cs.parse(f.toURI().toURL());
				Properties p = co.toProperties();
				return p;
			} else {
				logger.warn("config not found: {}", f.getAbsolutePath());
			}
			return new Properties();
		} catch (MalformedURLException e) {
			throw new ConfigurationException(e);
		}
	}

	private Optional<File> findConfigFileViaSystemProperty(String sysprop) {

		String configLocation = System.getProperty(sysprop);

		if (Strings.isNullOrEmpty(configLocation)) {
			return Optional.absent();
		}
		File f = new File(configLocation).getAbsoluteFile();
		if (!f.exists()) {
			logger.warn("-D{}={} specified but file not found", sysprop,
					configLocation);
			return Optional.absent();
		}
		return Optional.of(f);
	}

	public File findGroovyConfigFile() {
		File groovyConfig = new File(Kernel.determineExtensionDir(),
				"conf/config.groovy");
		Optional<File> of = findConfigFileViaSystemProperty(MACGYVER_GROOVY_CONFIG_SYSTEM_PROPERTY);
		groovyConfig = of.isPresent() ? of.get() : groovyConfig;

		return groovyConfig;
	}

	public File findPropertiesConfigFile() {
		File propertiesFile = new File(Kernel.determineExtensionDir(),
				"conf/config.properties");
		Optional<File> of = findConfigFileViaSystemProperty(MACGYVER_PROPERTIES_CONFIG_SYSTEM_PROPERTY);
		if (of.isPresent()) {
			propertiesFile = of.get();
		}
		return propertiesFile;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		FileSystemResource testResource = new FileSystemResource(new File(".",
				"src/test/resources/macgyver-test.properties"));
		List<Resource> rlist = Lists.newArrayList();

		File configFile = findPropertiesConfigFile();
		if (configFile.exists()) {
			rlist.add(new FileSystemResource(configFile));
		}

		if (testResource.exists()) {

			rlist.add(testResource);
		} else {
			logger.info("not using {} because file does not exist",
					testResource.getFile().getAbsolutePath());
		}

		setLocations(rlist.toArray(new Resource[0]));
		setIgnoreResourceNotFound(true);
		setIgnoreUnresolvablePlaceholders(false);

		setLocalOverride(true);

		Properties p = crypto.decryptProperties(processProperties());

		setProperties(p);
		effectiveProperties.putAll(p);
	}

	boolean alreadyConverted = false;

	/*
	 * This is needed because of https://jira.springsource.org/browse/SPR-8928
	 */
	@Override
	protected Properties mergeProperties() throws IOException {
		final Properties mergedProperties = super.mergeProperties();
		convertProperties(mergedProperties);
		return mergedProperties;
	}

	/*
	 * This is needed because of https://jira.springsource.org/browse/SPR-8928
	 */
	@Override
	protected void convertProperties(final Properties props) {
		if (!this.alreadyConverted) {
			super.convertProperties(props);
			this.alreadyConverted = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.config.PropertyResourceConfigurer#
	 * convertPropertyValue(java.lang.String)
	 */
	@Override
	protected String convertPropertyValue(final String originalValue) {
		String val = crypto.decryptStringWithPassThrough(originalValue);
		return val;
	}

	protected ServiceFactoryClassFinder getLocator() {
		if (locator == null) {
			locator = new ServiceFactoryClassFinder();
		}
		return locator;
	}

	protected void autoRegisterBean(String name, String serviceType,
			Properties props, BeanDefinitionRegistry beanDefinitionRegistry) {
		try {
			logger.info("autoRegister name={} serviceType={} props={}", name,
					serviceType, props.keySet());

			BeanDefinition beanDefinition = null;
			boolean useBuilder = true;
			ServiceFactoryClassFinder locator = getLocator();

			Class<ServiceFactoryBean> clazz = locator
					.forServiceType(serviceType);

			if (clazz==null) {
				logger.warn("no ServiceFactoryBean registered for serviceType='{}'",serviceType);
				return;
			}
			beanDefinition = BeanDefinitionBuilder
					.rootBeanDefinition(clazz.getName())
					.addPropertyValue("properties", props).getBeanDefinition();
			
			ServiceFactoryBean serviceFactoryBean = clazz.newInstance();

			CollaboratorRegistrationCallback reg = serviceFactoryBean
					.getCollaboratorRegistrationCallback();

			beanDefinitionRegistry.registerBeanDefinition(name, beanDefinition);
			if (reg != null) {
				CollaboratorRegistrationCallback.RegistrationDetail detail = new CollaboratorRegistrationCallback.RegistrationDetail();
				detail.setPrimaryBeanDefinition(beanDefinition);
				detail.setPrimaryBeanName(name);
				detail.setProperties(props);
				detail.setRegistry(beanDefinitionRegistry);
				reg.registerCollaborators(detail);
			}

		} catch (InstantiationException e) {
			throw new MacGyverException(e);
		} catch (ClassNotFoundException e) {
			throw new MacGyverException(e);
		} catch (IllegalAccessException e) {
			throw new MacGyverException(e);
		}

	}

	@Override
	public void postProcessBeanDefinitionRegistry(
			BeanDefinitionRegistry registry) throws BeansException {
		List<String> names = Lists.newArrayList();
		for (Object k : effectiveProperties.keySet()) {
			String key = k.toString();
			if (key.endsWith("serviceType")) {
				String val = effectiveProperties.getProperty(key);
				names.add(key.substring(0,
						key.length() - ".serviceType".length()));
			}
		}

		for (String name : names) {
			int dotIdx = name.lastIndexOf(".");
			String unqualifiedName = name;
			if (dotIdx >= 0) {
				unqualifiedName = unqualifiedName.substring(dotIdx + 1);
			}
			Properties scoped = new Properties();
			for (Map.Entry<Object, Object> entry : effectiveProperties
					.entrySet()) {
				String key = entry.getKey().toString();
				if (key.startsWith(name + ".")) {
					String scopedKey = key.substring((name + ".").length());
					scoped.setProperty(scopedKey, entry.getValue().toString());
				}
			}
			String serviceType = scoped.getProperty("serviceType");
			scoped.remove("serviceType");
			autoRegisterBean(unqualifiedName, serviceType, scoped, registry);
		}
	}
}
