package io.macgyver.plugin.atlassian.jira;

import io.macgyver.core.rest.JsonRestClient;

import com.fasterxml.jackson.databind.JsonNode;

public interface JiraClient extends JsonRestClient {

	JsonNode getIssue(String issueId);
	
}
