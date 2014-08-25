package io.macgyver.core.web.vaadin;

import com.google.common.base.Preconditions;

public class MacGyverUICreateEvent {

	MacGyverUI ui;
	
	public MacGyverUICreateEvent(MacGyverUI ui) {
		Preconditions.checkNotNull(ui);
		this.ui = ui;
	}
	
	public MacGyverUI getUI() {
		return ui;
	}
}
