package io.macgyver.core.web.vaadin.views.admin;

import io.macgyver.core.Plugin;
import io.macgyver.core.web.vaadin.MacGyverUI;

import com.vaadin.server.VaadinServletService;

public class AdminPlugin extends Plugin {

	@Override
	public void registerViews(MacGyverUI ui) {

		String user = VaadinServletService.getCurrentServletRequest()
				.getRemoteUser();


		
		ui.registerView(PropertyEncryptionView.VIEW_NAME, PropertyEncryptionView.class, "Admin", "Encrypt String");
		ui.registerView(ScriptsView.VIEW_NAME,ScriptsView.class,"Admin","Scripts");
		ui.registerView(BeansView.VIEW_NAME, BeansView.class, "Admin", "Spring Beans");
		
	}



}
