package io.macgyver.neo4j.rest.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.macgyver.neo4j.rest.Vertex;

public class Neo4jNodeImpl implements Vertex {
	ObjectNode objectNode;
	
	public  Neo4jNodeImpl(ObjectNode n) {
		this.objectNode = n;
	}
	@Override
	public ObjectNode getProperties() {
		return (ObjectNode) objectNode.get("data");
	}

}
