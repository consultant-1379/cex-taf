package com.ericsson.oss.cex.taf.tests.son;

public interface ISonFeaturesOperator {

	String getRequiredFdn(String nodeType,String groupType);

	boolean activateDeactivateSon(String requireFdn,String nodeType, String operationType, String featureType);
	
	boolean verifyChangeInCS(String requireFdn,String nodeType,String operationType,String featureType);
}
