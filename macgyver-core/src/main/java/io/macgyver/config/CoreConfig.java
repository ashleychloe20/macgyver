package io.macgyver.config;

import io.macgyver.core.ConfigStore;
import io.macgyver.core.ContextRefreshApplicationListener;
import io.macgyver.core.CoreBindingSupplier;
import io.macgyver.core.FileConfigStore;
import io.macgyver.core.Kernel;
import io.macgyver.core.Startup;
import io.macgyver.core.crypto.Crypto;
import io.macgyver.core.crypto.KeyStoreManager;
import io.macgyver.core.eventbus.EventBusPostProcessor;
import io.macgyver.core.eventbus.MacGyverEventBus;
import io.macgyver.core.script.BindingSupplierManager;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.google.common.collect.Lists;
import com.ning.http.client.AsyncHttpClient;

@Configuration
public class CoreConfig {

	@Autowired
	ApplicationContext applicationContext;

	static Logger logger = LoggerFactory.getLogger(CoreConfig.class);

	@Value("${macgyver.ext.location:.}")
	String extLocation;

	@Bean(name = "macgyverConfigurer")
	static public PropertySourcesPlaceholderConfigurer macgyverConfigurer() {
		PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();

		FileSystemResource testResource = new FileSystemResource(new File(".",
				"src/test/resources/macgyver-test.properties"));
		List<Resource> rlist = Lists.newArrayList();

		if (testResource.exists()) {
			rlist.add(testResource);
		} else {
			logger.info("not using {} because file does not exist",
					testResource.getFile().getAbsolutePath());
		}
		pspc.setLocations(rlist.toArray(new Resource[0]));
		pspc.setIgnoreResourceNotFound(true);
		pspc.setIgnoreUnresolvablePlaceholders(true);
		return pspc;
	}

	@Bean(name = "configStore")
	public ConfigStore configStore() {
		FileConfigStore fcs = new FileConfigStore(new File(extLocation,
				"conf/config.groovy"));

		return fcs;
	}

	@Bean
	public ContextRefreshApplicationListener contextRefreshApplicationListener() {
		return new ContextRefreshApplicationListener();
	}

	@Bean(name = "asyncHttpClient", destroyMethod = "close")
	public AsyncHttpClient asyncHttpClient() {
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

	@Bean
	public Kernel createKernel() {

		logger.info("macgyver.ext.location: {}", extLocation);
		File extensionDir = new File(extLocation);

		return new Kernel(extensionDir);
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
	public KeyStoreManager keyStoreManager() {
		return new KeyStoreManager();
	}

	@Bean
	public Crypto crypto() {
		return new Crypto();
	}

	@Bean(name = "testOverride")
	public Properties testOverride() {
		Properties props = new Properties();
		props.put("x", "from coreconfig");
		return props;
	}
}
