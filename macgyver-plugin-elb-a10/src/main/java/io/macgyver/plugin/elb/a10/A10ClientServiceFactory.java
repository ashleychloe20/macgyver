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
package io.macgyver.plugin.elb.a10;

import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.core.service.ServiceFactory;
import io.macgyver.core.service.ServiceRegistry;

import java.util.Set;

public class A10ClientServiceFactory extends ServiceFactory<A10Client> {

	public A10ClientServiceFactory() {
		super("a10");

	}

	@Override
	protected A10Client doCreateInstance(ServiceDefinition def) {

		A10Client c = new A10Client(def.getProperties().getProperty("url"), def
				.getProperties().getProperty("username"), def.getProperties()
				.getProperty("password"));

		try {
			if (!Boolean.parseBoolean(def.getProperties().getProperty(
					CERTIFICATE_VERIFICATION_ENABLED, "false"))) {
				c.setCertificateVerificationEnabled(false);
			}
		} catch (Exception e) {
			logger.warn("",e);
		}

		return c;
	}

	@Override
	protected void doCreateCollaboratorInstances(ServiceRegistry registry,
			ServiceDefinition primaryDefinition, A10Client primaryBean) {

	}

	@Override
	public void doCreateCollaboratorDefinitions(Set<ServiceDefinition> defSet,
			ServiceDefinition def) {

	}

}
