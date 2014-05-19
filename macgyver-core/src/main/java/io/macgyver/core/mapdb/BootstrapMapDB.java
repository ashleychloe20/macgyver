package io.macgyver.core.mapdb;

import io.macgyver.core.Bootstrap;
import io.macgyver.core.Kernel;
import io.macgyver.core.MacGyverException;

import java.io.File;
import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.provider.local.LocalFile;
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
		try {
			if (txMaker != null) {
				throw new IllegalStateException();
			}

			FileObject fo = Bootstrap.getInstance().getVfsManager()
					.getDataLocation();
			if (!(fo instanceof LocalFile)) {
				throw new IllegalStateException(
						"dataLocation must be a local directory");
			}

			LocalFile localFile = (LocalFile) fo;
			File dataDir = new File(localFile.getURL().getFile());
			System.out.println("FT: " + dataDir);

			dataDir.mkdirs();
			File dbFile = new File(dataDir, "macgyver-mapdb");
			TxMaker x = DBMaker.newFileDB(dbFile).closeOnJvmShutdown()
					.makeTxMaker();
			txMaker = x;
		} catch (IOException e) {
			throw new MacGyverException("", e);
		}
	}

}
