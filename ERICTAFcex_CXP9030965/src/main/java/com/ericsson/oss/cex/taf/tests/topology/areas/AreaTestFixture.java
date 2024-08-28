package com.ericsson.oss.cex.taf.tests.topology.areas;


import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;


public class AreaTestFixture extends TorTestCaseHelper implements TestCase   {
	
	private static Logger log = Logger.getLogger(AreaTestFixture.class);
	
	@Test
	public void locationAreaTest() throws IOException {
		//String result;
		LocationArea oper = new LocationArea();
		String result = oper.deletearea();
		Assert.assertEquals("OK", result);
		
	}
	@Test
	public void MbmsAreaTest() throws IOException {
		//String result;
		Mbms oper = new Mbms();
		String result = oper.deletearea();
		Assert.assertEquals("OK", result);
		
	}
	@Test
	public void routingAreaTest() throws IOException {
		//String result;
		RoutingArea oper = new RoutingArea();
		String result = oper.deletearea();
		Assert.assertEquals("OK", result);
		log.info("Location Area Deleted");
		
	}
	@Test
	public void serviceAreaTest() throws IOException {
		//String result;
		ServiceArea oper = new ServiceArea();
		String result = oper.deletearea();
		Assert.assertEquals("OK", result);
		log.info("Service Area Deleted");
		
	}
	

	
}

