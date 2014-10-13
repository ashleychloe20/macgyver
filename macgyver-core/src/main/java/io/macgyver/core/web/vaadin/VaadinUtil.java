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

import com.vaadin.data.Container;

public class VaadinUtil {

	public static boolean isEmpty(Container t) {
		if (t == null) {
			return true;
		}

		try {
			return t.getItemIds().isEmpty();
		} catch (Exception e) {

		}
		return true;
	}
}
