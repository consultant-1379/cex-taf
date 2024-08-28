package com.ericsson.oss.cex.taf.tests.pcp;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.oss.cex.taf.operators.PcpProfileOperator;

public class PcpProfileCrudTest extends TorTestCaseHelper implements TestCase {

	private static final String expectedResult = "SUCCESS";
	private static String result;
	private static final Logger log = Logger
			.getLogger(PcpProfileCrudTest.class);

	@Context(context = { Context.API })
	@Test
	public void testCreateProfileMimManager() {

		PcpProfileOperator oper = new PcpProfileOperator();
		result = oper.createProfileMimManager();
		Assert.assertEquals(expectedResult, result);
		log.info(result);

	}

	@Context(context = { Context.API })
	@Test
	public void duplicateProfileTest() {

		PcpProfileOperator obj = new PcpProfileOperator();
		String result = obj.duplicateProfileOper();
		log.info("Duplicate PCP Profile : result-->" + result);
		Assert.assertEquals(expectedResult, result);
	}

	@Context(context = { Context.API })
	@Test
	public void testDeleteProfileMimManager() {

		PcpProfileOperator oper = new PcpProfileOperator();
		result = oper.deleteProfileMimManager();
		log.info(result);
		Assert.assertEquals(expectedResult, result);

	}

	@TestId(id="Test-002", title = "Verify to CreateProfileExistMo")
	@Context(context = { Context.API })
	@Test
	public void createProfileExistMoTest() {

		PcpProfileOperator obj = new PcpProfileOperator();
		result = obj.CreateProfileExistMoOper();
		log.info("Create PCP Profile : result-->" + result);
		Assert.assertEquals(expectedResult, result);
	}

	@TestId(id="Test-003", title = "Verify to DeleteProfileExistMo")
	@Context(context = { Context.API })
	@Test
	public void deleteProfileExistMoTest() {

		PcpProfileOperator obj = new PcpProfileOperator();
		result = obj.deleteProfileExistMoOper();
		log.info("Delete PCP Profile : result-->" + result);
		Assert.assertEquals(expectedResult, result);
	}

	@Context(context = { Context.API })
	@Test
	public void createProfileExistMoMoreTest() {

		PcpProfileOperator obj = new PcpProfileOperator();
		result = obj.CreateProfileExistMoMoreOper();
		log.info("Create PCP Profile : result-->" + result);
		Assert.assertEquals(expectedResult, result);
	}

	@Context(context = { Context.API })
	@Test
	public void deletProfileExistMoMoreTest() {

		PcpProfileOperator obj = new PcpProfileOperator();
		result = obj.deleteProfileExistMoMoreOper();
		log.info("delete PCP Profile : result-->" + result);
		Assert.assertEquals(expectedResult, result);
	}

}
