package io.macgyver.plugin.splunk;


import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.splunk.Service;

public class TokenRefreshManager {

	long tokenRefreshIntervalSecs = TimeUnit.MINUTES.toSeconds(5);
	
	Logger logger = LoggerFactory.getLogger(TokenRefreshManager.class);
	Timer timer;
	
	WeakHashMap<Service, Object> serviceCache = new WeakHashMap<Service, Object>();

	public class AuthenticatorTask extends TimerTask {

		@Override
		public void run() {
			try {
				reauthenticate();
			} catch (Exception e) {
				logger.error("problem re-authenticating with splunk");
			}

		}

	}
	public TokenRefreshManager () {
		
	}
	
	@PostConstruct
	public void start() {
		timer  = new Timer("splunk token refresh", true);
		logger.info("will refresh splunk tokens every {} seconds",tokenRefreshIntervalSecs);
		timer.schedule(new AuthenticatorTask(), 0, TimeUnit.SECONDS.toMillis(tokenRefreshIntervalSecs));
	}
	protected synchronized void registerService(Service service) {
		serviceCache.put(service, "dummy");
	}

	protected void reauthenticate(Service s) {
		logger.info("reauthenticating Splunk service: "+s);
		s.login();
	}

	protected void reauthenticate() {
		Set<Service> s = null;
		synchronized (this) {
			s = serviceCache.keySet();
		}
		for (Service service : s) {
			try {
				reauthenticate(service);
			} catch (Exception e) {
				logger.warn("problem re-authenticating service", e);
			}
		}
	}
	
}
