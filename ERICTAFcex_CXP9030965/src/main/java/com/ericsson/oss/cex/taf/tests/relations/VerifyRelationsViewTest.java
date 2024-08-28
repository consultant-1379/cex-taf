package com.ericsson.oss.cex.taf.tests.relations;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

public class VerifyRelationsViewTest extends TorTestCaseHelper implements TestCase{

	@Inject
	private OperatorRegistry<IVerifyRelationsViewOperator> cexProvider;

	/**
	 * @DESCRIPTION Verify Relations View for various relations
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "VerifyRelationsView") 
	@Context(context = {Context.API})
	@Test(groups={"KGB", "GAT", "Feature"})
	public void verifyVariousRelationsView(@TestId @Input("TestId") String testId,
			@Input("RelationType") String relationType,
			@Input("NodeType") String nodeType,
			@Input("SourceType") String sourceType){
		
		final IVerifyRelationsViewOperator operator = cexProvider.provide(IVerifyRelationsViewOperator.class);

		setTestcase(testId, "Verify " + relationType + " for Source: " + sourceType + " ,Type="+ nodeType);
		String sourceFdn = operator.getSourceCell(nodeType, sourceType);
		assertNotNull(sourceFdn);
		
		setTestStep("Getting " + relationType + " List...");
		assertEquals(operator.getRelations(sourceFdn, relationType), "OK");
	}
	
	/**
	 * @DESCRIPTION Verify Geran Freq Relations View
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@TestId(id = "OSS-42947_Func_1", title = "Geran Freq Group Relation Test")
	@Context(context = {Context.API})
	@Test(groups={"KGB", "CDB", "Feature"})
	public void verifyGeranFreqRelationsView(){
		
		final IVerifyRelationsViewOperator operator = cexProvider.provide(IVerifyRelationsViewOperator.class);
		
		String nodeType = "MACRO";
		String sourceType = "EUtranCellFDD";
		String relationType = "Cdma2000FreqBandRelation";
		
		String sourceFdn = operator.getSourceCell(nodeType, sourceType);
		assertNotNull(sourceFdn);
		
		setTestStep("Getting " + relationType + " List...");
		assertEquals(operator.getRelations(sourceFdn, relationType), "OK");
	}
}
