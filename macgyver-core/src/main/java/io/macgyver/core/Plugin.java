package io.macgyver.core;

import io.macgyver.core.web.vaadin.MacGyverUI;

public abstract class Plugin {

	public abstract void registerViews(MacGyverUI ui);
	public void postRegisterViews(MacGyverUI ui) {}
}
