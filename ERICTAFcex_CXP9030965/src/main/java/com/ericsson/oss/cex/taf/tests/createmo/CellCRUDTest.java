package com.ericsson.oss.cex.taf.tests.createmo;

import javax.inject.Inject;

import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

public class CellCRUDTest extends TorTestCaseHelper implements TestCase {
	
	/**
	 * class for test case Create/Delete EutranCellFDD/TDD
	 * @author xharsja
	 *
	 */

	@Inject
	private OperatorRegistry<ICellCRUDOperator> cexOperator;	

	/**
	 * @DESCRIPTION Verify that a user is able to create cell for RNC $ ERBS from Common Explorer
	 * @PRE Common Explorer is online. A node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "CELL_CRUD_FILE")
	@Context(context = { Context.API })
	@Test(groups = { "KGB","Feature" })
	public void cellCreateTest(@Input("TestId") String testId, 
			@Input("NodeType") String nodeType, 
			@Input("Operation") String operation){

		final ICellCRUDOperator operator = cexOperator.provide(ICellCRUDOperator.class);

		setTestcase(testId, operation +" cell for " + nodeType);

		setTestStep("Performing " + operation + " operation on "+ nodeType);

		boolean result = operator.createDeleteCell(nodeType, operation);

		assertTrue(result);
	}
	/**
	 * @DESCRIPTION Verify that a user is able to create cell for RNC $ ERBS from Common Explorer
	 * @PRE Common Explorer is online. A node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	 @DataDriven(name = "CELL_CRUD_FILE_18B")
	@Context(context = { Context.API })
	@Test(groups = { "KGB","Feature" })
	public void cellCreate18B(@Input("TestId") String testId, 
			@Input("NodeType") String nodeType, 
			@Input("Operation") String operation){

		final ICellCRUDOperator operator = cexOperator.provide(ICellCRUDOperator.class);

		setTestcase(testId, operation +" cell for " + nodeType);

		setTestStep("Performing " + operation + " operation on "+ nodeType);

		boolean result = operator.createDeleteCell(nodeType, operation);

		assertTrue(result);
	}
	/**
	 * @DESCRIPTION Verify that a user is able to create cell for RNC $ ERBS from Common Explorer
	 * @PRE Common Explorer is online. A node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "CELL_CRUD_FILE_PLM")
	@Context(context = { Context.API })
	@Test(groups = { "KGB","Feature" })
	public void cellCreatePLMTest(@Input("TestId") String testId, 
			@Input("NodeType") String nodeType, 
			@Input("Operation") String operation){

		final ICellCRUDOperator operator = cexOperator.provide(ICellCRUDOperator.class);

		setTestcase(testId, operation +" cell for " + nodeType);

		setTestStep("Performing " + operation + " operation on "+ nodeType);

		boolean result = operator.createDeleteCell(nodeType, operation);

		assertTrue(result);
	}
	@AfterTest
	public void cleanUp(){
			
		final ICellCRUDOperator operator = cexOperator.provide(ICellCRUDOperator.class);
		
		setTestcase("OSS-24704", "Cleaning all the created Ecell");
		
		operator.cleanUpEUtranCell();
	}

}
