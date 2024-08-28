package com.ericsson.oss.cex.taf.tests.properties;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

@SuppressWarnings("deprecation")
public class VerifyIMSNodePropertiesTest extends TorTestCaseHelper implements TestCase{

	@Inject
	private OperatorRegistry<IVerifyPropertiesFeatureOperator> cexProvider;

	/**
	 * @DESCRIPTION Verify Properties for all the nodes in common explorer
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. 
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "VerifyIMSNodeProperties")
	@Context(context = {Context.API})
	@Test(groups={"KGB", "GAT", "Feature"})
	public void verifyPropertiesFunction(@TestId @Input("TestID") String testId,
			@Input("NodeType") String nodeType) {

		final IVerifyPropertiesFeatureOperator operator = cexProvider.provide(IVerifyPropertiesFeatureOperator.class);
		setTestcase(testId, " Verify IMS - "+nodeType +" Node Properties");
		assertTrue(operator.getIMSNode(nodeType));
	}

	/**
	 * @DESCRIPTION Verify Properties for all the nodes in common explorer - PLM suite
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. 
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "VerifyIMSNodeProperties_PLM")
	@Context(context = {Context.API})
	@Test(groups={"KGB", "GAT", "Feature"})
	public void verifyPropertiesFunctionPlm(@TestId @Input("TestID") String testId,
			@Input("NodeType") String nodeType) {

		final IVerifyPropertiesFeatureOperator operator = cexProvider.provide(IVerifyPropertiesFeatureOperator.class);
		setTestcase(testId, " Verify IMS - "+nodeType +" Node Properties");
		assertTrue(operator.getIMSNode(nodeType));

	}

}


