package io.macgyver.core.graph;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.neo4j.index.impl.lucene.LowerCaseKeywordAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tinkerpop.blueprints.IndexableGraph;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j2.Neo4j2Graph;

public abstract class GraphInitializer {

	Logger logger = LoggerFactory.getLogger(GraphInitializer.class);

	@Autowired
	TransactionalGraph graph;

	public void ensureVertexIndex(String pname, Class<? extends Object> type) {
		KeyIndexableGraph ig = (KeyIndexableGraph) graph;
		
		if (ig.getIndexedKeys(Vertex.class).contains(pname)) {
			logger.info("vertex key already indexed: "+pname);
		}
		else {
			ig.createKeyIndex(pname, Vertex.class);
		}
	
	}

	public void ensureUniqueVertexIndex(String pname,
			Class<? extends Object> type) {
		logger.warn("unique indexes not supported on neo4j");
	}

	public abstract void doInitGraphData(TransactionalGraph g);

	public abstract void doInitGraphMetadata(TransactionalGraph g);

	public final void initGraphMetadata(TransactionalGraph g) {
		g.rollback();
		doInitGraphMetadata(g);
		g.commit();
	}

	public final void initGraphData(TransactionalGraph g) {
		g.rollback();
		doInitGraphData(g);
		g.commit();
	}
}
