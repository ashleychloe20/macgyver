package io.macgyver.core.auth;

import java.util.List;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.ConsensusBased;

public class MacGyverAccessDecisionManager extends ConsensusBased {


	public MacGyverAccessDecisionManager(List<AccessDecisionVoter> decisionVoters) {
		super(decisionVoters);
		super.setAllowIfAllAbstainDecisions(false);
		super.setAllowIfEqualGrantedDeniedDecisions(false);
	}



}
