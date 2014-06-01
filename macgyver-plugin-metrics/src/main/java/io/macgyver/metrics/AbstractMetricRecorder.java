package io.macgyver.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public abstract class AbstractMetricRecorder implements MetricRecorder {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private String prefix=null;
	
	public final String getPrefix() {
		return prefix;
	}

	public final void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String qualify(String unqualified) {
		if (Strings.isNullOrEmpty(prefix)) {
			return unqualified;
		}
		else {
			return prefix+"."+unqualified;
		}
	}
	
	public abstract void doRecord(String name, Number value);
	
	@Override
	public final void record(String name, Number value) {
		name = qualify(name);
		
		if (logger.isDebugEnabled()) {
			logger.debug("recording {}={}",name,value);
		}
		doRecord(name,value);
	}

}
