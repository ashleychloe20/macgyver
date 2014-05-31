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

	public abstract void createVertexIndex(String pname);
	public abstract void createUniqueVertexIndex(String pname);
	
	public abstract void doInit(TransactionalGraph graph);
	
	
}
