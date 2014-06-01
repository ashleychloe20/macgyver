package io.macgyver.core.script;

import io.macgyver.core.Kernel;
import io.macgyver.core.VfsManager;
import io.macgyver.core.service.ServiceRegistry;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.apache.commons.vfs2.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.io.Closer;
import com.tinkerpop.blueprints.TransactionalGraph;

public class ScriptExecutor implements ApplicationContextAware {

	Logger logger = LoggerFactory.getLogger(ScriptExecutor.class);
	Bindings bindings = new SimpleBindings();
	
	
	ScriptException evalException;
	Object evalResult;
	ScriptContext scriptContext;

	StringWriter outWriter;
	StringWriter errWriter;

	public ScriptExecutor() {

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
		b.put("services",
				Kernel.getInstance().getApplicationContext()
						.getBean(ServiceRegistry.class).mapAdapter());
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
			collectBindings(bindings, Optional.fromNullable(engine.getFactory()
					.getLanguageName()));

			engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

			Object response = engine.eval(script);

			return response;
		} catch (ScriptException e) {
			this.evalException = e;
			Kernel.getInstance().getApplicationContext().getBean(TransactionalGraph.class).rollback();
			throw e;
		} finally {
			Kernel.getInstance().getApplicationContext().getBean(TransactionalGraph.class).commit();
		}

	}

	public Object run(String arg) throws IOException{
		return run(arg, null, true);
	}

	public boolean isSupportedScript(FileObject f) {
		try {
			ScriptEngineManager m = new ScriptEngineManager();
			String extension = getExtension(f);
			ScriptEngine engine = m.getEngineByExtension(extension);

			return engine != null;
		} catch (Exception e) {
			return false;
		}

	}

	public Object run(String arg, Map<String, Object> vars,
			boolean failIfNotFound) throws IOException {
		
		FileObject fo = Kernel.getInstance().getApplicationContext().getBean(VfsManager.class).getScriptsLocation();
		
		FileObject script = fo.resolveFile(arg);
		
		
		return run(script, vars, failIfNotFound);
	}

	String getExtension(FileObject fo) {
		Preconditions.checkNotNull(fo);
		int idx = fo.toString().lastIndexOf(".");
		return fo.toString().substring(idx + 1);
	}

	public Object run(FileObject f, Map<String, Object> vars,
			boolean failIfNotFound) {
		Closer closer = Closer.create();
		Object rval = null;
		try {

			logger.info("script {}", f);
			if (!f.exists() && !failIfNotFound) {
				logger.info("init script not found: {}", f);
				return null;
			}
			ScriptEngineManager factory = new ScriptEngineManager();
			ScriptEngine engine = factory.getEngineByExtension(getExtension(f));

			if (engine == null) {
				throw new ScriptExecutionException(
						"could not create ScriptEngine for extension: "
								+ getExtension(f));
			}

			ApplicationContext ctx = Kernel.getInstance()
					.getApplicationContext();

			Reader fr = new InputStreamReader(f.getContent().getInputStream());
			closer.register(fr);

			if (vars != null) {
				bindings.putAll(vars);
			}

			collectBindings(bindings, Optional.fromNullable(engine.getFactory()
					.getLanguageName()));

			rval = engine.eval(fr, bindings);

			fr.close();

		} catch (IOException e) {
			Kernel.getInstance().getApplicationContext().getBean(TransactionalGraph.class).commit();
			throw new ScriptExecutionException(e);
		} catch (ScriptException e) {
			Kernel.getInstance().getApplicationContext().getBean(TransactionalGraph.class).commit();
			throw new ScriptExecutionException(e);
		} catch (RuntimeException e) {
			Kernel.getInstance().getApplicationContext().getBean(TransactionalGraph.class).rollback();
			throw new ScriptExecutionException(e);
		} finally {
			try {
				closer.close();
			} catch (IOException e) {
				logger.warn("problem closing reader", e);
			}
			Kernel.getInstance().getApplicationContext().getBean(TransactionalGraph.class).commit();

		}
		return rval;
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
