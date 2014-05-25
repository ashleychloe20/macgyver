package io.macgyver.core.neo4j;

import javax.annotation.PostConstruct;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Neo4jInitializer {

	@Autowired
	GraphDatabaseService graphDb;
	
	@PostConstruct
	public void init() {
		doInit(graphDb);
	}
	
	public abstract void doInit(GraphDatabaseService graphDb);
}
