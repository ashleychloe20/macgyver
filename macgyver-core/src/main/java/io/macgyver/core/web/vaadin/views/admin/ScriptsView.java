package io.macgyver.core.web.vaadin.views.admin;

import java.io.IOException;
import java.util.List;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.macgyver.core.Kernel;
import io.macgyver.core.MacGyverException;
import io.macgyver.core.auth.AuthUtil;
import io.macgyver.core.auth.MacGyverRole;
import io.macgyver.core.resource.Resource;
import io.macgyver.core.resource.ResourceProvider;
import io.macgyver.core.resource.provider.filesystem.FileSystemResourceProvider;
import io.macgyver.core.scheduler.AutoScheduler;
import io.macgyver.core.script.ExtensionResourceProvider;
import io.macgyver.core.web.vaadin.IndexedJsonContainer;
import io.macgyver.core.web.vaadin.ViewDecorators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.ColumnGenerator;

public class ScriptsView extends VerticalLayout implements View {

	public static final String VIEW_NAME = "admin/scripts";
	Table table;
	IndexedJsonContainer container;

	boolean userHasExecutPermissions = false;

	Logger logger = LoggerFactory.getLogger(ScriptsView.class);
	public ScriptsView() {
		super();
		setMargin(true);
		userHasExecutPermissions = AuthUtil
				.currentUserHasRole(MacGyverRole.ROLE_MACGYVER_ADMIN);
		container = new IndexedJsonContainer();
		container.addContainerProperty("resource", String.class, "");
		container.addContainerProperty("providerType", String.class, "");
		container.addContainerProperty("actions", Button.class, null);

		table = new Table("Scripts");
		table.setContainerDataSource(container);
		table.setWidth("1000px");

		table.setColumnHeader("resource", "Resource");
		table.setColumnHeader("providerType", "Provider");
		table.setColumnHeader("actions", "Actions");
		ColumnGenerator cg = new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, final Object itemId,
					Object columnId) {

				if (!userHasExecutPermissions) {
					// no button to execute 
					return null;
				}
				Button b = new Button("Invoke");
				b.setStyleName("tiny");
				Button.ClickListener cl = new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						Item item = container.getItem(itemId);
						Property p = item.getItemProperty("resource");
						Object value = p.getValue();

						ObjectNode on = container.getJsonObject(itemId);
						if (on != null) {
							try {
								String hash = on.path("hash").asText();
								Optional<Resource> r = findResourceByHash(hash);
								if (r.isPresent()) {
									scheduleImmediate(r.get());
								}

							} catch (IOException e) {
								throw new MacGyverException(e);
							}

						}

					}
				};
				b.addClickListener(cl);
				return b;
			}
		};

		table.addGeneratedColumn("actions", cg);

		Button b = new Button("Reload Scripts");

	
		b.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				refresh();
			}
		});
		addComponent(b);
		addComponent(table);
		ViewDecorators.decorate(this);
	}

	protected ExtensionResourceProvider getExtensionResourceProvider() {
		return Kernel.getInstance().getApplicationContext()
				.getBean(ExtensionResourceProvider.class);
	}

	Optional<Resource> findResourceByHash(String hash) throws IOException {
		for (Resource r : getExtensionResourceProvider().findResources()) {
			String resourceHash = r.getHash();
			if (resourceHash.equals(hash)) {
				return Optional.of(r);
			}
		}
		return Optional.absent();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		refresh();

	}

	public void refresh() {
		try {
			logger.info("reloading");
			container.removeAllItems();
			ExtensionResourceProvider extensionProvider = Kernel.getInstance()
					.getApplicationContext()
					.getBean(ExtensionResourceProvider.class);
			extensionProvider.refresh();
			ObjectMapper mapper = new ObjectMapper();
			List<ObjectNode> list = Lists.newArrayList();

			for (Resource r : extensionProvider.findResources()) {
				ResourceProvider rp = r.getResourceProvider();

				if (r.getPath().startsWith("scripts/")) {
					ObjectNode n = mapper.createObjectNode();
					n.put("resource", r.getPath());
					if (rp.getClass().equals(FileSystemResourceProvider.class)) {
						n.put("providerType", "filesystem");
					} else if (rp.getClass().getName().contains("Git")) {
						n.put("providerType", "git");
					}
					n.put("hash", r.getHash());
					list.add(n);

				}

			}
			container.addJsonObjects(list);
		} catch (IOException e) {
			throw new MacGyverException(e);
		}
	}

	public void scheduleImmediate(Resource r) {
		try {
			AutoScheduler scheduler = Kernel.getInstance()
					.getApplicationContext().getBean(AutoScheduler.class);
			scheduler.scheduleImmediate(r);
		} catch (SchedulerException e) {
			throw new MacGyverException(e);
		}
	}
}
