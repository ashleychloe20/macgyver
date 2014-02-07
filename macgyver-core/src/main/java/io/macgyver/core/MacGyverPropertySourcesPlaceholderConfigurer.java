package io.macgyver.core;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import io.macgyver.core.crypto.Crypto;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.google.common.collect.Lists;

public class MacGyverPropertySourcesPlaceholderConfigurer extends
		PropertySourcesPlaceholderConfigurer {
	Logger logger = LoggerFactory.getLogger(getClass());

	Crypto crypto = null;

	public MacGyverPropertySourcesPlaceholderConfigurer() {

		crypto = new Crypto();
		Crypto.instance = crypto;

		FileSystemResource testResource = new FileSystemResource(new File(".",
				"src/test/resources/macgyver-test.properties"));
		List<Resource> rlist = Lists.newArrayList();

		if (testResource.exists()) {
			rlist.add(testResource);
		} else {
			logger.info("not using {} because file does not exist",
					testResource.getFile().getAbsolutePath());
		}

		setLocations(rlist.toArray(new Resource[0]));
		setIgnoreResourceNotFound(true);
		setIgnoreUnresolvablePlaceholders(true);
		setLocalOverride(true);

		Properties p = crypto.decryptProperties(processProperties());

		setProperties(processProperties());
	}

	protected Properties processProperties() {

		try {
			File f = new File(Kernel.determineExtensionDir(),
					"conf/config.groovy");
			if (f.exists()) {
				ConfigSlurper cs = new ConfigSlurper();
				ConfigObject co = cs.parse(f.toURI().toURL());
				Properties p = co.toProperties();
				logger.debug("keys: {}", p.keySet());
				return p;
			} else {
				logger.warn("config not found: {}", f.getAbsolutePath());
			}
			return new Properties();
		} catch (MalformedURLException e) {
			throw new ConfigurationException(e);
		}
	}

}
