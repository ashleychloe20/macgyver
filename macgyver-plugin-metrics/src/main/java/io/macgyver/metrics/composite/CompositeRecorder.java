package io.macgyver.metrics.composite;

import io.macgyver.metrics.MetricRecorder;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

public class CompositeRecorder implements MetricRecorder {

	Logger logger = LoggerFactory.getLogger(CompositeRecorder.class);
	
	Set<MetricRecorder> recorderList = Sets.newConcurrentHashSet();

	@Override
	public void record(String name, Number value) {
		for (MetricRecorder r: recorderList) {
			try {
				r.record(name, value);
			}
			catch (Exception e) {
				logger.warn("problem recording metric: "+name,e);
			}
		}
		
	}
	
	public void addRecorder(MetricRecorder r) {
		Preconditions.checkNotNull(r);
		if (recorderList.contains(r)) {
			logger.info("composite recorder already contains {}",r);
		}
		else {
			logger.info("adding {} to {}",r,this);
			recorderList.add(r);
		}
	}
}
