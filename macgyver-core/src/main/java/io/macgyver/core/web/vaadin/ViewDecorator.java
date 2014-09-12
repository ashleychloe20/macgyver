package io.macgyver.core.web.vaadin;

import org.springframework.core.Ordered;

import com.vaadin.navigator.View;

public abstract class ViewDecorator implements Ordered{

	public abstract boolean supports(View view);
	
	public final void decorate(View view) {
		if (view!=null && supports(view)) {
			doDecorate(view);
		}
	}
	public abstract void doDecorate(View view);
	
	public int getOrder() {
		return 0;
	}
	
}
