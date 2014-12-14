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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.FluentIterable.from;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.collect.Memoized;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadata.Status;
import org.jclouds.compute.domain.NodeMetadataBuilder;
import org.jclouds.compute.functions.GroupNamingConvention;
import org.jclouds.compute.predicates.HardwarePredicates;
import org.jclouds.domain.Credentials;
import org.jclouds.domain.Location;
import org.jclouds.domain.LoginCredentials;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.vmware.vim25.GuestInfo;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.mo.VirtualMachine;

@Singleton
public class VirtualMachineToNodeMetaData implements
		Function<VirtualMachine, NodeMetadata> {

	/*
	 * public static final Map<Server.Status, Status> serverStatusToNodeStatus =
	 * ImmutableMap .<Server.Status, Status> builder().put(Server.Status.ACTIVE,
	 * Status.RUNNING)// .put(Server.Status.BUILD, Status.PENDING)//
	 * .put(Server.Status.TERMINATED, Status.TERMINATED)//
	 * .put(Server.Status.UNRECOGNIZED, Status.UNRECOGNIZED)// .build();
	 */

	private final Supplier<Set<? extends Hardware>> hardware;
	private final Supplier<Set<? extends Location>> locations;
	private final Supplier<Set<? extends Image>> images;
	private final Map<String, Credentials> credentialStore;
	private final GroupNamingConvention nodeNamingConvention;

	@Inject
	VirtualMachineToNodeMetaData(Map<String, Credentials> credentialStore,
			@Memoized Supplier<Set<? extends Hardware>> hardware,
			@Memoized Supplier<Set<? extends Location>> locations,
			@Memoized Supplier<Set<? extends Image>> images,
			GroupNamingConvention.Factory namingConvention) {
		this.nodeNamingConvention = checkNotNull(namingConvention,
				"namingConvention").createWithoutPrefix();
		this.credentialStore = checkNotNull(credentialStore, "credentialStore");
		this.hardware = checkNotNull(hardware, "hardware");
		this.locations = checkNotNull(locations, "locations");
		this.images = checkNotNull(images, "images");
	}

	@Override
	public NodeMetadata apply(VirtualMachine from) {

		GuestInfo guestInfo = from.getGuest();
		VirtualMachineRuntimeInfo rt = from.getRuntime();



		
		
		
		// convert the result object to a jclouds NodeMetadata
		NodeMetadataBuilder builder = new NodeMetadataBuilder();

		String uuid = from.getConfig().getUuid();
		builder.id(uuid);
		builder.providerId(uuid);
		builder.name(from.getName());
		
		
		builder.group(nodeNamingConvention.groupInUniqueNameOrNull(""));
		builder.imageId("");

		builder.hardware(from(hardware.get()).firstMatch(
				HardwarePredicates.idEquals("")).orNull());
	
		
		VirtualMachinePowerState ps = rt.getPowerState();
	
		if (ps==VirtualMachinePowerState.poweredOn) {
			builder.status(Status.RUNNING);
		}
		else if (ps==VirtualMachinePowerState.poweredOff || ps==VirtualMachinePowerState.suspended) {
			builder.status(Status.SUSPENDED);
		}
		else {
			builder.status(Status.UNRECOGNIZED);
		}
		builder.publicAddresses(ImmutableSet.<String> of(""));
		if (guestInfo != null) {
			
			String ipAddress = guestInfo.getIpAddress();
			if (ipAddress != null) {
				builder.privateAddresses(ImmutableSet.<String> of(guestInfo
						.getIpAddress()));
			}
			else {
				builder.privateAddresses(ImmutableSet.<String>of(""));
			}
		}
		builder.credentials(LoginCredentials.fromCredentials(credentialStore
				.get("")));
		return builder.build();
	}
}
