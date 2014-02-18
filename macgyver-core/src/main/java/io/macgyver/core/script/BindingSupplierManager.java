package io.macgyver.core.script;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

public class BindingSupplierManager implements BindingSupplier{

	@Autowired
	ApplicationContext applicationContext;
	
	Logger logger = LoggerFactory.getLogger(BindingSupplierManager.class);
	
	public void collect(Map<String, Object> c, Optional<String> lang) {
		
		Map<String,BindingSupplier> map = applicationContext.getBeansOfType(BindingSupplier.class);
		for (Map.Entry<String, BindingSupplier> m: map.entrySet()) {
			try {
				if (m.getValue()!=this) {
					logger.trace("obtaining bindings from supplier: {}",m.getKey());
					m.getValue().collect(c,lang);
				}
				
				
			}
			catch (RuntimeException e) {
				logger.warn("problem processing binding supplier",e);
			}
		}
	}
	
	public Map<String,Object> collect(Optional<String> lang) {
		Map<String,Object> m = Maps.newConcurrentMap();
		collect(m,lang);
		return m;
	}
}
