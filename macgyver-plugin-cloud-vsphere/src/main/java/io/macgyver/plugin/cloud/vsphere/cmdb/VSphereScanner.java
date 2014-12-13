package io.macgyver.plugin.cloud.vsphere.cmdb;


import java.rmi.RemoteException;
import java.util.Iterator;

import io.macgyver.neorx.rest.NeoRxClient;
import io.macgyver.plugin.cloud.vsphere.ManagedObjectTypes;
import io.macgyver.plugin.cloud.vsphere.VSphereExceptionWrapper;
import io.macgyver.plugin.cloud.vsphere.VSphereQueryTemplate;

import org.jclouds.compute.domain.NodeMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gwt.thirdparty.guava.common.hash.Hashing;
import com.vmware.vim25.GuestInfo;
import com.vmware.vim25.GuestNicInfo;
import com.vmware.vim25.HostHardwareInfo;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.Tag;
import com.vmware.vim25.VirtualHardware;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.mo.Datastore;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.ServerConnection;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class VSphereScanner {

	ObjectMapper mapper = new ObjectMapper();
	Logger logger = LoggerFactory.getLogger(VSphereScanner.class);

	ServiceInstance serviceInstance;
	NeoRxClient client;
	String vcenterUuid;

	protected VSphereScanner() {
		// for unit testing
	}
	public VSphereScanner(ServiceInstance si, NeoRxClient client) {

		Preconditions.checkNotNull(si);
		Preconditions.checkNotNull(client);
		serviceInstance = si;
		this.client = client;
	}

	public synchronized String getVCenterId() {
		if (vcenterUuid == null) {
			vcenterUuid = serviceInstance.getAboutInfo().getInstanceUuid();
		}
		return vcenterUuid;
	}

	private void setVal(ObjectNode n, String prop, String val) {
		n.put(prop, val);
	}

	public String createSetClause(String alias, JsonNode n) {
		StringBuffer sb = new StringBuffer();
		Iterator<String> t = n.fieldNames();
		int i = 0;
		while (t.hasNext()) {
			String key = t.next();

			sb.append(String.format("%s %s.%s={%s} ", (i > 0) ? "," : "",
					alias, key, key));
			i++;
		}
		return sb.toString();
	}

	public void updateComputeHost(ObjectNode n) {
		String setClause = createSetClause("c", n);

		String cypher = String
				.format("merge (c:ComputeHost {macId:{macId}}) on match set %s ,c.lastUpdateTs=timestamp() ON CREATE SET %s, c.lastUpdateTs=timestamp() return c",
						setClause, setClause);

		client.execCypher(cypher, n);

	}

	public void updateComputeInstance(ObjectNode n) {

	
		String setClause = createSetClause("c", n);

		String cypher = "merge (c:ComputeInstance {macId:{macId}}) on match set "
				+ setClause
				+ ",c.lastUpdateTs=timestamp() ON CREATE SET "
				+ setClause + ", c.lastUpdateTs=timestamp() return c";

		JsonNode res = client.execCypher(cypher, n).toBlocking().first();
		System.out.println(res);
	}

	String getMacUuid(VirtualMachine vm) {
		return vm.getConfig().getInstanceUuid();
	}

	public String computeMacId(ManagedObjectReference mor) {
		Preconditions.checkNotNull(mor,"ManagedObjectReference cannot be null");
		
		Preconditions.checkArgument(! ManagedObjectTypes.VIRTUAL_MACHINE.equals(mor.getType()), "cannot call computeMacId() with mor.type=VirtualMachine");
	
		return Hashing
				.sha1()
				.hashString(getVCenterId() + mor.getType() + mor.getVal(),
						Charsets.UTF_8).toString();
	}

	ObjectNode toComputeNodeData(VirtualMachine vm) {
		ObjectNode n = mapper.createObjectNode();
		try {
			VirtualMachineConfigInfo cfg = vm.getConfig();

			// http://www.virtuallyghetto.com/2011/11/vsphere-moref-managed-object-reference.html

			ServerConnection sc = vm.getServerConnection();
			
			ManagedObjectReference mor = vm.getMOR();
			String moType = mor.getType();
			String moVal = mor.getVal();
			GuestInfo g = vm.getGuest();
			setVal(n, "name", vm.getName());
			setVal(n, "macId", getMacUuid(vm));
			setVal(n, "vmwInstanceUuid", cfg.getInstanceUuid());
			setVal(n, "vmwMorVal", moVal);
			setVal(n, "vmwMorType", moType);
			setVal(n, "vmwAnnotation", cfg.getAnnotation());
			setVal(n, "vmwGuestToolsVersion", g.getToolsVersion());
			setVal(n, "vmwGuestId", g.getGuestId());
			setVal(n, "vmwGuestFamily", g.getGuestFamily());
			setVal(n, "vmwGuestFullName", g.getGuestFullName());
			setVal(n, "vmwGuestIpAddress", g.getIpAddress());
			setVal(n, "vmwGuestId", g.getGuestId());
			setVal(n, "vmwGuestHostName", g.getHostName());
			setVal(n, "vmwGuestAlternateName", cfg.getAlternateGuestName());
			setVal(n, "vmwLocationId", cfg.getLocationId());

			/*
			 * GuestNicInfo[] gni = g.getNet(); if (gni != null) { for
			 * (GuestNicInfo ni : g.getNet()) {
			 * System.out.println(ni.macAddress+" "+ni.getNetwork());
			 * 
			 * } }
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}

	public void scan(VirtualMachine vm) {
		logger.info("scanning vm: {}", vm.getName());
		ObjectNode n = toComputeNodeData(vm);
		updateComputeInstance(n);
	}

	ObjectNode toObjectNode(HostSystem host) {
		ManagedObjectReference mor = host.getMOR();
		HostHardwareInfo hh = host.getHardware();
		ObjectNode n = mapper.createObjectNode()
				.put("macId", getMacId(host))
				.put("name", host.getName()).put("vmwMorType", mor.getType())
				.put("vmwMorVal", mor.getVal()).put("vmwHardwareModel",

				hh.getSystemInfo().getModel())
				.put("vmwCpuCoreCount", hh.getCpuInfo().getNumCpuCores())
				.put("vmwMemorySize", hh.getMemorySize());

		return n;
	}

	public void updateHostVmRelationship(HostSystem h, VirtualMachine vm) {
		String cypher = "match (h:ComputeHost {macId:{hostMacUuid} }), (c:ComputeInstance {macId: {computeMacUuid}}) MERGE (h)-[r:HOSTS]->(c) ON CREATE SET r.lastUpdateTs=timestamp() ON MATCH SET r.lastUpdateTs=timestamp() return r";
		client.execCypher(cypher, "hostMacUuid", getMacId(h), "computeMacUuid",
				getMacUuid(vm));

	}

	public void scanHost(HostSystem host)  {
		try {
		ObjectNode n = toObjectNode(host);

		System.out.println(n);
		updateComputeHost(n);

		long now = client.execCypher("return timestamp() as ts").toBlocking()
				.first().asLong();

		VirtualMachine[] vms = host.getVms();
		if (vms != null) {
			for (VirtualMachine vm : vms) {
				try {
					scan(vm);
					updateHostVmRelationship(host, vm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		clearStaleRelationships(host, now);
		}
		catch (RemoteException e) {
			throw new VSphereExceptionWrapper(e);
		}

	}

	protected String getMacId(HostSystem h) {
		return computeMacId(h.getMOR());

	}

	protected void clearStaleRelationships(HostSystem host, long ts) {
		String cypher = "match (h:ComputeHost {macId:{macId}})-[r:HOSTS]-(c:ComputeInstance) where r.lastUpdateTs<{ts} delete r";

		client.execCypher(cypher, "macId", getMacId(host), "ts", ts);

	}

	public void scanAllHosts() {
		VSphereQueryTemplate t = new VSphereQueryTemplate(serviceInstance);
		for (HostSystem host : t.findAllHostSystems()) {

			try {
				scanHost(host);

			} catch (Exception e) {
				e.printStackTrace();

			}

		}
	}

	public void scanAllVirtualMachines() {
		VSphereQueryTemplate t = new VSphereQueryTemplate(serviceInstance);
		for (VirtualMachine vm : t.findAllVirtualMachines()) {

			try {
				scan(vm);

			} catch (Exception e) {
				e.printStackTrace();

			}

		}
	}
}
