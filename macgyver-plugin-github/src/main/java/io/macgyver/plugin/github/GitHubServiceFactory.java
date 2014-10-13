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
package io.macgyver.plugin.github;

import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import java.io.IOException;
import java.util.Properties;

import org.kohsuke.github.GitHub;

public class GitHubServiceFactory extends BasicServiceFactory<GitHub> {

	public GitHubServiceFactory() {
		super("github");
	}

	@Override
	protected GitHub doCreateInstance(ServiceDefinition def) {
		try {
			Properties props = def.getProperties();
			String url = props.getProperty("url");
			String oauthToken = props.getProperty("oauthToken");
			GitHub gh = null;
			if (url != null) {
				logger.info("connecting to: {}", url);
				gh = GitHub.connectToEnterprise(url, oauthToken);
			} else {
				logger.info("connecting to github.com");
				gh = GitHub.connectUsingOAuth(oauthToken);

			}
			return gh;
		} catch (IOException e) {
			throw new io.macgyver.core.ConfigurationException(
					"problem creating GitHub client", e);
		}
	}

}
