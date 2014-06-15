package io.macgyver.plugin.cloud.vsphere;

import java.rmi.RemoteException;
import java.util.List;

import com.google.common.collect.Lists;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class VmQueryTemplate {

	ServiceInstance serviceInstance;

	public VmQueryTemplate(ServiceInstance serviceInstance) {
		this.serviceInstance = serviceInstance;
	}

	ServiceInstance getServiceInstance() {
		return serviceInstance;
	}

	public Iterable<VirtualMachine> findAllVirtualMachines() {
		try {

			InventoryNavigator nav = new InventoryNavigator(
					getServiceInstance().getRootFolder());

			ManagedEntity[] entitites = nav
					.searchManagedEntities("VirtualMachine");
			List<VirtualMachine> vmList = Lists.newArrayList();

			for (ManagedEntity me : entitites) {
				vmList.add((VirtualMachine) me);
			}

			return java.util.Collections.unmodifiableList(vmList);
		} catch (RemoteException e) {
			throw new VSphereExceptionWrapper(e);
		}

	}

}
