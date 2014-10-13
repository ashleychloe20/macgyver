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

import io.macgyver.xson.JsonPathPredicate;
import io.macgyver.xson.Xson;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Predicate;

public class PathPredicateImpl implements JsonPathPredicate<Object> {

	String path;

	public PathPredicateImpl(String jsonPath) {
		this.path = jsonPath;
	}

	@Override
	public boolean apply(Object input) {
		Object val = Xson.eval(input, path, null);
		if (val == null) {
			return false;
		}
		if (val instanceof JsonNode) {
			if (((JsonNode) val).isArray()) {
				return ((JsonNode) val).size() > 0;
			} else {
				return true;
			}
		}
		return false;

	}

}
