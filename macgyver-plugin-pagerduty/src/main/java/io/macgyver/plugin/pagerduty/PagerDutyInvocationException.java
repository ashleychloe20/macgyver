package io.macgyver.plugin.pagerduty;

import io.macgyver.core.ServiceInvocationException;

public class PagerDutyInvocationException extends ServiceInvocationException {

	public PagerDutyInvocationException() {
		super();
	}

	public PagerDutyInvocationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PagerDutyInvocationException(String arg0) {
		super(arg0);
	}

	public PagerDutyInvocationException(Throwable arg0) {
		super(arg0);
	}

}
