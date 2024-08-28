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

@SuppressWarnings("deprecation")
public class ActivateDeactivateFeatureTest extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<IActivateDeactivateFeatureOperator> cexProvider;
	
	private static  String requireLteFdn = null;
	private static  String requireLteSSRFdn = null;
	private static  String requireFdn = null;
	
	/**
	 * @DESCRIPTION Adding OptionalFeatureLicense with the help of Netsim API
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@BeforeTest
	@Context(context = {Context.API})
	public void getRequiredFdn(){

		final IActivateDeactivateFeatureOperator operator = cexProvider.provide(IActivateDeactivateFeatureOperator.class);

		setTestcase("OSS-85641_Func_1", "OptionalFeatureLicense");

		requireLteFdn = operator.getRequiredFdn("ERBS");
		
		requireLteSSRFdn = operator.getRequiredFdn("SSR-ERBS");

	}
	/**
	 * @DESCRIPTION Verify successful Activate/Deactivate Son features includes 15B nodes (KGB) All functions
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "NewOptionalFeature")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void activateDeactivateUlTrigTest(
			@TestId @Input("TestID") String testId,
			@Input("NodeType") String nodeType, 
			@Input("OperationType") String operationType,
			@Input("FeatureType") String featureType){


		final IActivateDeactivateFeatureOperator operator = cexProvider.provide(IActivateDeactivateFeatureOperator.class);

		setTestcase(testId, ""+operationType +" "+ featureType+" Feature For "+nodeType);

		setTestStep("Performing " + operationType +" operation...");
		
		requireFdn = requireLteFdn+":"+requireLteSSRFdn;

		assertTrue(operator.activateDeactivateFeature(requireFdn, nodeType,operationType,featureType));

		setTestInfo("Verify that "+featureType+" Feature"+operationType+" in SEG_CS");

		assertTrue(operator.verifyChangeInCS(requireFdn,nodeType,operationType,featureType));

	}
	
}
