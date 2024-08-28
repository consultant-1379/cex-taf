package com.ericsson.oss.cex.taf.tests.mobrowser.view;

import javax.inject.Inject;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.operator.interfaces.ICDBOperators;

public class RequestActionTest extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<ICDBOperators> cexProvider;


	/**
	 * @DESCRIPTION To Verify the Request Action from Mo Browser For IpOam MO
	 * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */


	@TestId(id = "OSS-42901_Func_1", title = "Perform Request Action Form MO Browser")
	@Context(context = {Context.API})
	@Test(groups={"KGB", "CDB", "GAT", "Feature"})
	public void performRequestActionFormMOBrowser() {

		final ICDBOperators operator = cexProvider.provide(ICDBOperators.class);

		String actualResult= operator.CexMoBrowserRequestAction();

		Assert.assertEquals("OK", actualResult);
	}



}
