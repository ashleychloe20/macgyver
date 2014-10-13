/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

	@Test(expected = IOException.class)
	public void testInvalidRef() throws IOException {

		provider.setGitRef("abcdef12345");
		provider.findResources();

	}

	@Test
	public void testPathNotFound() throws IOException {

		provider.setGitRef("refs/heads/master");
		try {
			provider.getResourceByPath("does/not/exist");
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

		Assert.assertNotNull(p.getResourceByPath("scripts/another.groovy"));
		Assert.assertNotNull(p.getResourceByPath("scripts/hello.groovy"));
		Assert.assertTrue(p.getResourceByPath("scripts/test/test.txt")
				.getContentAsString().startsWith("abc123"));

	}

}
