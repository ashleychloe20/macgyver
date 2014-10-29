package io.macgyver.plugin.pagerduty;

import java.util.Properties;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface PagerDutyClient {
	public ObjectNode postEvent(ObjectNode input);
	public ObjectNode createIncident(String incidentKey, String description, String client, String clientUrl, ObjectNode n);
	public ObjectNode createIncident(String incidentKey, String description);
	
}
