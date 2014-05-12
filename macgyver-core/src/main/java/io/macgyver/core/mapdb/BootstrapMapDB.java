package io.macgyver.core.mapdb;

import java.io.File;

import org.mapdb.DBMaker;
import org.mapdb.TxMaker;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import io.macgyver.core.Kernel;

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
	
	public ApplicationListener<ApplicationEnvironmentPreparedEvent> createEnvironmentPreparedEventListener() {
		ApplicationListener<ApplicationEnvironmentPreparedEvent> listener = new ApplicationListener<ApplicationEnvironmentPreparedEvent>() {

			@Override
			public void onApplicationEvent(
					ApplicationEnvironmentPreparedEvent event) {
				System.out.println("EVENT: "+event);
				
			}
		};
		return listener;
	}
}
