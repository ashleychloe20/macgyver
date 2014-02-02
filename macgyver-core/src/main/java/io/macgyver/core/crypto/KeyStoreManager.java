package io.macgyver.core.crypto;

import io.macgyver.core.Kernel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;

import com.google.common.io.Closer;

public class KeyStoreManager {

  char[] defaultPass = new char[] {0x63,0x68  ,0x61 ,0x6e ,0x67  ,0x65  ,0x69  ,0x74 };
 
	private char[] keyStorePass = defaultPass;
	private char[] globalKeyPass = defaultPass;
	KeyStore keyStore;

	public File getKeyStoreLocation() {
		return new File(Kernel.getInstance().getExtensionDir(),
				"conf/keystore.jceks");
	}

	protected char[] getKeyStorePassword() {
		return keyStorePass;
	}

	protected char[] getPasswordForKey(String key) {
		return globalKeyPass;
	}

	public Key getKey(String alias) throws GeneralSecurityException {
		return getKeyStore().getKey(alias, getPasswordForKey(alias));
	}

	public synchronized KeyStore getKeyStore() throws GeneralSecurityException {
		if (keyStore != null) {
			return keyStore;
		}
		KeyStore ks = KeyStore.getInstance("JCEKS");

		Closer c = Closer.create();
		try {
			BufferedInputStream is = new BufferedInputStream(
					new FileInputStream(getKeyStoreLocation()));
			c.register(is);
			ks.load(is, getKeyStorePassword());
			keyStore = ks;
		} catch (IOException e) {
			throw new GeneralSecurityException(e);
		} finally {

			try {
				c.close();
			} catch (Exception e) {
				// swallow
			}
		}

		return ks;
	}
}
