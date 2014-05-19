package io.macgyver.core;

import java.io.File;
import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;

import com.google.common.base.Strings;


/**
 * The purpose of bootstrap is to collect configuration settings that need to be configured before
 * they are passed to Spring for initialization.  
 * @author rschoening
 *
 */
public class Bootstrap {

	public static Bootstrap instance = new Bootstrap();
	VfsManager vfsManager = null;

	public static Bootstrap getInstance() {
		return instance;
	}

	public Bootstrap() {
		File extDir = determineExtensionDir();
		try {
			FileObject configLocation = VFS.getManager().resolveFile(
					new File(extDir, "config").toURI().toURL().toString());
			FileObject scriptsLocation = VFS.getManager().resolveFile(
					new File(extDir, "scripts").toURI().toURL().toString());
			FileObject dataLocation = VFS.getManager().resolveFile(
					new File(extDir, "data").toURI().toURL().toString());
			FileObject webLocation = VFS.getManager().resolveFile(
					new File(extDir, "web").toURI().toURL().toString());

			vfsManager = new VfsManager(configLocation, scriptsLocation,
					dataLocation, webLocation);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public VfsManager getVfsManager() {
		return vfsManager;
	}
	public File determineExtensionDir() {
		try {
			String location = System.getProperty("macgyver.ext.location");

			if (!Strings.isNullOrEmpty(location)) {
				return new File(location).getCanonicalFile();
			}
			File extLocation = new File("./src/test/resources/ext");
			if (extLocation.exists()) {
				return extLocation.getCanonicalFile();
			}
			return new File(".").getCanonicalFile();

		} catch (IOException e) {
			throw new ConfigurationException(e);
		}

	}
}
