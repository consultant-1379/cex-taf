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

public class EditProfileUsingConfProMgr extends TorTestCaseHelper implements TestCase {

	private final String PASSED = "OK";
	@Inject
	private OperatorRegistry<IPcpProfileOperator> cexProvider;

	/**
	 * @DESCRIPTION To verify that editing/Deleting of profile in Configuration Profiles Manager
	 * @PRE : CEx MC is online. CEx is online. 
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-43039_Func_1", title = "Verify editing/Deleting of profile in Configuration Profiles Manager")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	@DataDriven(name = "PCP_PROFILE")
	public void editProfileUsingConfProMgr(@Input("NAME") String name) throws IOException {

		final IPcpProfileOperator operator = cexProvider.provide(IPcpProfileOperator.class);

		String actualResult= operator.editCexProfilePCP(name);

		Assert.assertEquals(PASSED, actualResult);
	}

}
