package io.macgyver.core.graph;

import com.tinkerpop.blueprints.TransactionalGraph;


public class CoreIndexInitializer extends GraphInitializer {

	@Override
	public void doInitGraphMetadata(TransactionalGraph g) {
		
		ensureUniqueVertexIndex("vertexId", String.class);
		ensureVertexIndex("vertexType", String.class);
		ensureVertexIndex("timestamp",Long.class);
		ensureVertexIndex("name",String.class);
		ensureUniqueVertexIndex("macUsername", String.class);
	}

	@Override
	public void doInitGraphData(TransactionalGraph g) {
		// do nothing
		
	}
	

}
