package com.ericsson.oss.cex.taf.tests.properties;

public interface IVerifyPropertiesFeatureOperator {
	
	boolean getIMSNode(String nodeType);
	
	String getRequiredFdn(String cellType, String functionType, String nodeType);

	boolean propertiesMatcher(String cellFdn, String propertyName);
	
	boolean nodeMatcher(String requiredFdn, String moType);
}
