package io.macgyver.core;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;

import java.io.File;

import org.junit.Test;

public class FileConfigStoreTest {

	@Test
	public void testIt() {
		FileConfigStore fcs = new FileConfigStore(new File("./src/test/resources/ext/conf/config.groovy"));
		
	
		fcs.getRootConfigObject();
	}
	
	

}
