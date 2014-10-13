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
package io.macgyver.plugin.splunk;

import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import java.util.Properties;

import com.splunk.Service;
import com.splunk.ServiceArgs;

public class SplunkServiceFactory extends BasicServiceFactory<Service>{

	public SplunkServiceFactory() {
		super("Splunk");
		
	}

	@Override
	protected Service doCreateInstance(ServiceDefinition def) {
		
		
		Properties p = def.getProperties();
		
		ServiceArgs args = new ServiceArgs();
		String username = p.getProperty("username");
		String password = p.getProperty("password");
		String host = p.getProperty("host");
		String port = p.getProperty("port","8089");
		String scheme = p.getProperty("scheme");
		String token = p.getProperty("token");
		
		if (username!=null) {
			args.setUsername(username);
		}
		if (password!=null) {
			args.setPassword(password);
		}
		if (host!=null) {
			args.setHost(host);
		}
		if (port!=null) {
			args.setPort(Integer.parseInt(port));
		}
		if (token!=null) {
			args.setToken(token);
		}
		if (scheme!=null) {
			args.setScheme(scheme);
		}
		
		Service service = Service.connect(args);
		return service;
	}



}
