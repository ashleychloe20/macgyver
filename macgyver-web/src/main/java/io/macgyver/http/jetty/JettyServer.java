package io.macgyver.http.jetty;

import static spark.Spark.get;
import io.macgyver.core.Kernel;
import io.macgyver.http.shiro.MacGyverFilter;
import io.macgyver.http.spark.SparkletBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.servlet.SparkApplication;
import spark.servlet.SparkFilter;

import com.google.common.eventbus.Subscribe;

public class JettyServer extends Server {

	@Value("${macgyver.web.enabled:true}")
	boolean enabled = true;

	@Autowired
	ApplicationContext ctx;

	Logger log = LoggerFactory.getLogger(getClass());

	ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();

	public JettyServer(int port) {
		super(port);

	}

	public void doStart() throws Exception {
		super.doStart();
	}

	public void addHandler(ServletContextHandler h) throws Exception {
		log.info("adding ServletContextHandler for deferred startup: {}", h);

	       FilterHolder fh = new FilterHolder();

	   
	        fh.setName("shiroFilter");

	        fh.setFilter(new DelegatingFilterProxy());

	        fh.setInitParameter("targetFilterLifecycle", Boolean.TRUE.toString());
	        h.addFilter(fh, "/*", EnumSet.of(DispatcherType.REQUEST,
	                DispatcherType.ERROR, DispatcherType.FORWARD,
	                DispatcherType.INCLUDE));

	        fh = new FilterHolder();
	        fh.setFilter(new MacGyverFilter());
	        h.addFilter(fh, "/*", EnumSet.of(DispatcherType.REQUEST,
	                DispatcherType.ERROR, DispatcherType.FORWARD,
	                DispatcherType.INCLUDE));	

		contextHandlerCollection.addHandler(h);

	}

	/**
	 * This will get called once the Spring Container is fully started.
	 * 
	 * @param event
	 */
	@Subscribe
	public void startAfterSpringContextInitialized(ContextRefreshedEvent event)
			throws Exception {

		if (!enabled) {
			log.info("not starting jetty because it is disabled");
			return;
		}
		log.info("starting jetty " + event.getApplicationContext());
		if (event.getApplicationContext() != io.macgyver.core.Kernel
				.getInstance().getApplicationContext()) {
			// We are only interested context refresh events from the parent
			// Spring Context.
			return;
		}

		if (isStarted() || isStarting()) {

			// We don't deal with restarting
			return;
		}

		
		ServletContextHandler servletContextHandler = new ServletContextHandler();
		servletContextHandler.setSessionHandler(new SessionHandler());
		servletContextHandler.setContextPath("/");
		setHandler(servletContextHandler);
	
		

		servletContextHandler.addServlet(StatcResourceServlet.class,"/*");

	
	
		Filter sparkFilter = new SparkFilter();

		FilterHolder sparkFilterHolder = new FilterHolder();
		sparkFilterHolder.setInitParameter("targetFilterLifecycle",
				Boolean.TRUE.toString());
		sparkFilterHolder.setName("spark");
		SparkFilter sf = new SparkFilter();
		sparkFilterHolder.setFilter(sf);
		sparkFilterHolder.setInitParameter("applicationClass",
				SparkletBuilder.class.getName());

		
	       FilterHolder fh = new FilterHolder();

		   
	        fh.setName("shiroFilter");

	        fh.setFilter(new DelegatingFilterProxy());

	        fh.setInitParameter("targetFilterLifecycle", Boolean.TRUE.toString());
	        servletContextHandler.addFilter(fh, "/*", EnumSet.of(DispatcherType.REQUEST,
	                DispatcherType.ERROR, DispatcherType.FORWARD,
	                DispatcherType.INCLUDE));

	        fh = new FilterHolder();
	        fh.setFilter(new MacGyverFilter());
	        servletContextHandler.addFilter(fh, "/*", EnumSet.of(DispatcherType.REQUEST,
	                DispatcherType.ERROR, DispatcherType.FORWARD,
	                DispatcherType.INCLUDE));	
		
	        
	        // Now bind to applicationContext
	        GenericWebApplicationContext gwac = new GenericWebApplicationContext();
            gwac.setParent(Kernel.getInstance().getApplicationContext());
            gwac.setServletContext(servletContextHandler
                    .getServletContext());
            servletContextHandler
            .getServletContext()
            .setAttribute(
                    WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                    gwac);
            gwac.refresh();
            // End binding to GWAC
		
		servletContextHandler.addFilter(sparkFilterHolder, "/*",
				EnumSet.of(DispatcherType.REQUEST));
	
		Spark.staticFileLocation("/public");

		
		start();

		Connector[] connectors = getConnectors();
		if (connectors != null) {
			for (Connector c : getConnectors()) {
				// For loop is odd, but we may need to loop through if we have
				// multiple connectors in the future

				/*
				 * getPort() has gone missing in jetty 9 String banner =
				 * "\n\nHTTP Server available at: http://localhost:" +
				 * c.getPort() + "\n"; log.info(banner);
				 */
				return;
			}
		}

	}

}
