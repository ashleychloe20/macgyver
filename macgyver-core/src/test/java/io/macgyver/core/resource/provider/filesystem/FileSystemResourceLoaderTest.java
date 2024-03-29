package io.macgyver.core.resource.provider.filesystem;

import io.macgyver.core.resource.Resource;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class FileSystemResourceLoaderTest {

	@Test
	public void testX() throws IOException {
		
		FileSystemResourceProvider rl = new FileSystemResourceProvider(new File("./src/test/resources/resource-test")) {

			@Override
			public boolean isApprovedPath(File f) {
				return true;
			}
			
		};
		
		boolean foundF1=false;
		boolean foundD1F1=false;
		for (Resource r: rl.findResources()) {
	
			if (r.getPath().equals("f1.txt")) {
				foundF1=true;
			}
			if (r.getPath().equals("d1/d1f1.txt")) {
				foundD1F1=true;
			}
		}
		
		Assert.assertTrue(foundF1);
		Assert.assertTrue(foundD1F1);
	}

}
