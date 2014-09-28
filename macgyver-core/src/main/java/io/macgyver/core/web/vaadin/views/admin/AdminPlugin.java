package io.macgyver.core.web.vaadin.views.admin;

import io.macgyver.core.Plugin;
import io.macgyver.core.web.vaadin.MacGyverUI;

import com.vaadin.server.VaadinServletService;

public class AdminPlugin extends Plugin {

	@Override
	public void registerViews(MacGyverUI ui) {

		String user = VaadinServletService.getCurrentServletRequest()
				.getRemoteUser();


		
		ui.registerView(PropertyEncryptionView.class);
		ui.registerView(ScriptsView.class);
		ui.registerView(BeansView.class);
		ui.registerView(ClusterView.class);
		
	}



}
