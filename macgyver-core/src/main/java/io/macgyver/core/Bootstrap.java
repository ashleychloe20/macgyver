package io.macgyver.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystemException;
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
	VfsManager vfsManager = null;

	

	public static Bootstrap getInstance() {
		if (instance == null) {
			throw new IllegalStateException();
		}
		return instance;
	}

	public Bootstrap() {
		init();
	}



	public VfsManager getVfsManager() {
		return vfsManager;
	}

	private File determineExtensionDir() {
		try {
			String location = System.getProperty("macgyver.ext.home");
			if (!Strings.isNullOrEmpty(location)) {
				return new File(location).getCanonicalFile();
			}
			location = System.getenv("MACGYVER_EXT_HOME");
			if (!Strings.isNullOrEmpty(location)) {
				return new File(location).getCanonicalFile();
			}
			
			File extLocation = new File("./config");
			if (extLocation.exists()) {
				return extLocation.getParentFile().getCanonicalFile();
			}

	
			throw new ConfigurationException("macgyver.ext.dir not set");
		} catch (IOException e) {
			throw new ConfigurationException(e);
		}

	}

	AtomicBoolean initialized = new AtomicBoolean(false);

	protected FileObject findLocation(String name) throws org.apache.commons.vfs2.FileSystemException, MalformedURLException {
		
		String syspropKey = "macgyver."+name.toLowerCase()+".url";
		String val = System.getProperty(syspropKey);
		if (!Strings.isNullOrEmpty(val)) {
			logger.info("resolved location ("+name+") via sysprop: "+syspropKey+"="+val);
			return VFS.getManager().resolveFile(val);
		}
		
		syspropKey = "macgyver."+name.toLowerCase()+".dir";
		val = System.getProperty(syspropKey);
		if (!Strings.isNullOrEmpty(val)) {
			logger.info("resolved location ("+name+") via sysprop: "+syspropKey+"="+val);
			return VFS.getManager().resolveFile(new File(val).toURI().toURL().toString());
		}
		
		
		String envKey = "MACGYVER_EXT_"+name.toUpperCase().trim()+"_URL";
		val = System.getenv(envKey);
		if (!Strings.isNullOrEmpty(val)) {
			logger.info("resolved location ("+name+") via env var: "+envKey+"="+val);
			return VFS.getManager().resolveFile(val);
		}
		
		envKey = "MACGYVER_EXT_"+name.toUpperCase().trim()+"_DIR";
		val = System.getenv(envKey);
		if (!Strings.isNullOrEmpty(val)) {
			logger.info("resolved location ("+name+") via env var: "+envKey+"="+val);
			return VFS.getManager().resolveFile(new File(val).toURI().toURL().toString());
		}
	
		
		val = new File(determineExtensionDir(),name).getAbsolutePath();
		logger.info("resolved location ("+name+") via macgyver.ext.home: "+val);
		return VFS.getManager().resolveFile(val);
	}
	public synchronized void init() {
		if (initialized.get()) {
			throw new IllegalStateException("Already initialized");
		}
		printBanner();

		File extDir = determineExtensionDir();
		try {
			FileObject configLocation = findLocation("config");
			FileObject scriptsLocation = findLocation("scripts");
			FileObject dataLocation = findLocation("data");
			FileObject webLocation = findLocation("web");
		

			logger.info("macgyver config  : {}",configLocation);
			logger.info("macgyver scripts : {}",scriptsLocation);
			logger.info("macgyver   data  : {}",dataLocation);
			logger.info("macgyver    web  : {}",webLocation);
			vfsManager = new VfsManager(configLocation, scriptsLocation,
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
		logger.info(bannerText);
	}
}
