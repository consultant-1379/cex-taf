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


public class VerifyPropertiesFeatureTest extends TorTestCaseHelper implements TestCase{


	@Inject
	private OperatorRegistry<IVerifyPropertiesFeatureOperator> cexProvider;
	private String requiredFdn = null;


	/**
	 * @DESCRIPTION Verify Properties for all the nodes in common explorer
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. 
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "VerifyPropertiesFeature")
	@Context(context = {Context.API})
	@Test(groups={"KGB", "GAT", "Feature"})
	public void verifyPropertiesFunction(@TestId @Input("TestID") String testId,
			@Input("moType") String moType, 
			@Input("CellType") String cellType,
			@Input("FunctionType") String functionType,
			@Input("PropertyName") String propertyName) {

		final IVerifyPropertiesFeatureOperator operator = cexProvider.provide(IVerifyPropertiesFeatureOperator.class);

		setTestcase(testId, " Verify "+functionType +" Function Properties for "+ cellType+"-"+moType);

		setTestStep("Get "+ cellType+"-"+moType+" FDN");
		requiredFdn=operator.getRequiredFdn(cellType, functionType, moType);
		
		setTestInfo("Verify Fdn for "+ functionType+" Function should present.");
		assertNotNull(requiredFdn);

		setTestStep("Get Properties for "+ functionType+" Function from Topology & SEG_CS");
		
		setTestInfo("Verify Properties for "+ functionType+" Function from Topology & SEG_CS");
		assertTrue(operator.propertiesMatcher(requiredFdn, propertyName));
	}
}


