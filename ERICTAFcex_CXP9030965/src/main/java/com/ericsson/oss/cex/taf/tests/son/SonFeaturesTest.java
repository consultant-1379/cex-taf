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
public class SonFeaturesTest extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<ISonFeaturesOperator> cexProvider;

	private static  String requireLteFdn = null;
	private static  String requirePERBSFdn = null;
	private static  String requireLteSSRFdn = null;
	private static  String requireWcdmaFdn = null;
	private static  String requirePrbsFdn = null;
	private static  String require17A_ERBSFdn = null;
	private static  String require17A_SSR_ERBSFdn = null;
	private static  String requireFdn = null;
	
	/**
	 * @DESCRIPTION Adding OptionalFeatureLicense with the help of Netsim API
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@BeforeTest
	@Context(context = {Context.API})
	public void getRequiredFdn(){

		final ISonFeaturesOperator operator = cexProvider.provide(ISonFeaturesOperator.class);

		setTestcase("OSS-85641_Func_1", "OptionalFeatureLicense");

		requireLteFdn = operator.getRequiredFdn("ERBS","KGB");
		
		requirePERBSFdn = operator.getRequiredFdn("PICO-ERBS","KGB");
		
		requireLteSSRFdn = operator.getRequiredFdn("SSR-ERBS","KGB");

		requireWcdmaFdn = operator.getRequiredFdn("RBS","KGB");
		
		requirePrbsFdn = operator.getRequiredFdn("PICO-RBS","KGB");
		
		require17A_ERBSFdn = operator.getRequiredFdn("17A-ERBS","KGB");
		
		require17A_SSR_ERBSFdn = operator.getRequiredFdn("17A-SSR-ERBS","KGB");
	}
	/**
	 * @DESCRIPTION Verify successful Activate/Deactivate Son features includes 15B nodes (PLM) Excluding SSLM & MLSTM
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "SonFeature_PLM")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void activateDeactivateSonPlmTest(
			@TestId @Input("TestID") String testId,
			@Input("NodeType") String nodeType, 
			@Input("OperationType") String operationType,
			@Input("FeatureType") String featureType){

		final ISonFeaturesOperator operator = cexProvider.provide(ISonFeaturesOperator.class);

		setTestcase(testId, ""+operationType +" Son "+ featureType+" Feature For "+nodeType);

		setTestStep("Performing " + operationType +" operation...");
		
		requireFdn = requireLteFdn+":"+requireWcdmaFdn+":"+requirePERBSFdn+":"+requirePrbsFdn+":"+requireLteSSRFdn;
		

		assertTrue(operator.activateDeactivateSon(requireFdn, nodeType,operationType,featureType));

		setTestInfo("Verify that SON Feature "+featureType+" "+operationType+" in SEG_CS");

		assertTrue(operator.verifyChangeInCS(requireFdn,nodeType,operationType,featureType));

	}

	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void getRequiredFdnForRbot(){

		final ISonFeaturesOperator operator = cexProvider.provide(ISonFeaturesOperator.class);

		setTestcase("CIS-84536", "OptionalFeatureLicense");
		
		boolean result =false;
		
		requireLteFdn = operator.getRequiredFdn("ROBOTERBS","KGB");
		if(!requireLteFdn.isEmpty()){
			result = true;
		}
		assertTrue(result);
	}
	/**
	 * @DESCRIPTION Verify successful Activate/Deactivate Son features includes 15B nodes (KGB) All functions
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "SonFeature")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void activateDeactivateSonTest(
			@TestId @Input("TestID") String testId,
			@Input("NodeType") String nodeType, 
			@Input("OperationType") String operationType,
			@Input("FeatureType") String featureType){

		final ISonFeaturesOperator operator = cexProvider.provide(ISonFeaturesOperator.class);

		setTestcase(testId, ""+operationType +" Son "+ featureType+" Feature For "+nodeType);

		setTestStep("Performing " + operationType +" operation...");
		
		requireFdn = requireLteFdn+":"+requireWcdmaFdn+":"+requirePERBSFdn+":"+requirePrbsFdn+":"+requireLteSSRFdn+":"+require17A_ERBSFdn+":"+require17A_SSR_ERBSFdn;

		assertTrue(operator.activateDeactivateSon(requireFdn, nodeType,operationType,featureType));

		setTestInfo("Verify that SON Feature "+featureType+" "+operationType+" in SEG_CS");

		assertTrue(operator.verifyChangeInCS(requireFdn,nodeType,operationType,featureType));

	}
}
