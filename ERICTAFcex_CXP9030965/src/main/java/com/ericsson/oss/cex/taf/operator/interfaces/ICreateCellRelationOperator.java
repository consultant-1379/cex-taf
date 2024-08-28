package com.ericsson.oss.cex.taf.operator.interfaces;

public interface ICreateCellRelationOperator {

	String utranRelationOperator();
	
	String negUtranRelationOperator();

	String createExternalFreq(String elementType,String Type);
	
	String createNewRelation(String relationType);

	String getSouceCellList(String elementSourceType);
	
	String getTargetCellList(String elementTargetType);

	String getSourceCell();

	String getTargetCell();

	String deleteExRelation(String relationType);

}
