package com.ericsson.oss.cex.taf.tests.son;

public interface IActivateDeactivateFeatureOperator {
	
	String getRequiredFdn(String nodeType);

	boolean activateDeactivateFeature(String requireFdn,String nodeType, String operationType, String featureType);
	
	boolean verifyChangeInCS(String requireFdn,String nodeType,String operationType,String featureType);

}
