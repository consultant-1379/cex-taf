package com.ericsson.oss.cex.taf.tests.deleteMo;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.relations.operator.IRelationsOperator;

public class DeleteUtranCellRelation extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<IRelationsOperator> cEXProvider;

	/**
	 * @DESCRIPTION To verify Utran relations can be deleted in both Planned and Valid Areas
	 * @PRE CEx is online. Cex client is launched. Nodes for all supported domainsa are connected and synchronized. UtranRelations exist.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-42911_Func_1", title = "Delete All UtranCellRelation from Random EUtranCellFDD")
	@Context(context = {Context.API})
	@Test(groups={"Feature"})
	public void deleteUtranCellRelation(){

		IRelationsOperator operator = cEXProvider.provide(IRelationsOperator.class);

		assertTrue(operator.deleteUtranCellRelation());
	}	
}
