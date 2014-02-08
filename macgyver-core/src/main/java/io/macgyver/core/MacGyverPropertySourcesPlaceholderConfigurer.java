package io.macgyver.core;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import io.macgyver.core.crypto.Crypto;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringValueResolver;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

public class MacGyverPropertySourcesPlaceholderConfigurer extends
		PropertySourcesPlaceholderConfigurer implements InitializingBean,
		Ordered {
	Logger logger = LoggerFactory.getLogger(getClass());

	Crypto crypto = null;

	public MacGyverPropertySourcesPlaceholderConfigurer() {

		crypto = new Crypto();
		Crypto.instance = crypto;

	}

	protected Properties processProperties() {

		try {
			File f = new File(Kernel.determineExtensionDir(),
					"conf/config.groovy");
			if (f.exists()) {

				Optional<String> profile = Kernel.getExecutionProfile();

				ConfigSlurper cs = new ConfigSlurper();
				if (profile.isPresent()) {
					logger.info("sourcing {} with profile '{}'", f,
							profile.get());
					cs.setEnvironment(profile.get());
				} else {
					logger.info("sourcing {} with no profile '{}'");
				}
				ConfigObject co = cs.parse(f.toURI().toURL());
				Properties p = co.toProperties();
				logger.debug("keys: {}", p);
				return p;
			} else {
				logger.warn("config not found: {}", f.getAbsolutePath());
			}
			return new Properties();
		} catch (MalformedURLException e) {
			throw new ConfigurationException(e);
		}
	}

	public Optional<File> findLocalPropertiesFile() {
		String configFile = System.getProperty("macgyver.configFile");

		if (Strings.isNullOrEmpty(configFile)) {
			return Optional.absent();
		}
		File f = new File(configFile).getAbsoluteFile();
		if (!f.exists()) {
			logger.warn("-Dmacgyver.configFile not found: {}",
					f.getAbsolutePath());
			return Optional.absent();
		}
		return Optional.of(f);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		FileSystemResource testResource = new FileSystemResource(new File(".",
				"src/test/resources/macgyver-test.properties"));
		List<Resource> rlist = Lists.newArrayList();

		Optional<File> configFile = findLocalPropertiesFile();
		if (configFile.isPresent()) {
			rlist.add(new FileSystemResource(configFile.get()));
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
		String val =  crypto.decryptStringWithPassThrough(originalValue);		
		return val;
	}

}
