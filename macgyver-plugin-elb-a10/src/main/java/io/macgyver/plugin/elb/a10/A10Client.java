package io.macgyver.plugin.elb.a10;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;

public interface A10Client {

	public abstract ObjectNode getServiceGroupAll();

	public abstract ObjectNode getSystemPerformance();

	public abstract ObjectNode getSystemInfo();

	public abstract ObjectNode getDeviceInfo();

	public abstract ObjectNode invoke(String method, Map<String, String> params);

	public abstract ObjectNode invoke(String method, String... args);

}
