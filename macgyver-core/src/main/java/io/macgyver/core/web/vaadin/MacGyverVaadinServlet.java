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
