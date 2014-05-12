package io.macgyver.core;

import io.macgyver.core.testconfig.CoreIntegrationTestConfig;

import org.apache.tomcat.jni.File;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;


@ContextConfiguration(loader=AnnotationConfigContextLoader.class,  classes={CoreIntegrationTestConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class CoreIntegrationTestCase extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	protected ApplicationContext applicationContext;
	

	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@BeforeClass 
	public static void setup() {
		System.setProperty("macgyver.ext.location", new java.io.File("./src/test/resources/ext").getAbsolutePath());
		//Kernel.initialize();
	}
}