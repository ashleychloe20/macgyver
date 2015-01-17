package io.macgyver.core.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.thoughtworks.proxy.factory.CglibProxyFactory;
import com.thoughtworks.proxy.toys.hotswap.HotSwapping;
import com.thoughtworks.proxy.toys.hotswap.HotSwapping.HotSwappingWith;
import com.thoughtworks.proxy.toys.hotswap.Swappable;

public class HotSwapProxyTest {

	public static class Foo {
		
		String val;
		
		public Foo(String v) {
			this.val = v;
		}
		
		public String getBar() {
			return val;
		}
	}
	@Test
	public void testIt() {
		
		Foo f1 = new Foo("1");
		
		Foo f2 = new Foo("2");
		
		Foo fx = HotSwapping.proxy(Foo.class).with(null).build(new CglibProxyFactory());
		
		Swappable.class.cast(fx).hotswap(f1);
		Assertions.assertThat(fx.getBar()).isEqualTo("1");
		Swappable.class.cast(fx).hotswap(f2);
		Assertions.assertThat(fx.getBar()).isEqualTo("2");
	}
}
