package com.ericsson.oss.cex.taf.tests.relations;

public interface IVerifyRelationsViewOperator {

	String getSourceCell(String nodeType, String sourceType);
	
	String getRelations(String sourceFdn, String relationType);
}
