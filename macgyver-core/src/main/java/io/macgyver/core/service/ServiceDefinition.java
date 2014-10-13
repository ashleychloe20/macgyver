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
package io.macgyver.core.service;

import java.util.Properties;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class ServiceDefinition {
	String name;
	String primaryName;

	Properties properties = new Properties();

	public static final Boolean DEFAULT_LAZY_INIT=new Boolean(true);
	
	boolean lazyInit = DEFAULT_LAZY_INIT;

	@SuppressWarnings("rawtypes")
	ServiceFactory serviceFactory;

	@SuppressWarnings("rawtypes")
	public ServiceDefinition(String name, String primaryName, Properties props,
			ServiceFactory sf) {
		Preconditions.checkNotNull(name);
		Preconditions.checkNotNull(primaryName);
		Preconditions.checkNotNull(props);
		Preconditions.checkNotNull(sf);

		this.name = name;
		this.serviceFactory = sf;
		this.properties.putAll(props);
		this.primaryName = primaryName;
		
		if (props!=null) {
			boolean lazy = Boolean.parseBoolean(props.getProperty("lazyInit", DEFAULT_LAZY_INIT.toString()));
			setLazyInit(lazy);
		}
	}

	public boolean isCollaborator() {
		return !name.equals(primaryName);
	}

	public String getName() {
		return name;
	}

	public Properties getProperties() {
		return properties;
	}

	@SuppressWarnings("rawtypes")
	public ServiceFactory getServiceFactory() {
		return serviceFactory;
	}

	public String getPrimaryName() {
		return primaryName;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("name", name)
				.add("primaryName", primaryName).toString();
	}

	public void setLazyInit(boolean b) {
		this.lazyInit = b;
	}

	public boolean isLazyInit() {
		return lazyInit;
	}
	
	public ServiceDefinition createCollaboratorDefintiion(String suffix) {
		ServiceDefinition def = new ServiceDefinition(getName()+suffix, getName(), getProperties(), getServiceFactory());
		def.setLazyInit(isLazyInit());
		return def;
	}
}
