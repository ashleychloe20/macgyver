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
package io.macgyver.plugin.cloud.vsphere.jclouds;

import io.macgyver.plugin.cloud.vsphere.VSphereExceptionWrapper;
import io.macgyver.plugin.cloud.vsphere.VSphereQueryTemplate;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Singleton;

import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.rest.config.CredentialStoreModule;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.util.MorUtil;

/**
 * This would be replaced with the real connection to the service that can
 * create/list/reboot/get/destroy things
 */
@Singleton
public class VSphereServerManager {

	private static final AtomicInteger nodeIds = new AtomicInteger(0);

	@Inject
	VSphereApiMetadata smd;

	@Inject
	CreateAndConnectVSphereClient client;

	public VSphereServerManager() {

	}

	/**
	 * simulate creating a server, as this is really going to happen with the
	 * api underneath
	 * 
	 * @param name
	 * @param name
	 * @param imageId
	 * @param hardwareId
	 * @return new server
	 */
	public VirtualMachine createServerInDC(String datacenter, String name,
			int imageId, int hardwareId) {

		throw new UnsupportedOperationException();
	}

	public VirtualMachine getServer(String serverId) {
		try {
			ServiceInstance si = client.get().getInstance();

			VirtualMachine vm = (VirtualMachine) si.getSearchIndex()
					.findByUuid(null, serverId, true);
			
			if (vm==null) {
				ManagedObjectReference mor  =new ManagedObjectReference();
				mor.setType("VirtualMachine");
				mor.setVal(serverId);
				vm = (VirtualMachine) MorUtil.createExactManagedEntity(si.getServerConnection(), mor);
				
			}
			return vm;
		} catch (RemoteException e) {

			throw new VSphereExceptionWrapper(e);
		}

		
		
	}

	AtomicReference<String> vcenterUUID = new AtomicReference<String>();
	protected String getVCenterUUID() {
		if (vcenterUUID.get()==null) {
			String uuid = client.get().getInstance().getAboutInfo().getInstanceUuid();
			if (uuid==null) {
				throw new IllegalStateException("Could not obtain vcenter UUID");
			}
			vcenterUUID.set(uuid);
		}
		
		return vcenterUUID.get();
	}
	
	public Iterable<VirtualMachine> listServers() {

		ServiceInstance si = client.get().getInstance();

		try {

			InventoryNavigator nav = new InventoryNavigator(si.getRootFolder());

	
			ManagedEntity[] entitites = nav
					.searchManagedEntities("VirtualMachine");

			List<VirtualMachine> vmList = Lists.newArrayList();

			for (ManagedEntity me : entitites) {
				VirtualMachine vm = (VirtualMachine) me;

				
				vmList.add((VirtualMachine) me);
			}

			return java.util.Collections.unmodifiableList(vmList);
		} catch (RemoteException e) {
			throw new VSphereExceptionWrapper(e);
		}

	}

	public Image getImage(int imageId) {
		throw new UnsupportedOperationException();
	}

	public Iterable<Image> listImages() {
		throw new UnsupportedOperationException();
	}

	public Hardware getHardware(int hardwareId) {
		throw new UnsupportedOperationException();
	}

	public Iterable<io.macgyver.plugin.cloud.vsphere.jclouds.Hardware> listHardware() {
		return Lists.newArrayList();

	}

	public void destroyServer(int serverId) {
		throw new UnsupportedOperationException();
	}

	public void rebootServer(int serverId) {
		throw new UnsupportedOperationException();
	}

	public void stopServer(int serverId) {
		throw new UnsupportedOperationException();
	}

	public void startServer(int serverId) {
		throw new UnsupportedOperationException();
	}
}
