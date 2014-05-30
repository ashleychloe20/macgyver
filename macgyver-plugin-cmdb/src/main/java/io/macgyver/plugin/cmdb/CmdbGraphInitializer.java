package io.macgyver.plugin.cmdb;

import io.macgyver.core.titan.GraphInitializer;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;

public class CmdbGraphInitializer extends GraphInitializer{

	public CmdbGraphInitializer() {
		
	}

	@Override
	public void doInit(TransactionalGraph g) {
		TitanGraph graph = (TitanGraph) g;
		try {
			
			graph.makeKey("artifactId").dataType(String.class)
					.indexed(Vertex.class).make();
		} catch (IllegalArgumentException e) {

		} finally {
			graph.commit();
		}
		try {

			graph.makeKey("groupId").dataType(String.class)
					.indexed(Vertex.class).make();
		} catch (IllegalArgumentException e) {

		} finally {
			graph.commit();
		}
		try {

			graph.makeKey("host").dataType(String.class)
					.indexed(Vertex.class).make();
		} catch (IllegalArgumentException e) {

		} finally {
			graph.commit();
		}
		
	}

}
