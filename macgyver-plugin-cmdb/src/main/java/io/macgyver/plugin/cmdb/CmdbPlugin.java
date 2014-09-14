package io.macgyver.plugin.cmdb;

import io.macgyver.core.Plugin;
import io.macgyver.core.web.vaadin.MacGyverUI;
import io.macgyver.neo4j.rest.Neo4jRestClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.Navigator;

public class CmdbPlugin extends Plugin {

	Logger logger = LoggerFactory.getLogger(CmdbPlugin.class);

	@Autowired
	Neo4jRestClient neo4j;



	@Override
	public void registerViews(MacGyverUI ui) {
		logger.info("registerViews: {}", ui);

		ui.registerView(AppInstancesView.VIEW_NAME, AppInstancesView.class, "Inventory","App Instances");
		

	}



}
