package io.macgyver.plugin.cloud;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.gwt.thirdparty.guava.common.collect.Lists;

public class ComputeScannerManager {

	Logger logger = LoggerFactory.getLogger(ComputeScannerManager.class);

	Map<String, ComputeScanner> computeScannerMap = Maps.newConcurrentMap();
	AtomicReference<Thread> latch = new AtomicReference<Thread>();

	public void register(ComputeScanner scanner) {
		logger.info("registering scanner: {}", scanner);
		computeScannerMap.put(scanner.getServiceName(), scanner);
	}

	public void scan() {
		try {
			boolean success = latch.compareAndSet(null, Thread.currentThread());
			if (!success) {
				logger.warn("only one thread allowed to execute");
			}
			List<ComputeScanner> copy = Lists.newArrayList(computeScannerMap
					.values());
			for (ComputeScanner scanner : copy) {
				try {
					logger.info("scanning: {}", scanner);
					scanner.scan();
				} catch (RuntimeException e) {
					logger.warn("", e);
				}
			}
		} finally {
			latch.set(null);
		}

	}
}
