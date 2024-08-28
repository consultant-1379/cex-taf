package com.ericsson.oss.cex.taf.tests.lockUnlockSoftlock;

import java.util.List;


public interface ILockUnlockSoftlockMixedOperator {
	
	boolean getErbsAllNodes(); 
	
	boolean performSelectedOperation(String nodeType, String operation);

//	boolean gettingDataFromDatabse(String nodeType);
	
	boolean verifyCountMatching(String nodeType);
	
	String getPicoCellOId(String NodeType);
	
	String getCellFdn(String cellOId);

	List<SiteDetails> getSiteAndCellDetails(String nodeType);

	String getMsrbs_v2CellOId(String nodeType);

	List<SiteDetails> getSiteAndCellDetailsMSRBS_V2(String nodeType);
	
}
