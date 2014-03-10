package io.macgyver.core.script;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import com.google.common.collect.Sets;

/**
 * Exposes ApplicationContext as a Map
 * @author rschoening
 *
 */
public class SpringMapAdapter implements Map<String, Object> {

	protected ApplicationContext ctx;
	
	public SpringMapAdapter(ApplicationContext ctx) {
		this.ctx = ctx;
	}
	@Override
	public int size() {
		return ctx.getBeanDefinitionCount();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object get(Object key) {
		return ctx.getBean(key.toString());
	}

	@Override
	public Object put(String key, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> keySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<Object> values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return Sets.newHashSet();
		
	}

}
