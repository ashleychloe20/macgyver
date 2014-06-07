package io.macgyver.core.graph;

import com.thinkaurelius.titan.core.TitanGraph;

public class CoreIndexInitializer extends TitanGraphInitailizer {

	@Override
	public void doInitTitanGraph(TitanGraph g) {
		
		ensureUniqueVertexIndex("vertexId", String.class);
		ensureVertexIndex("vertexType", String.class);
		ensureUniqueVertexIndex("macUsername", String.class);
	}
	

}
