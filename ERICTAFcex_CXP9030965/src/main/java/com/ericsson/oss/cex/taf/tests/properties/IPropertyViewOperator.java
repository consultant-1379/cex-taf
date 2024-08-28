package com.ericsson.oss.cex.taf.tests.properties;

import java.util.List;


public interface IPropertyViewOperator {

	String checkIsDL();
	
	String viewRBSAttributes();
	
	List<String> getFDNPropertyListFromCS(String fdn);
	
//	String checkRBSUtranCellType();

}
