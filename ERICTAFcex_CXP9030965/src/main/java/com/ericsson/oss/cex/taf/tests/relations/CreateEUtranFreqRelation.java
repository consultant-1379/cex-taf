package com.ericsson.oss.cex.taf.tests.relations;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.relations.operator.IRelationsOperator;

public class CreateEUtranFreqRelation extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<IRelationsOperator> provider;

	/**
	 * @DESCRIPTION To Verify the Creation of EUtranFreqRelation
	 * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-42909_Func_1", title = "Creating EUtranFreq Relation")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	public void testEUtranFreqRelation(){

		final IRelationsOperator operator = provider.provide(IRelationsOperator.class);

		String actualResult = operator.createEUtranFreqRelation();

		assertEquals("OK", actualResult);
	}
	
	/**
	 * @DESCRIPTION To Verify the Creation of EUtranFreqRelation with frequency does not exist
	 * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-42909_Func_1", title = "Creating EUtranFreq Relation With Frequency Does Not Exist")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	public void testNegativeEUtranFreqRelation(){

		final IRelationsOperator operator = provider.provide(IRelationsOperator.class);

		String actualResult = operator.createNegEUtranFreqRelation();

		assertEquals("OK", actualResult);
	}

}
