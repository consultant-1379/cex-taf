package com.ericsson.oss.cex.taf.tests.content;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

/**
 * 
 * @author xmusgup
 *
 */
public class VerifyContentViewTest  extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<IVerifyContentViewOperator> cexProvider;

	/**
	 * @DESCRIPTION Verify Content View
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "VerifyContentView") 
	@Context(context = {Context.API})
	@Test(groups={"KGB", "GAT", "Feature"})
	public void verifyContentViewTest(@TestId @Input("TestId") String testId,
			@Input("ElementType") String elementType){

		final IVerifyContentViewOperator operator = cexProvider.provide(IVerifyContentViewOperator.class);

		setTestcase(testId, "Verify " + elementType + " Content View");

		setTestStep("Getting " + elementType + " From Domain Cache...");
		boolean result = operator.verifyContentView(elementType);
		
		setTestStep("Verifing " + elementType + " present on the server");
		assertTrue(result);
	}
	
	/**
	 * @DESCRIPTION Verify Content View MSRBS_V2 nodes {WRAN-SSR , LTE-SSR} , Separate for 16A onwards
	 * @PRE : CEx MC is online. CEx client has been launched. Nodes for the domain under test are connected and synchronised. In the case of cell type object, the WRAN database contains entries for the objects under test.
	 * @PRIORITY HIGH
	 */
	@DataDriven(name = "VerifyContentMSRBS_V2View") 
	@Context(context = {Context.API})
	@Test(groups={"KGB", "GAT", "Feature"})
	public void verifyContentViewMSRBS_V2Test(@TestId @Input("TestId") String testId,
			@Input("ElementType") String elementType){

		final IVerifyContentViewOperator operator = cexProvider.provide(IVerifyContentViewOperator.class);

		setTestcase(testId, "Verify " + elementType + " Content View");

		setTestStep("Getting " + elementType + " From Domain Cache...");
		boolean result = operator.verifyContentView(elementType);
		
		setTestStep("Verifing " + elementType + " present on the server");
		assertTrue(result);
	}
}
