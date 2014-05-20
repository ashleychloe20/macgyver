package io.macgyver.jsondb;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;

public class JsonDbTemplate {

	JsonDb db;
	
	public JsonDbTemplate(JsonDb db) {
		super();
		this.db = db;
	}

	public <T> T execute(JsonDbCallback<T>cb) {
		
		return cb.execute(db);
		
	}
	
	public void save(final String collection, final ObjectNode n) {
		JsonDbCallback<Boolean> cb = new JsonDbCallback<Boolean>() {
			
			@Override
			public Boolean execute(JsonDb db) {
				db.getCollection(collection).save(n);
				return true;
			}
		};
		db.execute(cb);
		
	}
	
	public Optional<ObjectNode> findOne(final String collection, final String id) {
		JsonDbCallback<ObjectNode> cb = new JsonDbCallback<ObjectNode>() {
			
			@Override
			public ObjectNode execute(JsonDb db) {
				return db.getCollection(collection).findOne(id);
	
			}
		};
		
		ObjectNode n = db.execute(cb);
		return Optional.fromNullable(n);
	}
	

	private JsonDbCursor  find(final String collection) {
		JsonDbCallback<JsonDbCursor> cb = new JsonDbCallback<JsonDbCursor>() {
			
			@Override
			public JsonDbCursor execute(JsonDb db) {
				return db.getCollection(collection).find();
	
			}
		};
		
		JsonDbCursor n = db.execute(cb);
		return n;
	}
}
