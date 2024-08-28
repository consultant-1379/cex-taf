package com.ericsson.oss.cex.taf.tests.relations;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;																					
import com.ericsson.oss.cex.taf.operators.GsmRelation;
public class GsmRelationTest extends TorTestCaseHelper implements TestCase {
	

	private static Logger log = Logger.getLogger(CreateEUtranFreqRelation.class);
	
	@Test
	public void testGsmRelation(){
		log.info("Creating GsmRelation....\n");
		GsmRelation obj=new GsmRelation();
		String result =obj.GsmRelationOperator();
		Assert.assertEquals("OK", result);
		log.info("GsmRelation Created");
	}
	

	


}
