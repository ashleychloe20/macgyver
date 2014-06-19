package io.macgyver.core.web.navigation;

import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Menu {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	private ObjectNode root;
	
	public Menu() {
		root = objectMapper.createObjectNode();
		root.put("id", "root");
		
		ArrayNode items = objectMapper.createArrayNode();
		root.put("items", items);
	}
	public ObjectMapper mapper() {
		return objectMapper;
	}
	
	
	public ObjectNode getRootObjectNode() {
		return root;
	}

	public ObjectNode getTopObjectNode(String top) {
		ObjectNode topNode = null;
		ArrayNode an = (ArrayNode) root.get("items");
		for (int i=0; i<an.size(); i++) {
			String id = an.get(i).path("id").asText();
			if (id.equals(top)) { 
				topNode = (ObjectNode) an.get(i);
			}
		}
		
		if (topNode==null) {
			ObjectNode n = objectMapper.createObjectNode();
			n.put("id", top);
			n.put("items", objectMapper.createArrayNode());
			n.put("display",top);
		
			topNode = n;
			an.add(topNode);
		}	
		
		return topNode;
	}
	
	public void setDisplayName(String key, String display) {
		ObjectNode n = getTopObjectNode(key);
		n.put("display", display);
	}
	public void addMenuItem(String top, String url, String display) {
		ObjectNode topNode = getTopObjectNode(top);
		ObjectNode foundNode = null;
		ArrayNode an = (ArrayNode) topNode.get("items");
		Iterator<JsonNode> t = an.iterator();
		while (t.hasNext()) {
			ObjectNode n = (ObjectNode) t.next();
			String existingUrl = n.path("url").asText();
			if (existingUrl.equals(url)) {
				foundNode = n;
			}
		}
		if (foundNode==null) {
			foundNode = objectMapper.createObjectNode();
			foundNode.put("url", url);
			foundNode.put("display", display);
		
			an.add(foundNode);
		}
		else {
			foundNode.put("display", display);
		}
	}
}
