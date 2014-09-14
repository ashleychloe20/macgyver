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
