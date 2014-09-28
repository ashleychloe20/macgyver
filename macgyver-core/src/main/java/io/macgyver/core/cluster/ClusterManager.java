package io.macgyver.core.cluster;

import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicReference;
import com.hazelcast.core.ILock;
import com.hazelcast.core.Member;
import com.hazelcast.core.MemberAttributeEvent;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;

public class ClusterManager {

	Logger logger = LoggerFactory.getLogger(ClusterManager.class);

	@Autowired
	HazelcastInstance hazelcast;

	Timer timer;

	IAtomicReference<String> masterUuidRef;

	public HazelcastInstance getHazelcastInstance() {
		return hazelcast;
	}

	@PostConstruct
	public void register() {
		hazelcast.getCluster().addMembershipListener(new ClusterListener());

		timer = new Timer("heartbeat", true);

		timer.schedule(new HeartbeatTask(), 0L, 5000L);

		masterUuidRef = hazelcast.getAtomicReference("macMasterUuid");
	}

	public class HeartbeatTask extends TimerTask {

		@Override
		public void run() {
			try {
				processHeartbeat();
			} catch (Exception e) {
				logger.warn("heartbeat error", e);
			}
		}

	}

	public class ClusterListener implements MembershipListener {

		@Override
		public void memberAdded(MembershipEvent membershipEvent) {
			logger.info("memberAdded: {}", membershipEvent);

		}

		@Override
		public void memberRemoved(MembershipEvent membershipEvent) {
			logger.info("memberRemoved: {}", membershipEvent);

		}

		@Override
		public void memberAttributeChanged(
				MemberAttributeEvent memberAttributeEvent) {
			// this is a bit noisy

		}

	}

	public void processHeartbeat() {

		logger.debug("heartbeat!");

		Member localMember = hazelcast.getCluster().getLocalMember();
	
		localMember.setLongAttribute("heartbeat", System.currentTimeMillis());
		String myUuid = localMember.getUuid();
		String masterUuid = masterUuidRef.get();
		boolean foundLiveMaster = false;
		if (masterUuid==null) {
			foundLiveMaster=false;
		}
		else if (myUuid.equals(masterUuid)) {
			foundLiveMaster=true;
		}
		else {
			
			for (Member m: hazelcast.getCluster().getMembers()) {
				String otherUuid = m.getUuid();
				if (otherUuid.equals(masterUuid)) {
					foundLiveMaster=true;
				}
				
			}
		}
		
		if (foundLiveMaster) {
			logger.info("found master: {}",masterUuid);
		}
		else {
			logger.info("master not found");
			electMaster(masterUuid);
		}
		
		
	}
	
	
	public void electMaster(String existingUuid) {
		String myUuid = hazelcast.getCluster().getLocalMember().getUuid();
		boolean result = masterUuidRef.compareAndSet(existingUuid, myUuid);
		
	}
	public boolean isMaster(String uuid) {
		String masterUuid = masterUuidRef.get();
		if (masterUuid==null || uuid==null) {
			return false;
		}
		return masterUuid.equals(uuid);
	}
	public boolean isMaster() {
		String masterUuid = masterUuidRef.get();
		
		String myUuid = hazelcast.getCluster().getLocalMember().getUuid();
		
		if (masterUuid == null) {
			return false;
		}
		else {
			return masterUuid.equals(myUuid);
		}
	}

}
