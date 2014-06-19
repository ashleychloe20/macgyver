package io.macgyver.core.web.navigation;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

public class MenuManager {

	Logger logger = LoggerFactory.getLogger(MenuManager.class);
	@Inject
	ApplicationContext applicationContext;
	
	
	public List<MenuDecorator> getMenuDecorators() {
		List<MenuDecorator> decoratorList = Lists.newArrayList();
		
		decoratorList.addAll(applicationContext.getBeansOfType(MenuDecorator.class).values());
		return decoratorList;
	}
	
	public Menu getRootMenuForCurrentUser() {
		Menu menu = new Menu();
		for (MenuDecorator d: getMenuDecorators()) {
			try {
				d.decorate(menu);
			}
			catch (RuntimeException e) {
				logger.warn("problem invoking MenuDecorator: "+d,e);
			}
		}
		
		// Now invoke any post-decorators that were registered during initial decoration
		menu.invokePostDecorators();
	
		return menu;
	

	}
}
