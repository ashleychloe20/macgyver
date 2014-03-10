package io.macgyver.core.service;


import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Exposes Services as a Map
 * @author rschoening
 *
 */
public class ServiceMapAdapter implements Map<String, Object> {

	protected ServiceRegistry ctx;
	
	public ServiceMapAdapter(ServiceRegistry ctx) {
		this.ctx = ctx;
	}
	@Override
	public int size() {
		return ctx.instances.size();
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
		return ctx.get(key.toString());
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
