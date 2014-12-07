/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.core.web.vaadin;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.functions.Action1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.vaadin.data.Property;
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

	protected Map<Object, JsonNode> rawDataMap = Maps.newConcurrentMap();

	public final AddItemAction ADD_ITEM_ACTION = new AddItemAction();

	/**
	 * Maps a Json object into this container.
	 * 
	 * @param node
	 */
	@SuppressWarnings("unchecked")
	public void addJsonObject(JsonNode node) {
		Preconditions.checkNotNull(node);
		Object itemId = addItem();
		rawDataMap.put(itemId, node);
		for (Object pid : getContainerPropertyIds()) {
			try {
				String val = extractPropertyValue(node, pid.toString());
				Property p = getItem(itemId).getItemProperty(pid);
				if (p != null && p.getType().equals(String.class)) {
					getItem(itemId).getItemProperty(pid).setValue(val);
				}
			} catch (RuntimeException e) {
				logger.warn("problem mapping json to container item: " + pid, e);

			}

		}
	}

	public JsonNode getJsonObject(Object itemId) {
		return rawDataMap.get(itemId);
	}

	@Override
	protected void internalRemoveAllItems() {
		super.internalRemoveAllItems();
		rawDataMap.clear();
	}

	@Override
	protected boolean internalRemoveItem(Object itemId) {
		if (itemId != null) {
			rawDataMap.remove(itemId);
		}
		return super.internalRemoveItem(itemId);
	}

	@Override
	public boolean removeAllItems() {
		rawDataMap.clear();
		return super.removeAllItems();
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

	public void addJsonObjects(Iterable<JsonNode> list) {
		for (JsonNode n : list) {
			addJsonObject(n);
		}
	}

	public String extractPropertyValue(JsonNode data, String propertyId) {
		Preconditions.checkNotNull(data);
		Preconditions.checkNotNull(propertyId);

		return data.path(propertyId).asText();

	}

	/**
	 * We are not implementing Obervable pattern in the classic sense. This is
	 * really just a callback for adding Observable<JsonNode> into the
	 * contianer.
	 * 
	 * @author rschoening
	 *
	 */
	public class AddItemAction implements Action1<JsonNode> {

		@Override
		public void call(JsonNode t1) {
			if (t1 != null) {
				addJsonObject(t1);
			}
		}
	}

}
