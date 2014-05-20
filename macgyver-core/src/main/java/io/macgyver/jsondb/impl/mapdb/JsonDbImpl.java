package io.macgyver.jsondb.impl.mapdb;

import io.macgyver.jsondb.JsonDb;
import io.macgyver.jsondb.JsonDbCallback;
import io.macgyver.jsondb.JsonDbCollection;

import org.mapdb.TxMaker;

public class JsonDbImpl implements JsonDb {

	TxMaker txmaker;

	public JsonDbImpl(TxMaker txmaker) {
		this.txmaker = txmaker;
	}

	@Override
	public JsonDbCollection getCollection(String name) {

		return new CollectionImpl(this,name);
	}

	@Override
	public <T> T execute(JsonDbCallback<T> cb) {
		
		return cb.execute(this);
		

	
	}
	
	
}
