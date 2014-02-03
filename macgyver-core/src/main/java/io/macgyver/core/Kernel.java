package io.macgyver.core;

import io.macgyver.config.CoreConfig;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

public class Kernel implements InitializingBean, ApplicationContextAware {

	static Logger logger = LoggerFactory.getLogger(Kernel.class);

	static AtomicReference<Kernel> kernelRef = new AtomicReference<Kernel>();

	private ApplicationContext applicationContext;
	private File extensionDir;
	private static Throwable startupError = null;

	public Kernel(File extensionDir) {

		this.extensionDir = extensionDir.getAbsoluteFile();
		if (this.extensionDir.exists()) {
			try {
				extensionDir = extensionDir.getCanonicalFile();
			} catch (IOException e) {
				logger.warn(
						"could not determine canonical directory name for: {}",
						extensionDir);
			}
		}
	}

	public static Optional<Throwable> getStartupError() {
		return Optional.fromNullable(startupError);
	}

	public static void registerStartupError(Throwable t) {
		if (t != null) {
			startupError = t;
		}
	}

	public boolean isRunning() {
		return startupError == null;
	}

	public synchronized static void initialize() {
		if (kernelRef.get() == null) {
			try {
				Kernel k = new Kernel(new File(".").getCanonicalFile());

				k.applicationContext = new AnnotationConfigApplicationContext(
						CoreConfig.class.getPackage().getName());
				kernelRef.set(k);
			} catch (IOException e) {
				throw new ConfigurationException(e);
			}
		} else {
			throw new IllegalStateException(
					"spring context already initialized");
		}
		if (startupError != null) {
			throw new MacGyverException(startupError);
		}
	}

	public synchronized static Kernel getInstance() {
		Kernel k = kernelRef.get();
		if (k == null) {
			throw new IllegalStateException("kernel not initialized");
		}
		return k;
	}

	public synchronized ApplicationContext getApplicationContext() {

		if (applicationContext == null) {

			throw new IllegalStateException(
					"Kernel's ApplicationContext not initialized");

		}
		return applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		kernelRef.set(this);
	}

	public File getExtensionDir() {
		return extensionDir;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if (this.applicationContext != null) {
			throw new IllegalStateException("application context already set");
		}
		this.applicationContext = applicationContext;

	}

	static String profile = null;

	public static synchronized String getExecutionProfile() {
		if (profile==null){
			String p = System.getProperty("profile");
			if (!Strings.isNullOrEmpty(p)) {
				profile = p;
				logger.info("execution profile: {}",profile);
			
			}
		}
		return profile;
	}
}
