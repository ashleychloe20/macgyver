package io.macgyver.core.eventbus;

import io.macgyver.core.ContextRefreshApplicationListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.eventbus.EventBus;

public class EventBusPostProcessor implements BeanPostProcessor,
		ApplicationContextAware {

	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	EventBus eventBus;

	private ApplicationContext ctx;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {

		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {

		if (bean!=this && !(bean instanceof EventBus) && !(bean instanceof ContextRefreshApplicationListener)) {
			Package p = bean.getClass().getPackage();
			
			if (p!=null) {
				log.info("registering spring bean {} with {}", beanName,
						eventBus);
				eventBus.register(bean);
			}
		}
		return bean;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;

	}

}