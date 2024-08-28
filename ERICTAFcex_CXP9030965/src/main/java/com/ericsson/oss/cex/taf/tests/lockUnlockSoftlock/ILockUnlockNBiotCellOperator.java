package com.ericsson.oss.cex.taf.tests.lockUnlockSoftlock;

public interface ILockUnlockNBiotCellOperator {
	
	String getNbiotCell(String cellType);
	
	String lockUnlockCell(String fdn,String operation);
	
	String verifyAdminState(String fdn,String operation);

}
