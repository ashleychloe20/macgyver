package io.macgyver.core;

public class MacGyverException extends RuntimeException {


	private static final long serialVersionUID = -8657450988916494626L;

	public MacGyverException() {
		super();
		
	}

	public MacGyverException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

	public MacGyverException(String arg0) {
		super(arg0);
		
	}

	public MacGyverException(Throwable arg0) {
		super(arg0);
		
	}

}
