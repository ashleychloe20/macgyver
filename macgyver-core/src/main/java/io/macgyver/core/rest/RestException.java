package io.macgyver.core.rest;

import io.macgyver.core.ServiceInvocationException;

public class RestException extends ServiceInvocationException {

	int responseCode=0;
	public RestException() {
		super();
		
	}

	public RestException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

	public RestException(String arg0) {
		super(arg0);

	}

	public RestException(Throwable arg0) {
		super(arg0);
		
	}
	
	public RestException(int responseCode) {
		super("HTTP responseCode="+responseCode);
		this.responseCode = responseCode;
	}
	
	int getResponseCode() {
		return responseCode;
	}
}
