package com.ericsson.oss.cex.taf.relations.operator;

public interface IRelationsOperator {

	String createEUtranFreqRelation();
	
	String createNegEUtranFreqRelation();
	
	boolean deleteUtranCellRelation();
}
