package io.macgyver.http.spark;

import org.springframework.context.ApplicationContext;

import io.macgyver.core.Kernel;
import spark.servlet.SparkApplication;

public abstract class Sparklet implements SparkApplication {

	protected Kernel getKernel() {
		return Kernel.getInstance();
	}
	
	protected ApplicationContext getApplicationContext() {
		return getKernel().getApplicationContext();
	}
}
