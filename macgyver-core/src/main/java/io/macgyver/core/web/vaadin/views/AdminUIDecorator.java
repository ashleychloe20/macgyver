package io.macgyver.core.web.vaadin.views;

import io.macgyver.core.auth.MacGyverRole;
import io.macgyver.core.web.vaadin.MacGyverUIDecorator;

import java.util.Collection;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.server.VaadinServletService;

public class AdminUIDecorator extends MacGyverUIDecorator {

	@Override
	public void decorate(MacGyverUICreateEvent createEvent) {

		String user = VaadinServletService.getCurrentServletRequest()
				.getRemoteUser();

		createEvent.getUI().addMenuItem("Admin", "Scripts",
				ScriptAdminView.VIEW_NAME);
		createEvent.getUI().getNavigator()
				.addView(ScriptAdminView.VIEW_NAME, ScriptAdminView.class);
	}

	@Override
	public void decorate(MacGyverUIPostCreateEvent event) {
		// TODO Auto-generated method stub

	}

}
