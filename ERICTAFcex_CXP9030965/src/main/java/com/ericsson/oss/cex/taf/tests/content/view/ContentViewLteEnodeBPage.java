package com.ericsson.oss.cex.taf.tests.content.view;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.content.operator.IContentViewOperator;

public class ContentViewLteEnodeBPage extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<IContentViewOperator> cexProvider;

	/**
	 * @DESCRIPTION To verify that the object under test is correctly displayed in its appropriate page of the Content View.
	 * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@TestId(id = "OSS-43042_Func_1", title = "Verify the LTE EnodeB Page")
	@Context(context = {Context.API})
	@Test(groups={"KGB", "CDB", "GAT", "Feature"})
	public void verifyTheLTEEnodeBPage() {

		final IContentViewOperator operator = cexProvider.provide(IContentViewOperator.class);

		assertEquals("OK", operator.viewLteEnodeBPage());
	}


}
