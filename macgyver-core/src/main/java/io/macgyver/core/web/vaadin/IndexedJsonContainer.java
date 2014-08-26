package io.macgyver.core.web.vaadin;

import io.macgyver.core.MacGyverException;
import io.macgyver.xson.Xson;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.vaadin.data.util.IndexedContainer;

/**
 * IndexedJsonContainer makes it easy to add Json structures to Vaadin.
 * 
 * @author Rob Schoening
 *
 */
public class IndexedJsonContainer extends IndexedContainer {

	protected static ObjectMapper mapper = new ObjectMapper();

	Logger logger = LoggerFactory.getLogger(IndexedJsonContainer.class);

	protected Map<String, String> xsonMap = Maps.newConcurrentMap();

	/**
	 * Maps a Json object into this container.
	 * 
	 * @param node
	 */
	@SuppressWarnings("unchecked")
	public void addJsonObject(ObjectNode node) {
		Preconditions.checkNotNull(node);
		Object itemId = addItem();
		for (Object pid : getContainerPropertyIds()) {
			try {
				String val = extractPropertyValue(node, pid.toString());

				getItem(itemId).getItemProperty(pid).setValue(val);
			} catch (RuntimeException e) {
				logger.warn("problem mapping json to container item: {}", pid);
			}

		}
	}

	/**
	 * Adds each element in the given array to this container.
	 * 
	 * @param arr
	 *            array of json objects to add
	 */
	public void addJsonArray(ArrayNode arr) {
		for (int i = 0; i < arr.size(); i++) {
			JsonNode node = arr.get(i);
			if (node instanceof ObjectNode) {
				addJsonObject((ObjectNode) arr.get(i));
			}
		}
	}

	public void addJsonPathPropertyExtractor(String propertyId, String jsonPath) {
		Preconditions.checkNotNull(propertyId);
		Preconditions.checkNotNull(jsonPath);
		xsonMap.put(propertyId, jsonPath);
	}
	
	
	public String extractPropertyValue(ObjectNode data, String propertyId) {
		Preconditions.checkNotNull(data);
		Preconditions.checkNotNull(propertyId);

		String xson = xsonMap.get(propertyId);
		if (xson == null) {
			return data.path(propertyId).asText();
		}
		else {
			return Xson.eval(data, xson);
		}
	}

}
