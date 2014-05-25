package io.macgyver.core.titan;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.thinkaurelius.titan.core.TitanGraph;

public abstract class TitanInitializer {

	@Autowired
	TitanGraph graph;
	
	@PostConstruct
	public void init() {
		doInit(graph);
	}
	
	public abstract void doInit(TitanGraph graph);
}
