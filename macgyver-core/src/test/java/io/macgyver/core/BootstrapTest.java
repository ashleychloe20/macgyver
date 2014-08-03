package io.macgyver.core;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import io.macgyver.test.MacGyverIntegrationTest;

import org.junit.Test;

public class BootstrapTest extends MacGyverIntegrationTest {

	@Test
	public void testPaths() throws IOException {

		Bootstrap bootstrap = Bootstrap.getInstance();
		assertNotNull(bootstrap);
		assertSame(bootstrap, Bootstrap.getInstance());
		
		File d = new File("./src/test/resources/ext").getCanonicalFile();
		assertEquals(d,Bootstrap.getInstance().getExtensionDir().getCanonicalFile());
		assertEquals(new java.io.File("./src/test/resources/ext/config").getCanonicalPath(),Bootstrap.getInstance().getConfigDir().getCanonicalPath());
		assertEquals(new java.io.File("./src/test/resources/ext/web").getCanonicalPath(),Bootstrap.getInstance().getWebDir().getCanonicalPath());
		assertEquals(new java.io.File("./src/test/resources/ext/scripts").getCanonicalPath(),Bootstrap.getInstance().getScriptsDir().getCanonicalPath());
		
	}
}
