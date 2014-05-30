package io.macgyver.core.titan;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;

public class CoreIndexInitializer extends GraphInitializer {

	@Override
	public void doInit(TransactionalGraph g) {
		TitanGraph graph = (TitanGraph) g;
		try {
			graph.makeKey("vertexType").dataType(String.class)
					.indexed(Vertex.class).make();

		} catch (IllegalArgumentException e) {

		} finally {
			graph.commit();
		}
		try {

			graph.makeKey("vertexId").dataType(String.class)
					.indexed(Vertex.class).unique().make();
		} catch (IllegalArgumentException e) {

		} finally {
			graph.commit();
		}
	}

}
