package io.macgyver.jsondb;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface JsonDbCursor extends Iterable<ObjectNode> {

	public int size();
}
