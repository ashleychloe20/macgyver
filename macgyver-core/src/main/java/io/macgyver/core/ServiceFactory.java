package io.macgyver.core;

public interface  ServiceFactory<T> {

	public abstract T get();
}
