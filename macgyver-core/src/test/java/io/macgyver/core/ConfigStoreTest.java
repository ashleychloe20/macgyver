package io.macgyver.core;

import javax.json.JsonObject;

import org.junit.Assert;
import org.junit.Test;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;

import com.google.common.base.Optional;

public class ConfigStoreTest {

	ConfigStore cs = new ConfigStore() {

		@Override
		public Optional<ConfigObject> getRootConfigObject() {
			return Optional.of(new ConfigSlurper().parse("a=1\nfoo='bar'\ntest {\nb=2\n}\n"));
			
		}
		
	};
	

	
	@Test
	public void testX() {
		JsonObject root = cs.getRootConfig();
		System.out.println(root);
	}
	
}
