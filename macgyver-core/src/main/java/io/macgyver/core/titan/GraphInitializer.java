package io.macgyver.core.titan;

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

	public abstract void doInit(TransactionalGraph graph);
}
