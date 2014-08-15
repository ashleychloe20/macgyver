package io.macgyver.ssh;

import java.io.IOException;

public class SshException extends IOException {

	
	int exitStatus=0;
	String errorText;
	
	public SshException(int exitStatus, String errorText) {
		super("rc="+exitStatus +"error='"+((errorText!=null) ? errorText : "")+"'");
		this.exitStatus = exitStatus;
		this.errorText = errorText;
	}
}
