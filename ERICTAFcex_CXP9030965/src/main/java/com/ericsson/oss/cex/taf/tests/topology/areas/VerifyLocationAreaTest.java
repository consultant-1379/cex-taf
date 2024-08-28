package com.ericsson.oss.cex.taf.tests.topology.areas;


import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;


public class VerifyLocationAreaTest extends TorTestCaseHelper implements TestCase   {

	
	@Test
	public void testVerifyLocationArea() throws IOException {
		//String result;
		VerifyLocationArea oper = new VerifyLocationArea();
		String result = oper.modifyMO();
		Assert.assertEquals("OK", result);
		
	}
	

	
}

