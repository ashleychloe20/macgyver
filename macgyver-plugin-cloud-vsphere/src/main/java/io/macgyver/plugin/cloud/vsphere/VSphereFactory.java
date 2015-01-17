/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.plugin.cloud.vsphere;

import io.macgyver.core.MacGyverException;
import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.plugin.cloud.ComputeScannerManager;
import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.thoughtworks.proxy.factory.CglibProxyFactory;
import com.thoughtworks.proxy.toys.delegate.DelegationMode;
import com.thoughtworks.proxy.toys.hotswap.HotSwapping;
import com.vmware.vim25.mo.ServerConnection;
import com.vmware.vim25.mo.ServiceInstance;

@Component
public class VSphereFactory extends BasicServiceFactory<ServiceInstance> {

	public static final String REFRESH_SCHEDULE = "* * * * *";

	public static final String CERTIFICATE_VERIFICATION_DEFAULT = "false";

	CglibProxyFactory cglibProxyFactory = new CglibProxyFactory();

	@Autowired
	ComputeScannerManager computeScannerManager;

	@Autowired
	Scheduler scheduler;

	List<WeakReference<RefreshingServiceInstance>> instances = Lists
			.newCopyOnWriteArrayList();

	public VSphereFactory() {
		super("vsphere");

	}

	@PostConstruct
	public void scheduleRefresh() {
		logger.info("scheduling VSphere ServiceInstance session token refresh");
		scheduler.schedule(REFRESH_SCHEDULE, new RefreshTask());
	}

	public boolean isRefreshRequired(RefreshingServiceInstance si) {
		try {
			si.currentTime();
			Calendar cal = si.getSessionManager().getCurrentSession()
					.getLoginTime();
			long loginTime = cal.getTimeInMillis();
			long age = System.currentTimeMillis() - loginTime;
			
			if (age > TimeUnit.MINUTES.toMillis(15)) {
				return true;
			}
		} catch (Exception e) {
			// if the currentTime() call is rejected our session token is likely expired
			return true;
		}

		return false;
	}

	public class RefreshTask extends Task {

		@Override
		public void execute(TaskExecutionContext arg0) throws RuntimeException {

			List<WeakReference<RefreshingServiceInstance>> deleteList = Lists
					.newArrayList();

			for (WeakReference<RefreshingServiceInstance> ref : instances) {
				RefreshingServiceInstance si = ref.get();

				if (si != null) {
					try {
						if (isRefreshRequired(si)) {
							logger.info("refreshing session token: {}", si);
							RefreshingServiceInstance.refreshToken(si);
						}
					} catch (Exception e) {
						logger.warn("problem refreshing session token", e);
					}
				} else {
					deleteList.add(ref);
				}
			}

			instances.removeAll(deleteList);
		}
	}

	@Override
	public void doConfigureDefinition(ServiceDefinition def) {
		super.doConfigureDefinition(def);
		computeScannerManager.register(new VSphereComputeServiceScanner(def
				.getName()));
	}

	@Override
	protected Object doCreateInstance(ServiceDefinition def) {
		try {
			boolean ignoreCerts = !Boolean.parseBoolean(def.getProperties()
					.getProperty(
							VSphereFactory.CERTIFICATE_VERIFICATION_ENABLED,
							VSphereFactory.CERTIFICATE_VERIFICATION_DEFAULT)); // invert
			// boolean
			// value
			// meaning
			RefreshingServiceInstance si = new RefreshingServiceInstance(
					new URL(

					def.getProperties().getProperty("url")), def
							.getProperties().getProperty("username"), def
							.getProperties().getProperty("password"),
					ignoreCerts);

			final RefreshingServiceInstance cglibProxy = HotSwapping
					.proxy(RefreshingServiceInstance.class).with(si)
					.mode(DelegationMode.DIRECT).build(cglibProxyFactory);
			addAutoRefresh(cglibProxy);
			return cglibProxy;
		} catch (IOException e) {
			throw new MacGyverException(e);
		}

	}

	protected void addAutoRefresh(RefreshingServiceInstance serviceInstance) {
		instances.add(new WeakReference<RefreshingServiceInstance>(
				serviceInstance));
	}
}
