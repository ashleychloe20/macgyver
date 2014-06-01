package io.macgyver.core;

import io.macgyver.test.MacGyverIntegrationTest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.vfs2.FileObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.io.Files;

public class VfsManagerTest extends MacGyverIntegrationTest {

	@Autowired
	VfsManager vfsManager;

	@Test
	public void testConfigVfs() throws IOException {

		File configDir = new File("./src/test/resources/ext/config");
		configDir.mkdirs();
		
		File tempFile = new File(configDir, ".junit_"
				+ UUID.randomUUID().toString() + ".tmp");
		Files.touch(tempFile);
		try {

			FileObject fo = vfsManager.getConfigLocation();

			Assert.assertTrue(tempFile.exists());

			FileObject vfsTest2 = fo.resolveFile(tempFile.getName());
		
			Assert.assertTrue(vfsTest2.exists());
		} finally {
			tempFile.delete();
		}

	}
	

	
	@Test
	public void testWebVfs() throws IOException {

		File dir = new File("./src/test/resources/ext/web");

		File tempFile = new File(dir, ".junit_"
				+ UUID.randomUUID().toString() + ".tmp");
		Files.touch(tempFile);
		try {
			
			FileObject fo = vfsManager.getWebLocation();

			Assert.assertTrue(tempFile.exists());

			FileObject vfsTest2 = fo.resolveFile(tempFile.getName());
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

			FileObject fo = vfsManager.getScriptsLocation();

			Assert.assertTrue(tempFile.exists());

			FileObject vfsTest2 = fo.resolveFile(tempFile.getName());
			Assert.assertTrue(vfsTest2.exists());
		} finally {
			tempFile.delete();
		}

	}
	@Test
	public void testDataVfs() throws IOException {


			FileObject fo = vfsManager.getDataLocation();

			
			
			Assert.assertTrue("location should exist: "+fo,fo.exists());
	
	}
	

}
