package io.macgyver.core.web.navigation;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

public class MenuManager {

	@Inject
	ApplicationContext applicationContext;
	
	
	public List<MenuDecorator> getMenuDecorators() {
		List<MenuDecorator> decoratorList = Lists.newArrayList();
		
		decoratorList.addAll(applicationContext.getBeansOfType(MenuDecorator.class).values());
		return decoratorList;
	}
	
	public MenuItem getRootMenuForCurrentUser() {
		MenuItem root = new JsonMenuItem(new JsonObject());
		for (MenuDecorator d: getMenuDecorators()) {
			d.decorate(root);
		}
		return root;
	

	}
}
