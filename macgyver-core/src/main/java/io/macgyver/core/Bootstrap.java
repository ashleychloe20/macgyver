package io.macgyver.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.tools.FileObject;

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

	

	public static Bootstrap getInstance() {
		if (instance == null) {
			throw new IllegalStateException();
		}
		return instance;
	}

	public Bootstrap() {
		init();
	}


	public File getExtensionDir() {
		return determineExtensionDir();
	}
	public File getWebDir() {
		return new File(determineExtensionDir(),"web");
	}
	public File getConfigDir() {
		return new File(determineExtensionDir(),"config");
	}
	public File getDataDir() {
		return new File(determineExtensionDir(),"data");
	}
	public File getScriptsDir() {
		return new File(determineExtensionDir(),"scripts");
	}
	private File determineExtensionDir() {
		try {
			String location = System.getProperty("macgyver.home");
			if (!Strings.isNullOrEmpty(location)) {
				return new File(location).getCanonicalFile();
			}
			location = System.getenv("MACGYVER_HOME");
			if (!Strings.isNullOrEmpty(location)) {
				return new File(location).getCanonicalFile();
			}
			
			
			
			return new File(".").getCanonicalFile();
	
			
		} catch (IOException e) {
			throw new ConfigurationException(e);
		}

	}

	AtomicBoolean initialized = new AtomicBoolean(false);

	public File resolveConfig(String name) {
		return new File(getConfigDir(),name);
	}
	protected File findLocation(String name) throws  MalformedURLException {
		

		
		String syspropKey = "macgyver."+name.toLowerCase()+".dir";
		String val = System.getProperty(syspropKey);
		if (!Strings.isNullOrEmpty(val)) {
			logger.info("resolved location ("+name+") via sysprop: "+syspropKey+"="+val);
			return new File(val);
		}
		
		
	
		
		String envKey = "MACGYVER_EXT_"+name.toUpperCase().trim()+"_DIR";
		val = System.getenv(envKey);
		if (!Strings.isNullOrEmpty(val)) {
			logger.info("resolved location ("+name+") via env var: "+envKey+"="+val);
			return new File(val);
		}
	
		
		val = new File(determineExtensionDir(),name).getAbsolutePath();
		logger.info("resolved location ("+name+") via macgyver.home: "+val);
		return new File(val);
	}
	public synchronized void init() {
		if (initialized.get()) {
			throw new IllegalStateException("Already initialized");
		}
		printBanner();

		File extDir = determineExtensionDir();
	
			
		
			logger.info("macgyver home    : {}",determineExtensionDir());
			logger.info("macgyver config  : {}",getConfigDir());
			logger.info("macgyver scripts : {}",getScriptsDir());
			logger.info("macgyver   data  : {}",getDataDir());
			logger.info("macgyver    web  : {}",getWebDir());
			
	

		

		

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
