package io.macgyver.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

import com.google.common.base.Strings;
import com.google.common.io.Files;

@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { TestConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class MacGyverIntegrationTest extends
		AbstractJUnit4SpringContextTests {

	
	private static Properties privateProperties = new Properties();
	@Autowired
	protected ApplicationContext applicationContext;

	 protected static Logger logger = LoggerFactory.getLogger(MacGyverIntegrationTest.class);

	@BeforeClass
	public static void setup() throws IOException {


		
		
		String macGyverHome = System.getProperty("macgyver.home");
		if (Strings.isNullOrEmpty(macGyverHome)) {
			File dir = new File(".");
			File f = new File(dir, "config/services.groovy");
			if (f.exists()) {
				macGyverHome = dir.getAbsolutePath();
			}
		}
		if (Strings.isNullOrEmpty(macGyverHome)) {
			File dir = new File("./src/test/resources/ext");
			macGyverHome = dir.getAbsolutePath();
		}
		System.setProperty("macgyver.home", macGyverHome);
		logger.info("macgyver.home: "+macGyverHome);
		
		String macGyverDataDir = System.getProperty("macgyver.data.dir");
		if (Strings.isNullOrEmpty(macGyverDataDir)) {
			System.setProperty("macgyver.data.dir", Files.createTempDir()
					.getAbsolutePath());
			
		}
		logger.info("set macgyver.data.dir: "+macGyverDataDir);
		
		
		File f = new File(System.getProperty("user.home"),
				".macgyver/private-test.properties");
		if (f.exists()) {

			try (InputStream is = new FileInputStream(f)) {
				privateProperties.load(is);
			} finally {
				//
			}
		}

	}

	/**
	 * Allows integration tests to be written using properties that are held
	 * outside the project.
	 * 
	 * @param key
	 */
	public String getPrivateProperty(String key) {
		return privateProperties.getProperty(key);
	}
}
