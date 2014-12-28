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
package io.macgyver.plugin.cloud.aws;

import java.util.Properties;
import java.util.Set;

import io.macgyver.test.MacGyverIntegrationTest;

import org.jclouds.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.domain.Location;
import org.jclouds.ec2.EC2ApiMetadata;
import org.jclouds.ec2.compute.EC2ComputeService;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.junit.Assume;
import org.junit.Test;
import org.springframework.boot.logging.log4j.Log4JLoggingSystem;

import com.google.common.collect.ImmutableSet;

public class TestJClouds extends MacGyverIntegrationTest {

	@Test
	public void testX() {

		String awsAccessKey = getPrivateProperty("aws.accessKey");
		String awsSecretKey = getPrivateProperty("aws.secretKey");

		Assume.assumeTrue(awsAccessKey!=null);
		Assume.assumeTrue(awsSecretKey!=null);
		
		Properties overrides = new Properties();
		// overrides.setProperty(Constants.PROPERTY_PROXY_HOST, "localhost");
		// overrides.setProperty(Constants.PROPERTY_PROXY_PORT, "8888");
		overrides.setProperty(Constants.PROPERTY_TRUST_ALL_CERTS, "true");
		overrides.setProperty(Constants.PROPERTY_RELAX_HOSTNAME, "true");
		overrides.setProperty("jclouds.regions",
				"us-west-1,us-west-2,us-east-1");
		ComputeServiceContext context = ContextBuilder.newBuilder("aws-ec2")
				.overrides(overrides).credentials(awsAccessKey, awsSecretKey)
				.modules(ImmutableSet.of(new SLF4JLoggingModule()))
				.buildView(ComputeServiceContext.class);

		// http://jclouds.apache.org/guides/aws/
		// https://github.com/jclouds/jclouds-examples/blob/master/compute-basics/src/main/java/org/jclouds/examples/compute/basics/MainApp.java

		Set<? extends Location> locations = context.getComputeService()
				.listAssignableLocations();
		for (Location location : locations) {
			logger.debug("location: " + location);
		}

		ComputeService svc = context.getComputeService();

		for (ComputeMetadata cn : svc.listNodes()) {
			NodeMetadata nm = (NodeMetadata) cn;
			logger.debug("id: {}",nm.getId());
			logger.debug("location: {}",nm.getLocation());
			logger.debug("userMetadata: {}",nm.getUserMetadata());
			logger.debug("privateAddresses: {}",nm.getPrivateAddresses());
			logger.debug("name: {}",nm.getName());

		}

	}
}
