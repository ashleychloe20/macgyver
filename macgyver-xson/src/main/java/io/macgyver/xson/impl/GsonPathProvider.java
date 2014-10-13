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
package io.macgyver.xson.impl;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class GsonPathProvider implements JsonPathProvider {

	Gson gson = new Gson();

	@Override
	public <T> T path(Object element, String path) {

		return path(element, path, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T path(Object element, String path, Object defaultVal) {

		try {
		
			Object val = JsonPath.read(element.toString(), path);

			if (val == null) {
				return (T) defaultVal;
			}

			if (val instanceof Number) {
				return (T) val;
			} else if (val instanceof String) {
				return (T) val;
			} else if (val instanceof Boolean) {
				return (T) val;
			} else if (val instanceof JSONObject) {
				return (T) gson.fromJson(val.toString(), JsonObject.class);
			} else if (val.getClass().equals(JSONArray.class)) {
				
				return (T) gson.fromJson(val.toString(), JsonArray.class);
			}
			throw new IllegalArgumentException("unsupported type: "
					+ val.getClass());
		} catch (PathNotFoundException e) {
			return (T) defaultVal;
		}
		catch (RuntimeException e) {
			return (T) defaultVal;
		}
	}
	public boolean supports(Object x) {
		return false;
	}
}
