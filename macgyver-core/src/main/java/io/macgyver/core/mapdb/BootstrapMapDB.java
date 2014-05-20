package io.macgyver.core.mapdb;

import io.macgyver.core.MacGyverException;
import io.macgyver.core.VirtualFileSystem;

import java.io.File;
import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.provider.local.LocalFile;
import org.mapdb.DBMaker;
import org.mapdb.TxMaker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class BootstrapMapDB {

	TxMaker txMaker;

	public synchronized Optional<TxMaker> getTxMaker() {
		return Optional.fromNullable(txMaker);
	}

	public synchronized void init(VirtualFileSystem vfs) {
		Preconditions.checkNotNull(vfs);
		try {
			if (txMaker != null) {
				throw new IllegalStateException();
			}
			
			
			FileObject fo = vfs.getDataLocation();
			if (!(fo instanceof LocalFile)) {
				throw new IllegalStateException(
						"dataLocation must be a local directory");
			}

			LocalFile localFile = (LocalFile) fo;
			File dataDir = new File(localFile.getURL().getFile());
			

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
