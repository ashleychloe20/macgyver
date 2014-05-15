package io.macgyver.core.web.navigation;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonMenuItem implements MenuItem {
	JsonObject myNode;
	
	
	public JsonMenuItem(JsonObject obj) {
		Preconditions.checkNotNull(obj);
		this.myNode = obj;
	}
	@Override
	public List<MenuItem> getChildMenuItems() {
		JsonElement items = myNode.get("items");
		if (items==null) {
			return Lists.newArrayList();
		}
		JsonArray array =items.getAsJsonArray();
		List<MenuItem> list = Lists.newArrayList();
		for (int i=0; i<array.size(); i++) {
			JsonObject child =  array.get(i).getAsJsonObject();
			list.add(new JsonMenuItem(child));
		}
		return list;
	}

	@Override
	public String getUriPath() {
		JsonElement el = myNode.get("uriPath");
		if (el==null) {
			return null;
		}
		else {
			return el.getAsString();
		}
		
	}

	@Override
	public String getDisplayName() {
		JsonElement el = myNode.get("displayName");
		if (el==null) {
			return null;
		}
		else {
			return el.getAsString();
		}
		
	}
	public String getId() {
		return myNode.get("id").getAsString();
	}
	@Override
	public MenuItem getChildById(String id, boolean create) {
		JsonElement itemArr = myNode.get("items");
		JsonArray items =  null;
		if (itemArr==null) {
			if (!create) {
				return null;
			}
			else {
				items = new JsonArray();
				myNode.add("items", items);
			}
		}
		else {
			items = itemArr.getAsJsonArray();
		}
		
		for (int i=0; i<items.size(); i++) {
			JsonObject obj = items.get(i).getAsJsonObject();
			String childId = obj.get("id").getAsString();
			if (id.equals(childId)) {
				return new JsonMenuItem(obj);
			}
		}
		JsonObject newItem = new JsonObject();
		newItem.addProperty("id", id);
		newItem.addProperty("displayName",id);
		newItem.add("items",new JsonArray());
		items.add(newItem);
		return new JsonMenuItem(newItem);
	}

	public void setDisplayName(String name) {
		myNode.addProperty("displayName", name);
	}
	@Override
	public JsonObject getJsonObject() {
		return myNode;
	}
	public void setUriPath(String path) {
		myNode.addProperty("uriPath",path);
	}
}
