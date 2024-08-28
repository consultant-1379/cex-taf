package com.ericsson.oss.cex.taf.tests.topology.areas;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.handlers.implementation.SshRemoteCommandExecutor;
import com.ericsson.cifwk.taf.osgi.client.ContainerNotReadyException;
import com.ericsson.oss.cex.taf.content.operator.ContentViewOperator;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.CexApiGetter;
import com.ericsson.oss.cex.taf.ui.getters.CexOperators;
import com.ericsson.oss.cex.taf.ui.getters.IGroovyTestOperators;

import net.sf.saxon.functions.IndexOf;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AddLocationArea extends TorTestCaseHelper implements TestCase {

	private static final String PASSED = "OK";

	private static final String FAILED = "FAILED";
	
	
	
	/**
     * @DESCRIPTION To verify that a LA can be added to a PLMN.
     * @PRE : CEx MC is online. CEx is online. CEx client is launched. A PLMN exists in valid area
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-25491_Func_1", title = "Add a Location Area")
    @Context(context = {Context.API})
    @Test(groups={"Feature"})
    public void addALocationArea() {

		assertEquals(PASSED,addLA());
		}

	private String addLA() {
		String result;

		GroovyTestOperators operator = new GroovyTestOperators();
		result = operator.invokeGroovyMethodOnArgs("AreaTopologyOperation", "createLocationArea");
		System.out.println(result);
		if(result.contains(PASSED))
		return PASSED;
		else 
			return FAILED;
			
	}
	
}
