package io.macgyver.plugin.cmdb;

import io.macgyver.core.MacGyverException;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.junit.Ignore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.BaseEncoding;


@JsonIgnoreProperties(ignoreUnknown=true)
public class AppInstance {

	public static final String VERTEX_TYPE_PROP="vertexType";
	public static final String VERTEX_TYPE="AppInstance";
	private String host;
	private String appId;
	
	
	Map<String, Object> props = Maps.newConcurrentMap();

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host.toLowerCase();
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getVertexId() {
		return calculateVertexId(getHost(), getAppId(), null);
	}

	public static String calculateVertexId(String host, String appId) {
		return calculateVertexId(host, appId, null);
	}

	public static String calculateVertexId(String host, String appId, String id) {
		Preconditions.checkNotNull(host);
		Preconditions.checkNotNull(appId);
		id = Strings.nullToEmpty(id);
		try {
			MessageDigest md = MessageDigest.getInstance("sha1");
			md.update("AppInstance:".getBytes("UTF-8"));
			md.update(host.toLowerCase().trim().getBytes("UTF-8"));
			md.update(":".getBytes("UTF-8"));
			md.update(appId.toLowerCase().getBytes("UTF8"));
			md.update((":" + id).getBytes());

			return BaseEncoding.base16().lowerCase().encode(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new MacGyverException(e);
		} catch (UnsupportedEncodingException e) {
			throw new MacGyverException(e);
		}
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
