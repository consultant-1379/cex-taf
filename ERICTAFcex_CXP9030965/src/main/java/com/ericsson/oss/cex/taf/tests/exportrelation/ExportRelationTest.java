package com.ericsson.oss.cex.taf.tests.exportrelation;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.generic.manager.ICellServiceManager;

public class ExportRelationTest extends TorTestCaseHelper implements TestCase {
	
	
	@Inject
	private OperatorRegistry<ICellServiceManager> cexProvider;
	
	@TestId(id="OSS-84787_Func_1", title="Create ExternalUtranCell")
	@Context(context = {Context.API})
	@Test(groups = {"KGB", "GAT", "Feature"})
	public void exportRelation() {
		
		final ICellServiceManager operator = cexProvider.provide(ICellServiceManager.class);
		
		assertTrue(operator.exportRelation());
	}

}