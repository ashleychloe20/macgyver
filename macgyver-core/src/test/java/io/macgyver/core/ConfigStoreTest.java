package io.macgyver.core;

import java.security.GeneralSecurityException;

import io.macgyver.core.crypto.Crypto;
import io.macgyver.test.MacgyverIntegrationTest;

import javax.json.JsonObject;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;

import com.google.common.base.Optional;

public class ConfigStoreTest extends MacgyverIntegrationTest{

	Logger logger = LoggerFactory.getLogger(ConfigStoreTest.class);
	@Autowired
	Crypto crypto;
	
	@Autowired
	ConfigStore configStore;
	

	
	@Test
	public void testX() throws GeneralSecurityException {

		JsonObject root = configStore.getRootConfig();
		logger.debug("CS: "+root);
	}
	
}
