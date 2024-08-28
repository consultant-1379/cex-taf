package com.ericsson.oss.cex.taf.tests.pcp;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

public class PcpProfileCliTest extends TorTestCaseHelper implements TestCase{

	@Inject
	private OperatorRegistry<IPcpProfileOperator> provider;

	@TestId(id = "TAFTEST-1_Func_1", title = "PCPProfileExport")
	@Context(context = {Context.API})
	@Test(groups={"KGB"})
	public void PCPProfileExport(){
		IPcpProfileOperator nSDMCOperator = provider.provide(IPcpProfileOperator.class);
		setTestInfo("PCP Export Profile Check");
		String result= nSDMCOperator.exportProfile();
		assertTrue(result.contains("EXPORTED")|| result.contains("PROFILE IS EMPTY"));
	}

	@TestId(id = "TAFTEST-2_Func_2", title = "PCPProfileImport")
	@Context(context = {Context.API})
	@Test(groups={"KGB"})
	public void PCPProfileImport(){
		IPcpProfileOperator nSDMCOperator = provider.provide(IPcpProfileOperator.class);
		setTestInfo("PCP Import Profile Check");
		String result= nSDMCOperator.importProfile();
		assertTrue(result.contains("OK"));
	}

	@TestId(id = "TAFTEST-3_Func_3", title = "PCPProfileDelete")
	@Context(context = {Context.API})
	@Test(groups={"KGB"})
	public void PCPProfileDelete(){
		IPcpProfileOperator nSDMCOperator = provider.provide(IPcpProfileOperator.class);
		setTestInfo("PCP Delete Profile Check");
		String result= nSDMCOperator.delProfile();
		assertTrue(result.contains("DELETED") || result.contains("OK"));
	}

	@DataDriven(name = "PCPCLI")
	@Context(context = {Context.API})
	@Test(groups={"KGB"})
	public void PCPProfileCLI(@TestId @Input("TestID") String testId,
			@Input("OperationType") String operationType){

		IPcpProfileOperator nSDMCOperator = provider.provide(IPcpProfileOperator.class);

		setTestcase(testId, " PCP CLI Command of "+operationType+" Profile");

		setTestStep("Performing " + operationType +" operation...");

		String result= nSDMCOperator.PCPProfileCLI(operationType);

		assertTrue(result.contains("OK"));
	}

}
