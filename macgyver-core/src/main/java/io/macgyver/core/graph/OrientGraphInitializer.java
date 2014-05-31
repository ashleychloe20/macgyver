package io.macgyver.core.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public abstract class OrientGraphInitializer extends GraphInitializer {
	Logger logger = LoggerFactory.getLogger(OrientGraphInitializer.class);
	OrientGraph orientGraph;

	public OrientGraphInitializer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public final void doInit(TransactionalGraph graph) {
		this.orientGraph = (OrientGraph) graph;
		doInitOrientGraph((OrientGraph) graph);
	}

	public abstract void doInitOrientGraph(OrientGraph g);

	@Override
	public void createVertexIndex(String pname) {
		try {
			orientGraph.createIndex(pname, Vertex.class);
		} catch (Exception e) {
			logger.info(e.toString());
		}

	}

	@Override
	public void createUniqueVertexIndex(String pname) {
		try {
			orientGraph.createKeyIndex(pname, Vertex.class, new Parameter(
					"type", "UNIQUE"));
		} catch (Exception e) {
			logger.info(e.toString());
		}
	}

}
