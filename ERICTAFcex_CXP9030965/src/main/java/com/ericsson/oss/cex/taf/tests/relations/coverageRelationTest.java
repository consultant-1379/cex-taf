package com.ericsson.oss.cex.taf.tests.relations;


import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.oss.cex.taf.operators.CoverageRelation;


public class coverageRelationTest extends TorTestCaseHelper implements TestCase   {

	
	private static Logger log = Logger.getLogger(coverageRelationTest.class);
	
	@Test
	public void testCoverageRelation() throws IOException {
		log.info("Creating coverage relation ---");
		CoverageRelation oper = new CoverageRelation();
		String result = oper.createCoverageRelation();
		Assert.assertEquals("OK", result);
		log.info("Coverage relation Created");
		
	}
	

	
}

