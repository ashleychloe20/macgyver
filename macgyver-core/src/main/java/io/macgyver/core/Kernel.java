package io.macgyver.core;

import groovy.lang.GroovyShell;
import io.macgyver.core.config.CoreConfig;
import io.macgyver.core.eventbus.MacGyverEventBus;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.vfs2.FileObject;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.StandardEnvironment;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

public class Kernel implements ApplicationContextAware {

	static Logger logger = LoggerFactory.getLogger(Kernel.class);

	static AtomicReference<Kernel> kernelRef = new AtomicReference<Kernel>();

	private ApplicationContext applicationContext;

	private static Throwable startupError = null;

	public Kernel() {

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


	public synchronized static Kernel getInstance() {
		Kernel k = kernelRef.get();
		if (k == null) {
			throw new IllegalStateException("Kernel not yet initialized");

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
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if (this.applicationContext != null
				&& this.applicationContext != applicationContext) {
			new RuntimeException().printStackTrace();
			throw new IllegalStateException("application context already set: "
					+ this.applicationContext+" ;new: "+applicationContext);
		}
		this.applicationContext = applicationContext;
		kernelRef.set(this);

	}

	static Optional<String> profile = null;

	public static synchronized Optional<String> getExecutionProfile() {
		if (profile != null) {
			return profile;
		}
		StandardEnvironment standardEnvironment = new StandardEnvironment();

		String[] activeProfiles = standardEnvironment.getActiveProfiles();
		if (activeProfiles == null) {
			profile = Optional.absent();
		}
		for (String p : activeProfiles) {
			if (p != null && p.endsWith("_env")) {
				p = p.substring(0, p.length() - "_env".length());
				profile = Optional.of(p);
			}
		}
		if (profile == null) {
			profile = Optional.absent();
		}
		
		logger.info("macgyver profile: {}",profile.or("none"));
		return profile;

	}

	
	
	public static class KernelStartedEvent {

	}
}
