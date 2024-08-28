package com.ericsson.oss.cex.taf.tests.relations;


import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.oss.cex.taf.operators.CoverageRelationDiffRNC;


public class coverageRelationDiffRncTest extends TorTestCaseHelper implements TestCase   {
	
	
	private static Logger log = Logger.getLogger(coverageRelationDiffRncTest.class);
	
	
	@Test
	public void testcoverageRelationDiffRnc() throws IOException {
		//String result;
		log.info("Creating coverage relation for different RNC");
		CoverageRelationDiffRNC oper = new CoverageRelationDiffRNC();
		String result = oper.createCoverageRelation();
		Assert.assertEquals("OK", result);
		log.info("coverage relation over different RNC's is not allowed");
	}
	
	
	

	
}

