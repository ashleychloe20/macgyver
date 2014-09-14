package io.macgyver.core;

import io.macgyver.core.web.vaadin.MacGyverUI;
import io.macgyver.core.web.vaadin.views.HomeView;

public class CorePlugin extends Plugin {

	@Override
	public void registerViews(MacGyverUI ui) {
		ui.registerView("home", HomeView.class, "MacGyver", "Home");
	}

}
