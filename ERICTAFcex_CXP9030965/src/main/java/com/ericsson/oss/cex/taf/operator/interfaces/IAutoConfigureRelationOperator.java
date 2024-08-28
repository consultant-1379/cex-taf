package com.ericsson.oss.cex.taf.operator.interfaces;

public interface IAutoConfigureRelationOperator {

	String autoConfigureEUtranFreqRelation(String eutranfrequencyfdn,
			String eutrancellfddfdn);

	String autoConfigureCdma2000FreqRelation(String cdma2000Freqfdn,
			String eUtranCellFDDfdn);

	String autoConfigureCdma2000CellRelation(String cdma2000cellfdn,
			String eutrancellfddfdn);
	
	String autoConfigureECellFDDOperator();

	boolean utranFreqR_ECellFDD();

}
