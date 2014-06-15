package io.macgyver.plugin.elb.a10;

import io.macgyver.plugin.elb.ElbException;

public class A10RemoteException extends ElbException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7177057268162853156L;
	String code;
	String msg;

	public A10RemoteException(String code, String msg) {
		super(code + ": " + msg);
		this.code = code;
		this.msg = msg;
	}

	public String getErrorCode() {
		return code;
	}

	public String getErrorMessage() {
		return msg;
	}

}
