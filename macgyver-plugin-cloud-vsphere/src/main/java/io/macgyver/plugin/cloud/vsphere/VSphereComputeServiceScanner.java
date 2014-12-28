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

import com.vmware.vim25.mo.ServiceInstance;

import io.macgyver.core.Kernel;
import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.neorx.rest.NeoRxClient;
import io.macgyver.plugin.cloud.ComputeScanner;
import io.macgyver.plugin.cloud.vsphere.cmdb.VSphereScanner;

public class VSphereComputeServiceScanner implements ComputeScanner {

	String serviceName;
	
	public VSphereComputeServiceScanner(String name) {
		this.serviceName = name;
	}
	public String getServiceName() {
		return serviceName;
	}
	@Override
	public void scan() {
		NeoRxClient client = Kernel.getApplicationContext().getBean(NeoRxClient.class);
		ServiceRegistry reg = Kernel.getInstance().getApplicationContext().getBean(ServiceRegistry.class);
		
		ServiceInstance service = reg.get(serviceName, ServiceInstance.class);
		
		VSphereScanner scanner = new VSphereScanner(service, client);
		scanner.scanAllHosts();

	}

}
