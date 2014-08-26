package io.macgyver.core.web.vaadin;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.server.VaadinServlet;

/**
 * MacGyver-specific customization of the standard VaadinServlet. Handles
 * authentication redirect.
 * 
 * @author rschoening
 *
 */
public class MacGyverVaadinServlet extends VaadinServlet {

	@Override
	public void service(ServletRequest rq, ServletResponse arg1)
			throws ServletException, IOException {

		HttpServletRequest request = (HttpServletRequest) rq;
		String ui = request.getRequestURI();
		Principal p = request.getUserPrincipal();

		// If we don't have a principal, then we are not authenticated.
		if (p == null) {

			// Redirect only if this is a browser page requesst.
			if (ui != null && ui.equals("/ui/") || ui.equalsIgnoreCase("/ui")) {
				HttpServletResponse response = (HttpServletResponse) arg1;
				response.sendRedirect("/login");
				return;
			}

		}
		super.service(rq, arg1);
	}

}
