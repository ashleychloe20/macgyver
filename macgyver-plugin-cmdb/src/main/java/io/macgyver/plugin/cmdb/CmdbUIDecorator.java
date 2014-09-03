package io.macgyver.plugin.cmdb;

import io.macgyver.core.web.vaadin.MacGyverUI;
import io.macgyver.core.web.vaadin.MacGyverUIDecorator;
import io.macgyver.neo4j.rest.Neo4jRestClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;

public class CmdbUIDecorator extends MacGyverUIDecorator {

	Logger logger = LoggerFactory.getLogger(CmdbUIDecorator.class);

	@Autowired
	Neo4jRestClient neo4j;



	@Override
	public void decorate(MacGyverUICreateEvent event) {
		logger.info("onCreateEvent: {}", event);

		MacGyverUI ui = event.getUI();

		Navigator nav = ui.getNavigator();
	

		ui.addMenuItem("Inventory", "App Instances", AppInstancesView.VIEW_NAME);
		nav.addView(AppInstancesView.VIEW_NAME, AppInstancesView.class);

	}

	@Override
	public void decorate(MacGyverUIPostCreateEvent event) {
		// TODO Auto-generated method stub

	}

}
