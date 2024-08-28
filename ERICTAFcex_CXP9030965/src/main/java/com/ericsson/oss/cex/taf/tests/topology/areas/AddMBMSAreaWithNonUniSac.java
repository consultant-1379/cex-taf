package com.ericsson.oss.cex.taf.tests.topology.areas;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cex.taf.operators.GroovyTestOperators;
import com.ericsson.oss.cex.taf.ui.getters.IGroovyTestOperators;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import javax.inject.Inject;

public class AddMBMSAreaWithNonUniSac extends TorTestCaseHelper implements TestCase {

	private static final String PASSED = "OK";
	private static Logger log = Logger.getLogger(AddMBMSAreaWithNonUniSac.class);
	private static final String FAILED = "FAILED";
	
	
	
    /**
     * @DESCRIPTION To verify that a MSA cannot be added to a PLMN  with non-unque Sac.
     * @PRE : CEx MC is online. CEx is online. CEx client is launched. A PLMN exists in valid area
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-25496_Func_2", title = "Add a Mbms Service Area with non-unque Sac")
    @Context(context = {Context.API})
    @Test(groups={"Feature"})
    public void addAMbmsServiceAreaWithNonUnqueSac() {
		assertEquals(PASSED,addMSAWithNSac());
		}

	private String addMSAWithNSac() {
		String result;
		GroovyTestOperators operator = new GroovyTestOperators();
		result = operator.invokeGroovyMethodOnArgs("AddMbmsServiceArea", "createMbmsServiceAreaWithNonUniSac");
		
		log.info(result);
		if(!result.contains(PASSED))
		return PASSED;
		else 
			return FAILED;
			
	}
	
}
