package com.ericsson.oss.cex.taf.tests.lockUnlockSoftlock;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;


@SuppressWarnings("deprecation")
public class LockUnlockNBiotCell extends TorTestCaseHelper implements TestCase {
	
	private static final String RESULT = "OK";
	@Inject
	private OperatorRegistry<ILockUnlockNBiotCellOperator> cEXProvider;
	
	/**
	 * @DESCRIPTION Verify that a user is able to lock EUtranCells from Common Explorer
	 * @PRE Common Explorer is online. An ERBS node is connected and synchronized with cells available
	 * @PRIORITY HIGH
	 */

	@DataDriven(name = "lockUnlockNbiotCell")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	public void lockUnlockNbiotCell(@TestId @Input("TestID") String testId,
			@Input("NodeType") String nodeType,
			@Input("CellType") String cellType,
			@Input("OperationType") String operationType) {
		
		final ILockUnlockNBiotCellOperator operator = cEXProvider.provide(ILockUnlockNBiotCellOperator.class);

		setTestStep("Getting fdn for a random cell.");
		
		String fdn = operator.getNbiotCell(cellType);
		
		setTestStep("Performing lock operation on the cell.");
		assertEquals(operator.lockUnlockCell(fdn, operationType),RESULT);
		
		
		setTestStep("Verifying the changes in CS database.");
		assertEquals(operator.verifyAdminState(fdn,operationType),RESULT);

	}

}
