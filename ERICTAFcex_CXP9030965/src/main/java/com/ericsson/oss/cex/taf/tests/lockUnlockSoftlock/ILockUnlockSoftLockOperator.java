package com.ericsson.oss.cex.taf.tests.lockUnlockSoftlock;

import java.util.List;

import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler.AdministrativeState;

public interface ILockUnlockSoftLockOperator {

	String getUtranCellOId();
	
	String getCellFdn(String cellOId);
		
	String getEUtranCellOId();
	
	String createPlan();
	
	void deletePlan(String planName);
	
	String softLockRBS_ERBS(String fdn, String cells);
	
	String lockUnlockInPlan(String cellOId, String fdn, AdministrativeState resultAdminState,String planName);
	
	String lockUnlockCell(String cellOId, String fdn, AdministrativeState resultAdminState);
	
	List<SiteDetails> getSiteAndCellDetails(String elementType);
	
	String lockUnlockSSR(String operation);
	
	boolean verifyInCS(String nodeType, String operation);
	
	boolean lockUnlockRBS_ERBS(AdministrativeState resultAdminState);
	
	boolean verifyAndModifySiteCellsState(AdministrativeState resultAdminState);
	
	boolean verifyChangesInCS(AdministrativeState resultAdminState);
	
	boolean verifyChangesInCexDomain(AdministrativeState resultAdminState);
	
	void getRbs_ErbsList();
	
	String performMixedRBS_ERBS(String nodeType, String  operation);

	String performMultinodeMixedRBS_ERBS(String nodeType, String operation);
	
	String lockUnlocklteSSR(String operation);
	
	String lockUnlockrbsSSR(String operation);

}