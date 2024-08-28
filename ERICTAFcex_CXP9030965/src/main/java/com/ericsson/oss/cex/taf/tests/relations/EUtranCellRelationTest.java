package com.ericsson.oss.cex.taf.tests.relations;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.oss.cex.taf.operators.EUtranCellRelation;

public class EUtranCellRelationTest extends TorTestCaseHelper implements TestCase {

	private static Logger log = Logger.getLogger(EUtranCellRelationTest.class);

	
	@Test
	public void testEUtranCellRelation(){
		log.info("Creating EUtranCellRelation....\n");
		EUtranCellRelation obj=new EUtranCellRelation();
		String result =obj.EUtranCellRelationOperator();
		Assert.assertEquals("OK", result);
		log.info("EUtranCellRelation Created");
	}
	
	


}
