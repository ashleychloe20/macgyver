package io.macgyver.plugin.cmdb;

import com.thinkaurelius.titan.core.TitanGraph;

import io.macgyver.core.graph.TitanGraphInitailizer;

public class CmdbGraphInitializer extends TitanGraphInitailizer {

	public CmdbGraphInitializer() {

	}

	@Override
	public void doInitTitanGraph(TitanGraph g) {

		g.commit();

		ensureVertexIndex("host", String.class);
		ensureVertexIndex("artifactId", String.class);
		ensureVertexIndex("groupId", String.class);

		g.commit();
	}

}
