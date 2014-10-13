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
package io.macgyver.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * SoftDependencyInitializer exists so that we can avoid a cyclic dependency between
 * macgyver-core and macgyver-test.
 * 
 * @author rschoening
 *
 */
public class SoftDependencyInitializer implements
		ApplicationContextInitializer<ConfigurableApplicationContext> {

	Logger logger = LoggerFactory.getLogger(SoftDependencyInitializer.class);

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {

		ApplicationContextInitializer<ConfigurableApplicationContext> ci = null;

		try {
		
			Class<?> clazz = Class
					.forName("io.macgyver.core.SpringContextInitializer");
			ci = ((ApplicationContextInitializer<ConfigurableApplicationContext>) clazz
					.newInstance());
		} catch (Exception e) {
			logger.warn("", e);
		}
	
		if (ci != null) {
			ci.initialize(applicationContext);
		}

	}

}
