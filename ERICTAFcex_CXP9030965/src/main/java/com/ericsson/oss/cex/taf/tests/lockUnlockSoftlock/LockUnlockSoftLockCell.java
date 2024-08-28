package com.ericsson.oss.cex.taf.tests.lockUnlockSoftlock;

import java.util.List;

import javax.inject.Inject;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler.AdministrativeState;

public class LockUnlockSoftLockCell extends TorTestCaseHelper implements TestCase {

	private static final String RESULT = "OK";

	@Inject
	private OperatorRegistry<ILockUnlockSoftLockOperator> cEXProvider;


	/**
	 * @DESCRIPTION Verify that a user is able to lock EUtranCells from Common Explorer
	 * @PRE Common Explorer is online. An ERBS node is connected and synchronized with cells available
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-34806_Func_3", title = "Lock EUtranCell")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	public void lockEUtranCell() {
		final ILockUnlockSoftLockOperator operator = cEXProvider.provide(ILockUnlockSoftLockOperator.class);

		setTestStep("Getting fdn for a random cell.");
		String cellOId = operator.getEUtranCellOId();
		String fdn = operator.getCellFdn(cellOId);

		setTestStep("Getting present administrative state of the cell.");
		setTestStep("Performing lock operation on the cell.");
		final String result = operator.lockUnlockCell(cellOId, fdn, AdministrativeState.LOCKED);

		setTestInfo("Verifying the changes in CS database.");
		setTestInfo("Verifying the changes in CEX domain cache.");
		Assert.assertEquals(result, RESULT);
	}

	/**
	 * @DESCRIPTION Verify that a user is able to unlock EUtranCells from Common Explorer
	 * @PRE Common Explorer is online. An ERBS node is connected and synchronized with cells available
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-34806_Func_4", title = "Unlock EUtranCell")
	@Context(context = {Context.API})
	@Test(groups={"KGB", "CDB", "GAT", "Feature"})	
	public void unlockEUtranCell(){
		final ILockUnlockSoftLockOperator operator = cEXProvider.provide(ILockUnlockSoftLockOperator.class);

		setTestStep("Getting fdn for a random cell.");
		String cellOId = operator.getEUtranCellOId();
		String fdn = operator.getCellFdn(cellOId);

		setTestStep("Getting present administrative state of the cell.");
		setTestStep("Performing unlock operation on the cell.");
		final String result = operator.lockUnlockCell(cellOId, fdn, AdministrativeState.UNLOCKED);

		setTestInfo("Verifying the changes in CS database.");
		setTestInfo("Verifying the changes in CEX domain cache.");
		Assert.assertEquals(result, RESULT);
	}

	/**
	 * @DESCRIPTION Verify that a user is able to SoftLock EUtranCells from Common Explorer
	 * @PRE Common Explorer is online. An ERBS node is connected and synchronized with cells available
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-24702", title = "SoftLock EUtranCell")
	@Context(context = {Context.API})
	@Test(groups={"Feature"})
	public void softLockEUtranCell(){

		final ILockUnlockSoftLockOperator operator = cEXProvider.provide(ILockUnlockSoftLockOperator.class);

		setTestStep("Getting fdn for a random cell.");
		String cellOId = operator.getEUtranCellOId();
		String fdn = operator.getCellFdn(cellOId);

		setTestStep("Getting present administrative state of the cell.");
		setTestStep("Performing unlock operation on the cell.");
		final String result = operator.lockUnlockCell(cellOId, fdn, AdministrativeState.SHUTTING_DOWN);

		setTestInfo("Verifying the changes in CS database.");
		setTestInfo("Verifying the changes in CEX domain cache.");
		Assert.assertEquals(result, RESULT);
	}

	/**
	 * @DESCRIPTION Verify that a user is able to lock UtranCells from Common Explorer
	 * @PRE Common Explorer is online. A RNC node is connected and synchronized with cells available
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-34806_Func_1", title = "Lock Utrancell")
	@Context(context = {Context.API})
	@Test(groups={"KGB", "CDB", "GAT", "Feature"})
	public void lockUtranCell(){
		final ILockUnlockSoftLockOperator operator = cEXProvider.provide(ILockUnlockSoftLockOperator.class);

		setTestStep("Getting fdn for a random cell.");
		String cellOId = operator.getUtranCellOId();
		String fdn = operator.getCellFdn(cellOId);

		setTestStep("Getting present administrative state of the cell.");
		setTestStep("Performing lock operation on the cell.");
		final String result = operator.lockUnlockCell(cellOId, fdn, AdministrativeState.LOCKED);

		setTestInfo("Verifying the changes in CS database.");
		setTestInfo("Verifying the changes in CEX domain cache.");
		Assert.assertEquals(result, RESULT);
	}


	/**
	 * @DESCRIPTION Verify that a user is able to unlock UtranCells from Common Explorer
	 * @PRE Common Explorer is online. A RNC node is connected and synchronized with cells available
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-34806_Func_2", title = "Unlock Utrancell")
	@Context(context = {Context.API})
	@Test(groups={"KGB", "CDB", "GAT", "Feature"})
	public void unlockUtranCell(){
		final ILockUnlockSoftLockOperator operator = cEXProvider.provide(ILockUnlockSoftLockOperator.class);

		setTestStep("Getting fdn for a random cell.");
		String cellOId = operator.getUtranCellOId();
		String fdn = operator.getCellFdn(cellOId);

		setTestStep("Getting present administrative state of the cell.");
		setTestStep("Performing unlock operation on the cell.");
		final String result = operator.lockUnlockCell(cellOId, fdn, AdministrativeState.UNLOCKED);

		setTestInfo("Verifying the changes in CS database.");
		setTestInfo("Verifying the changes in CEX domain cache.");
		Assert.assertEquals(result, RESULT);
	}

	/**
	 * @DESCRIPTION Verify that a user is able to SoftLock UtranCells from Common Explorer
	 * @PRE Common Explorer is online. A RNC node is connected and synchronized with cells available
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-24701", title = "SoftLock UtranCell")
	@Context(context = {Context.API})
	@Test(groups={"Feature"})
	public void softLockUtranCell(){

		final ILockUnlockSoftLockOperator operator = cEXProvider.provide(ILockUnlockSoftLockOperator.class);

		setTestStep("Getting fdn for a random cell.");
		String cellOId = operator.getUtranCellOId();
		String fdn = operator.getCellFdn(cellOId);

		setTestStep("Getting present administrative state of the cell.");
		setTestStep("Performing unlock operation on the cell.");
		final String result = operator.lockUnlockCell(cellOId, fdn, AdministrativeState.SHUTTING_DOWN);

		setTestInfo("Verifying the changes in CS database.");
		setTestInfo("Verifying the changes in CEX domain cache.");
		Assert.assertEquals(result, RESULT);
	}
	/**
     * @DESCRIPTION Verify that a user is able to lock/unlock (E)RBS nodes from Common Explorer
     * @PRE Common Explorer is online. A (E)RBS node is connected and synchronized.
     * @PRIORITY HIGH
     */
	@DataDriven(name = "LockUnlockSite") 
	@Context(context = {Context.API})
	@Test(groups={"KGB", "CDB", "GAT", "Feature"})
	public void lockUnlockSite(@TestId @Input("TestId") String testId, @Input("ElementType") String elementType, @Input("Operation") String operation){
		final ILockUnlockSoftLockOperator operator = cEXProvider.provide(ILockUnlockSoftLockOperator.class);
				
		setTestcase(testId, operation + " " + elementType);
		
		setTestStep("Getting fdn for a random " + elementType + " and its cells.");
		final List<SiteDetails> rbsAndCellDetails = operator.getSiteAndCellDetails(elementType);
		assertNotNull(rbsAndCellDetails);
		
		final AdministrativeState adminState = AdministrativeState.getState(operation.toLowerCase());
		
		setTestStep("Checking if at least one of cells of the " + elementType + " is not " + adminState.name().toLowerCase() + ". Else modify the state of one of the cells.");
		assertTrue(operator.verifyAndModifySiteCellsState(adminState));
		
		setTestStep("Performing " + operation.toLowerCase() + " operation on " + elementType + ".");
		assertTrue(operator.lockUnlockRBS_ERBS(adminState));
		
		setTestInfo("Verifying the changes in CS database.");
		assertTrue(operator.verifyChangesInCS(adminState));
		
		setTestInfo("Verifying the changes in CEX domain cache.");
		assertTrue(operator.verifyChangesInCexDomain(adminState));
	}
}