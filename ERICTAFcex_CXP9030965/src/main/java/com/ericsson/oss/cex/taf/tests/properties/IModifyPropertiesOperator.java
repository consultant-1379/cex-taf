package com.ericsson.oss.cex.taf.tests.properties;

public interface IModifyPropertiesOperator {

	String UpdateUtranCellName();
	
	String UpdateUtranCellNegative();
	
	String UpdateEUtranCellName();
	
	String UpdateRangeNegative();
	
	String getRbsFdn();
	
	String UpdateRncName();
	
	String UpdateMgwName();
	
	String UpdateRbsName(String rbsFdn);
	
	String ResetRbsName(String rbsFdn);
	
	String UpdateMgwBulkName();
}
