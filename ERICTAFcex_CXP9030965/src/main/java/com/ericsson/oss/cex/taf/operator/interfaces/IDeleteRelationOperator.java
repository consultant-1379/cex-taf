package com.ericsson.oss.cex.taf.operator.interfaces;

public interface IDeleteRelationOperator {

	String deleteCdma2000FreqRelation(String eutrancellfddfdn);

	String deleteCdma2000CellRelation(String eutrancellfddfdn);

	String deleteEUtranCellRelation(String eutrancellfddfdn);

}
