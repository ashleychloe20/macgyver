package io.macgyver.core;

import io.macgyver.core.resource.Resource;
import io.macgyver.core.script.ExtensionResourceProvider;
import io.macgyver.test.MacGyverIntegrationTest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.tools.FileObject;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.io.Files;

public class VfsManagerTest extends MacGyverIntegrationTest {

	@Autowired
	VfsManager vfsManager;

	
	@Autowired
	ExtensionResourceProvider resourceLoader;

	
	@Test
	public void testWebVfs() throws IOException {

		File dir = new File("./src/test/resources/ext/web");

		File tempFile = new File(dir, ".junit_"
				+ UUID.randomUUID().toString() + ".tmp");
		Files.touch(tempFile);
		try {
			
			File fo = vfsManager.getWebLocation();

			Assert.assertTrue(tempFile.exists());

			File vfsTest2 = vfsManager.resolveWeb(tempFile.getName());
			Assert.assertTrue(vfsTest2.exists());
		} finally {
			tempFile.delete();
		}

	}
	
	@Test
	public void testScriptsVfs() throws IOException {

		File dir = new File("./src/test/resources/ext/scripts");

		File tempFile = new File(dir, ".junit_"
				+ UUID.randomUUID().toString() + ".tmp");
		Files.touch(tempFile);
		try {

			Resource r = resourceLoader.getResourceByPath("scripts/"+tempFile.getName());
			
			Assert.assertNotNull(r);
			
			Assert.assertTrue(tempFile.exists());

	
		} finally {
			tempFile.delete();
		}

	}
	
	

}
