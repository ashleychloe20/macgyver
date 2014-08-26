package io.macgyver.core.web.vaadin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.macgyver.core.Kernel;
import io.macgyver.core.eventbus.MacGyverEventBus;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;

/**
 * MacGyverUIDecorator allows plugins to customize the MacGyver UI.  A single instance of each concrete subclass
 * should be registered as a spring bean.  When a given Vaadin UI instance is instantiated, the appropriate methods 
 * will be called.
 * 
 * @author rschoening
 *
 */
public abstract class MacGyverUIDecorator {

	static Logger logger = LoggerFactory.getLogger(MacGyverUIDecorator.class);
	
	public static abstract class AbstractUIEvent {

		MacGyverUI ui;
		
		public AbstractUIEvent(MacGyverUI ui) {
			Preconditions.checkNotNull(ui);
			this.ui = ui;
		}
		
		public MacGyverUI getUI() {
			return ui;
		}
	}
	public static class MacGyverUICreateEvent extends AbstractUIEvent {

		public MacGyverUICreateEvent(MacGyverUI ui) {
			super(ui);
		
		}


	}
	public static class MacGyverUIPostCreateEvent extends AbstractUIEvent {

		public MacGyverUIPostCreateEvent(MacGyverUI ui) {
			super(ui);
		
		}

	}
	
	@Subscribe
	public final void dispatchCreateEvent(MacGyverUICreateEvent createEvent) {
		LoggerFactory.getLogger(getClass()).info("dispatchCreateEvent");
		decorate(createEvent);
	}
	
	public abstract void decorate(MacGyverUICreateEvent createEvent);
	

	@Subscribe
	public final void dispatchPostCreateEvent(MacGyverUIPostCreateEvent postCreateEvent) {
		decorate(postCreateEvent);
	}
	
	public abstract void decorate(MacGyverUIPostCreateEvent event);
	
	public static void dispatch(MacGyverUICreateEvent event) {
		 MacGyverEventBus bus = Kernel.getInstance().getApplicationContext().getBean(MacGyverEventBus.class);
	        logger.info("post {}",event);
	        bus.post(event);
	}
	public static void dispatch(MacGyverUIPostCreateEvent event) {
		 MacGyverEventBus bus = Kernel.getInstance().getApplicationContext().getBean(MacGyverEventBus.class);
	        logger.info("post {}",event);
	        bus.post(event);
	}
}
