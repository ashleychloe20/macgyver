package io.macgyver.core.web.vaadin;

import io.macgyver.core.Kernel;
import io.macgyver.core.MacGyverConfigurationException;
import io.macgyver.core.PluginManager;
import io.macgyver.core.auth.AuthUtil;
import io.macgyver.core.auth.MacGyverRole;
import io.macgyver.core.web.vaadin.views.HomeView;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Theme("valo")
public class MacGyverUI extends UI {

	Logger logger = LoggerFactory.getLogger(MacGyverUI.class);

	ObjectMapper mapper = new ObjectMapper();
	protected MacGyverMenuLayout menuLayout = new MacGyverMenuLayout();
	protected Navigator navigator;

	ComponentContainer viewDisplay = menuLayout.getContentContainer();
	CssLayout menu = new CssLayout();
	CssLayout menuItemsLayout = new CssLayout();
	private LinkedHashMap<String, String> menuItems = new LinkedHashMap<String, String>();

	ObjectNode appDescriptor = createAppDescriptorSkeleton();

	public static MacGyverUI getMacGyverUI() {
		return (MacGyverUI) UI.getCurrent();
	}

	public Navigator getNavigator() {
		return navigator;
	}

	boolean testMode = false;

	private boolean browserCantRenderFontsConsistently() {
		// PhantomJS renders font correctly about 50% of the time, so
		// disable it to have consistent screenshots
		// https://github.com/ariya/phantomjs/issues/10592

		// IE8 also has randomness in its font rendering...

		return getPage().getWebBrowser().getBrowserApplication()
				.contains("PhantomJS")
				|| (getPage().getWebBrowser().isIE() && getPage()
						.getWebBrowser().getBrowserMajorVersion() <= 9);
	}

	static boolean isTestMode() {
		return ((MacGyverUI) getCurrent()).testMode;
	}

	public PluginManager getPluginManager() {
		return Kernel.getApplicationContext().getBean(PluginManager.class);
	}

	@Override
	protected void init(VaadinRequest request) {

		if (request.getParameter("test") != null) {
			testMode = true;

			if (browserCantRenderFontsConsistently()) {
				getPage().getStyles().add(
						".v-app.v-app.v-app {font-family: Sans-Serif;}");
			}
		}

		if (getPage().getWebBrowser().isIE()
				&& getPage().getWebBrowser().getBrowserMajorVersion() == 9) {
			menu.setWidth("320px");
		}

		if (!testMode) {
			Responsive.makeResponsive(this);
		}

		getPage().setTitle("MacGyver");
		setContent(menuLayout);
		menuLayout.setWidth("100%");

		navigator = new Navigator(this, viewDisplay);

		

		registerView("home", HomeView.class, null, null);

		getPluginManager().dispatchRegisterViews(this);

		menuLayout.addMenu(buildMenu());
		
		String f = Page.getCurrent().getUriFragment();
		if (f == null || f.equals("")) {
			navigator.navigateTo("home");
		}

		/*
		 * navigator.addViewChangeListener(new ViewChangeListener() {
		 * 
		 * @Override public boolean beforeViewChange(ViewChangeEvent event) {
		 * return true; }
		 * 
		 * @Override public void afterViewChange(ViewChangeEvent event) { for
		 * (Iterator<Component> it = menuItemsLayout.iterator(); it .hasNext();)
		 * { it.next().removeStyleName("selected"); } for (Entry<String, String>
		 * item : menuItems.entrySet()) { if
		 * (event.getViewName().equals(item.getKey())) { for
		 * (Iterator<Component> it = menuItemsLayout .iterator(); it.hasNext();)
		 * { Component c = it.next(); if (c.getCaption() != null &&
		 * c.getCaption().startsWith( item.getValue())) {
		 * c.addStyleName("selected"); break; } } break; } }
		 * menu.removeStyleName("valo-menu-visible"); } });
		 */

	}





	private ObjectNode createAppDescriptorSkeleton() {

		ObjectNode top = mapper.createObjectNode();
		ObjectNode menu = mapper.createObjectNode();
		top.put("menu", menu);
		ArrayNode m0 = mapper.createArrayNode();
		menu.put("items", m0);

		return top;

	}

