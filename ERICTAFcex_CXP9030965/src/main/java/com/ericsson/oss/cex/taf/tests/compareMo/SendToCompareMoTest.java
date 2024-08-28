package com.ericsson.oss.cex.taf.tests.compareMo;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

public class SendToCompareMoTest extends TorTestCaseHelper implements TestCase{

	@Inject
	private OperatorRegistry<ISendToCompareMoOperator> cexProvider;

	/**
	 * @DESCRIPTION To Verify Send To CompareMo View Test ->KGB
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "SendToCompareMo")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void sendToCompareMoTest(
			@TestId @Input("TestID") String testId,
			@Input("CellName") String cellName){

		final ISendToCompareMoOperator operator = cexProvider.provide(ISendToCompareMoOperator.class);
		String cell = null;

		setTestcase(testId, " Send " + cellName + " to Compare Mo View");

		setTestStep("Getting a random  "+ cellName);
		cell = operator.cellFdn(cellName);
		assertNotNull(cell);

		setTestStep("Performing the operation...");
		assertEquals("OK", operator.sendCellToCompareMo(cell));

	}
	
	/**
	 * @DESCRIPTION To Verify Send To CompareMo View Test ->CDB
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "SendToCompareMoCDB")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	public void sendToCompareMoCDBTest(
			@TestId @Input("TestID") String testId, 
			@Input("CellName") String cellName){

		final ISendToCompareMoOperator operator = cexProvider.provide(ISendToCompareMoOperator.class);
		String cell = null;

		setTestcase(testId, " Send " + cellName + " to Compare Mo View");

		setTestStep("Getting a random  "+ cellName);
		cell = operator.cellFdn(cellName);
		assertNotNull(cell);

		setTestStep("Performing the operation...");
		assertEquals("OK", operator.sendCellToCompareMo(cell));

	}
}
