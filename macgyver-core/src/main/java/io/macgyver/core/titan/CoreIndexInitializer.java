package io.macgyver.core.titan;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;

public class CoreIndexInitializer extends AbstractTitanInitializer{

	@Override
	public void doInit(TitanGraph graph) {
		try {
					graph.makeKey("vertexType").dataType(String.class).indexed(Vertex.class).make();
					graph.makeKey("vertexId").dataType(String.class).indexed(Vertex.class).unique().make();	
		}
		finally {
			graph.commit();
		}
		
	}

}
