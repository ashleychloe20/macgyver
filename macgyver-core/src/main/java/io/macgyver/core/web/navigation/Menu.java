package io.macgyver.core.web.navigation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.parboiled.common.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

public class Menu {
	Logger logger = LoggerFactory.getLogger(Menu.class);

	private ObjectMapper objectMapper = new ObjectMapper();
	private ObjectNode root;
	private List<MenuDecorator> postDecoratorList = Lists.newArrayList();

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
		for (int i = 0; i < an.size(); i++) {
			String id = an.get(i).path("id").asText();
			if (id.equals(top)) {
				topNode = (ObjectNode) an.get(i);
			}
		}

		if (topNode == null) {
			ObjectNode n = objectMapper.createObjectNode();
			n.put("id", top);
			n.put("items", objectMapper.createArrayNode());
			n.put("display", top);

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
		if (foundNode == null) {
			foundNode = objectMapper.createObjectNode();
			foundNode.put("url", url);
			foundNode.put("display", display);

			an.add(foundNode);
		} else {
			foundNode.put("display", display);
		}
	}

	/**
	 * During decoration, a decorator can register another decorator to be run *after* the first round of decorators has executed.
	 * Among other things, this allows menus to be sorted after all menu items are created.
	 * @param d
	 */
	public void addPostDecorator(MenuDecorator d) {
		Preconditions.checkNotNull(d);
		postDecoratorList.add(d);
	}

	public void invokePostDecorators() {
		for (MenuDecorator d : postDecoratorList) {
			try {
				d.decorate(this);
			} catch (RuntimeException e) {
				logger.warn("problem invoking post menu decorator", e);
			}
		}
	}
}
