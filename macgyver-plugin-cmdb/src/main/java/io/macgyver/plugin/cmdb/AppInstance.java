package io.macgyver.plugin.cmdb;

import io.macgyver.core.util.HashUtils;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppInstance {

	public static final String VERTEX_TYPE_PROP = "vertexType";
	public static final String VERTEX_TYPE = "AppInstance";

	Map<String, Object> props = Maps.newConcurrentMap();

	public AppInstance() {
		
	}
	public AppInstance(String host, String appId) {
		this();
		setHost(host);
		setAppId(appId);
	}
	public String getHost() {
		return getStringProperty("host", null);
	}

	public void setHost(String host) {
		props.put("host", host);
	}

	public String getAppId() {
		return getStringProperty("appId", null);
	}

	public void setAppId(String appId) {
		props.put("appId", appId);
	}

	public String getVertexId() {
		return calculateVertexId(getHost(), getAppId(), null);
	}

	public static String calculateVertexId(String host, String app, String q) {

		List<String> list = Lists.newArrayList("default", "AppInstance", host,
				app, q);

		return HashUtils.calculateCompositeId(list.toArray(new String[0]));

	}

	public Map<String, Object> getProperties() {
		return props;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("host", getHost())
				.add("appId", getAppId()).toString();
	}

	public String getStringProperty(String key, String defaultVal) {
		Object val = getProperties().get(key);
		if (val == null) {
			return defaultVal;
		}
		return val.toString();
	}

}
