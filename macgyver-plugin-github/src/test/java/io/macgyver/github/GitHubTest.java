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
package io.macgyver.github;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.plugin.github.GitHubServiceFactory;
import io.macgyver.test.MacGyverIntegrationTest;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRef;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;

public class GitHubTest extends MacGyverIntegrationTest {

	@Autowired
	ServiceRegistry reg;

	@Autowired
	GitHubServiceFactory githubServiceFactory;
	
	
	@Test
	public void testX() throws IOException {
		GitHub gh = (GitHub) reg.get("testGitHub");

		Assert.assertNotNull(gh);
		
		Assert.assertNotNull(githubServiceFactory);
	}
	


}
