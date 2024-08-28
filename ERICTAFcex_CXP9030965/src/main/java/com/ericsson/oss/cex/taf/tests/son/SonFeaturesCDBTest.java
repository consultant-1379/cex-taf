package com.ericsson.oss.cex.taf.tests.son;

import javax.inject.Inject;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

public class SonFeaturesCDBTest extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<ISonFeaturesOperator> cexProvider;
	
	private static  String requireLteFdn = null;
	private static  String requireFdn = null;

	/**
	 * @DESCRIPTION Adding OptionalFeatureLicense with the help of Netsim API only for CDB 
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@BeforeTest
	@Context(context = {Context.API})
	public void getRequiredFdn(){

		final ISonFeaturesOperator operator = cexProvider.provide(ISonFeaturesOperator.class);

		setTestcase("OSS-85641_Func_1", "OptionalFeatureLicense - Anr");
		
		requireLteFdn = operator.getRequiredFdn("ERBS","CDB");
	}
	
	/**
	 * @DESCRIPTION Verify successful Activate/Deactivate Son features includes 15B nodes (CDB)
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "SonFeature_CDB")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "CDB", "GAT", "Feature"})
	public void testActivateDeactivateSonTest(
			@TestId @Input("TestID") String testId,
			@Input("NodeType") String nodeType, 
			@Input("OperationType") String operationType,
			@Input("FeatureType") String featureType){

		final ISonFeaturesOperator operator = cexProvider.provide(ISonFeaturesOperator.class);

		setTestcase(testId, ""+operationType +" Son "+ featureType+" Feature For"+nodeType);

		setTestStep("Performing " + operationType +" operation...");

		requireFdn = requireLteFdn+":";

		assertTrue(operator.activateDeactivateSon(requireFdn, nodeType,operationType,featureType));

		setTestInfo("Verify that SON Feature "+featureType+" "+operationType+" in SEG_CS");

		assertTrue(operator.verifyChangeInCS(requireFdn,nodeType,operationType,featureType));
	}
}
