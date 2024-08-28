package com.ericsson.oss.cex.taf.tests.autoconfigure;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

@SuppressWarnings("deprecation")
public class AutoConfigureGeranCellRelationTest extends TorTestCaseHelper implements TestCase{
	
	@Inject
	private OperatorRegistry<IAutoConfigureRelationsOperator> cexProvider;

	/**
	 * @DESCRIPTION Verify Successful AutoConfigure Various Relations
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@TestId(id="TEST_ID_6", title = "AutoConfigure GeranCell Relation")
	@Context(context = {Context.API})
	@Test(groups={"KGB", "GAT", "Feature"})
	public void autoConfigureRelationsTest() {

		final IAutoConfigureRelationsOperator operator = cexProvider.provide(IAutoConfigureRelationsOperator.class);

		setTestStep("Getting EUtranCellFDD for MACRO from domain...");
		String sourceFdn = operator.getSourceFdn("MACRO", "EUtranCellFDD", "GeranCellRelation");
		assertNotNull(sourceFdn);
		
		setTestStep("Getting ExternalGSMFreqGroup from domain...");
		String frequencyFdn = operator.getTargetFdn("MACRO", "ExternalGSMFreqGroup");
		
		setTestInfo(" EUtranCellFDD Should be locked to release the on-going Relation");
		operator.performLocking(sourceFdn);
		
		String dependentRelations ="GeranFreqGroupRelation:GeranCellRelation";
		setTestInfo("Deleting " + dependentRelations + " Relations...");
		operator.deleteRelations(sourceFdn, dependentRelations);
		
		setTestInfo("Getting AdjacentRelation for " + sourceFdn);
		String relationType="GeranFreqGroupRelation";
		String adjacentRelation = operator.getAdjacentAttribute(sourceFdn, relationType,frequencyFdn);
		
		setTestInfo("Deleting AdjacentRelation for " + sourceFdn);
		assertTrue(operator.deleteAdjacentAtrribute(adjacentRelation));
		
		setTestStep("Performing Autoconfigure GeranFreqGroupRelation ...");
		assertEquals(operator.performAutoConfigure(sourceFdn, frequencyFdn, "GeranFreqGroupRelation"),"OK");
		
		setTestStep("Getting ExternalGSMCell from domain...");
		String targetFdn = operator.getGeranCell(frequencyFdn);
		assertNotNull(targetFdn);
		
		setTestStep("Performing Autoconfigure GeranCellRelation ...");
		assertEquals(operator.performAutoConfigure(sourceFdn, targetFdn, "GeranCellRelation"),"OK");

	}
	
}
