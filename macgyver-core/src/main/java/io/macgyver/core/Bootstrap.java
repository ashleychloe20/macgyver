package io.macgyver.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.CharStreams;

/**
 * The purpose of bootstrap is to collect configuration settings that need to be
 * configured before they are passed to Spring for initialization.
 * 
 * @author rschoening
 * 
 */
public class Bootstrap {

	Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	public static Bootstrap instance = new Bootstrap();
	VirtualFileSystem vfsManager = null;

	

	public static Bootstrap getInstance() {
		if (instance == null) {
			throw new IllegalStateException();
		}
		return instance;
	}

	public Bootstrap() {
		init();
	}



	public VirtualFileSystem getVirtualFileSystem() {
		return vfsManager;
	}

	private File determineExtensionDir() {
		try {
			String location = System.getProperty("macgyver.ext.location");

			if (!Strings.isNullOrEmpty(location)) {
				return new File(location).getCanonicalFile();
			}

			File extLocation = new File("./config");
			if (extLocation.exists()) {
				return extLocation.getParentFile().getCanonicalFile();
			}

			extLocation = new File("./src/test/resources/ext");
			if (extLocation.exists()) {
				return extLocation.getCanonicalFile();
			}
			throw new ConfigurationException("macgyver.ext.location not set");
		} catch (IOException e) {
			throw new ConfigurationException(e);
		}

	}

	AtomicBoolean initialized = new AtomicBoolean(false);

	public synchronized void init() {
		if (initialized.get()) {
			throw new IllegalStateException("Already initialized");
		}
		printBanner();

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

			vfsManager = new VirtualFileSystem(configLocation, scriptsLocation,
					dataLocation, webLocation);
	

		

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		initialized.set(true);

	}

	public void printBanner() {

		// Spring boot doesn't support alternate banner until 1.1.x
		String bannerText = "\n";
		try {
			URL url = ServerMain.class.getResource("/banner.txt");
			if (url != null) {
				try (InputStreamReader reader = new InputStreamReader(
						url.openStream(), Charsets.UTF_8)) {
					String text = CharStreams.toString(reader);

					bannerText += text;
				}
			}

			url = null;
			url = ServerMain.class
					.getResource("/macgyver-core-revision.properties");
			if (url != null) {
				Properties p = new Properties();
				try (InputStream x = url.openStream()) {
					p.load(x);
				}

				bannerText += String.format(
						"\n\n                      (v%s rev:%s)\n",
						p.getProperty("version"),
						p.getProperty("gitShortCommitId"));
			}

		} catch (Exception e) {
			logger.warn("could not load banner");
		}
		System.out.println(bannerText);
	}
}
