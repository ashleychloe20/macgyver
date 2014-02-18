package io.macgyver.core.script;

import io.macgyver.core.Kernel;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.base.Optional;
import com.google.common.io.Closer;
import com.google.common.io.Files;

public class ScriptExecutor implements ApplicationContextAware {

	Logger logger = LoggerFactory.getLogger(ScriptExecutor.class);
	Bindings bindings = new SimpleBindings();

	ApplicationContext applicationContext;
	Kernel kernel;

	ScriptException evalException;
	Object evalResult;
	ScriptContext scriptContext;

	StringWriter outWriter;
	StringWriter errWriter;

	public ScriptExecutor() {
		this.applicationContext = Kernel.getInstance().getApplicationContext();
		this.kernel = Kernel.getInstance();
	}

	public String getStandardOutputString() {
		return outWriter.toString();
	}

	public String getStandardErrorString() {
		return errWriter.toString();
	}

	public void collectBindings(Bindings b, Optional<String> lang) {

		b.put("kernel", Kernel.getInstance());
		b.put("ctx", Kernel.getInstance().getApplicationContext());
		b.put("log", LoggerFactory.getLogger("io.macgyver.script"));
		b.put("beans", new SpringMapAdapter(Kernel.getInstance()
				.getApplicationContext()));

		BindingSupplierManager bsm = Kernel.getInstance()
				.getApplicationContext().getBean(BindingSupplierManager.class);
		Map<String, Object> m = bsm.collect(lang);
		b.putAll(m);

	}

	public Object eval(String script, String language) throws ScriptException {
		evalException = null;
		evalResult = null;
		try {
			ScriptEngineManager factory = new ScriptEngineManager();
			ScriptEngine engine = factory.getEngineByName(language);

			scriptContext = engine.getContext();
			outWriter = new StringWriter();
			errWriter = new StringWriter();
			scriptContext.setWriter(outWriter);
			scriptContext.setErrorWriter(errWriter);

			Bindings bindings = new SimpleBindings();
			collectBindings(bindings, Optional.fromNullable(engine.getFactory().getLanguageName()));

			engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

			Object response = engine.eval(script);

	
			return response;
		} catch (ScriptException e) {
			this.evalException = e;
			throw e;
		}

	}

	public void run(String arg) {
		run(arg, null, true);
	}
	public boolean isSupportedScript(File f) {
		try {
			ScriptEngineManager m = new ScriptEngineManager();
			String extension = Files.getFileExtension(f.getName());
			ScriptEngine engine = m.getEngineByExtension(extension);

			return engine != null;
		} catch (Exception e) {
			return false;
		}

	}
	public void run(String arg, Map<String, Object> vars, boolean failIfNotFound) {
		File scriptDir = new File(kernel.getExtensionDir(), "scripts");

		File f = new File(scriptDir, arg);

		run(f, vars, failIfNotFound);
	}

	public void run(File f, Map<String, Object> vars, boolean failIfNotFound) {
		Closer closer = Closer.create();

		try {

			logger.info("script {}", f.getAbsolutePath());
			if (!f.exists() && !failIfNotFound) {
				logger.info("init script not found: {}", f.getAbsolutePath());
				return;
			}
			ScriptEngineManager factory = new ScriptEngineManager();
			ScriptEngine engine = factory.getEngineByExtension(Files
					.getFileExtension(f.getPath()));

			if (engine == null) {
				throw new ScriptExecutionException(
						"could not create ScriptEngine for extension: "
								+ Files.getFileExtension(f.getPath()));
			}

			ApplicationContext ctx = Kernel.getInstance()
					.getApplicationContext();

			FileReader fr = new FileReader(f);
			closer.register(fr);

			if (vars != null) {
				bindings.putAll(vars);
			}

			collectBindings(bindings, Optional.fromNullable(engine.getFactory().getLanguageName()));

			logger.info("invoking script");
			Object val = engine.eval(fr, bindings);

			fr.close();

		} catch (IOException e) {
			throw new ScriptExecutionException(e);
		} catch (ScriptException e) {
			throw new ScriptExecutionException(e);
		} catch (RuntimeException e) {
			throw new ScriptExecutionException(e);
		} finally {
			try {
				closer.close();
			} catch (IOException e) {
				logger.warn("problem closing reader", e);
			}

		}
	}

	public Bindings getBindings() {
		return bindings;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		throw new IllegalStateException(
				"ScriptExecutor cannot be a spring bean");

	}

}
