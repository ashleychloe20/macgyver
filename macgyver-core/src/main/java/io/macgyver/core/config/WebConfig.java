package io.macgyver.core.config;

import io.macgyver.core.web.mvc.CoreApiController;
import io.macgyver.core.web.mvc.HomeController;
import io.macgyver.core.web.mvc.MacgyverWeb;
import io.macgyver.core.web.vaadin.MacGyverUI;
import io.macgyver.core.web.vaadin.MacGyverVaadinServlet;
import io.macgyver.core.web.vaadin.ViewDecorator;
import io.macgyver.core.web.vaadin.ViewDecorators;
import io.macgyver.core.web.vaadin.views.admin.AdminPlugin;
import io.macgyver.core.web.vaadin.views.admin.BeansView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.vaadin.navigator.View;

@Configuration
@ComponentScan(basePackageClasses = { HomeController.class })
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true,prePostEnabled=true)
public class WebConfig implements EnvironmentAware {

	Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Autowired
	private final org.springframework.core.io.ResourceLoader resourceLoader = new DefaultResourceLoader();

	@Override
	public void setEnvironment(Environment environment) {

	}

	@Bean
	public CoreApiController macCoreApiController() {
		return new CoreApiController();
	}

	@Bean
	public BeanPostProcessor macHandlerMappingPostProcessor() {
		return new BeanPostProcessor() {

			@Override
			public Object postProcessBeforeInitialization(Object bean,
					String beanName) throws BeansException {
				if (bean instanceof RequestMappingHandlerMapping
						&& "requestMappingHandlerMapping".equals(beanName)) {
					RequestMappingHandlerMapping m = ((RequestMappingHandlerMapping) bean);
					
					
					
				}

				return bean;
			}

			@Override
			public Object postProcessAfterInitialization(Object bean,
					String beanName) throws BeansException {
				return bean;
			}
		};
	}

	@Bean
	public MacgyverWeb macWebConfig() {
		return new MacgyverWeb();
	}




	@Bean
	public ServletRegistrationBean macVaadinServlet() {
		ServletRegistrationBean sb = new ServletRegistrationBean(new MacGyverVaadinServlet(), "/ui/*","/VAADIN/*");
		sb.addInitParameter("ui", MacGyverUI.class.getName());
		
		return sb;
	}
	
	@Bean
	public ViewDecorators macViewDecorators() {
		return new ViewDecorators();
	}
	
	@Bean
	public AdminPlugin macAdminUIDecorator() {
		return new AdminPlugin();
	}
	
}
