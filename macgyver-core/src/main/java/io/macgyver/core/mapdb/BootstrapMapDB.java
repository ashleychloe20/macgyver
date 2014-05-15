package io.macgyver.core.mapdb;

import io.macgyver.core.Kernel;

import java.io.File;

import org.mapdb.DBMaker;
import org.mapdb.TxMaker;

import com.google.common.base.Optional;

public class BootstrapMapDB {

	private static BootstrapMapDB instance = new BootstrapMapDB();
	
	TxMaker txMaker;
	
	public static BootstrapMapDB getInstance() {
		return instance;
	}
	public synchronized Optional<TxMaker> getTxMaker() {
		return Optional.fromNullable(txMaker);
	}
	public synchronized void init() {
		if (txMaker!=null) {
			throw new IllegalStateException();
		}

		File dataDir = Kernel.getExtensionDir("data");
		dataDir.mkdirs();
		File dbFile = new File(dataDir,"macgyver-mapdb");
		TxMaker x = DBMaker.newFileDB(dbFile).closeOnJvmShutdown().makeTxMaker();
		txMaker = x;
	}
	

}
