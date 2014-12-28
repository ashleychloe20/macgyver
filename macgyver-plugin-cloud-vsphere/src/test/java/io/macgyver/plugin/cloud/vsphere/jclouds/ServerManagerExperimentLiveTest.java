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
package io.macgyver.plugin.cloud.vsphere.jclouds;

import io.macgyver.plugin.cloud.vsphere.jclouds.VSphereApiMetadata;
import io.macgyver.test.MacGyverIntegrationTest;

import java.util.Properties;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;

import com.google.web.bindery.requestfactory.shared.impl.Constants;

public class ServerManagerExperimentLiveTest extends MacGyverIntegrationTest {

	public ServerManagerExperimentLiveTest() {

	}

	static ComputeServiceContext context;

	@Before
	public void setupContext() {
		try {
			if (context == null) {

				String url = getPrivateProperty("vcenter.url");
				String username = getPrivateProperty("vcenter.username");
				String password = getPrivateProperty("vcenter.password");
				Properties p = new Properties();

				context = ContextBuilder.newBuilder(new VSphereApiMetadata())
						.credentials(username, password).endpoint(url)
						.overrides(p).build(ComputeServiceContext.class);
			

			}
		} catch (Exception e) {
			logger.warn("could not connect", e);
			Assume.assumeTrue(false);
		}
	}

	@org.junit.Test
	@Ignore
	public void testAndExperiment() {
	/*	for (ComputeMetadata cmd: context.getComputeService().listNodes()) {
			logger.info("computeMetaData: {}",cmd);
		}
		*/
		NodeMetadata md = context.getComputeService().getNodeMetadata("42143f20-3672-298a-75b5-ba18b3390fc4");
	
	}

}
