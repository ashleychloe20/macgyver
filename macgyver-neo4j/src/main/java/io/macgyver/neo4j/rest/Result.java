package io.macgyver.neo4j.rest;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Result {

	boolean hasNext();
	boolean next();
	int getColumnIndex(String name);
	List<String> getColumnNames();
	
	String getString(int column);
	String getString(String column);
	List getList(String column);
	List getList(int column);
	ObjectNode getObjectNode(String column);
	ObjectNode getObjectNode(int column);
	
	
	List<ObjectNode> asVertexList(String column);
	List<ObjectNode> asVertexList(int column);

	List<ObjectNode> asVertexDataList(String column);
	List<ObjectNode> asVertexDataList(int c);
}
