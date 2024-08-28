package com.ericsson.oss.cex.taf.tests.relations;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.log4j.Logger;

import com.ericsson.oss.cex.taf.operators.UtranFreqRelation;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.TestId;

public class CreateUtranFreqRelation extends TorTestCaseHelper implements TestCase {
	
	
	private static Logger log = Logger.getLogger(CreateUtranFreqRelation.class);
	
	@TestId(id="OSS-24802", title = "Create UtranFreqRelation")
	@Test
	public void testUtranFreqRelation(){
		log.info("Creating UtranFreqRelation....\n");
		UtranFreqRelation obj=new UtranFreqRelation();
		String result =obj.utranFreqRelation();
		Assert.assertEquals("OK", result);
	}

}