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
package io.macgyver.config;

import io.macgyver.core.factory.TestBeanServiceFactory;
import io.macgyver.core.web.mvc.MacgyverWeb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreTestConfig {
	@Bean
	public TestBeanServiceFactory macTestBeanFactory() {
		return new TestBeanServiceFactory();
	}
	
	@Bean
	public MacgyverWeb macWEb() {
		return new MacgyverWeb();
	}
}


