package io.macgyver.plugin.cmdb;

import io.macgyver.core.util.HashUtils;

import java.util.Collections;
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
	public static final String DEFAULT_INSTANCE_INDEX = "";

	String artifactId;
	String groupId;
	String host;
	String profile;
	String environment;
	String scmRevision;
	String scmBranch;
	String version;
	String instanceIndex = DEFAULT_INSTANCE_INDEX;

	public AppInstance() {
		super();
	}

	public AppInstance(String host, String groupId, String artifactId) {
		this(host, groupId, artifactId, DEFAULT_INSTANCE_INDEX);
	}

	public AppInstance(String host, String groupId, String artifactId,
			String index) {
		this.setHost(host);
		this.setGroupId(groupId);
		this.setArtifactId(artifactId);
		this.instanceIndex = index;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getScmRevision() {
		return scmRevision;
	}

	public void setScmRevision(String scmRevision) {
		this.scmRevision = scmRevision;
	}

	public String getScmBranch() {
		return scmBranch;
	}

	public void setScmBranch(String scmBranch) {
		this.scmBranch = scmBranch;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String computeVertexId() {
		return calculateVertexId(getHost(), getGroupId(), getArtifactId(), null);
	}

	public static String calculateVertexId(String host, String group,
			String app, String q) {

		List<String> list = Lists.newArrayList("default", "AppInstance", host,
				group, app, q);

		return HashUtils.calculateCompositeId(list.toArray(new String[0]));

	}

	public String toString() {
		return Objects.toStringHelper(this).add("host", getHost())
				.add(KEY_GROUP_ID, getGroupId())
				.add(KEY_APP_ID, getArtifactId()).toString();
	}

}
