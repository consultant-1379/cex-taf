package com.ericsson.oss.cex.taf.tests.topology.areas;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;

import org.testng.annotations.Test;

public class AddServiceAreaWithNonUniSac extends TorTestCaseHelper implements TestCase {

	private static final String PASSED = "OK";
	String result= null;
	
	
	
	/**
     * @DESCRIPTION To verify that a SA cannot be added to a PLMN  with non-unque Sac.
     * @PRE : CEx MC is online. CEx is online. CEx client is launched. A PLMN exists in valid area
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-25497_Func_1", title = "Add a Service Area with non-unque Sac")
    @Context(context = {Context.API})
    @Test(groups={"Feature"})
    public void addAServiceAreaWithNonUnqueSac() {
    	GroovyTestOperators operator = new GroovyTestOperators();
    	result = operator.invokeGroovyMethodOnArgs("AddServiceArea", "createServiceAreaWithNonUniSac");
		assertEquals(PASSED,result);
		}

	
}
