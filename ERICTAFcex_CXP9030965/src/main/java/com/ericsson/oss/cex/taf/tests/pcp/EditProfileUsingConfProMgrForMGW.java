package com.ericsson.oss.cex.taf.tests.pcp;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.oss.cex.taf.operators.*;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class EditProfileUsingConfProMgrForMGW extends TorTestCaseHelper implements TestCase {
	
	private static Logger log = Logger.getLogger(EditProfileUsingConfProMgrForMGW.class);
	private final String PASSED = "OK";
	
	@Test
	@DataDriven(name = "PCP_PROFILE")
	public void editProfile(@Input("NAME") String name) throws IOException {
		setTestcase("Test 010", "Edit ProfilePCPTest For MGW");
		String result;
		GroovyTestOperators oper = new GroovyTestOperators();
		result = oper.invokeGroovyMethodOnArgs("CexProfilePCP", "updateProfile",name);
		log.info("Edit PCP PROFILE result-->"+result);
		Assert.assertEquals(PASSED, result);
	}
	
}
