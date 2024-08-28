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

public class CreateProfileUsingConfProMgrForMGW extends TorTestCaseHelper implements TestCase {
	
	private static Logger log = Logger.getLogger(CreateProfileUsingConfProMgrForMGW.class);
	private final String PASSED = "OK";
	
	
	@Test
	@DataDriven(name = "PCP_PROFILE")
	public void testCreateProfileUsingConfProMgrForMGW(@Input("NAME") String name, @Input("DESCRIPTION") String desc) throws IOException {
		String result;
		setTestcase("Test 009", "Create Profile PCP Test For MGW");
		GroovyTestOperators oper = new GroovyTestOperators();
		result = oper.invokeGroovyMethodOnArgs("CexProfilePCP", "createProfile", name, desc);
		log.info("Create PCP Profile for MGW(1): result-->"+result);
		Assert.assertEquals(PASSED, result);
	}
	@Test
	@DataDriven(name = "PCP_PROFILE")
	public void verifyProfile(@Input("NAME") String name) throws IOException {
		String result;
		GroovyTestOperators oper = new GroovyTestOperators();
		result = oper.invokeGroovyMethodOnArgs("CexProfilePCP", "findProfileByName",name);
		log.info("Verify PCP Profile for MGW: result-->"+result);		
		Assert.assertEquals(PASSED, result);
	}
		
}
