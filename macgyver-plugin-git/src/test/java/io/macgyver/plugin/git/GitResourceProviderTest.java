package io.macgyver.plugin.git;

import io.macgyver.core.resource.Resource;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import com.google.common.io.Files;

public class GitResourceProviderTest {

	static File repoDir = null;
	static GitResourceProvider provider;

	@BeforeClass
	public static void setup() throws ZipException, IOException {
		File dir = Files.createTempDir();
		File zip = new File("./src/test/resources/test-repo.zip");

		ZipFile zf = new ZipFile(zip);
		zf.extractAll(dir.getAbsolutePath());
		repoDir = dir;

		provider = new GitResourceProvider(repoDir.toURI().toURL().toString(),"","");

	}

	@AfterClass
	public static void cleanup() {
		provider.close();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidRef() throws IOException {

		provider.setGitRef("abcdef12345");
		provider.findFileResources();

	}

	@Test
	public void testPathNotFound() throws IOException {

		provider.setGitRef("refs/heads/master");
		try {
			provider.getResource("does/not/exist");
			Assert.fail();
		} catch (IOException e) {

		}
	}


	@Test
	@Ignore
	public void testClone() throws GitAPIException, IOException {

		GitResourceProvider p = new GitResourceProvider(
				"https://github.com/if6was9/macgyver-resource-test.git");

		p.setGitRef("7e0ad83ff14d");

		Assert.assertNotNull(p.getResource("scripts/another.groovy"));
		Assert.assertNotNull(p.getResource("scripts/hello.groovy"));
		Assert.assertTrue(p.getResource("scripts/test/test.txt")
				.getContentAsString().startsWith("abc123"));

	}

}
