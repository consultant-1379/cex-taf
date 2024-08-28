package com.ericsson.oss.cex.taf.tests.lockUnlockSoftlock;


import javax.inject.Inject;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler.AdministrativeState;

public class LockUnlockSoftLockCellPlan extends TorTestCaseHelper implements TestCase {
	
	private static final String RESULT = "OK";

	@Inject
	private OperatorRegistry<ILockUnlockSoftLockOperator> cEXProvider;
	String planName =null;

	/**
	 * @DESCRIPTION Adding OptionalFeatureLicense with the help of Netsim API
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@BeforeTest
	@Context(context = {Context.API})
	public void createPlan(){
		
		final ILockUnlockSoftLockOperator operator = cEXProvider.provide(ILockUnlockSoftLockOperator.class);
		
		setTestStep(" Create Plan ");
		planName = operator.createPlan();
	
	}

	/**
	 * @DESCRIPTION Verify that a user is able to lock/unlock/softlock EUtranCells in Plan from Common Explorer
	 * @PRE Common Explorer is online. An ERBS node is connected and synchronized with cells available
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "LockUnlockPlan") 
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	public void lockUnlockSoftLockEUtranCellPlan(@TestId @Input("TestId") String testId, @Input("ElementType") String elementType, @Input("Operation") String operation) {
		
		final ILockUnlockSoftLockOperator operator = cEXProvider.provide(ILockUnlockSoftLockOperator.class);
		setTestStep("Getting fdn for a random " + elementType);
		String cellOId = operator.getEUtranCellOId();
		String fdn = operator.getCellFdn(cellOId);

		setTestStep("Performing "+operation+" operation on the cell.");
		
		final AdministrativeState adminState = AdministrativeState.getState(operation.toLowerCase());
		
		final String result = operator.lockUnlockInPlan(cellOId, fdn, adminState,planName);
		
		setTestInfo("Verifying the changes in CS database.");
		setTestInfo("Verifying the changes in CEX domain cache.");
		Assert.assertEquals(result, RESULT);
	}
	
	@AfterSuite
	public void deletePlan() {
		final ILockUnlockSoftLockOperator operator = cEXProvider.provide(ILockUnlockSoftLockOperator.class);
		
		setTestStep(" Delete Plan ");
		operator.deletePlan(planName);
		
	}

}
