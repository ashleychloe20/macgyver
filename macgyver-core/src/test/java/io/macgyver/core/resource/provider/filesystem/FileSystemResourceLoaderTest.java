package io.macgyver.core.resource.provider.filesystem;

import io.macgyver.core.resource.Resource;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class FileSystemResourceLoaderTest {

	@Test
	public void testX() throws IOException {
		
		FileSystemResourceLoader rl = new FileSystemResourceLoader(new File("./src/test/resources/resource-test"));
		
		for (Resource r: rl.findFileResources()) {
			System.out.println(r +"  "+r.getSha1());
		}
	}

}
