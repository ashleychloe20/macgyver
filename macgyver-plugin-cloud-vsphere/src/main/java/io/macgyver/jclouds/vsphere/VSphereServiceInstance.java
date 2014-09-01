package io.macgyver.jclouds.vsphere;

import java.io.Closeable;

import com.google.common.base.Preconditions;
import com.vmware.vim25.mo.ServiceInstance;

public class VSphereServiceInstance implements Closeable{
	  private ServiceInstance instance;
	
	   public VSphereServiceInstance(ServiceInstance instance) {
	      this.instance = Preconditions.checkNotNull(instance, "ServiceInstance");
	   }
	
	   public ServiceInstance getInstance() {
	      return instance;
	   }
	
	   @Override
	   public void close() throws java.io.IOException {
	      instance.getServerConnection().logout();
	   }
}
