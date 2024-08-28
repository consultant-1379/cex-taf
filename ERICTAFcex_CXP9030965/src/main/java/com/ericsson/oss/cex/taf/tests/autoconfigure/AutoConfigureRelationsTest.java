package com.ericsson.oss.cex.taf.tests.autoconfigure;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

public class AutoConfigureRelationsTest extends TorTestCaseHelper implements TestCase{

	@Inject
	private OperatorRegistry<IAutoConfigureRelationsOperator> cexProvider;

	/**
	 * @DESCRIPTION Verify Successful AutoConfigure Various Relations
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "AutoConfigureRelations") 
	@Context(context = {Context.API})
	@Test(groups={"KGB", "GAT", "Feature"})
	public void autoConfigureRelationsTest(@TestId @Input("TestId") String testId,
			@Input("NodeType") String nodeType,
			@Input("SourceType") String sourceType,
			@Input("TargetType") String targetType,
			@Input("RelationType") String relationType,
			@Input("DependentRelations") String dependentRelations) {

		final IAutoConfigureRelationsOperator operator = cexProvider.provide(IAutoConfigureRelationsOperator.class);

		setTestcase(testId, "AutoConfigure " + relationType + " for Source " + sourceType + " & Target " + targetType + " ,NodeType = "+ nodeType);

		setTestStep("Getting " + sourceType + " For " + nodeType + " from domain...");
		String sourceFdn = operator.getSourceFdn(nodeType, sourceType, relationType);
		assertNotNull(sourceFdn);
		
		setTestStep("Getting " + targetType + " For " + nodeType + " from domain...");
		String targetFdn = operator.getTargetFdn(nodeType, targetType);
		assertNotNull(targetFdn);
		
		setTestInfo(sourceType + " Should be locked to release the on-going Relation");
		operator.performLocking(sourceFdn);
		
		setTestInfo("Deleting " + dependentRelations + " Relations...");
		operator.deleteRelations(sourceFdn, dependentRelations);
		
		setTestInfo("Getting AdjacentRelation for " + sourceFdn);
		String adjacentRelation = operator.getAdjacentAttribute(sourceFdn, relationType,targetFdn);
		
		setTestInfo("Deleting AdjacentRelation for " + sourceFdn);
		assertTrue(operator.deleteAdjacentAtrribute(adjacentRelation));
		
		setTestStep("Performing AutoConfigure for " + relationType + "...");
		assertEquals(operator.performAutoConfigure(sourceFdn, targetFdn, relationType),"OK");
		
		setTestStep("Verifing Relation created or not in domain cache...");
		assertNotNull(operator.verifyAutoConfigureRelation(sourceFdn, relationType,targetFdn));
	}
	
	/**
	 * @DESCRIPTION Verify Successful AutoConfigure Various Relations
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "AutoConfigureRelations_PLM") 
	@Context(context = {Context.API})
	@Test(groups={"KGB", "GAT", "Feature"})
	public void autoConfigureRelations_PLMTest(@TestId @Input("TestId") String testId,
			@Input("NodeType") String nodeType,
			@Input("SourceType") String sourceType,
			@Input("TargetType") String targetType,
			@Input("RelationType") String relationType,
			@Input("DependentRelations") String dependentRelations) {

		final IAutoConfigureRelationsOperator operator = cexProvider.provide(IAutoConfigureRelationsOperator.class);

		setTestcase(testId, "AutoConfigure " + relationType + " for Source " + sourceType + " & Target " + targetType + " ,NodeType = "+ nodeType);

		setTestStep("Getting " + sourceType + " For " + nodeType + " from domain...");
		String sourceFdn = operator.getSourceFdn(nodeType, sourceType, relationType);
		assertNotNull(sourceFdn);
		
		setTestStep("Getting " + targetType + " For " + nodeType + " from domain...");
		String targetFdn = operator.getTargetFdn(nodeType, targetType);
		assertNotNull(targetFdn);
		
		setTestInfo(sourceType + " Should be locked to release the on-going Relation");
		operator.performLocking(sourceFdn);
		
		setTestInfo("Deleting " + dependentRelations + " Relations...");
		operator.deleteRelations(sourceFdn, dependentRelations);
		
		setTestInfo("Getting AdjacentRelation for " + sourceFdn);
		String adjacentRelation = operator.getAdjacentAttribute(sourceFdn, relationType,targetFdn);
		
		setTestInfo("Deleting AdjacentRelation for " + sourceFdn);
		assertTrue(operator.deleteAdjacentAtrribute(adjacentRelation));
		
		setTestStep("Performing AutoConfigure for " + relationType + "...");
		assertEquals(operator.performAutoConfigure(sourceFdn, targetFdn, relationType),"OK");
		
		setTestStep("Verifing Relation created or not in domain cache...");
		assertNotNull(operator.verifyAutoConfigureRelation(sourceFdn, relationType,targetFdn));
	}
	
	
	/**
	 * @DESCRIPTION Verify Successful AutoConfigure Various Relations
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "AutoConfigureRelations_CDB") 
	@Context(context = {Context.API})
	@Test(groups={"KGB", "GAT", "Feature"})
	public void autoConfigureRelations_CDBTest(@TestId @Input("TestId") String testId,
			@Input("NodeType") String nodeType,
			@Input("SourceType") String sourceType,
			@Input("TargetType") String targetType,
			@Input("RelationType") String relationType,
			@Input("DependentRelations") String dependentRelations) {

		final IAutoConfigureRelationsOperator operator = cexProvider.provide(IAutoConfigureRelationsOperator.class);

		setTestcase(testId, "AutoConfigure " + relationType + " for Source " + sourceType + " & Target " + targetType + " ,NodeType = "+ nodeType);

		setTestStep("Getting " + sourceType + " For " + nodeType + " from domain...");
		String sourceFdn = operator.getSourceFdn(nodeType, sourceType, relationType);
		assertNotNull(sourceFdn);
		
		setTestStep("Getting " + targetType + " For " + nodeType + " from domain...");
		String targetFdn = operator.getTargetFdn(nodeType, targetType);
		assertNotNull(targetFdn);
		
		setTestInfo(sourceType + " Should be locked to release the on-going Relation");
		operator.performLocking(sourceFdn);
		
		setTestInfo("Deleting " + dependentRelations + " Relations...");
		operator.deleteRelations(sourceFdn, dependentRelations);
		
		setTestInfo("Getting AdjacentRelation for " + sourceFdn);
		String adjacentRelation = operator.getAdjacentAttribute(sourceFdn, relationType,targetFdn);
		
		setTestInfo("Deleting AdjacentRelation for " + sourceFdn);
		assertTrue(operator.deleteAdjacentAtrribute(adjacentRelation));
		
		setTestStep("Performing AutoConfigure for " + relationType + "...");
		assertEquals(operator.performAutoConfigure(sourceFdn, targetFdn, relationType),"OK");
		
		setTestStep("Verifing Relation created or not in domain cache...");
		assertNotNull(operator.verifyAutoConfigureRelation(sourceFdn, relationType,targetFdn));
	}
}
