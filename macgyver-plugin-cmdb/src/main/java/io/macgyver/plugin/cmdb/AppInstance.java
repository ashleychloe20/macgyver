package io.macgyver.plugin.cmdb;

import io.macgyver.core.util.HashUtils;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppInstance {

	public static final String APP_INSTANCE_TYPE = "AppInstance";
	public static final String KEY_APP_ID = "artifactId";
	public static final String KEY_GROUP_ID = "groupId";
	public static final String KEY_HOST = "host";
	public static final String KEY_VERTEX_TYPE = "vertexType";
	Map<String, Object> props = Maps.newConcurrentMap();

	public AppInstance() {

	}
	public AppInstance(String host, String groupId, String appId) {
		this(host,groupId,appId,null);
	}
	public AppInstance(String host, String groupId, String appId, String qualifier) {
		this();
		Preconditions.checkNotNull(host);
		Preconditions.checkNotNull(groupId);
		Preconditions.checkNotNull(appId);
		setHost(host);
		setGroupId(groupId);
		setAppId(appId);
	}

	public String getHost() {
		return getStringProperty(KEY_HOST, null);
	}

	public void setHost(String host) {
		Preconditions.checkNotNull(host);
		props.put(KEY_HOST, host);
	}

	public void setGroupId(String groupId) {
		Preconditions.checkNotNull(groupId);
		props.put(KEY_GROUP_ID, groupId);
	}

	public String getGroupId() {
		return getStringProperty(KEY_GROUP_ID, null);
	}

	public String getAppId() {
		
		return getStringProperty(KEY_APP_ID, null);
	}

	public void setAppId(String appId) {
		Preconditions.checkNotNull(appId);
		props.put(KEY_APP_ID, appId);
	}

	public String computeVertexId() {
		return calculateVertexId(getHost(), getGroupId(), getAppId(), null);
	}

	public static String calculateVertexId(String host, String group,
			String app, String q) {

		List<String> list = Lists.newArrayList("default", "AppInstance", host,
				group, app, q);

		return HashUtils.calculateCompositeId(list.toArray(new String[0]));

	}

	public Map<String, Object> getProperties() {
		return props;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("host", getHost())
				.add(KEY_GROUP_ID, getGroupId()).add(KEY_APP_ID, getAppId())
				.toString();
	}

	public String getStringProperty(String key, String defaultVal) {
		Object val = getProperties().get(key);
		if (val == null) {
			return defaultVal;
		}
		return val.toString();
	}

}
