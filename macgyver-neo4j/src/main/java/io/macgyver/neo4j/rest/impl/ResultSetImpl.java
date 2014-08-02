package io.macgyver.neo4j.rest.impl;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import io.macgyver.neo4j.rest.Vertex;
import io.macgyver.neo4j.rest.Result;

public class ResultSetImpl implements Result{

	ObjectNode resultData;
	int rowIndex=-1;
	int rowCount;
	
	public ResultSetImpl(ObjectNode resultData) {
		Preconditions.checkNotNull(resultData);
		this.resultData = resultData;
		rowCount = getDataArrayNode().size();
	}
	ArrayNode getColumnArrayNode() {
		ArrayNode an = (ArrayNode) resultData.path("columns");
		return an;
	}
	ArrayNode getDataArrayNode() {
		ArrayNode an = (ArrayNode) resultData.path("data");
		return an;
	}
	@Override
	public boolean hasNext() {
		return rowIndex+1<rowCount;
	}

	@Override
	public boolean next() {
	
		if (hasNext()) {
			rowIndex++;
			return true;
		}
		return false;
	}

	@Override
	public int getColumn(String name) {
		ArrayNode n = getColumnArrayNode();
		for (int i=0; i<n.size(); i++) {
			if (n.get(i).asText().equals(name)) {
				return i;
			}
		}
		throw new IllegalArgumentException("column not found: "+name);
	}

	@Override
	public List<String> getColumnNames() {
		ArrayNode n = getColumnArrayNode();
		List<String> x  = Lists.newArrayList();
		for (int i=0; i<n.size(); i++) {
			x.add(n.get(i).asText());
		}
		return x;
	}
	public String getString(int column) {
		ArrayNode dataRow = (ArrayNode) getDataArrayNode().get(rowIndex);
		JsonNode n= dataRow.get(column);
		return convertToString(n);
	}
	
	public String getString(String columnName) {
		return getString(getColumn(columnName));
	}
	
	public ObjectNode getObjectNode(int column) {
		ArrayNode dataRow = (ArrayNode) getDataArrayNode().get(rowIndex);
		JsonNode n= dataRow.get(column);
		if (n instanceof ObjectNode) {
			return (ObjectNode) n;
		}
		throw new IllegalArgumentException("column "+column+" does not contain a json object");
	}
	
	public ObjectNode getObjectNode(String columnName) {
		return getObjectNode(getColumn(columnName));
	}
	public Vertex getVertex(int i) {
		return new Neo4jNodeImpl(getObjectNode(i));
	}
	public Vertex getVertex(String columnName) {
		return new Neo4jNodeImpl(getObjectNode(getColumn(columnName)));
	}
	public String convertToString(JsonNode n) {
		if (n.isObject()) {
			return n.toString();
		}
		return n.asText();
	}
	
}
