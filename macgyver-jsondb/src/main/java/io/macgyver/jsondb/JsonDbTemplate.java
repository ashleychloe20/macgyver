package io.macgyver.jsondb;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class JsonDbTemplate {

	JsonDb db;

	public JsonDbTemplate(JsonDb db) {
		super();
		Preconditions.checkNotNull(db);
		this.db = db;
	}

	public <T> T execute(JsonDbCallback<T> cb) {

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

	public JsonDb getJsonDb() {
		return db;
	}

	public void remove(final String collection, final String id) {
		JsonDbCallback<Boolean> cb = new JsonDbCallback<Boolean>() {

			@Override
			public Boolean execute(JsonDb db) {
				db.getCollection(collection).remove(id);
				return true;
			}
		};

		db.execute(cb);

	}

	public void removeAll(final String collection) {
		JsonDbCallback<Boolean> cb = new JsonDbCallback<Boolean>() {

			@Override
			public Boolean execute(JsonDb db) {
				db.getCollection(collection).removeAll();
				return true;
			}
		};

		db.execute(cb);

	}

	public Optional<ObjectNode> findOneByExpression(final String collection,
			final String expression) {
		JsonDbCallback<ObjectNode> cb = new JsonDbCallback<ObjectNode>() {

			@Override
			public ObjectNode execute(JsonDb db) {
				return db.getCollection(collection)
						.findOneByExpression( expression).orNull();

			}
		};

		ObjectNode n = db.execute(cb);
		return Optional.fromNullable(n);
	}

	public Optional<ObjectNode> findOneById(final String collection,
			final String id) {
		JsonDbCallback<ObjectNode> cb = new JsonDbCallback<ObjectNode>() {

			@Override
			public ObjectNode execute(JsonDb db) {
				return db.getCollection(collection).findOneById(id).orNull();

			}
		};

		ObjectNode n = db.execute(cb);
		return Optional.fromNullable(n);
	}

}
