package io.macgyver.plugin.atlassian.jira;

import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

public class JiraServiceFactory extends BasicServiceFactory<JiraClient>{

	public JiraServiceFactory() {
		super("jira");
	
	}

	@Override
	protected Object doCreateInstance(ServiceDefinition def) {
		
		JiraClientImpl ci = new JiraClientImpl(def.getProperties().getProperty("url","http://jira.example.com"),def.getProperties().getProperty("username"),def.getProperties().getProperty("password"));

		return ci;
	}

}
