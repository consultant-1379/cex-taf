package com.ericsson.oss.cex.taf.tests.pcp;

import java.io.IOException;

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

public class CreateProfileUsingConfProMgr extends TorTestCaseHelper implements TestCase {

	private final String PASSED = "OK";

	@Inject
	private OperatorRegistry<IPcpProfileOperator> cexProvider;

	/**
	 * @DESCRIPTION To verify that creation of profile in Configuration Profiles Manager
	 * @PRE : CEx MC is online. CEx is online. 
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-43037_Func_1", title = "creation of profile in Configuration Profiles Manager")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	@DataDriven(name = "PCP_PROFILE")
	public void CreateProfileUsingConfProMgrTest(@Input("NAME") String name, @Input("DESCRIPTION") String desc) throws IOException {

		final IPcpProfileOperator operator = cexProvider.provide(IPcpProfileOperator.class);

		String actualResult= operator.createCexProfilePCP(name, desc);

		Assert.assertEquals(PASSED, actualResult);
	}

	/**
	 * @DESCRIPTION To verify of profile in Configuration Profiles Manager
	 * @PRE : CEx MC is online. CEx is online. 
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-43037_Func_2", title = "verify of profile in Configuration Profiles Manager")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	@DataDriven(name = "PCP_PROFILE")
	public void verifyProfileUsingConfProMgrTest(@Input("NAME") String name) throws IOException {

		final IPcpProfileOperator operator = cexProvider.provide(IPcpProfileOperator.class);

		setTestStep("Verifying PCP");

		String actualResult= operator.verifyCexProfilePCP(name);
		Assert.assertEquals(PASSED, actualResult);
	}

}
