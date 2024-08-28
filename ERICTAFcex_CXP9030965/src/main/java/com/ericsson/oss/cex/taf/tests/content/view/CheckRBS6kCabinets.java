package com.ericsson.oss.cex.taf.tests.content.view;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.content.operator.IRBS6kViewOperator;

public class CheckRBS6kCabinets extends TorTestCaseHelper implements TestCase {


	@Inject
	private OperatorRegistry<IRBS6kViewOperator> RBS6koperator;

	@TestId(id = "oss-", title = "CEX content view should show all cabinets for a RBS6K node in comma separated values")
	@Context(context = { Context.API })
	@Test(groups = { "CDB" })
	public void checkRBS6kCabinet() {

		setTestStep("Looking for RBS6k nodes");
		final IRBS6kViewOperator operator = RBS6koperator.provide(IRBS6kViewOperator.class);
		assertEquals("OK",operator.getRBS6kCabinet());


	}

}
