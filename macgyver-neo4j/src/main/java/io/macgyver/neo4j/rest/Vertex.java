package io.macgyver.neo4j.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Vertex {

	ObjectNode getProperties();
}
