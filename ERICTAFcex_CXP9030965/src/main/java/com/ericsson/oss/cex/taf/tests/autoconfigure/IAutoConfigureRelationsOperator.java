package com.ericsson.oss.cex.taf.tests.autoconfigure;

public interface IAutoConfigureRelationsOperator {

	/**
     * Perform AutoConfigure on various source & target.
     *
     * @param AutoConfigure
     *   
     * @return Result else Exception.
     */
	String performAutoConfigure(String sourceFdn, String targetFdn, String relationType);

	String getSourceFdn(String nodeType, String sourceType, String relationType);
	
	String getTargetFdn(String nodeType, String targetType);
	
	void performLocking(String sourceFdn);
	
	String  getAdjacentAttribute(String sourceFdn, String relationType, String targetFdn);
	
	boolean deleteAdjacentAtrribute(String adjacentRelation);
	
	void getRelations(String sourceFdn, String relationType);
	
	void deleteRelations(String sourceFdn, String relationType);
	
	String verifyAutoConfigureRelation(String sourceFdn, String relationType, String targetFdn);
	
	String getGeranCell(String targetFdn);
	
	String createGeranCellRelation(String sourceFdn, String targetFdn, String relationType);
	
}
