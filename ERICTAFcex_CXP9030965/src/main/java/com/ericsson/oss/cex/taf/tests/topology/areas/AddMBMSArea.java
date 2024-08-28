package com.ericsson.oss.cex.taf.tests.topology.areas;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.IGroovyTestOperators;

import org.testng.annotations.Test;

import javax.inject.Inject;

public class AddMBMSArea extends TorTestCaseHelper implements TestCase {

	private static final String PASSED = "OK";

	private static final String FAILED = "FAILED";
	
	
	
	
	/**
     * @DESCRIPTION To verify that a Mbms SA can be added to a PLMN.
     * @PRE CEx is online. CEx client is launched. A Plmn exists in valid area
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-25494_Func_1", title = "Add a Mbms Service Area")
    @Context(context = {Context.API})
    @Test(groups={"Feature"})
    public void addAMbmsServiceArea() {
    	assertEquals(PASSED,addMSA());
		}

	private String addMSA() {
		String result;
		GroovyTestOperators operator = new GroovyTestOperators();
		
		result = operator.invokeGroovyMethodOnArgs("AddMbmsServiceArea", "createMbmsServiceArea");
		
		System.out.println(result);
		if(result.contains(PASSED))
		return PASSED;
		else 
			return FAILED;
			
	}
	
}
