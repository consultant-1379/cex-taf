package com.ericsson.oss.cex.taf.tests.createmo;

public interface ICreateExternalMo {

	String createExternalCell();

	String createExternalFreq(String elementType);

	String getCellFdn(String cellType);
	
	String setLinkValues(String elementType,String Type,String dlValue);

	String deleteExternalCell(String cellFdn);

}
