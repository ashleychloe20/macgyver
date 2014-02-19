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

	ServiceInstance serviceInstance;

	Gson gson = VSphereGsonBuilder.createBuilder().setPrettyPrinting().create();

	public VmQueryTemplate(ServiceInstance serviceInstance) {
		this.serviceInstance = serviceInstance;
	}
	
	ServiceInstance getServiceInstance() {
		return serviceInstance;
	}

	public JsonElement asJson(VirtualMachine vm) {

		return (JsonElement) gson.toJsonTree(vm);
	}

	public Iterable<JsonElement> findAllVirtualMachinesAsJson() {

		final Iterator<VirtualMachine> innerT = findAllVirtualMachines()
				.iterator();

		final Iterator<JsonElement> t = new Iterator<JsonElement>() {

			@Override
			public boolean hasNext() {
				return innerT.hasNext();
			}

			@Override
			public JsonElement next() {
				VirtualMachine vm = innerT.next();
				return asJson(vm);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};

		return new Iterable<JsonElement>() {

			@Override
			public Iterator<JsonElement> iterator() {
				return t;
			}

		};

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
