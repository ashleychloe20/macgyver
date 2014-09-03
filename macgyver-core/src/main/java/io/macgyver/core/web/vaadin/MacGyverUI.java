package io.macgyver.core.web.vaadin;

import io.macgyver.core.web.views.BeansView;
import io.macgyver.core.web.views.HomeView;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("valo")
public class MacGyverUI extends UI {

	Logger logger = LoggerFactory.getLogger(MacGyverUI.class);

	protected MenuBar menubar;
	protected Navigator navigator;

	public static MacGyverUI getMacGyverUI() {
		return (MacGyverUI) UI.getCurrent();
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public MenuBar getMenuBar() {
		return menubar;
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

		HorizontalLayout hl = new HorizontalLayout();

		menubar = new MenuBar();

		hl.addComponent(menubar);
		hl.setWidth(100, Unit.PERCENTAGE);

		layout.addComponent(hl);

		VerticalLayout contentLayout = new VerticalLayout();
		layout.addComponent(contentLayout);
		navigator = new Navigator(this, contentLayout);

		navigator.addView("", HomeView.class);
		navigator.addView("admin/beans", BeansView.class);

	

		// https://vaadin.com/book/vaadin7/-/page/advanced.navigator.html

		MenuItem home = menubar.addItem("Home", navigateMenuCommand(""));

		addMenuItem("Admin", "Beans", navigateMenuCommand("admin/beans"));

		hl.setComponentAlignment(menubar, Alignment.MIDDLE_CENTER);

		MacGyverUIDecorator
				.dispatch(new MacGyverUIDecorator.MacGyverUICreateEvent(this));
		MacGyverUIDecorator
				.dispatch(new MacGyverUIDecorator.MacGyverUIPostCreateEvent(
						this));

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

	/**
	 * Adds a menu item: topLevel > item
	 * 
	 * @param topLevel
	 * @param item
	 * @param cmd
	 *            command to be executed when menu item is selected
	 * @return The newly created menu item
	 */

	public MenuItem addMenuItem(String topLevel, String item,
			MenuBar.Command cmd) {
		return addMenuItem(topLevel, item, cmd, true);
	}

	public MenuItem addMenuItem(String topLevel, String item, String view) {
		return addMenuItem(topLevel, item, view, true);	
	}
	public MenuItem addMenuItem(String topLevel, String item, String view, boolean replace) {
		return addMenuItem(topLevel, item, navigateMenuCommand(view));	
	}
	public MenuItem addMenuItem(String topLevel, String item,
			MenuBar.Command cmd, boolean replace) {
		Preconditions.checkNotNull(topLevel);
		Preconditions.checkNotNull(item);
		MenuItem topItem = null;
		for (MenuItem t : menubar.getItems()) {
			if (topLevel.equals(t.getText())) {
				topItem = t;
			}
		}
		if (topItem == null) {
			topItem = menubar.addItem(topLevel, null);
		}

		List<MenuItem> itemsToRemove = Lists.newArrayList();
		MenuItem subItem = null;
		if (topItem != null && topItem.getChildren() != null) {
			for (MenuItem t : topItem.getChildren()) {
				if (t.getText().equals(item)) {
					if (!replace) {
						throw new IllegalStateException("Menu item " + topLevel
								+ "/" + item + " already exists");
					}
					// need to go through this pattern to avoid concurrent
					// modification exception
					itemsToRemove.add(t);
				}
			}
		}

		for (MenuItem t : itemsToRemove) {
			topItem.removeChild(t);
		}
		logger.debug("adding menu item {}/{} with command ", topLevel, item,
				cmd);
		return topItem.addItem(item, cmd);

	}
}
