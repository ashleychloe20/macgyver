package io.macgyver.plugin.atlassian.jira;

import org.assertj.core.util.Preconditions;

import com.fasterxml.jackson.databind.JsonNode;

import io.macgyver.core.rest.BasicJsonRestClient;

public class JiraClientImpl extends BasicJsonRestClient implements JiraClient {

	public JiraClientImpl(String url, String username, String password) {
		super(url);

		addBasicAuthInjector(username, password);
	}

	public JsonNode getIssue(String issueId) {
		return get("issue/"+issueId);
	}
}
