package io.macgyver.core.graph;

import java.util.Map;

import io.macgyver.core.Kernel.KernelStartedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.tinkerpop.blueprints.TransactionalGraph;

public class GraphManager {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	TransactionalGraph graph;

	@Autowired
	ApplicationContext applicationContext;

	public GraphManager() {

	}

	@Subscribe
	public final void init(KernelStartedEvent evt) {

		logger.info("graph init starting: {}", graph);

		for (Map.Entry<String, GraphInitializer> entry : applicationContext
				.getBeansOfType(GraphInitializer.class).entrySet()) {
			logger.info("initGraphMetadata {}: {}", entry.getKey(),
					entry.getValue());
			try {
				entry.getValue().initGraphMetadata(graph);
			} catch (RuntimeException e) {
				logger.warn("problem initializing graph metadata", e);
			}
		}

		for (Map.Entry<String, GraphInitializer> entry : applicationContext
				.getBeansOfType(GraphInitializer.class).entrySet()) {
			logger.info("initGraphData {}: {}", entry.getKey(),
					entry.getValue());
			try {
				entry.getValue().initGraphData(graph);
			} catch (RuntimeException e) {
				logger.warn("problem initializing graph data", e);
			}
		}

		logger.info("graph init complete: ", graph);
	}
}
