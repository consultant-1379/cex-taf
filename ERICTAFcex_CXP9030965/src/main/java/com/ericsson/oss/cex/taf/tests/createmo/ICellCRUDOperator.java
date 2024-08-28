package com.ericsson.oss.cex.taf.tests.createmo;

public interface ICellCRUDOperator {


	boolean createDeleteCell(String nodeType, String operation);

	void cleanUpEUtranCell();
}
