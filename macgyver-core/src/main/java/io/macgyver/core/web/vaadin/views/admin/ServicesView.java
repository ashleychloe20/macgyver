package io.macgyver.core.web.vaadin.views.admin;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

import io.macgyver.core.Kernel;
import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.core.service.ServiceFactory;
import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.core.web.vaadin.IndexedJsonContainer;
import io.macgyver.core.web.vaadin.MacGyverView;
import io.macgyver.core.web.vaadin.ViewConfig;

@ViewConfig(viewName = ServicesView.VIEW_NAME, menuPath = { "Admin", "Services" })
public class ServicesView extends MacGyverView {

	Logger logger = LoggerFactory.getLogger(ServicesView.class);
	public static final String VIEW_NAME = "admin/services";

	Table table;
	IndexedJsonContainer container;

	ServiceRegistry serviceRegistry;

	Table detailTable;
	IndexedJsonContainer detailContainer;

	NativeSelect newServiceSelect;

	NativeSelect createSelect() {
		NativeSelect s = new NativeSelect();
		List<String> temp = Lists.newArrayList();
		for (ServiceFactory sf : getServiceFactories()) {
			temp.add(sf.getServiceType());

		}
		Collections.sort(temp);
		for (String t : temp) {
			s.addItem(t);
		}
		return s;
	}

	public ServicesView() {
		super();
		logger.info("serviceFactories: {}", getServiceFactories());
		setMargin(true);
		setSpacing(true);
		serviceRegistry = Kernel.getApplicationContext().getBean(
				ServiceRegistry.class);

		/*
		 * Feature WIP
			newServiceSelect = createSelect();
			addComponent(newServiceSelect);
		*/

		container = new IndexedJsonContainer();
		container.addContainerProperty("serviceName", String.class, "");
		container.addContainerProperty("serviceType", String.class, "");
		table = new Table("Services");

		table.addStyleName("compact");
		table.addStyleName("small");
		table.setColumnHeader("serviceName", "Service Name");
		table.setColumnHeader("serviceType", "Service Type");
		table.setContainerDataSource(container);

		table.setNullSelectionAllowed(false);
		table.setHeight("300px");
		table.addItemClickListener(new ClickListener());
		table.setSizeFull();
		table.setWidth("800px");
		addComponent(table);

		detailContainer = new IndexedJsonContainer();
		detailContainer.addContainerProperty("propertyName", String.class, "");
		detailContainer.addContainerProperty("propertyValue", String.class, "");
		detailTable = new Table();
		detailTable.setContainerDataSource(detailContainer);
		detailTable.addStyleName("compact");
		detailTable.addStyleName("small");
		detailTable.setWidth("800px");
		detailTable.setColumnHeader("propertyName", "Property Name");
		detailTable.setColumnHeader("propertyValue", "Property Value");

		addComponent(detailTable);

	}

	@Override
	public void enter(ViewChangeEvent event) {

		ObjectMapper m = new ObjectMapper();

		container.removeAllItems();
		Map<String, ServiceDefinition> defMap = serviceRegistry
				.getServiceDefinitions();

		for (ServiceDefinition def : defMap.values()) {
			ObjectNode n = m.createObjectNode();
			n.put("serviceName", def.getName());
			n.put("serviceType", def.getServiceFactory().getServiceType());
			container.addJsonObject(n);
		}

	}

	public String maskProperty(String key, String val) {
		key = Objects.toString(key, "").toLowerCase();
		if (key.contains("password") || key.contains("apikey")
				|| key.contains("secret") || key.contains("apitoken")) {

			if (!Strings.isNullOrEmpty(val)) {

				return "******";
			}
		}
		return val;
	}

	public class ClickListener implements ItemClickEvent.ItemClickListener {

		@Override
		public void itemClick(ItemClickEvent event) {
			Object id = event.getItemId();

			Item item = table.getItem(id);
			detailContainer.removeAllItems();
			String serviceName = Objects.toString(
					item.getItemProperty("serviceName").getValue(), "");

			Map<String, ServiceDefinition> defMap = serviceRegistry
					.getServiceDefinitions();

			ServiceDefinition def = defMap.get(serviceName);
			ObjectMapper m = new ObjectMapper();
			if (def != null) {
				Properties p = def.getProperties();

				for (Object keyObject : p.keySet()) {
					String key = Objects.toString(keyObject, "");
					String val = p.getProperty(key);

					ObjectNode n = m.createObjectNode();
					n.put("propertyName", key);
					n.put("propertyValue", maskProperty(key, val));

					detailContainer.addJsonObject(n);

				}
			}

		}

	}

	List<ServiceFactory> getServiceFactories() {
		Map<String, ServiceFactory> map = Kernel.getApplicationContext()
				.getBeansOfType(ServiceFactory.class);
		List<ServiceFactory> list = Lists.newArrayList(map.values());

		return list;
	}
}
