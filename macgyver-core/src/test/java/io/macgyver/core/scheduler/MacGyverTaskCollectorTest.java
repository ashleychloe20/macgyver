package io.macgyver.core.scheduler;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MacGyverTaskCollectorTest {

	@Test
	public void testIt() {
		MacGyverTaskCollector c = new MacGyverTaskCollector();
		Assertions.assertThat(c.enhanceCronExpression("* * * * *")).isEqualTo("* * * * *");
		Assertions.assertThat(c.enhanceCronExpression("* * * * * ?")).isEqualTo("* * * * *");
		Assertions.assertThat(c.enhanceCronExpression("1 2 3 4 5 ?")).isEqualTo("2 3 4 5 *");
	}
}
