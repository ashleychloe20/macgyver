package io.macgyver.core.graph;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tinkerpop.blueprints.TransactionalGraph;

public class TransactionalGraphFilter implements Filter {

	@Autowired
	TransactionalGraph graph;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("initializing {}",this);

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		}
		finally {
			logger.debug("rollback {}",graph);
			graph.rollback();
		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
