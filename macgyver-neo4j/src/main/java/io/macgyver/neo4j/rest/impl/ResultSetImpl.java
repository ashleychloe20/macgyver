package io.macgyver.neo4j.rest.impl;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import io.macgyver.neo4j.rest.Neo4jException;
import io.macgyver.neo4j.rest.Vertex;
import io.macgyver.neo4j.rest.Result;

public class ResultSetImpl implements Result {

	ObjectNode resultData;
	ArrayNode dataNode;
	ArrayNode columnsNode;
	int rowIndex = -1;
	int rowCount;

	public ResultSetImpl(ObjectNode resultData) {
		Preconditions.checkNotNull(resultData);
		this.resultData = resultData;

		JsonNode an = (JsonNode) resultData.path("data");
		if (!an.isArray()) {
			throw new Neo4jException("data node missing from response");
		}
		dataNode = (ArrayNode) an;

		an = (JsonNode) resultData.path("columns");
		if (!an.isArray()) {
			throw new Neo4jException("data node missing from response");
		}
		columnsNode = (ArrayNode) an;

		rowCount = getDataArrayNode().size();
	}

	ArrayNode getColumnArrayNode() {
		return columnsNode;
	}

	ArrayNode getDataArrayNode() {
		return dataNode;
	}

	@Override
	public boolean hasNext() {
		return rowIndex + 1 < rowCount;
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
		for (int i = 0; i < n.size(); i++) {
			if (n.get(i).asText().equals(name)) {
				return i;
			}
		}
		throw new IllegalArgumentException("column not found: " + name);
	}

	@Override
	public List<String> getColumnNames() {
		ArrayNode n = getColumnArrayNode();
		List<String> x = Lists.newArrayList();
		for (int i = 0; i < n.size(); i++) {
			x.add(n.get(i).asText());
		}
		return x;
	}

	public String getString(int column) {
		ArrayNode dataRow = (ArrayNode) getDataArrayNode().get(rowIndex);
		Preconditions.checkNotNull(dataRow);
		JsonNode n = dataRow.get(column);

		return convertToString(n);
	}

	public String getString(String columnName) {
		return getString(getColumn(columnName));
	}

	public ObjectNode getObjectNode(int column) {
		ArrayNode dataRow = (ArrayNode) getDataArrayNode().get(rowIndex);
		JsonNode n = dataRow.get(column);
		if (n instanceof ObjectNode) {
			return (ObjectNode) n;
		}
		throw new IllegalArgumentException("column " + column
				+ " does not contain a json object");
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
		String output = null;

		if (n.isNull()) {
			output = null;
		} else if (n.isArray()) {
			ArrayNode an = (ArrayNode)n;
			output = an.toString();
		} else if (n.isObject()) {
			output = n.toString();
		} else {
			output = n.asText();
		}
		return output;
	}

	@Override
	public List getList(String column) {
		return getList(getColumn(column));
	}

	@Override
	public List getList(int column) {
		ArrayNode dataRow = (ArrayNode) getDataArrayNode().get(rowIndex);
		JsonNode val = dataRow.get(column);
		if (val.isArray()) {
			List rval = Lists.newArrayList();
			ArrayNode n = (ArrayNode) val;
			for (int i=0; i<n.size(); i++) {
				rval.add(n.get(i).asText());
			}
			return rval;
		}
		else {
			throw new IllegalArgumentException("type cannot be converted to a list: "+val.getNodeType());
		}
	}

}
