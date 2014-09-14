package io.macgyver.core;

import io.macgyver.core.eventbus.MacGyverEvent;

import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.StandardEnvironment;

import com.google.common.base.Optional;

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


	public synchronized ApplicationContext applicationContext() {

		if (applicationContext == null) {

			throw new IllegalStateException(
					"Kernel's ApplicationContext not initialized");

		}
		return applicationContext;
	}
	public static ApplicationContext getApplicationContext() {
		return Kernel.getInstance().applicationContext();
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if (this.applicationContext != null
				&& this.applicationContext != applicationContext) {
		
			throw new IllegalStateException("application context already set: "
					+ this.applicationContext + " ;new: " + applicationContext);
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

		logger.info("macgyver profile: {}", profile.or("none"));
		return profile;

	}

	public static class ServerStartedEvent extends MacGyverEvent {
		public ServerStartedEvent(Kernel source) {
			super(source);
		}
	}

	public static class KernelStartedEvent extends MacGyverEvent {

		public KernelStartedEvent(Kernel source) {
			super(source);

		}

	}
}
