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
package io.macgyver.core;

import java.util.List;

import io.macgyver.core.web.vaadin.MacGyverUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.Lists;

public class PluginManager {

	@Autowired
	ApplicationContext applicationContext;
	
	
	public List<Plugin> collectPlugins() {
		
		
		return Lists.newArrayList(applicationContext.getBeansOfType(Plugin.class).values());
	}
	
	
	public void dispatchRegisterViews(MacGyverUI ui) {
		
		List<Plugin> plugins = collectPlugins();
		
		for (Plugin pi: plugins) {
			pi.registerViews(ui);
		}
	}
}
