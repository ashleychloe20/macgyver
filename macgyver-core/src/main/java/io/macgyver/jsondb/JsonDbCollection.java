package io.macgyver.jsondb;

import java.util.Iterator;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface JsonDbCollection {

	public void save(ObjectNode n);

	public ObjectNode findOne(String id);

	public JsonDbCursor find();

	public void remove(String id);
	
	public void remove(ObjectNode n);
}
