package io.macgyver.core.graph;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.tinkerpop.blueprints.TransactionalGraph;

public abstract class GraphInitializer {

	@Autowired
	TransactionalGraph graph;

	@PostConstruct
	public void init() {
		doInit(graph);
	}

	public abstract void ensureVertexIndex(String pname, Class<? extends Object> type);

	public abstract void ensureUniqueVertexIndex(String pname, Class<? extends Object> type);

	public abstract void doInit(TransactionalGraph graph);

}
