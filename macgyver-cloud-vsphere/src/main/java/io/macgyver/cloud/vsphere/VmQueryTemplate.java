package io.macgyver.cloud.vsphere;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class VmQueryTemplate {

	ServiceInstanceFactory factory;

	public VmQueryTemplate(ServiceInstanceFactory factory) {
		this.factory = factory;
	}
	
	ServiceInstance getServiceInstance() {
		return factory.getServiceInstance();
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
