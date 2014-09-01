/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.servermanager.compute;

import io.macgyver.jclouds.vsphere.VSphereApiMetadata;
import io.macgyver.test.MacGyverIntegrationTest;

import java.util.Properties;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.junit.Assume;
import org.junit.Before;

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
				context.getComputeService().listNodes();  // force a connection

			}
		} catch (Exception e) {
			logger.warn("could not connect", e);
			Assume.assumeTrue(false);
		}
	}

	@org.junit.Test
	public void testAndExperiment() {
		for (ComputeMetadata cmd: context.getComputeService().listNodes()) {
			System.out.println(cmd);
		}
		

	}

}
