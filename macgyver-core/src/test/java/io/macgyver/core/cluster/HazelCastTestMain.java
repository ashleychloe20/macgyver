/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.core.cluster;

import org.jboss.logging.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.hazelcast.core.MemberAttributeEvent;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;

public class HazelCastTestMain {

	static org.slf4j.Logger logger = LoggerFactory
			.getLogger(HazelCastTestMain.class);
	
	public static final void main(String[] args) {

		
		Config cfg = new Config();
		cfg.getGroupConfig().setName("macgyver");
		HazelcastInstance i = Hazelcast.newHazelcastInstance(cfg);

		MembershipListener ml = new MembershipListener() {

			@Override
			public void memberRemoved(MembershipEvent membershipEvent) {
				logger.debug("remove: " + membershipEvent);

			}

			@Override
			public void memberAttributeChanged(
					MemberAttributeEvent memberAttributeEvent) {
				// TODO Auto-generated method stub

			}

			@Override
			public void memberAdded(MembershipEvent membershipEvent) {
				logger.debug("add: " + membershipEvent);

			}
		};
		i.getCluster().addMembershipListener(ml);

		for (int j = 0; j < 9999999; j++) {
			i.getCluster().getLocalMember()
					.setLongAttribute("heartbeat", System.currentTimeMillis());

			try {
				Thread.sleep(1000L);
			} catch (Exception e) {
			}
		}
	}
}
