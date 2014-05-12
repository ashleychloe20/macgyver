package io.macgyver.core.mapdb;

import io.macgyver.core.Kernel;

import java.io.File;

import org.mapdb.DBMaker;
import org.mapdb.TxMaker;

public class BootstrapMapDB {

	private static BootstrapMapDB instance = new BootstrapMapDB();
	
	TxMaker txMaker;
	
	public static BootstrapMapDB getInstance() {
		return instance;
	}
	public synchronized TxMaker getTxMaker() {
		if (txMaker==null) {
			throw new IllegalStateException();
		}
		
		
		return txMaker;
	}
	public synchronized void init() {
		if (txMaker!=null) {
			throw new IllegalStateException();
		}
		File extDir = Kernel.determineExtensionDir();
		File dataDir = new File(extDir,"data");
		dataDir.mkdirs();
		File dbFile = new File(dataDir,"macgyver.db");
		TxMaker x = DBMaker.newFileDB(dbFile).closeOnJvmShutdown().makeTxMaker();
		txMaker = x;
	}
	

}
