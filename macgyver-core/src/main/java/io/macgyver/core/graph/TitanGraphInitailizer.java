package io.macgyver.core.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;

public abstract class TitanGraphInitailizer extends GraphInitializer {
	Logger logger = LoggerFactory.getLogger(TitanGraphInitailizer.class);

	@Override
	public final void doInitGraphMetadata(TransactionalGraph graph) {
		graph.rollback();
		doInitTitanGraphMetadata((TitanGraph) graph);
		graph.commit();
	}

	@Override
	public final void doInitGraphData(TransactionalGraph graph) {
		graph.rollback();
		doInitTitanGraphData((TitanGraph) graph);
		graph.commit();
	}

	public abstract void doInitTitanGraphMetadata(TitanGraph g);

	public abstract void doInitTitanGraphData(TitanGraph g);

	public TitanGraph getTitanGraph() {
		return (TitanGraph) graph;
	}

	public boolean isIndexed(String key) {
		for (String k : getTitanGraph().getIndexedKeys(Vertex.class)) {
			if (key.equals(k)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void ensureVertexIndex(String pname, Class<? extends Object> type) {

		if (isIndexed(pname)) {
			logger.info("key already indexed: {}", pname);
			return;
		}
		getTitanGraph().makeKey(pname).dataType(type).indexed(Vertex.class)
				.make();
		getTitanGraph().commit();

	}

	@Override
	public void ensureUniqueVertexIndex(String pname,
			Class<? extends Object> type) {
		if (isIndexed(pname)) {
			logger.info("key already indexed: {}", pname);
			return;
		}
		getTitanGraph().makeKey(pname).dataType(type).unique()
				.indexed(Vertex.class).make();
		getTitanGraph().commit();
	}

}
