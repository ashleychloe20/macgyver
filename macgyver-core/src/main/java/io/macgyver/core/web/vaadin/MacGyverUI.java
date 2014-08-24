package io.macgyver.core.web.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


@SuppressWarnings("serial")
@Theme("valo")
public class MacGyverUI extends UI
{

  
	MenuBar menubar;
	Navigator navigator;
	
	
	public static  MacGyverUI getMacGyverUI() {
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
        
     // Define a common menu command for all the menu items.
        MenuBar.Command mycommand = new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
            	
            	Notification.show(UI.getCurrent().toString());

            }  
        };
        
        // https://vaadin.com/book/vaadin7/-/page/advanced.navigator.html
     
        MenuItem home = menubar.addItem("Home", navigateMenuCommand(""));
        


   
   
      
        MenuItem snacks = menubar.addItem("Admin", null);
        snacks.addItem("Beans",  navigateMenuCommand("admin/beans"));
      
        
        hl.setComponentAlignment(menubar, Alignment.MIDDLE_CENTER);
        
        
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

}