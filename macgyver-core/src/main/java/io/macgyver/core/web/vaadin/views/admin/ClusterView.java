package io.macgyver.core.web.vaadin.views.admin;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hazelcast.core.Member;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;

import io.macgyver.core.Kernel;
import io.macgyver.core.cluster.ClusterManager;
import io.macgyver.core.web.vaadin.IndexedJsonContainer;
import io.macgyver.core.web.vaadin.ViewConfig;
import io.macgyver.core.web.vaadin.views.StandardMacGyverView;

@ViewConfig(menuPath = { "Admin", "Cluster Info" }, viewName = "admin/cluster")
public class ClusterView extends StandardMacGyverView {

	Table table;
	IndexedJsonContainer container;

	public ClusterView() {
		super();

		container = new IndexedJsonContainer();

		table = new Table("Cluster Info", container);
		table.addContainerProperty("uuid", String.class, null, "UUID", null,
				Align.LEFT);
		table.addContainerProperty("host", String.class, null, "Host", null,
				Align.LEFT);
		table.addContainerProperty("port", String.class, null, "Port", null,
				Align.LEFT);
		table.addContainerProperty("master", String.class, null, "Master",
				null, Align.LEFT);
		table.addContainerProperty("lastHeartbeatSecs", String.class, null,
				"Last Heartbeat (Seconds Ago)", null, Align.LEFT);

		addComponent(table);

	}

	public void refresh() {
		container.removeAllItems();

		ClusterManager clusterManager = Kernel.getApplicationContext().getBean(
				ClusterManager.class);
		Set<Member> members = clusterManager.getHazelcastInstance()
				.getCluster().getMembers();
		
		for (Member m : members) {

			InetSocketAddress addr = m.getSocketAddress();

			ObjectNode n = new ObjectMapper().createObjectNode();
			n.put("uuid", m.getUuid());
			n.put("host", addr.getHostString());
			n.put("port", addr.getPort());
			Long heartbeat = m.getLongAttribute("heartbeat");
			
			boolean master = clusterManager.isMaster(m.getUuid());
			if (heartbeat == null) {
				heartbeat = 0L;
			}
			
			n.put("master", master);
			long secsAgo = Math.max(0, System.currentTimeMillis() - heartbeat) / 1000L;
			if (heartbeat == 0) {
				n.put("lastHeartbeatSecs", "never");
			} else {
				n.put("lastHeartbeatSecs", secsAgo);
			}
			container.addJsonObject(n);

		}

	}

	@Override
	public void enter(ViewChangeEvent event) {
		refresh();

	}

}
