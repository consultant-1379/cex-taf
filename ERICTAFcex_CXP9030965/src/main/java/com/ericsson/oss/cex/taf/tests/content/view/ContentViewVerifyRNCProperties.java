package com.ericsson.oss.cex.taf.tests.content.view;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.content.operator.IContentViewOperator;

public class ContentViewVerifyRNCProperties extends TorTestCaseHelper implements TestCase {


	@Inject
	private OperatorRegistry<IContentViewOperator> cexProvider;
	/**
	 * @DESCRIPTION To verify that the Properties view is correctly displayed when the RNC is selected from the Topology view
	 * @PRE : CEx MC is online. CEx is online. RNC’s and RBS’s are connected and synchronised. CEx client is opened.
	 * @PRIORITY HIGH
	 */
	@TestId(id = "OSS-43045_Func_1", title = "Verify properties for RNC selected in the Topology")
	@Context(context = {Context.API})
	@Test(groups={"KGB", "CDB", "GAT", "Feature"})
	public void verifyPropertiesForRNCSelectedInTheTopology() {

		final IContentViewOperator operator = cexProvider.provide(IContentViewOperator.class);
		assertEquals("OK",operator.viewRNCProperties());
	}



}
