package io.macgyver.core.config;

import grails.util.Environment;
import io.macgyver.core.Bootstrap;
import io.macgyver.core.ContextRefreshApplicationListener;
import io.macgyver.core.CoreBindingSupplier;
import io.macgyver.core.CoreSystemInfo;
import io.macgyver.core.Kernel;
import io.macgyver.core.MacGyverBeanFactoryPostProcessor;
import io.macgyver.core.ScriptHookManager;
import io.macgyver.core.Startup;
import io.macgyver.core.auth.InternalUserManager;
import io.macgyver.core.crypto.Crypto;
import io.macgyver.core.eventbus.EventBusPostProcessor;
import io.macgyver.core.eventbus.MacGyverEventBus;
import io.macgyver.core.resource.provider.filesystem.FileSystemResourceProvider;
import io.macgyver.core.script.BindingSupplierManager;
import io.macgyver.core.script.ExtensionResourceProvider;
import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.neo4j.rest.Neo4jRestClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.google.common.base.Preconditions;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.ning.http.client.AsyncHttpClient;

@Configuration
public class CoreConfig implements EnvironmentAware {

	@Autowired
	org.springframework.core.env.Environment env;

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

	
	public static boolean isUnitTest() {

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
	public static MacGyverBeanFactoryPostProcessor macBeanFactoryPostProcessor() {
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

	@Bean(name = "macGraphClient")
	public Neo4jRestClient macGraphClient() throws MalformedURLException {
		Preconditions.checkNotNull(env);
		return new Neo4jRestClient(env.getProperty("neo4j.url"));

	}

	@Bean
	public ExtensionResourceProvider macExtensionResourceProvider() {
		ExtensionResourceProvider loader = new ExtensionResourceProvider();

		FileSystemResourceProvider fsLoader = new FileSystemResourceProvider(
				Bootstrap.getInstance().getMacGyverHome());
		loader.addResourceLoader(fsLoader);

		return loader;
	}

	@Override
	public void setEnvironment(
			org.springframework.core.env.Environment environment) {
		this.env = environment;

	}

	@Bean
	public HazelcastInstance macHazelcast() {
		Config cfg = new Config();
		
			logger.warn("disabling hazelcast multicast discovery to speed things up");
			cfg.setProperty("hazelcast.local.localAddress", "127.0.0.1");
			cfg.getNetworkConfig().getJoin().getMulticastConfig()
					.setEnabled(false);
		
		return Hazelcast.newHazelcastInstance(cfg);
	}
}
