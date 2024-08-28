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
import com.ericsson.oss.cex.taf.tests.selectionList.view.AutoConfEUtranCellR_ECellFDD;
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

public class AddRoutingAreaWithNonUniRac extends TorTestCaseHelper implements TestCase {

	private static final String PASSED = "OK";
	private static Logger log = Logger.getLogger(AddRoutingAreaWithNonUniRac.class);
	
	private static final String FAILED = "FAILED";
	
	
	 
    /**
     * @DESCRIPTION To verify that a RA cannot be added to a PLMN  with non-unque Rac.
     * @PRE : CEx MC is online. CEx is online. CEx client is launched. A PLMN exists in valid area
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-25499_Func_1", title = "Add a Routing Area with non-unque Rac")
    @Context(context = {Context.API})
    @Test(groups={"Feature"})
    public void addARoutingAreaWithNonUnqueRac() {
		assertEquals(PASSED,addRA());
		}

	private String addRA() {
		String result;
		GroovyTestOperators operator = new GroovyTestOperators();
		result = operator.invokeGroovyMethodOnArgs("AddRoutingArea", "createLocationAreaWithNonUniRac");
		
		log.info(result);
		if(!result.contains(PASSED))
		return PASSED;
		else 
			return FAILED;
			
	}
	
}
