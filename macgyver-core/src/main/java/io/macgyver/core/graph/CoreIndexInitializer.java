package io.macgyver.core.graph;

import com.thinkaurelius.titan.core.TitanGraph;

public class CoreIndexInitializer extends TitanGraphInitailizer {

	@Override
	public void doInitTitanGraphMetadata(TitanGraph g) {
		
		ensureUniqueVertexIndex("vertexId", String.class);
		ensureVertexIndex("vertexType", String.class);
		ensureUniqueVertexIndex("macUsername", String.class);
	}

	@Override
	public void doInitTitanGraphData(TitanGraph g) {
		// do nothing
		
	}
	

}
