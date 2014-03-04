package io.macgyver.web.rythm;

import javax.servlet.ServletContext;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

public class RythmViewResolver extends AbstractTemplateViewResolver {

	public RythmViewResolver() {
		setViewClass(requiredViewClass());
	}
// https://github.com/lawrence0819/spring-webmvc-rythm/blob/master/src/main/java/com/ctlok/springframework/web/servlet/view/rythm/RythmViewResolver.java

	@Override
	protected Class<?> requiredViewClass() {
		return RythmView.class;
	}
    @Override
    protected void initServletContext(ServletContext servletContext) {
        super.initServletContext(servletContext);
   
    }

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		// TODO Auto-generated method stub
		AbstractUrlBasedView v = super.buildView(viewName);
		System.out.println("URL: "+v.getUrl());
		return v;
	}
	
}
