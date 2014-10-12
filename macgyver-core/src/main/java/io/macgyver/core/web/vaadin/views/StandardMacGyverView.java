package io.macgyver.core.web.vaadin.views;

import com.vaadin.navigator.View;
import com.vaadin.ui.VerticalLayout;

public abstract class StandardMacGyverView extends VerticalLayout implements View {

	public StandardMacGyverView() {
		super();
		setMargin(true);
		setSizeFull();
	}

}
