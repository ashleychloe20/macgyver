package io.macgyver.core.graph;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;

public class GraphRepository {

	Logger logger = LoggerFactory.getLogger(GraphRepository.class);

	ObjectMapper mapper;

	TransactionalGraph graph;

	public GraphRepository() {

		mapper = createObjectMapper();
	}

	public GraphRepository(TransactionalGraph g) {
		Preconditions.checkNotNull(g);
		this.graph = g;
		mapper = createObjectMapper();
	}

	public Optional<ObjectNode> findOneObjectNode(String key, Object val) {
		Map<String, Object> m = Maps.newHashMap();
		m.put(key, val);

		return findOneObjectNode(m);
	}

	public Iterable<Vertex> findVertices(String key, Object val) {
		return findVertices(key, val, null);
	}

	public Iterable<Vertex> findVertices(String key, Object val, Comparator c) {
		return findVertices(toGraphQuery(key, val), c);
	}

	public Iterable<Vertex> findVertices(Map<String, Object> x) {
		return findVertices(x, null);
	}

	public Iterable<Vertex> findVertices(Map<String, Object> x, Comparator c) {
		GraphQuery q = graph.query();
		for (Map.Entry<String, Object> me : x.entrySet()) {
			q = q.has(me.getKey(), me.getValue());
		}
		return findVertices(q, c);
	}

	public Iterable<Vertex> findVertices(GraphQuery q) {
		return findVertices(q, null);
	}

	public Iterable<Vertex> findVertices(GraphQuery q, Comparator c) {

		Iterable<Vertex> v = q.vertices();
		if (c != null && c instanceof VertexComparator) {

			List tmp = Lists.newArrayList();
			Iterables.addAll(tmp, v);
			Collections.sort(tmp, c);
			v = tmp;
		}
		return v;

	}

	public Iterable<ObjectNode> findObjectNodes(String key, Object val) {
		return findObjectNodes(key, val, null);
	}

	public Iterable<ObjectNode> findObjectNodes(String key, Object val,
			Comparator c) {
		Map<String, Object> x = Maps.newHashMap();
		x.put(key, val);
		return findObjectNodes(x, c);
	}

	public Iterable<ObjectNode> findObjectNodes(Map<String, Object> x) {
		return findObjectNodes(x);
	}

	public Iterable<ObjectNode> findObjectNodes(GraphQuery q) {
		return findObjectNodes(q,null);
	}
	public Iterable<ObjectNode> findObjectNodes(GraphQuery q, Comparator c) {
		Iterable<Vertex> vt = findVertices(q, c);

		List<ObjectNode> list = Lists.newArrayList();
		for (Vertex v : vt) {
			ObjectNode n = toObjectNode(v);
			list.add(n);
		}

		if (c != null) {
			Collections.sort(list, c);
		}
		return list;

	}

	public Iterable<ObjectNode> findObjectNodes(Map<String, Object> x,
			Comparator c) {
		return findObjectNodes(toGraphQuery(x), c);

	}

	public Optional<ObjectNode> findOneObjectNode(GraphQuery q) {
		Optional<Vertex> v = findOneVertex(q);
		if (v.isPresent()) {
			Vertex vx = v.get();
			return Optional.of(toObjectNode(vx));
		} else {
			return Optional.absent();
		}
	}

	public Optional<ObjectNode> findOneObjectNode(Map<String, Object> x) {
		return findOneObjectNode(toGraphQuery(x));

	}

	public Optional<Vertex> findOneVertex(String key, Object val) {
		Map<String, Object> m = Maps.newHashMap();
		m.put(key, val);
		return findOneVertex(m);
	}

	
	public Optional<Vertex> findOneVertex(GraphQuery q) {

		Iterator<Vertex> t = q.limit(1).vertices().iterator();

		if (t.hasNext()) {

			return Optional.of(t.next());

		} else {

			return Optional.absent();
			
		}

	}

	public Optional<Vertex> findOneVertex(Map<String, Object> x) {
		GraphQuery q = toGraphQuery(x);

		return findOneVertex(q);

	}

	ObjectNode toObjectNode(Vertex v) {
		ObjectNode n = mapper.createObjectNode();
		
		for (String key : v.getPropertyKeys()) {
			Object val = v.getProperty(key);
			if (val instanceof String) {
				n.put(key, (String) val);
			}
		}
		return n;
	}

	public <T> Iterable<T> find(Map<String, Object> m, Class<T> clazz,
			Comparator c) {
		Iterable<ObjectNode> t = findObjectNodes(m);
		List<T> tmp = Lists.newArrayList();
		for (ObjectNode n : t) {
			try {
				T val = mapper.treeToValue(n, clazz);
				tmp.add(val);
			} catch (Exception e) {
				logger.warn("problem converting item to {}", clazz);
			}
		}
		if (c != null) {
			Collections.sort(tmp, c);
		}
		return tmp;
	}

	public <T> Optional<T> findOne(String key, Object val,
			Class<? extends T> clazz) {
		return findOne(toKeyValueMap(key, val), clazz);
	}

	public <T> Optional<T> findOne(GraphQuery q, Class<? extends T> clazz) {

		try {
			Optional<ObjectNode> t = findOneObjectNode(q);

			if (!t.isPresent()) {
				return Optional.absent();
			}
			JsonNode properties = t.get();
			return (Optional<T>) Optional.of(mapper.treeToValue(properties,
					clazz));
		} catch (Exception e) {
			logger.debug("problem processing entity", e);
			return Optional.absent();
		}
	}

	public <T> Optional<T> findOne(Map<String, Object> m,
			Class<? extends T> clazz) {
		try {
			Optional<ObjectNode> t = findOneObjectNode(m);

			if (!t.isPresent()) {
				return Optional.absent();
			}
			JsonNode properties = t.get();
			return (Optional<T>) Optional.of(mapper.treeToValue(properties,
					clazz));
		} catch (Exception e) {
			logger.debug("problem processing entity", e);
			return Optional.absent();
		}

	}

	private Map<String, Object> toKeyValueMap(String key, Object val) {
		Map<String, Object> m = Maps.newHashMap();
		if (val != null) {
			m.put(key, val);
		}
		return m;
	}

	public GraphQuery newQuery() {
		return graph.query();
	}

	public TransactionalGraph getGraph() {
		return graph;
	}

	public void setGraph(TransactionalGraph g) {
		this.graph = g;
	}

	public ObjectMapper createObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		return mapper;

	}

	public GraphQuery toGraphQuery(String key, Object val) {
		return toGraphQuery(toKeyValueMap(key, val));
	}

	public GraphQuery toGraphQuery(Map<String, Object> m) {
		GraphQuery q = getGraph().query();
		for (Map.Entry<String, Object> me : m.entrySet()) {
			q = q.has(me.getKey(), me.getValue());
		}
		return q;

	}
}
