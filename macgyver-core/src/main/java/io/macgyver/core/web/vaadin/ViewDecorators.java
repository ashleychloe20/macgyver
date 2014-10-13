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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.base.Preconditions;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.navigator.View;

public class ViewDecorators {

	static org.slf4j.Logger logger = LoggerFactory
			.getLogger(ViewDecorators.class);

	// vaadin isn't springified, so this makes it easy to use
	static ViewDecorators instance;

	@Autowired
	ApplicationContext applicationContext;

	@PostConstruct
	public void postConstruct() {
		instance = this;
	}

	public static class DecoratorComparator implements
			Comparator<ViewDecorator> {

		@Override
		public int compare(ViewDecorator o1, ViewDecorator o2) {
			if (o1.getOrder() > o2.getOrder()) {
				return 1;
			} else if (o1.getOrder() < o2.getOrder()) {
				return -1;
			}
			return 0;
		}

	}

	List<ViewDecorator> getViewDecorators(View v) {

		List<ViewDecorator> decorators = Lists.newArrayList();

		for (ViewDecorator candidate : applicationContext.getBeansOfType(
				ViewDecorator.class).values()) {
			if (candidate.supports(v)) {
				decorators.add(candidate);
			}
		}
		Collections.sort(decorators, new DecoratorComparator());
		return Collections.unmodifiableList(decorators);

	}

	/**
	 * Applies all spring-registered ViewDecorator instances that support decorating the given view.
	 * @param v
	 */
	public static void decorate(View v) {
		Preconditions.checkNotNull(v);
		Preconditions.checkNotNull(instance);

		List<ViewDecorator> list = instance.getViewDecorators(v);

		for (ViewDecorator d : list) {

			d.decorate(v);

		}
	}
}
