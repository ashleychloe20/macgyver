package io.macgyver.config;

import io.macgyver.core.ContextRefreshApplicationListener;
import io.macgyver.core.CoreBindingSupplier;
import io.macgyver.core.Kernel;
import io.macgyver.core.MacGyverCrashAuthPlugin;
import io.macgyver.core.Startup;
import io.macgyver.core.crypto.Crypto;
import io.macgyver.core.eventbus.EventBusPostProcessor;
import io.macgyver.core.eventbus.MacGyverEventBus;
import io.macgyver.core.mapdb.BootstrapMapDB;
import io.macgyver.core.script.BindingSupplierManager;
import io.macgyver.core.service.ServiceRegistry;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.mapdb.DBMaker;
import org.mapdb.TxMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.actuate.autoconfigure.ShellProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.google.common.base.Optional;
import com.ning.http.client.AsyncHttpClient;

@Configuration
public class CoreConfig {

	@Autowired
	ApplicationContext applicationContext;

	static Logger logger = LoggerFactory.getLogger(CoreConfig.class);


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
		File extLocation = Kernel.determineExtensionDir();
		logger.info("macgyver.ext.location: {}", extLocation);
		return new Kernel(extLocation);
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
	public static AutowiredAnnotationBeanPostProcessor autowiredPostProcessor() {
		return new AutowiredAnnotationBeanPostProcessor();

	}

	@Bean
	public ServiceRegistry serviceInstanceRegistry() {
		return new ServiceRegistry();
	}

/*
	@Bean(name="macgyverMapDb")
	@ConditionalOnClass(name={"org.junit.Assert"})
	TxMaker createTestMapDb() {
		
	}*/
	@Bean(name="io.macgyver.MapDB",destroyMethod="close")
	public synchronized TxMaker mapDb() {
		if (isUnitTest()) {
			TxMaker txm = DBMaker.newMemoryDB().closeOnJvmShutdown()
					.makeTxMaker();
			return txm;
		}
		else {
			Optional<TxMaker> txm = BootstrapMapDB.getInstance().getTxMaker();
			if (txm.isPresent()) {
				return txm.get();
			}
			else {
				BootstrapMapDB.getInstance().init();
				return BootstrapMapDB.getInstance().getTxMaker().get();
			}
		
		}
	

	}
	public boolean isUnitTest() {
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		new RuntimeException().printStackTrace(pw);
		pw.close();
		return sw.toString().contains("at org.junit");
	
	}
	@Bean
	public ShellProperties.CrshShellAuthenticationProperties authProperties() {
		ShellProperties.CrshShellAuthenticationProperties x = new ShellProperties.CrshShellAuthenticationProperties() {
			
			@Override
			protected void applyToCrshShellConfig(Properties config) {
				System.out.println("CONFIG: "+config);
				config.put("crash.auth", "macgyvercrash");
			//	config.put("crash.auth.simple.username", "abc");
			//	config.put("crash.auth.simple.password", "abc");
				
			}
		};
		return x;
	}
	@Bean
	public MacGyverCrashAuthPlugin macgyverCrashAuthPlugin() {
		return new MacGyverCrashAuthPlugin();
	}
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
	    return new PropertySourcesPlaceholderConfigurer();
	}
}
