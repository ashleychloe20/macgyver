package io.macgyver.plugin.cmdb;

import io.macgyver.core.MacGyverException;
import io.macgyver.neo4j.rest.Neo4jRestClient;
import io.macgyver.neo4j.rest.Result;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;

public class AppInstanceManager {
	Logger logger = LoggerFactory.getLogger(AppInstanceManager.class);

	@Autowired
	Neo4jRestClient neo4j;

	public ObjectNode getOrCreateAppInstance(String host, String groupId,
			String appId) {

		String cypher = "match (ai:AppInstance) where ai.host={host} and ai.groupId={groupId} and ai.appId={appId} return ai";

		Result r = neo4j.execCypher(cypher, "host", host, "groupId", groupId,
				"appId", appId);

		if (r.next()) {

			ObjectNode n = r.getObjectNode("ai");
			return n;
		} else {

			String createCypher = "CREATE (ai:AppInstance {host:{host}, appId:{appId}, groupId:{groupId}}) RETURN ai";

			r = neo4j.execCypher(createCypher, "host", host, "appId", appId,
					"groupId", groupId);

			if (r.next()) {
				ObjectNode n = r.getObjectNode("ai");
				return n;
			}

		}
		throw new MacGyverException("could not get or create new AppInstance");
	}

	protected void processLegacy(ObjectNode n) {
		if (n.has("app")) {
			JsonNode app = n.get("app");
			if (app.isObject()) {
				n.remove("app");
				ObjectNode appObj = (ObjectNode) app;

				Iterator<Entry<String, JsonNode>> t = appObj.fields();
				while (t.hasNext()) {
					Entry<String, JsonNode> xx = t.next();
					n.put(xx.getKey(), xx.getValue().asText());
				}

			}
		}
		if (n.has("artifactId")) {
			n.put("appId", n.path("artifactId").asText());
			n.remove("artifactId");
		}
	}

	public ObjectNode processCheckIn(ObjectNode data) {
		processLegacy(data);

		String host = data.path("host").asText();
		String group = data.path("groupId").asText();
		String app = data.path("appId").asText();

		if (Strings.isNullOrEmpty(group)) {
			group = "";
		}
		logger.info("host:{} group:{} app:{}", host, group, app);

		if (!Strings.isNullOrEmpty(host) && !Strings.isNullOrEmpty(app)) {
			ObjectNode n = getOrCreateAppInstance(host, group, app);
		
			ObjectNode set = (ObjectNode) n.get("data");
			set.put("lastContactTs", System.currentTimeMillis());
			set.setAll(data);

			ObjectNode p = new ObjectMapper().createObjectNode();
			p.put("host", host);
			p.put("groupId", group);
			p.put("appId", app);
			p.put("props", set);
			String cypher = "match (ai:AppInstance) where ai.host={host} and ai.appId={appId} set ai={props} return ai";

			Result r = neo4j.execCypher(cypher, p);
			if (r.next()) {
				ObjectNode rv = (ObjectNode) r.getObjectNode("ai").path("data");
				return rv;
			}
		}
		return new ObjectMapper().createObjectNode();
	}

	public void enrich(ObjectNode n) {
		String scmCommitUrl = "";
		String artifactId = n.path("artifactId").asText();
		if (Strings.isNullOrEmpty(artifactId)) {

		} else {

			/*
			 * Optional<String> url = resolver.getViewRevisionUrl(artifactId, n
			 * .path("scmRevision").asText()); if (url.isPresent()) {
			 * n.put("scmViewRevisionUrl", url.get()); }
			 */
		}
		n.put("scmCommitUrl", scmCommitUrl);

		String scmBranch = n.path("scmBranch").asText().replace("origin/", "");
		String scmRevision = n.path("scmRevision").asText();

		if (scmBranch.startsWith(scmRevision) && scmRevision.length() > 2) {
			n.put("scmBranch", "");
		}
		if (n.path("profile").asText().trim().length() == 0) {
			n.put("profile", "unknown");
		}
	}
}
