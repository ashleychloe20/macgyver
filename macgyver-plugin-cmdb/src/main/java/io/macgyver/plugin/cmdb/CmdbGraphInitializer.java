package io.macgyver.plugin.cmdb;

import io.macgyver.core.graph.GraphInitializer;

import com.tinkerpop.blueprints.TransactionalGraph;

public class CmdbGraphInitializer extends GraphInitializer {

	public CmdbGraphInitializer() {

	}

	@Override
	public void doInitGraphMetadata(TransactionalGraph g) {

	
		ensureVertexIndex("host", String.class);
		ensureVertexIndex("artifactId", String.class);
		ensureVertexIndex("groupId", String.class);

	
	}

	@Override
	public void doInitGraphData(TransactionalGraph g) {
		// TODO Auto-generated method stub
		
	}

}
