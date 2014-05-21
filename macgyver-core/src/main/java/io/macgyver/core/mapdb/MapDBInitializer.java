package io.macgyver.core.mapdb;

import javax.annotation.PostConstruct;

import org.mapdb.DB;
import org.mapdb.TxMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class MapDBInitializer {

	@Autowired
	@Qualifier("macTxMaker")
	TxMaker txMaker;
	
	@PostConstruct
	public void init() {
		DB db = txMaker.makeTx();
		init(db);
		db.commit();
	}
	
	public abstract void init(DB db);
}
