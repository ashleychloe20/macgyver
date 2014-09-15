package io.macgyver.core.web.vaadin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.bouncycastle.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.navigator.View;


public class ViewMetadata {

	static Logger logger = LoggerFactory.getLogger(ViewMetadata.class);

	String viewName;
	String [] menuPath;

	public ViewMetadata(String viewName, String [] menuPath) {
		
		
		this.viewName = viewName;
		this.menuPath = menuPath!=null ? menuPath : new String[0];
	}



	public String getViewName() {
		return viewName;
	}


	public String [] getMenuPath() {
		
		return menuPath;
	}

	public static Optional<ViewMetadata> forView(Class<? extends View> v) {
		
		ViewConfig cfg = v.getAnnotation(ViewConfig.class);
		if (cfg!=null) {
			String viewName = cfg.viewName();
			String [] menuPath = cfg.menuPath();
		
			ViewMetadata vmd = new ViewMetadata(viewName, menuPath);
			return Optional.of(vmd);
		}
		
		return Optional.absent();
	}
}
