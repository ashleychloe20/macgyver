package io.macgyver.neo4j.rest;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Result {

	boolean hasNext();
	boolean next();
	int getColumn(String name);
	List<String> getColumnNames();
	
	String getString(int column);
	String getString(String column);
	List getList(String column);
	List getList(int column);
	ObjectNode getObjectNode(String column);
	ObjectNode getObjectNode(int column);
	Vertex getVertex(String column);
	Vertex getVertex(int col);
}
