package io.macgyver.core.web.shiro;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MacGyverFilter implements Filter {

	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("init({})", filterConfig);

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		Subject subject = null;
		WebApplicationContextUtils.getRequiredWebApplicationContext(request
				.getServletContext());

		if (log.isTraceEnabled()) {
			subject = SecurityUtils.getSubject();
			if (subject != null) {

				log.trace("subject.principal: {}", subject.getPrincipal());
			}

		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		log.info("detroy()");

	}

}