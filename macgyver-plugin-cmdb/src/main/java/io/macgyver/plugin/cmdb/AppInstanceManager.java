package io.macgyver.plugin.cmdb;

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
import com.tinkerpop.blueprints.Vertex;

public class AppInstanceManager {
	Logger logger = LoggerFactory.getLogger(AppInstanceManager.class);

	@Autowired
	TitanGraph graph;

	Set<String> keyExcludes = ImmutableSet.of("vertexType",
			AppInstance.KEY_APP_ID, AppInstance.KEY_GROUP_ID, "vertexId");

	public void save(AppInstance app) {

		Preconditions.checkNotNull(app);
		String host = app.getHost();
		String appId = app.getAppId();
		Preconditions.checkNotNull(host);
		Preconditions.checkNotNull(appId);

		Vertex vertex = getOrCreateVertex(host, appId);

		for (Map.Entry<String, Object> entry : app.getProperties().entrySet()) {
			if (!isFilteredKey(entry.getKey())) {
				vertex.setProperty(entry.getKey().toString(), entry.getValue());
			}
		}

		Map<String, Object> appInstanceProperties = app.getProperties();
		for (String vertexKey : vertex.getPropertyKeys()) {
			if ((!isFilteredKey(vertexKey))
					&& (!appInstanceProperties.containsKey(vertexKey))) {

				vertex.removeProperty(vertexKey);
			}
		}

	}

	public AppInstance getOrCreateAppInstance(String host, String groupId,
			String appId) {
		return getOrCreateAppInstance(host, groupId, appId, null);
	}

	protected Vertex getOrCreateVertex(String host, String appId) {

		Iterator<Vertex> t = graph.query()
				.has(AppInstance.KEY_VERTEX_TYPE, "AppInstance")
				.has(AppInstance.KEY_HOST, host)
				.has(AppInstance.KEY_APP_ID, appId).vertices().iterator();
		if (t.hasNext()) {
			Vertex v = t.next();
			v.setProperty(AppInstance.KEY_HOST, host);
			v.setProperty(AppInstance.KEY_APP_ID, appId);
			return v;
		} else {
			Vertex v = graph.addVertex(null);
			v.setProperty(AppInstance.KEY_HOST, host);
			v.setProperty(AppInstance.KEY_APP_ID, appId);
			return v;
		}

	}

	public AppInstance getOrCreateAppInstance(String host, String groupId,
			String appId, Map<String, Object> props) {

		Preconditions.checkNotNull("host cannot be null or empty",
				Strings.emptyToNull(host));
		Preconditions.checkNotNull("appId cannot be null or empty",
				Strings.emptyToNull(appId));
		Optional<AppInstance> ao = getAppInstance(host, groupId, appId);
		if (ao.isPresent()) {
			return ao.get();
		} else {
			Vertex v = graph.addVertex(null);
			v.setProperty("host", host);
			v.setProperty(AppInstance.KEY_APP_ID, appId);
			v.setProperty(AppInstance.KEY_GROUP_ID, groupId);
			v.setProperty("vertexType", "AppInstance");

			if (props != null) {
				for (Map.Entry<String, Object> entry : props.entrySet()) {
					v.setProperty(entry.getKey(), entry.getValue());
				}
			}
			return getAppInstance(host, groupId, appId).get();
		}

	}

	public void remove(AppInstance ai) {
		Preconditions.checkNotNull(ai);

		Iterator<Vertex> t = graph.query().has("vertexType", "AppInstance")
				.has("host", ai.getHost())
				.has(AppInstance.KEY_APP_ID, ai.getAppId()).vertices()
				.iterator();
		while (t.hasNext()) {
			Vertex v = t.next();
			graph.removeVertex(v);
		}

	}

	public Optional<AppInstance> getAppInstance(String host, String groupId,
			String appId) {

		Iterator<Vertex> t = graph.query().has("vertexType", "AppInstance")
				.has(AppInstance.KEY_HOST, host)
				.has(AppInstance.KEY_APP_ID, appId).vertices().iterator();
		if (t.hasNext()) {
			Vertex v = t.next();

			AppInstance ai = new AppInstance(Objects.toString(v
					.getProperty(AppInstance.KEY_HOST)), Objects.toString(v
					.getProperty(AppInstance.KEY_HOST)), Objects.toString(v
					.getProperty(AppInstance.KEY_APP_ID)));

			for (String key : v.getPropertyKeys()) {
				if (!isFilteredKey(key)) {
					ai.props.put(key, v.getProperty(key));
				}
			}
			return Optional.of(ai);
		} else {
			return Optional.absent();
		}

	}

	protected boolean isFilteredKey(String key) {

		return keyExcludes.contains(key);

	}

	protected Optional<AppInstance> fromVertex(Vertex v) {
		
		String host = Objects.toString(v.getProperty("host"));
		String groupId = Objects.toString(v
				.getProperty(AppInstance.KEY_GROUP_ID));
		String artifactId = Objects.toString(v.getProperty(AppInstance.KEY_APP_ID));
		
		if (Strings.isNullOrEmpty(host) || Strings.isNullOrEmpty(groupId) || Strings.isNullOrEmpty(artifactId) || host.equals("null") || groupId.equals("null") || artifactId.equals("null")) {
			return Optional.absent();
		}
		
		AppInstance ai = new AppInstance(host,groupId,artifactId);

		for (String p : v.getPropertyKeys()) {
			Object val = v.getProperty(p);
			if (val != null) {
				ai.getProperties().put(p,val);
			}
		}
		return Optional.of(ai);
	}

	public Iterable<AppInstance> find() {
		return find(null, null);
	}

	public Iterable<AppInstance> find(Predicate<AppInstance> predicate) {
		return find(predicate, null);
	}

	public Iterable<AppInstance> find(Comparator<AppInstance> comparator) {
		return find(null, comparator);
	}

	public Iterable<AppInstance> find(Predicate<AppInstance> predicate,
			Comparator<AppInstance> comparator) {

		List<AppInstance> list = Lists.newArrayList();
		for (Vertex v : graph
				.query()
				.has(AppInstance.KEY_VERTEX_TYPE, AppInstance.APP_INSTANCE_TYPE)

				.vertices()) {
			try {
				Optional<AppInstance> ai = fromVertex(v);
				if (ai.isPresent() && (predicate == null || predicate.apply(ai.get()))) {
					
				
						list.add(ai.get());
					
				}
			} catch (RuntimeException e) {
			
				logger.debug("error processing row:", e);
			}

		}
		if (comparator != null) {
			Collections.sort(list, comparator);
		}
		return list;
	}
}
