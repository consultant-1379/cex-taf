package com.ericsson.oss.cex.taf.tests.selectionList.view;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.operator.interfaces.IAutoConfigureRelationOperator;

public class AutoConfEUtranCellR_ECellFDD extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<IAutoConfigureRelationOperator> cexProvider;

	/**
	 * @DESCRIPTION Verify successful creation of EUtranCellRelation on EUtranCellFDD
	 * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@TestId(id = "OSS-42926_Func_1", title = "Auto-Configure EUtranCellRelation on EUtranCellFDD")
	@Context(context = {Context.API})
	@Test(groups={"KGB", "CDB", "GAT", "Feature"})
	public void autoConfigureEUtranCellRelationOnEUtranCellFDD() {

		final IAutoConfigureRelationOperator operator = cexProvider.provide(IAutoConfigureRelationOperator.class);


		assertEquals(operator.autoConfigureECellFDDOperator(), "OK");
	}
}
