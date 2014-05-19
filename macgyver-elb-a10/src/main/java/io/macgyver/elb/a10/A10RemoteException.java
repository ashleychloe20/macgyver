package io.macgyver.elb.a10;


import io.macgyver.elb.ElbException;

public class A10RemoteException extends ElbException {

	String code;
	String msg;
	public A10RemoteException(String code, String msg) {
		super(code+": "+msg);
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
