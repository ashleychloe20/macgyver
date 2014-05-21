package io.macgyver.jsondb;

public abstract class JsonDbCallback<T> {

	public abstract T execute(JsonDb db);
}
