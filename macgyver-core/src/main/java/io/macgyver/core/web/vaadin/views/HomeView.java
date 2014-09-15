package io.macgyver.core.web.vaadin.views;

import io.macgyver.core.web.vaadin.ViewDecorators;
import io.macgyver.core.web.vaadin.ViewMetadata;
import io.macgyver.core.web.vaadin.ViewConfig;

import com.google.common.collect.Lists;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@ViewConfig(viewName="home", menuPath={"MacGyver","Home"})
public class HomeView extends VerticalLayout implements View {

	public HomeView() {
		setMargin(true);
		setSizeFull();
	//	Label home = new Label("Welcome to MacGyver");
		//addComponent(home);

		//setComponentAlignment(home, Alignment.MIDDLE_CENTER);

		ViewDecorators.decorate(this);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// do something...
	}


}
