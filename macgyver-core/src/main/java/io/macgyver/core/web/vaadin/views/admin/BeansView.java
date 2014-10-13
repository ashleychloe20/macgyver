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
package io.macgyver.core.web.vaadin.views.admin;

import org.springframework.context.ApplicationContext;

import io.macgyver.core.Kernel;
import io.macgyver.core.web.vaadin.ViewConfig;
import io.macgyver.core.web.vaadin.ViewDecorators;
import io.macgyver.core.web.vaadin.ViewMetadata;

import com.google.common.collect.Lists;
import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@ViewConfig(viewName="admin/beans",menuPath={"Admin","Beans"})
public class BeansView extends VerticalLayout implements View {

	public static final String VIEW_NAME="admin/beans";
	Table table;

	public BeansView() {
		setMargin(true);
		setSizeFull();
		//setWidth(800, Unit.PIXELS);
		table = new Table("Spring Beans");

		table.addStyleName("compact");
		table.addStyleName("small");
		// Define two columns for the built-in container
		table.addContainerProperty("name", String.class, null);
		table.addContainerProperty("class", String.class, null);
		table.setWidth(1200, Unit.PIXELS);
		table.setHeight(600,Unit.PIXELS);
		
		table.setPageLength(table.size());

		table.setColumnWidth("name", 600);
		table.setColumnWidth("class", 600);
		addComponent(table);

		table.setColumnHeader("name", "Name");
		table.setColumnHeader("class", "Class");
		
		setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		ViewDecorators.decorate(this);
	}

	public Table getTable() {
		return table;
	}
	@Override
	public void enter(ViewChangeEvent event) {
		refresh();
	}

	public void refresh() {
		table.removeAllItems();

		ApplicationContext ctx = Kernel.getInstance().getApplicationContext();
		for (String name : ctx.getBeanDefinitionNames()) {

			Object newItemId = table.addItem();
			Item row1 = table.getItem(newItemId);
			row1.getItemProperty("name").setValue(name);
			try {
				row1.getItemProperty("class").setValue(
						ctx.getBean(name).getClass().toString());
			} catch (Exception e) {
			}

		}
		table.sort(new Object[]{"name"}, new boolean[]{true});
	}

}
