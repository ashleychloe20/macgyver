package io.macgyver.core.script;

import java.util.Map;

import com.google.common.base.Optional;

public interface BindingSupplier {

	public void collect(Map<String, Object> offerBindings, Optional<String> lang);
}
