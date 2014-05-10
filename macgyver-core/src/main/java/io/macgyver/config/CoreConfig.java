package io.macgyver.config;

import io.macgyver.core.ContextRefreshApplicationListener;
import io.macgyver.core.CoreBindingSupplier;
import io.macgyver.core.Kernel;
import io.macgyver.core.MacGyverPropertySourcesPlaceholderConfigurer;
import io.macgyver.core.Startup;
import io.macgyver.core.crypto.Crypto;
import io.macgyver.core.eventbus.EventBusPostProcessor;
import io.macgyver.core.eventbus.MacGyverEventBus;
import io.macgyver.core.script.BindingSupplierManager;
import io.macgyver.core.service.ServiceRegistry;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ning.http.client.AsyncHttpClient;

@Configuration
public class CoreConfig {

	@Autowired
	ApplicationContext applicationContext;

	static Logger logger = LoggerFactory.getLogger(CoreConfig.class);

	@Value("${macgyver.ext.location:.}")
	public String extLocation;

	@Bean(name = "macgyverConfigurer")
	static public MacGyverPropertySourcesPlaceholderConfigurer macgyverConfigurer() {
		return new MacGyverPropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public ContextRefreshApplicationListener contextRefreshApplicationListener() {
		return new ContextRefreshApplicationListener();
	}

	@Bean(name = "macgyverAsyncHttpClient", destroyMethod = "close")
	public AsyncHttpClient macgyverAsyncHttpClient() {
		return new AsyncHttpClient();
	}

	@Bean(name = "macGyverEventBus")
	public MacGyverEventBus macGyverEventBus() {
		MacGyverEventBus b = new MacGyverEventBus();
		return b;
	}

	@Bean
	public EventBusPostProcessor createEventBusPostProcessor() {
		return new EventBusPostProcessor();
	}

	@Bean(name = "macgyverKernel")
	public Kernel createKernel() {

		logger.info("macgyver.ext.location: {}", extLocation);
		return new Kernel(new java.io.File(extLocation));
	}

	@Bean
	public Startup startup() {
		return new Startup();
	}

	@Bean
	public BindingSupplierManager bindingSupplierManager() {
		return new BindingSupplierManager();
	}

	@Bean
	public CoreBindingSupplier coreBindingSupplier() {
		return new CoreBindingSupplier();
	}

	@Bean
	public Crypto crypto() {
		return Crypto.instance;
	}

	@Bean(name = "testOverride")
	public Properties testOverride() {
		Properties props = new Properties();
		props.put("x", "from coreconfig");
		return props;
	}

	@Bean
	public static AutowiredAnnotationBeanPostProcessor autowiredPostProcessor() {
		return new AutowiredAnnotationBeanPostProcessor();

	}

	@Bean
	public ServiceRegistry serviceInstanceRegistry() {
		return new ServiceRegistry();
	}

	
}
