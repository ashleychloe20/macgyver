package io.macgyver.core.config;

import io.macgyver.core.Bootstrap;
import io.macgyver.core.ContextRefreshApplicationListener;
import io.macgyver.core.CoreBindingSupplier;
import io.macgyver.core.CoreSystemInfo;
import io.macgyver.core.Kernel;
import io.macgyver.core.MacGyverBeanFactoryPostProcessor;
import io.macgyver.core.ScriptHookManager;
import io.macgyver.core.Startup;
import io.macgyver.core.VfsManager;
import io.macgyver.core.auth.InternalUserManager;
import io.macgyver.core.crypto.Crypto;
import io.macgyver.core.eventbus.EventBusPostProcessor;
import io.macgyver.core.eventbus.MacGyverEventBus;
import io.macgyver.core.graph.CoreIndexInitializer;
import io.macgyver.core.graph.GraphManager;
import io.macgyver.core.graph.GraphRepository;
import io.macgyver.core.resource.provider.filesystem.FileSystemResourceProvider;
import io.macgyver.core.script.BindingSupplierManager;
import io.macgyver.core.script.ExtensionResourceProvider;
import io.macgyver.core.service.ServiceRegistry;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.Properties;

import org.apache.commons.configuration.BaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.google.common.io.Files;
import com.ning.http.client.AsyncHttpClient;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.impls.neo4j2.Neo4j2Graph;

@Configuration
public class CoreConfig {

	@Autowired
	ApplicationContext applicationContext;

	static Logger logger = LoggerFactory.getLogger(CoreConfig.class);

	@Bean
	public ContextRefreshApplicationListener macContextRefreshApplicationListener() {
		return new ContextRefreshApplicationListener();
	}

	@Bean(name = "macAsyncHttpClient", destroyMethod = "close")
	public AsyncHttpClient macAsyncHttpClient() {
		return new AsyncHttpClient();
	}

	@Bean(name = "macEventBus")
	public MacGyverEventBus macEventBus() {
		MacGyverEventBus b = new MacGyverEventBus();
		return b;
	}

	@Bean
	public EventBusPostProcessor macEventBusPostProcessor() {
		return new EventBusPostProcessor();
	}

	@Bean(name = "macKernel")
	public Kernel macKernel() {
		
		return new Kernel();
	}

	@Bean
	public Startup macStartup() {
		return new Startup();
	}

	@Bean
	public BindingSupplierManager macBindingSupplierManager() {
		return new BindingSupplierManager();
	}

	@Bean
	public CoreBindingSupplier macCoreBindingSupplier() {
		return new CoreBindingSupplier();
	}

	@Bean
	public Crypto macCrypto() {
		Crypto crypto = new Crypto();
		Crypto.instance = crypto;
		return crypto;
	}

	@Bean(name = "testOverride")
	public Properties testOverride() {
		Properties props = new Properties();
		props.put("x", "from coreconfig");
		return props;
	}

	@Bean
	public static AutowiredAnnotationBeanPostProcessor macAutowiredPostProcessor() {
		return new AutowiredAnnotationBeanPostProcessor();

	}

	@Bean
	public ServiceRegistry macServiceRegistry() {
		return new ServiceRegistry();
	}




	public boolean isUnitTest() {

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		new RuntimeException().printStackTrace(pw);
		pw.close();
		return sw.toString().contains("at org.junit");

	}

	/*
	 * @Bean public CrshShellAuthenticationProperties macCrashAuth() { // In
	 * case no shell.auth property is provided fall back to Spring Security //
	 * based authentication and get role to access shell from //
	 * ManagementServerProperties. // In case shell.auth is set to spring and
	 * roles are configured using // shell.auth.spring.roles the below default
	 * role will be overridden by // ConfigurationProperties.
	 * SpringAuthenticationProperties authenticationProperties = new
	 * SpringAuthenticationProperties(); authenticationProperties.setRoles(new
	 * String[] {"FOOR"});
	 * 
	 * return authenticationProperties; }
	 */

	@Bean
	public static PropertySourcesPlaceholderConfigurer macPropertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public ScriptHookManager macHookScriptManager() {
		return new ScriptHookManager();
	}

	@Bean
	public MacGyverBeanFactoryPostProcessor macBeanFactoryPostProcessor() {
		return new MacGyverBeanFactoryPostProcessor();
	}

	@Bean(name = "macCoreRevisionInfo")
	public CoreSystemInfo macCoreRevisionInfo() {
		return new CoreSystemInfo();
	}

	@Bean(name = "macUserManager")
	public InternalUserManager macUserManager() {
		return new InternalUserManager();
	}



	@Bean(name = "macVfsManager")
	public VfsManager macVfsManager() {

		VfsManager mgr = Bootstrap.getInstance().getVfsManager();

		logger.info("macVfsManager: {}", mgr);
		return mgr;

	}
	@Bean(name = "macGraphRepository")
	@Primary
	public GraphRepository macGraphRepository() throws MalformedURLException{
		return new GraphRepository(macGraph());
	}
	@Bean(name = "macGraph", destroyMethod = "shutdown")
	public TransactionalGraph macGraph() throws MalformedURLException{
		if (isUnitTest()) {

		
			TransactionalGraph graph = new Neo4j2Graph(Files.createTempDir().getAbsolutePath());
		
			return graph;

		} else {
			File dir = Bootstrap.getInstance().getVfsManager().resolveData("macgyver-neo4j");
			
			
		

			TransactionalGraph g = new Neo4j2Graph(dir.getAbsolutePath());
			return g;

		}

	}
	
	@Bean
	public CoreIndexInitializer macCoreIndexInitializer() {
		return new CoreIndexInitializer();
	}
	
	@Bean
	public GraphManager macGraphManager() {
		return new GraphManager();
	}
	
	@Bean
	public ExtensionResourceProvider macExtensionResourceLoader() {
		ExtensionResourceProvider loader = new ExtensionResourceProvider();
		
		FileSystemResourceProvider fsLoader = new FileSystemResourceProvider(Bootstrap.getInstance().getExtensionDir());
		loader.addResourceLoader(fsLoader);
		
		
		
		return loader;
	}
}
