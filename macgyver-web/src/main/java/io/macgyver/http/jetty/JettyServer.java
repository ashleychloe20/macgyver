package io.macgyver.http.jetty;

import io.macgyver.core.Kernel;
import io.macgyver.http.shiro.MacGyverFilter;
import io.macgyver.web.rythm.TestController;
import io.macgyver.webconfig.MacWebConfig;

import java.io.File;
import java.util.EnumSet;
import java.util.UUID;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;

import com.google.common.eventbus.Subscribe;

public class JettyServer extends Server {

	Logger logger = LoggerFactory.getLogger(JettyServer.class);
	
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

		h.setResourceBase(new File(Kernel.getInstance().getExtensionDir(),
				"web").getAbsolutePath());
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
	public void startAfterSpringContextInitialized(
			Kernel.KernelStartedEvent event) throws Exception {

		if (!enabled) {
			log.info("not starting jetty because it is disabled");
			return;
		}
		log.info("starting jetty ");

		if (isStarted() || isStarting()) {

			// We don't deal with restarting
			return;
		}

		ServletContextHandler servletContextHandler = new ServletContextHandler();
		servletContextHandler.setSessionHandler(new SessionHandler());
		servletContextHandler.setContextPath("/");
		servletContextHandler.setResourceBase(new File(Kernel.getInstance().getExtensionDir(),"web").getAbsolutePath());
		setHandler(servletContextHandler);


		
		

		FilterHolder fh = new FilterHolder();

		fh.setName("shiroFilter");

		fh.setFilter(new DelegatingFilterProxy());

		fh.setInitParameter("targetFilterLifecycle", Boolean.TRUE.toString());
		servletContextHandler.addFilter(fh, "/*", EnumSet.of(
				DispatcherType.REQUEST, DispatcherType.ERROR,
				DispatcherType.FORWARD, DispatcherType.INCLUDE));

		fh = new FilterHolder();
		fh.setFilter(new MacGyverFilter());
		servletContextHandler.addFilter(fh, "/*", EnumSet.of(
				DispatcherType.REQUEST, DispatcherType.ERROR,
				DispatcherType.FORWARD, DispatcherType.INCLUDE));

		
		
		
		// Now bind to applicationContext
		
		AnnotationConfigWebApplicationContext gwac = new AnnotationConfigWebApplicationContext();
		gwac.setParent(Kernel.getInstance().getApplicationContext());
		gwac.setServletContext(servletContextHandler.getServletContext());
		gwac.register(MacWebConfig.class);
		logger.info("creating WebApplicationContext: {}",gwac ,Kernel.getInstance().getApplicationContext());
		servletContextHandler.getServletContext().setAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
				gwac);
		gwac.refresh();
		// End binding to GWAC

		
		ServletHolder sh = new ServletHolder(StaticResourceServlet.class);
		logger.info("registering default servlet: {}",sh);
		sh.setName(StaticResourceServlet.class.getName());
		servletContextHandler.addServlet(sh, "/"+UUID.randomUUID().toString());
	
		
		DispatcherServlet dispatcher = new DispatcherServlet(gwac);
		logger.info("registering Spring MVC Dispatcher: {}",dispatcher);
		dispatcher.getWebApplicationContext().getBean(TestController.class);
		ServletHolder dispatcherServletHolder = new ServletHolder(dispatcher);
		dispatcherServletHolder.setName(DispatcherServlet.class.getName());
		servletContextHandler.addServlet(dispatcherServletHolder, "/");
		
	
	
	

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
