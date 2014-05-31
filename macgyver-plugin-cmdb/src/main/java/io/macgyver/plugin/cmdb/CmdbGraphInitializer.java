package io.macgyver.plugin.cmdb;

import io.macgyver.core.graph.OrientGraphInitializer;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class CmdbGraphInitializer extends OrientGraphInitializer {

	public CmdbGraphInitializer() {

	}

	@Override
	public void doInitOrientGraph(OrientGraph g) {
		
		g.commit();
		createUniqueVertexIndex("vertexId");
		createVertexIndex("host");
		createVertexIndex("artifactId");
		createVertexIndex("groupId");
		

	}

}
