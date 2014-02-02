package io.macgyver.jdbc;

import java.util.Map;

import com.google.common.collect.Maps;

import groovy.text.GStringTemplateEngine;

public class TemplateTest {

	
	public static void main(String [] args) throws Exception {
		GStringTemplateEngine eng = new GStringTemplateEngine();
		Map<String,String> x = Maps.newHashMap();
		x.put("blah", "x");
		eng.createTemplate("<% for (int i=0; i<10; i++) {%>${i}<%}%>").make(x).toString();
		
		
	}
}
