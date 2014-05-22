package io.macgyver.jsondb;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;

public interface JsonDbCollection {

	public void save(ObjectNode n);

	public Optional<ObjectNode> findOneById(String id);

	public Optional<ObjectNode> findOneByExpression(String id);

	public JsonDbCursor find();

	public JsonDbCursor findByExpression(String expression);
	
	public void remove(String id);
	
	public void remove(ObjectNode n);
	
	public void removeAll();
}
