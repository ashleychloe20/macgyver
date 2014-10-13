/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.plugin.cmdb;

import io.macgyver.core.MacGyverException;
import io.macgyver.neo4j.rest.Neo4jRestClient;
import io.macgyver.neo4j.rest.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;

public class AppInstanceManager {
	Logger logger = LoggerFactory.getLogger(AppInstanceManager.class);

	@Autowired
	Neo4jRestClient neo4j;

	

	
	CheckInProcessor processor = new BasicCheckInProcessor();

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



	public ObjectNode processCheckIn(ObjectNode data) {
	

		String host = data.path("host").asText();
		String group = data.path("groupId").asText();
		String app = data.path("appId").asText();

		if (Strings.isNullOrEmpty(group)) {
			group = "";
		}
		logger.debug("host:{} group:{} app:{}", host, group, app);

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
	public CheckInProcessor getCheckInProcessor() {
		return processor;
	}
	public void setCheckInProcessor(CheckInProcessor p) {
		this.processor = p;
	}

}
