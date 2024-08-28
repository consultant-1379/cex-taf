package com.ericsson.oss.cex.taf.ui.getters;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.oss.taf.hostconfigurator.HostGroup;
import com.ericsson.oss.taf.smhandler.SmtoolHandler;

public class SmToolTest extends TorTestCaseHelper implements TestCase{

	private static String managedCompnents = null;
	
	final SmtoolHandler smHandler = new SmtoolHandler(HostGroup.getOssmaster());

	/**
	 * @DESCRIPTION To test online the managed components 
	 * @Component MCs present in ManagedComponent.csv
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-xxx_Func_1", title = "Performing Online Operation for the given MCs.")
	@DataDriven(name = "ManagedComponent")
	@Context(context = {Context.API})
	@Test(priority = 1, groups = {"KGB", "CDB", "GAT", "Feature"})
	public void SmtoolOnlineTest(@Input("Component") String component){

		managedCompnents = component;

		assertTrue(smHandler.smtoolOnline(managedCompnents));

	}
	
	/**
	 * @DESCRIPTION To test offline the exceptions managed component
	 * @Component MCs present in ManagedComponent.csv
	 * @PRIORITY HIGH
	 *//*
	@TestId(id="OSS-xxx_Func_1", title = "Performing Offline Operation except given MCs.")
	@DataDriven(name = "ManagedComponent")
	@Context(context = {Context.API})
	@Test(priority = 2,groups = {"KGB", "CDB", "GAT", "Feature"})
	public void SmtoolExceptionTest(@Input("Component") String component){

		managedCompnents = component;

		assertTrue(smHandler.ExceptionMCsforOffline(managedCompnents));

	}*/
	
	/**
	 * @DESCRIPTION To test offline the managed components 
	 * @Component MCs present in ManagedComponent.csv
	 * @PRIORITY HIGH
	 */
	@AfterSuite
	public void SmtoolOfflineTest(){

		setTestStep("Performing Offline Operation for the given MCs.");

		assertTrue(smHandler.smtoolOffline(managedCompnents));

	}

}
