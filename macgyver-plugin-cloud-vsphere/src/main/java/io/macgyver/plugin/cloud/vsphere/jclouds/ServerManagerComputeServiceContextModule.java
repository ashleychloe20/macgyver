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

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.inject.Singleton;

import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.config.ComputeServiceAdapterContextModule;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.domain.Location;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class ServerManagerComputeServiceContextModule
		extends
		ComputeServiceAdapterContextModule<VirtualMachine, Hardware, Image, Datacenter> {

	@Override
	protected void configure() {
		super.configure();
		bind(
				new TypeLiteral<ComputeServiceAdapter<VirtualMachine, Hardware, Image, Datacenter>>() {
				}).to(VSphereComputeServiceAdapter.class);
		bind(new TypeLiteral<Function<VirtualMachine, NodeMetadata>>() {
		}).to(VirtualMachineToNodeMetaData.class);
		bind(
				new TypeLiteral<Function<Image, org.jclouds.compute.domain.Image>>() {
				}).to(VSphereImageToImage.class);
		bind(new TypeLiteral<Supplier<VSphereServiceInstance>>() {
		}).to((Class) CreateAndConnectVSphereClient.class);
		bind(
				new TypeLiteral<Function<Hardware, org.jclouds.compute.domain.Hardware>>() {
				}).to(ServerManagerHardwareToHardware.class);
		bind(new TypeLiteral<Function<Datacenter, Location>>() {
		}).to(DatacenterToLocation.class);
		// to have the compute service adapter override default locations
		install(new LocationsFromComputeServiceAdapterModule<VirtualMachine, Hardware, Image, Datacenter>() {
		});

	}

	@Provides
	@Singleton
	protected Function<Supplier<NodeMetadata>, ServiceInstance> client() {
		return new Function<Supplier<NodeMetadata>, ServiceInstance>() {

			@Override
			public ServiceInstance apply(Supplier<NodeMetadata> nodeSupplier) {
				try {
					if (true==true) {
						throw new UnsupportedOperationException();
					}
					return new ServiceInstance(
							new URL("https://localhost/sdk"), "root", "", true);
				} catch (RemoteException e) {
					Throwables.propagate(e);
					return null;
				} catch (MalformedURLException e) {
					Throwables.propagate(e);
					return null;
				}
			}

			@Override
			public String toString() {
				return "createInstanceByNodeId()";
			}

		};
	}
}
