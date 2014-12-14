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
