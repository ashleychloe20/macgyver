package io.macgyver.core.eventbus;

import java.util.concurrent.Executor;

import com.google.common.eventbus.AsyncEventBus;

public class MacGyverAsyncEventBus extends AsyncEventBus {

	public MacGyverAsyncEventBus(Executor executor) {
		super(executor);
		
	}

	public MacGyverAsyncEventBus(String identifier, Executor executor) {
		super(identifier, executor);
	
	}

}
