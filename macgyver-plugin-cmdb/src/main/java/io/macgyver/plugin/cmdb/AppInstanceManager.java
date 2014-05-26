package io.macgyver.plugin.cmdb;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;

public class AppInstanceManager {

	@Autowired
	TitanGraph graph;

	Set<String> keyExcludes = ImmutableSet.of("vertexType");

	public void save(AppInstance app) {

		try {
			Vertex v = null;
			Iterator<Vertex> t = graph.query()
					.has(AppInstance.VERTEX_TYPE_PROP, AppInstance.VERTEX_TYPE)
					.has("host", app.getHost()).has("appId", app.getAppId())
					.vertices().iterator();
			if (t.hasNext()) {
				v = t.next();

			} else {
				v = graph.addVertex(null);
				v.setProperty("host", app.getHost());
				v.setProperty("appId", app.getAppId());
			}

			for (Map.Entry<String, Object> entry : app.getProperties()
					.entrySet()) {
				v.setProperty(entry.getKey().toString(), entry.getValue());
			}
			
			
			Map<String,Object> appInstanceProperties = app.getProperties();
			for (String vertexKey : v.getPropertyKeys()) {
				if (!appInstanceProperties.containsKey(vertexKey)) {
					v.removeProperty(vertexKey);
				}
			}
		} finally {
			graph.commit();
		}
	}

	public AppInstance getOrCreateAppInstance(String host, String appId) {
		return getOrCreateAppInstance(host, appId, null);
	}

	public AppInstance getOrCreateAppInstance(String host, String appId,
			Map<String, Object> props) {
		Preconditions.checkNotNull("host cannot be null or empty",
				Strings.emptyToNull(host));
		Preconditions.checkNotNull("appId cannot be null or empty",
				Strings.emptyToNull(appId));
		Optional<AppInstance> ao = getAppInstance(host, appId);
		if (ao.isPresent()) {
			return ao.get();
		} else {
			Vertex v = graph.addVertex(null);
			v.setProperty("host", host);
			v.setProperty("appId", appId);
			v.setProperty("vertexType", "AppInstance");
			v.setProperty("vertexId",
					AppInstance.calculateVertexId(host, appId,null));
			if (props != null) {
				for (Map.Entry<String, Object> entry : props.entrySet()) {
					v.setProperty(entry.getKey(), entry.getValue());
				}
			}
			return getAppInstance(host, appId).get();
		}
	}

	public void remove(AppInstance ai) {
		Preconditions.checkNotNull(ai);

		try {
			Iterator<Vertex> t = graph.query().has("vertexType", "AppInstance")
					.has("host", ai.getHost()).has("appId", ai.getAppId())
					.vertices().iterator();
			while (t.hasNext()) {
				Vertex v = t.next();
				graph.removeVertex(v);
			}
		} finally {
			graph.commit();
		}
	}

	public Optional<AppInstance> getAppInstance(String host, String appId) {
		try {
			Iterator<Vertex> t = graph.query().has("vertexType", "AppInstance")
					.has("host", host).has("appId", appId).vertices()
					.iterator();
			if (t.hasNext()) {
				Vertex v = t.next();

				AppInstance ai = new AppInstance(v.getProperty("host").toString(),v.getProperty("appId").toString());
			
				for (String key : v.getPropertyKeys()) {
					if (!isFilteredKey(key)) {
						ai.props.put(key, v.getProperty(key));
					}
				}
				return Optional.of(ai);
			} else {
				return Optional.absent();
			}
		} finally {
			graph.commit();
		}
	}

	protected boolean isFilteredKey(String key) {

		return keyExcludes.contains(key);

	}

	public AppInstance fromVertex(Vertex v) {
		AppInstance ai = new AppInstance(v.getProperty("host").toString(),v.getProperty("appId").toString());
		
		for (String p : v.getPropertyKeys()) {
			ai.getProperties().put(p, v.getProperty(p));
		}
		return ai;
	}

	public Iterable<AppInstance> findAll() {
		
		List<AppInstance> list = Lists.newArrayList();
		for (Vertex v : graph.query()
				.has(AppInstance.VERTEX_TYPE_PROP, AppInstance.VERTEX_TYPE)

				.vertices()) {
			AppInstance ai = fromVertex(v);
			list.add(ai);
		}
		return list;
	}
}
