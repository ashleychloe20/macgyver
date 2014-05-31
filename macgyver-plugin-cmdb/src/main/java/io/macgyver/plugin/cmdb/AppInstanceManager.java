package io.macgyver.plugin.cmdb;

import io.macgyver.core.graph.GraphRepository;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
//https://github.com/orientechnologies/orientdb/wiki/Graph-Database-Tinkerpop
public class AppInstanceManager extends GraphRepository {
	Logger logger = LoggerFactory.getLogger(AppInstanceManager.class);

	Set<String> keyExcludes = ImmutableSet.of("vertexType",
			AppInstance.KEY_APP_ID, AppInstance.KEY_GROUP_ID, "vertexId");

	public AppInstanceManager(TransactionalGraph g) {
		super(g);
	}

	public synchronized Vertex getOrCreateAppInstanceVertex(String host,
			String groupId, String appId) {

		GraphQuery q = getGraph().query()
				.has(AppInstance.KEY_VERTEX_TYPE, "AppInstance")
				.has(AppInstance.KEY_HOST, host)
				.has(AppInstance.KEY_GROUP_ID, groupId)
				.has(AppInstance.KEY_APP_ID, appId);

		Optional<Vertex> vo = findOneVertex(q);
		if (vo.isPresent()) {

			Vertex v = vo.get();
			return v;
		} else {

			vo = findOneVertex(q);
			if (vo.isPresent()) {

				Vertex v = vo.get();
				return v;
			}

			Vertex v = getGraph().addVertex("class:AppInstance");
			v.setProperty("vertexId",
					AppInstance.calculateVertexId(host, groupId, appId, null));
			v.setProperty(AppInstance.KEY_VERTEX_TYPE,
					AppInstance.APP_INSTANCE_TYPE);
			v.setProperty(AppInstance.KEY_HOST, host);
			v.setProperty(AppInstance.KEY_GROUP_ID, groupId);
			v.setProperty(AppInstance.KEY_APP_ID, appId);

			return v;

		}

	}

}
