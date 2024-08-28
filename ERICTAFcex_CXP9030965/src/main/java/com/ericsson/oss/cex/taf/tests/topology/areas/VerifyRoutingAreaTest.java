package com.ericsson.oss.cex.taf.tests.topology.areas;


import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;


public class VerifyRoutingAreaTest extends TorTestCaseHelper implements TestCase   {

	
	@Test
	public void testVerifyRoutingArea() throws IOException {
		//String result;
		VerifyRoutingArea oper = new VerifyRoutingArea();
		String result = oper.modifyMO();
		Assert.assertEquals("OK", result);
		
	}
	

	
}

