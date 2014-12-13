package io.macgyver.plugin.cloud.vsphere.cmdb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.vim25.ManagedObjectReference;

import io.macgyver.plugin.cloud.vsphere.cmdb.VSphereScanner;

public class VSphereScannerTest {

	ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testCreateSetClause() {
		VSphereScanner scanner = new VSphereScanner();

		String clause = scanner.createSetClause("x", mapper.createObjectNode()
				.put("a", "1").put("foo", "bar"));

		assertThat(clause).contains("x.a={a}").contains("x.foo={foo}")
				.contains(",");

	}

	@Test
	public void testComputeMacId() {
		ManagedObjectReference mor = new ManagedObjectReference();
		mor.setType("HostSystem");
		mor.setVal("host-123");

		VSphereScanner s = Mockito.mock(VSphereScanner.class);
		when(s.getVCenterId()).thenReturn("abcdef");
		Mockito.when(s.computeMacId(mor)).thenCallRealMethod();

		assertThat(mor.getType()).isEqualTo("HostSystem");
		assertThat(s.computeMacId(mor)).isEqualTo(
				"21b23eae3d48797d8d057329705825e637e35d81");

		VSphereScanner s2 = Mockito.mock(VSphereScanner.class);

		when(s2.getVCenterId()).thenReturn("another");
		Mockito.when(s2.computeMacId(mor)).thenCallRealMethod();
		assertThat(s.computeMacId(mor)).isNotEqualTo(s2.computeMacId(mor));

		try {
			new VSphereScanner().computeMacId(null);
		} catch (Exception e) {
			assertThat(e)
					.isExactlyInstanceOf(NullPointerException.class)
					.hasMessageContaining("cannot be null");
		}

		mor = new ManagedObjectReference();
		mor.setType("VirtualMachine");
		mor.setVal("vm-123");
		try {
			new VSphereScanner().computeMacId(mor);
			fail();
		} catch (Exception e) {
			assertThat(e).isInstanceOf(
					IllegalArgumentException.class);
		}

	}
}
