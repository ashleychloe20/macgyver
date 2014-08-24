package io.macgyver.core.web.vaadin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class HomeView extends VerticalLayout implements View {
	
	
	   public HomeView() {
		   setMargin(true);
	        setSizeFull();
	        Label home = new Label("Welcome to MacGyver");
	        addComponent(home);
	        
	       
	        
	        setComponentAlignment(home, Alignment.MIDDLE_CENTER);
	        
	    }        
	        
	    @Override
	    public void enter(ViewChangeEvent event) {
	      //  Notification.show("Welcome to the Animal Farm");
	    }
	

}
