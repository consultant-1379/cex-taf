package com.ericsson.oss.cex.taf.tests.relations;

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
import com.ericsson.oss.cex.taf.operator.interfaces.ICreateCellRelationOperator;

public class UtranRelationTest extends TorTestCaseHelper implements TestCase {


	@Inject
	private OperatorRegistry<ICreateCellRelationOperator> cexProvider;
	
	/**
	 * @DESCRIPTION To Verify the Creation of Utran Cell that has Relation
	 * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-42910_Func_1", title = "Create UtranCell Relation")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	public void testUtranRelation(){

		final ICreateCellRelationOperator operator = cexProvider.provide(ICreateCellRelationOperator.class);

		String result = operator.utranRelationOperator();

		Assert.assertEquals("OK", result);
	}

	/**
	 * @DESCRIPTION To Verify the Creation of UtranCell Relation with Wrong Cell Type
	 * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@TestId(id="OSS-42910_Func_1", title = "Create UtranCell Relation with Wrong Cell Type")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	public void testNegUtranCellRelation(){

		final ICreateCellRelationOperator operator = cexProvider.provide(ICreateCellRelationOperator.class);

		String result = operator.negUtranRelationOperator();

		Assert.assertEquals("OK", result);
	}
	
	/**
	 * @DESCRIPTION To Verify the Creation of Various Relation with  @Utrancell 
	 * @PRE : CEx MC is online. CEx client has been launched and the Content View is open. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "UtrancellRelations") 
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void relationTestOnUtranfeature(
			@Input("TestID") String testId, 
			@Input("ElementSourceType") String elementSourceType, 
			@Input("ElementTargetType") String elementTargetType, 
			@Input("RelationType") String relationType,
			@Input("SourceCell") String sourceCell, 
			@Input("TargetCell") String targetCell,
			@Input("ExternalFreq") String externalFreq){

		final ICreateCellRelationOperator operator = cexProvider.provide(ICreateCellRelationOperator.class);

		setTestcase(testId, relationType + " " + "on "+ elementSourceType  + " "+ "with" + " " +elementTargetType);

		setTestStep("Getting Source " + sourceCell + " List from Domain.");
		assertNotNull(operator.getSouceCellList(elementSourceType));

		setTestStep("Getting Target " + targetCell + " List from Domain.");
		assertNotNull(operator.getTargetCellList(elementTargetType));

		setTestStep("Getting Source " + sourceCell + " from selected " +elementSourceType+ " Source");
		assertNotNull(operator.getSourceCell());

		setTestStep("Getting Target " + targetCell + " from selected " +elementTargetType+ " Source");
		assertNotNull(operator.getTargetCell());

		setTestStep("Deleting existing  " + relationType + " from " + "Source");
		assertNotNull(operator.deleteExRelation(relationType));

		//Not Require to create externalFreq for Freq Relations
		if(externalFreq != null){        
			setTestStep("Creating " + externalFreq + " for source cell");
			operator.createExternalFreq(externalFreq, "UtranCell");
		}
		setTestStep("Creating " + relationType + " on source");
		assertEquals(operator.createNewRelation(relationType),"OK");
	}
}