	CssLayout buildMenu() {

		HorizontalLayout top = new HorizontalLayout();
		top.setWidth("100%");
		top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		top.addStyleName("valo-menu-title");
		menu.addComponent(top);
		menu.addComponent(new Label("")); // placeholder

		Button showMenu = new Button("Menu", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (menu.getStyleName().contains("valo-menu-visible")) {
					menu.removeStyleName("valo-menu-visible");
				} else {
					menu.addStyleName("valo-menu-visible");
				}
			}
		});
		showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
		showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
		showMenu.addStyleName("valo-menu-toggle");
		showMenu.setIcon(FontAwesome.LIST);
		menu.addComponent(showMenu);

		Label title = new Label("<h3><strong>MacGyver</strong></h3>",
				ContentMode.HTML);
		title.setSizeUndefined();
		top.addComponent(title);
		top.setExpandRatio(title, 1);

		MenuBar settings = new MenuBar();
		settings.addStyleName("user-menu");

		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		if (Strings.isNullOrEmpty(username)) {
			username = "anonymous";
		}
		MenuItem settingsItem = settings.addItem(username, new ExternalResource("/resources/images/profile-pic-300px.jpg"), null);
		// settingsItem.addItem("Edit Profile", null);
		// settingsItem.addItem("Preferences", null);
		// settingsItem.addSeparator();
		settingsItem.addItem("Sign Out", null);
		menu.addComponent(settings);

		menuItemsLayout.setPrimaryStyleName("valo-menuitems");
		menu.addComponent(menuItemsLayout);

		Label label = null;
		int count = -1;

		ArrayNode topItems = (ArrayNode) appDescriptor.path("menu").path(
				"items");

		for (int i = 0; i < topItems.size(); i++) {
			JsonNode topItem = topItems.get(i);
			
			logger.info("build item: "+topItem);
			label = new Label(topItem.path("display").asText(),
					ContentMode.HTML);
			label.setPrimaryStyleName("valo-menu-subtitle");
			label.addStyleName("h4");
			label.setSizeUndefined();
			menuItemsLayout.addComponent(label);
			JsonNode subItems = topItem.path("items");
			for (int j = 0; j < subItems.size(); j++) {
				JsonNode subItem = subItems.get(j);
				String display = subItem.path("display").asText();
				final String viewName = subItem.path("viewName").asText();
				Button b = new Button(display, new Button.ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						 navigator.navigateTo(viewName);
					}
				});
				b.setHtmlContentAllowed(true);
				b.setPrimaryStyleName("valo-menu-item");
				menuItemsLayout.addComponent(b);
			}
		}

		return menu;
	}

	public MenuBar.Command navigateMenuCommand(final String name) {
		MenuBar.Command cmd = new MenuBar.Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				navigator.navigateTo(name);

			}
		};
		return cmd;
	}





	public boolean currentUserHasRole(MacGyverRole role) {
		return AuthUtil.currentUserHasRole(role);
	}

	public boolean currentUserHasRole(String role) {
		return AuthUtil.currentUserHasRole(role);
	}

	public void registerView(String viewName, Class<? extends View> v,
			String topLevelMenu, String subMenu) {

		logger.info("registerView: viewName={}, class={}, topMenu={}, subMenu{}",viewName,v,topLevelMenu,subMenu);
		if (topLevelMenu != null && subMenu != null) {
			ArrayNode m0 = (ArrayNode) appDescriptor.path("menu").path("items");
			ObjectNode topLevelMenuNode = null;
			for (int i = 0; i < m0.size(); i++) {
				ObjectNode n = (ObjectNode) m0.get(i);
				if (n.path("display").asText().equals(topLevelMenu)) {
					topLevelMenuNode = n;
				}
			}
			if (topLevelMenuNode == null) {
				topLevelMenuNode = mapper.createObjectNode();
				topLevelMenuNode.put("display", topLevelMenu);
				m0.add(topLevelMenuNode);
			}
			
			ArrayNode subItems = null;
			if (!topLevelMenuNode.has("items")) {
				subItems = mapper.createArrayNode();
				topLevelMenuNode.put("items",subItems);
			}
			else {
				subItems = topLevelMenuNode.withArray("items");
			}
			
			ObjectNode subItem=null;
			for (int i=0; i<subItems.size(); i++) {
				ObjectNode si = (ObjectNode) subItems.get(i);
				if (si.path("display").asText().equals(subMenu)) {
					subItem = si;
				}
			
			}
			if (subItem == null) {
				subItem = mapper.createObjectNode();
				subItem.put("display",subMenu);
				subItem.put("viewName", viewName);
				subItems.add(subItem);
			}
		}
		
		getNavigator().addView(viewName, v);
	
		logger.info("appDescriptor: {}",appDescriptor);

	}
}
