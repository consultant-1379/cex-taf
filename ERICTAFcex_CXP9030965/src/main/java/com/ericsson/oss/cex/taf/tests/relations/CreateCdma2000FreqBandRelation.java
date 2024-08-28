package com.ericsson.oss.cex.taf.tests.relations;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.oss.cex.taf.operators.cdma2000freqband;

public class CreateCdma2000FreqBandRelation extends TorTestCaseHelper implements TestCase   {

	
	private static Logger log = Logger.getLogger(CreateCdma2000FreqBandRelation.class);
	
	@TestId(id="OSS-24803", title = "Create Cdma2000FreqBandRelation")
	@Test
	public void testCdma2000FreqBandRelation() throws IOException {
		//String result;
		log.info("Creating Cdma2000FreqBandRelation---");
		cdma2000freqband oper = new cdma2000freqband();
		String result = oper.Cdma2000FreqBandRelation();
		Assert.assertEquals("OK", result);
		log.info("Cdma2000FreqBandRelation successfully created");
		
	}
	

	
}

