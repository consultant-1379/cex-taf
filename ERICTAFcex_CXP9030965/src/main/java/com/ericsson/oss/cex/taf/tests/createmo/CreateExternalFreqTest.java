package com.ericsson.oss.cex.taf.tests.createmo;

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
public class CreateExternalFreqTest extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<ICreateExternalMo> cexProvider;


	@TestId(id="OSS-24807_Func_1", title="Create ExternalUtranCell")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void createExternalUtranCell(){

		final ICreateExternalMo operator = cexProvider.provide(ICreateExternalMo.class);

		String result = operator.createExternalCell();

		assertEquals("OK", result);
	}

	@DataDriven(name = "CreateExternalFreq") 
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void createExternalUtranFreq(
			@Input("TestID") String testId, 
			@Input("ElementType") String elementType){

		final ICreateExternalMo operator = cexProvider.provide(ICreateExternalMo.class);

		setTestcase(testId, "Create " + elementType + " testcase");

		setTestStep("Setting Required attribute for "+ elementType);
		assertNotNull(operator.setLinkValues(elementType,null,null));

		setTestStep("Creating " + elementType);
		String result = operator.createExternalFreq(elementType);
		assertEquals("OK", result);
	}
	
	@DataDriven(name = "DeleteExternalCell") 
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void deleteExternalCell(@Input("TestID") String testId, 
			@Input("ElementType") String elementType){

		final ICreateExternalMo operator = cexProvider.provide(ICreateExternalMo.class);
		
		setTestcase(testId, "Delete " + elementType + " testcase");
		setTestStep("Get "+elementType);
		String cellFdn = operator.getCellFdn(elementType);
		assertNotNull(cellFdn);
		
		setTestStep("Deleting "+elementType);
		String result = operator.deleteExternalCell(cellFdn);
		assertEquals("OK", result);
	}
}
