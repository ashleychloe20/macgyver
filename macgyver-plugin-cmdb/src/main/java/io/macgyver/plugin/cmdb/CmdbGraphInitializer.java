package io.macgyver.plugin.cmdb;

import io.macgyver.core.graph.TitanGraphInitailizer;

import com.thinkaurelius.titan.core.TitanGraph;

public class CmdbGraphInitializer extends TitanGraphInitailizer {

	public CmdbGraphInitializer() {

	}

	@Override
	public void doInitTitanGraphMetadata(TitanGraph g) {

	
		ensureVertexIndex("host", String.class);
		ensureVertexIndex("artifactId", String.class);
		ensureVertexIndex("groupId", String.class);

	
	}

	@Override
	public void doInitTitanGraphData(TitanGraph g) {
		// TODO Auto-generated method stub
		
	}

}
