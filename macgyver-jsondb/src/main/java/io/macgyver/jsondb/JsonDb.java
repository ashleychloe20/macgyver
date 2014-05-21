package io.macgyver.jsondb;

public interface JsonDb {

	public JsonDbCollection getCollection(String name);
	
	public <T> T execute(JsonDbCallback<T> cb);
}
