package com.ericsson.oss.cex.taf.tests.lockUnlockSoftlock;

import java.util.List;

import javax.inject.Inject;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.ui.getters.CexCsHandler.AdministrativeState;

public class LockUnlockSoftlockMixedNodes extends TorTestCaseHelper implements TestCase {

	private static final String RESULT = "OK";
	@Inject
	private OperatorRegistry<ILockUnlockSoftlockMixedOperator> newNodeOperatorProvider;

	@Inject
	private OperatorRegistry<ILockUnlockSoftLockOperator> lockUnlockProvider;
	
	/**
	 * Setting up the RBS_ERBS list for mixedRBS_ERBS Testcase
	 */
	@BeforeTest
	@Context(context = { Context.API })
	public void getRbsErbsFdn(){

		final ILockUnlockSoftLockOperator operator = lockUnlockProvider.provide(ILockUnlockSoftLockOperator.class);
		
		setTestcase("OSS-63414_Func", "Getting RBS_ERBS node List");
		operator.getRbs_ErbsList();
	}
	/**
	 * @DESCRIPTION Verify that a user is able to lock mixed RBS/ERBS nodes from Common Explorer
	 * @PRE Common Explorer is online. A RBS node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "LockUnlockMixedNode") 
	@Context(context = {Context.API})
	@Test(groups={"KGB", "Feature"})
	public void mixedRBS_ERBS(
			@Input("TestId") String testId,
			@Input("NodeType") String nodeType,
			@Input("Operation") String operation){

		final ILockUnlockSoftLockOperator operator = lockUnlockProvider.provide(ILockUnlockSoftLockOperator.class);

		setTestcase(testId, operation + " " + nodeType);
		
		assertEquals(operator.performMixedRBS_ERBS(nodeType, operation), RESULT);
	}
	/**
	 * @DESCRIPTION Verify that a user is able to lock mixed RBS/ERBS Multiple nodes from Common Explorer
	 * @PRE Common Explorer is online. A RBS node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "LockUnlockMultiMixedNode") 
	@Context(context = {Context.API})
	@Test(groups={"KGB", "Feature"})
	public void multiNodemixedRBS_ERBS(
			@Input("TestId") String testId,
			@Input("NodeType") String nodeType,
			@Input("Operation") String operation){

		final ILockUnlockSoftLockOperator operator = lockUnlockProvider.provide(ILockUnlockSoftLockOperator.class);

		setTestcase(testId, operation + " " + nodeType);
		
		assertEquals(operator.performMultinodeMixedRBS_ERBS(nodeType, operation), RESULT);
	}
	/**
	 * @DESCRIPTION Verify that a user is able to lock/unlock PICO cell from Common Explorer
	 * @PRE Common Explorer is online. PICO node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "LockUnlockCell")
	@Context(context = { Context.API })
	@Test(groups = { "KGB","Feature" })
	public void lockUnlockSoftlockPico(@Input("TestId") String testId, @Input("NodeType") String nodeType, @Input("Operation") String operation){

		final ILockUnlockSoftlockMixedOperator newNodeoperator = newNodeOperatorProvider.provide(ILockUnlockSoftlockMixedOperator.class);
		final ILockUnlockSoftLockOperator lockUnlockoperator = lockUnlockProvider.provide(ILockUnlockSoftLockOperator.class);

		setTestcase(testId, operation + " " + nodeType);

		setTestStep("Getting fdn for a random " + nodeType + " and its cells.");
		String cellOId = newNodeoperator.getPicoCellOId(nodeType);
		assertNotNull(cellOId);
		String fdn = newNodeoperator.getCellFdn(cellOId);
		assertNotNull(fdn);

		final AdministrativeState adminState = AdministrativeState.getState(operation.toLowerCase());

		setTestStep("Performing " + operation + " operation on the cell.");
		final String result = lockUnlockoperator.lockUnlockCell(cellOId, fdn, adminState);

		setTestInfo("Verifed the changes in CS database & CEX domain cache.");
		Assert.assertEquals(result, RESULT);
	}

	/**
	 * @DESCRIPTION Verify that a user is able to lock/unlock PICO (E)RBS nodes from Common Explorer
	 * @PRE Common Explorer is online. A PICO (E)RBS node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "LockUnlockNode") 
	@Context(context = {Context.API})
	@Test(groups={"KGB", "Feature"})
	public void lockUnlockSitePico(@Input("TestId") String testId, @Input("NodeType") String nodeType, @Input("Operation") String operation){

		final ILockUnlockSoftLockOperator lockUnlockoperator = lockUnlockProvider.provide(ILockUnlockSoftLockOperator.class);
		final ILockUnlockSoftlockMixedOperator newNodeoperator = newNodeOperatorProvider.provide(ILockUnlockSoftlockMixedOperator.class);

		setTestcase(testId, operation + " " + nodeType);

		setTestStep("Getting fdn for a random " + nodeType + " and its cells.");
		final List<SiteDetails> rbsAndCellDetails = newNodeoperator.getSiteAndCellDetails(nodeType);
		assertNotNull(rbsAndCellDetails);

		final AdministrativeState adminState = AdministrativeState.getState(operation.toLowerCase());

		setTestStep("Checking if at least one of cells of the " + nodeType + " is not " + adminState.name().toLowerCase() + ". Else modify the state of one of the cells.");
		assertTrue(lockUnlockoperator.verifyAndModifySiteCellsState(adminState));

		setTestStep("Performing " + operation.toLowerCase() + " operation on " + nodeType + ".");
		assertTrue(lockUnlockoperator.lockUnlockRBS_ERBS(adminState));

		setTestInfo("Verifying the changes in CS database.");
		assertTrue(lockUnlockoperator.verifyChangesInCS(adminState));

		setTestInfo("Verifying the changes in CEX domain cache.");
		assertTrue(lockUnlockoperator.verifyChangesInCexDomain(adminState));
	}
	
	/**
	 * @DESCRIPTION Verify that a user is able to lock/unlock MSRBS_V2 cell from Common Explorer
	 * @PRE Common Explorer is online. MSRBS_V2 node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "LockUnlockSsrCell")
	@Context(context = { Context.API })
	@Test(groups = { "KGB","Feature" })
	public void lockUnlockSoftlockSsr(@Input("TestId") String testId, @Input("NodeType") String nodeType, @Input("Operation") String operation){

		final ILockUnlockSoftlockMixedOperator newNodeoperator = newNodeOperatorProvider.provide(ILockUnlockSoftlockMixedOperator.class);
		final ILockUnlockSoftLockOperator lockUnlockoperator = lockUnlockProvider.provide(ILockUnlockSoftLockOperator.class);

		setTestcase(testId, operation + " " + nodeType);

		setTestStep("Getting fdn for a random " + nodeType + " and its cells.");
		String cellOId = newNodeoperator.getMsrbs_v2CellOId(nodeType);
		assertNotNull(cellOId);
		String fdn = newNodeoperator.getCellFdn(cellOId);
		assertNotNull(fdn);

		final AdministrativeState adminState = AdministrativeState.getState(operation.toLowerCase());

		setTestStep("Performing " + operation + " operation on the cell.");
		final String result = lockUnlockoperator.lockUnlockCell(cellOId, fdn, adminState);

		setTestInfo("Verifying the changes in CS database.");
		setTestInfo("Verifying the changes in CEX domain cache.");
		assertEquals(result, RESULT);
	}
	
	/**
	 * @DESCRIPTION Verify that a user is able to lock/unlock MSRBS_V2 node from Common Explorer
	 * @PRE Common Explorer is online. MSRBS_V2 node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "LockUnlockErbs_RbsSsrCell")
	@Context(context = { Context.API })
	@Test(groups = { "KGB","Feature" })
	public void lockUnlockSoftlockErbs_RbsSSR(@Input("TestId") String testId, @Input("NodeType") String nodeType, @Input("Operation") String operation){
		
		final ILockUnlockSoftLockOperator operator = lockUnlockProvider.provide(ILockUnlockSoftLockOperator.class);

		setTestcase(testId, operation + " " + nodeType);
		
		assertEquals(operator.lockUnlockSSR(operation), RESULT);
		
		setTestInfo("Verifed the changes in CS database & CEX domain cache.");
				
		assertTrue(operator.verifyInCS(nodeType,operation)); 


	}
	/**
	 * @DESCRIPTION Verify that a user is able to lock/unlock MSRBS_V2 Multiple node from Common Explorer
	 * @PRE Common Explorer is online. MSRBS_V2 node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "LockUnlockRbsSSR")
	@Context(context = { Context.API })
	@Test(groups = { "KGB","Feature" })
	public void lockUnlockSoftlockrbs(@Input("TestId") String testId, @Input("NodeType") String nodeType, @Input("Operation") String operation){
		
		final ILockUnlockSoftLockOperator operator = lockUnlockProvider.provide(ILockUnlockSoftLockOperator.class);

		setTestcase(testId, operation + " " + nodeType);
		
		assertEquals(operator.lockUnlockrbsSSR(operation), RESULT);

	}
	/**
	 * @DESCRIPTION Verify that a user is able to lock/unlock MSRBS_V2 Multiple node from Common Explorer
	 * @PRE Common Explorer is online. MSRBS_V2 node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "LockUnlocklteSSR")
	@Context(context = { Context.API })
	@Test(groups = { "KGB","Feature" })
	public void lockUnlockSoftlockErbs(@Input("TestId") String testId, @Input("NodeType") String nodeType, @Input("Operation") String operation){
		
		final ILockUnlockSoftLockOperator operator = lockUnlockProvider.provide(ILockUnlockSoftLockOperator.class);

		setTestcase(testId, operation + " " + nodeType);
		
		assertEquals(operator.lockUnlocklteSSR(operation), RESULT);
		
		//setTestInfo("Verifed the changes in CS database & CEX domain cache.");
				
		//assertTrue(operator.verifyInCS(nodeType,operation)); 


	}
	
	/**
	 * @DESCRIPTION Verify that a user is able to lock/unlock MSRBS_V2 (E)RBS nodes from Common Explorer
	 * @PRE Common Explorer is online. A MSRBS_V2 (E)RBS node is connected and synchronized.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "LockUnlockSsrNode") 
	@Context(context = {Context.API})
	@Test(groups={"KGB", "Feature"})
	public void lockUnlockSiteSsr(@Input("TestId") String testId, @Input("NodeType") String nodeType, @Input("Operation") String operation){

		final ILockUnlockSoftLockOperator lockUnlockoperator = lockUnlockProvider.provide(ILockUnlockSoftLockOperator.class);
		final ILockUnlockSoftlockMixedOperator newNodeoperator = newNodeOperatorProvider.provide(ILockUnlockSoftlockMixedOperator.class);

		setTestcase(testId, operation + " " + nodeType);

		setTestStep("Getting fdn for a random " + nodeType + " and its cells.");
		final List<SiteDetails> msrbsAndCellDetails = newNodeoperator.getSiteAndCellDetailsMSRBS_V2(nodeType);
		assertNotNull(msrbsAndCellDetails);

		final AdministrativeState adminState = AdministrativeState.getState(operation.toLowerCase());

		setTestStep("Checking if at least one of cells of the " + nodeType + " is not " + adminState.name().toLowerCase() + ". Else modify the state of one of the cells.");
		assertTrue(lockUnlockoperator.verifyAndModifySiteCellsState(adminState));

		setTestStep("Performing " + operation.toLowerCase() + " operation on " + nodeType + ".");
		assertTrue(lockUnlockoperator.lockUnlockRBS_ERBS(adminState));

		setTestInfo("Verifying the changes in CS database.");
		assertTrue(lockUnlockoperator.verifyChangesInCS(adminState));

		setTestInfo("Verifying the changes in CEX domain cache.");
		assertTrue(lockUnlockoperator.verifyChangesInCexDomain(adminState));
	}
}
