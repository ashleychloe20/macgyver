package io.macgyver.core;

import io.macgyver.core.crypto.Crypto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class TestBean {

	@Autowired
	Crypto crypto;
	
	String foo;

	public String getFoo() {
		return foo;
	}

	public void setFoo(String foo) {
		Kernel.getInstance().getApplicationContext();
		this.foo = foo;
	}

	public Crypto getCrypto() {
		return crypto;
	}

	public void setCrypto(Crypto crypto) {
		this.crypto = crypto;
	}
	
}
