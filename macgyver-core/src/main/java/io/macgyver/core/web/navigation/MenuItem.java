package io.macgyver.core.web.navigation;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;

public interface MenuItem {
	List<MenuItem> getChildMenuItems();
	String getUriPath();
	String getDisplayName();
	void setDisplayName(String name);
	void setUriPath(String path);
	String getId();
	MenuItem getChildById(String id, boolean create);
	ObjectNode getObjectNode();
}
