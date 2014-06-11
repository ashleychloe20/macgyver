package io.macgyver.core.graph;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.tinkerpop.blueprints.TransactionalGraph;

public abstract class GraphInitializer {

	@Autowired
	TransactionalGraph graph;


	public abstract void ensureVertexIndex(String pname, Class<? extends Object> type);

	public abstract void ensureUniqueVertexIndex(String pname, Class<? extends Object> type);

	public abstract void doInitGraphData(TransactionalGraph g);

	public abstract void doInitGraphMetadata(TransactionalGraph g);
	
	public final void initGraphMetadata(TransactionalGraph g) {
		doInitGraphMetadata(g);
	}
	
	public final void initGraphData(TransactionalGraph g) {
		doInitGraphData(g);
	}
}
