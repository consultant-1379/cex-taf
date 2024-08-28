package com.ericsson.oss.cex.taf.tests.topology.areas;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.handlers.implementation.SshRemoteCommandExecutor;
import com.ericsson.cifwk.taf.osgi.client.ContainerNotReadyException;
import com.ericsson.oss.cex.taf.content.operator.ContentViewOperator;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexOperators;

import net.sf.saxon.functions.IndexOf;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddRoutingArea extends TorTestCaseHelper implements TestCase {

	private static final String PASSED = "OK";

	private static final String FAILED = "FAILED";
	

	
	@Test
	public void testAddRA(){
		setTestcase("Test 025", "Test Creation of Routing Area");
		assertEquals(PASSED,addRA());
		}

	private String addRA() {
		String result;
		
		GroovyTestOperators oper = new GroovyTestOperators();
		result = oper.invokeGroovyMethodOnArgs("AddRoutingArea", "createRoutingArea");
		System.out.println(result);
		if(result.contains(PASSED))
		return PASSED;
		else 
			return FAILED;
			
	}
	
}
