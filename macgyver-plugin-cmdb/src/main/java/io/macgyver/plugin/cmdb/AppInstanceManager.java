package io.macgyver.plugin.cmdb;

import io.macgyver.core.titan.GraphRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.RunElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanGraphQuery;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;

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

			Vertex v = getGraph().addVertex(null);
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
