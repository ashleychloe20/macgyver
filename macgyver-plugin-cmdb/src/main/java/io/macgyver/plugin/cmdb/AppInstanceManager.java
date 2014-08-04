package io.macgyver.plugin.cmdb;


import io.macgyver.core.MacGyverException;
import io.macgyver.neo4j.rest.Neo4jRestClient;
import io.macgyver.neo4j.rest.Result;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;


public class AppInstanceManager  {
	Logger logger = LoggerFactory.getLogger(AppInstanceManager.class);

	Set<String> keyExcludes = ImmutableSet.of("vertexType",
			AppInstance.KEY_APP_ID, AppInstance.KEY_GROUP_ID, "vertexId");


	@Autowired
	Neo4jRestClient neo4j;
	
	
	public synchronized AppInstance getOrCreateAppInstanceVertex(String host,
			String groupId, String appId) {

	
		
		String cypher = "match (ai:AppInstance) where ai.host={host} and ai.groupId={groupId} and ai.appId={appId} return ai";
		
		
		Result r = neo4j.execCypher(cypher, "host", host, "groupId", groupId, "appId", appId);
		
		
		if (r.next()) {

			ObjectNode n = r.getObjectNode("ai");
			AppInstance ai = new AppInstance(n);
			return ai;
		} else {

			String createCypher = "CREATE (ai:AppInstance {host:{host}, appId:{appId}, groupId:{groupId}}) RETURN ai";
			
			r = neo4j.execCypher(createCypher,"host",host,"appId",appId,"groupId",groupId);
			
			
			
			r = neo4j.execCypher(cypher, "host", host, "groupId", groupId, "appId", appId);
			
			if (r.next()) {
				return new AppInstance(r.getObjectNode("ai"));
			}
			

		}
		throw new MacGyverException("could not get or create new AppInstance");
	}

}
