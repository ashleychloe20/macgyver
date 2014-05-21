package io.macgyver.jsondb;

public abstract class JsonDbCallbackWithoutResult extends JsonDbCallback<Boolean> {

	
	public abstract void executeWithoutResult(JsonDb db);
	
	@Override
	public final Boolean execute(JsonDb db) {
		execute(db);
		return true;
	}

}
