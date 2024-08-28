package com.ericsson.oss.cex.taf.tests.relations;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.oss.cex.taf.operators.ExternalCdma2000FreqRelation;

public class CreateCdma2000FreqRelation extends TorTestCaseHelper implements TestCase{
	
	
	@TestId(id="OSS-24804_Func_1", title = "Create Cdma2000FreqRelation")
	@Test
	public void cdma2000FreqRelation(){
		
		ExternalCdma2000FreqRelation obj=new ExternalCdma2000FreqRelation();
		String result =obj.Cdma2000FreqRelation();
		assertEquals("OK", result);
	}

}
