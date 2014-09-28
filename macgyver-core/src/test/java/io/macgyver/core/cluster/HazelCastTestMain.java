package io.macgyver.core.cluster;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.hazelcast.core.MemberAttributeEvent;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;

public class HazelCastTestMain {

	public static final void main(String[] args) {

		Config cfg = new Config();
		cfg.getGroupConfig().setName("macgyver");
		HazelcastInstance i = Hazelcast.newHazelcastInstance(cfg);

		MembershipListener ml = new MembershipListener() {

			@Override
			public void memberRemoved(MembershipEvent membershipEvent) {
				System.out.println("remove: " + membershipEvent);

			}

			@Override
			public void memberAttributeChanged(
					MemberAttributeEvent memberAttributeEvent) {
				// TODO Auto-generated method stub

			}

			@Override
			public void memberAdded(MembershipEvent membershipEvent) {
				System.out.println("add: " + membershipEvent);

			}
		};
		i.getCluster().addMembershipListener(ml);

		for (int j = 0; j < 9999999; j++) {
			i.getCluster().getLocalMember()
					.setLongAttribute("heartbeat", System.currentTimeMillis());
		
			try {
				Thread.sleep(1000L);
			}
			catch (Exception e ){}
		}
	}
}
